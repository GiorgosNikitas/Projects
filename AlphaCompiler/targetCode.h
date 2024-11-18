#include <iostream>
#include <string>
#include <vector>
#include <stack>
#include <assert.h>
#include <algorithm>
#include <map>
#include <iomanip>
#include <fstream>
#include <cstring>
#include "quads.h"

#define AVM_WIPEOUT(m) memset(&(m), 0, sizeof(m))

#define AVM_TABLE_HASHSIZE 211

using namespace std;

typedef enum vmopcode {
    assign_v,       add_v,          sub_v,
    mul_v,          div_v,          mod_v,
    uminus_v,       and_v,          or_v,
    not_v,          jump_v,         jeq_v,          jne_v,
    jle_v,          jge_v,          jlt_v,
    jgt_v,          call_v,         pusharg_v,
    funcenter_v,    funcexit_v,     newtable_v,
    tablegetelem_v, tablesetelem_v, nop_v
} vmopcode;

map<vmopcode, string> vmopcodeToString = {  { assign_v, "ASSIGN" }, { add_v, "ADD" }, { sub_v, "SUB" }, 
                                     { mul_v, "MUL" }, { div_v, "DIV" }, { mod_v, "MOD" },
                                     { uminus_v, "UMINUS" }, { and_v, "AND" }, { or_v, "OR" },
                                     { not_v, "NOT" }, { jump_v, "JUMP" }, { jeq_v, "JEQ" },
                                     { jne_v, "JNE" }, { jle_v, "JLE" }, { jge_v, "JGE" },
                                     { jlt_v, "JLT" }, { jgt_v, "JGT" }, { call_v, "CALL" },
                                     { pusharg_v, "PUSHARG" }, { funcenter_v, "FUNCENTER" }, { funcexit_v, "FUNCEXIT" },
                                     { newtable_v, "NEWTABLE" }, { tablegetelem_v, "TABLEGETELEM" },
                                     { tablesetelem_v, "TABLESETELEM" }, { nop_v, "NOP" } };

typedef enum vmarg_t {
    label_a = 0,
    global_a = 1,
    formal_a = 2,
    local_a = 3,
    number_a = 4,
    string_a = 5,
    bool_a = 6,
    nil_a = 7,
    userfunc_a = 8,
    libfunc_a = 9,
    retval_a = 10
} vmarg_t;

map<vmarg_t, string> vmarg_tToString = { {label_a, "label"}, {global_a, "global"}, {formal_a, "formal"},
                                         {local_a, "local"}, {number_a, "number"}, {string_a, "string"},
                                         {bool_a, "bool"},   {nil_a, "nil"}, {userfunc_a, "userfunc"}, {libfunc_a, "libfunc"}, {retval_a, "retval"} };

typedef struct vmarg {
    vmarg_t type;
    unsigned val;
    bool empty = true;
} vmarg;

typedef struct instruction {
    vmopcode opcode;
    vmarg result;
    vmarg arg1;
    vmarg arg2;
    unsigned scrLine;
} instruction;

vector<instruction> instructions;
unsigned int currInstruction = 0;

typedef struct userfunc {
    unsigned address;
    unsigned localSize;
    string id;
} userfunc;

vector<double> numConsts;
unsigned int currNumConst = 0;
vector<string> stringConsts;
unsigned int currStrConst = 0;
vector<string> namedLibFuncs;
unsigned int currLibFunc = 0;
vector<userfunc> userFuncs;
unsigned int currUserFunc = 0;

typedef enum avm_memcell_t {
    number_m = 0,
    string_m = 1,
    bool_m = 2,
    table_m = 3,
    userfunc_m = 4,
    libfunc_m = 5,
    nil_m = 6,
    undef_m = 7
} avm_memcell_t;

typedef struct avm_table avm_table;

typedef struct avm_memcell {
    avm_memcell_t type;
    union {
        double numVal;
        char* strVal;
        bool boolVal;
        avm_table* tableVal;
        unsigned funcVal;
        char* libfuncVal;
    } data;  
} avm_memcell;

typedef struct avm_table_bucket {
    avm_memcell key;
    avm_memcell value;
    struct avm_table_bucket* next;
} avm_table_bucket;

typedef struct avm_table {
    unsigned refCounter;
    // vector<avm_table_bucket*> strIndexed;
    // vector<avm_table_bucket*> numIndexed;
    avm_table_bucket* strIndexed[AVM_TABLE_HASHSIZE];
    avm_table_bucket* numIndexed[AVM_TABLE_HASHSIZE];
    unsigned total;
} avm_table;

stack<node*> funcStack;

inline void avm_tablebucketsinit(avm_table_bucket** p) {
    for (int i = 0; i < AVM_TABLE_HASHSIZE; i++) {
        p[i] = nullptr;
    }
}

inline avm_table* avm_tablenew() {
    avm_table* t = new avm_table;
    AVM_WIPEOUT(*t);

    t->refCounter = 0;
    t->total = 0;
    avm_tablebucketsinit(t->numIndexed);
    avm_tablebucketsinit(t->strIndexed);

    return t;
}

inline void avm_tableincrefcounter(avm_table* t) {
    ++t->refCounter;
}

inline int avm_intindex(int index) {
    return index % AVM_TABLE_HASHSIZE;
}

inline int avm_stringindex(string index) {
    return index[0] % AVM_TABLE_HASHSIZE;
}

inline void avm_tablebucketsdestroy(avm_table_bucket** p); // ignore

inline void avm_tabledestroy(avm_table* t) {
    avm_tablebucketsdestroy(t->strIndexed);
    avm_tablebucketsdestroy(t->numIndexed);
    free(t);
}

// automatic garbage collection
inline void avm_tabledecrefcounter(avm_table* t) {
    assert(t->refCounter > 0);
    if (!--t->refCounter) {
        avm_tabledestroy(t);
    }
}

typedef void (*memclear_func_t) (avm_memcell*);

inline void memclear_string(avm_memcell* m) {
    assert(m->data.strVal);
    free(m->data.strVal);
}

inline void memclear_table(avm_memcell* m) {
    assert(m->data.tableVal);
    avm_tabledecrefcounter(m->data.tableVal);
}

memclear_func_t memclearFuncs[] = {
    0,
    memclear_string,
    0,
    memclear_table,
    0,
    0,
    0,
    0
};

inline void avm_memcellclear(avm_memcell* m) {
    if (m->type != undef_m) {
        memclear_func_t f = memclearFuncs[m->type];
        if (f) {
            (*f)(m);
        }
        m->type = undef_m;
    }
}

inline void avm_tablebucketsdestroy(avm_table_bucket** p) {
    for (unsigned i = 0; i < AVM_TABLE_HASHSIZE; i++) {
        for (avm_table_bucket* b = *p; b;) {
            avm_table_bucket* del = b;
            b = b->next;
            avm_memcellclear(&del->key);
            avm_memcellclear(&del->value);
            free(del);
        }
        p[i] = nullptr;
    }
}

unsigned consts_newstring(string s) {
    stringConsts.push_back(s);
    currStrConst++;
    return currStrConst - 1;
}

unsigned consts_newnumber(double n) {
    numConsts.push_back(n);
    currNumConst++;
    return currNumConst - 1;
}

unsigned libfuncs_newused(string s) {
    namedLibFuncs.push_back(s);
    currLibFunc++;
    return currLibFunc - 1;
}

unsigned userfuncs_newfunc(node* sym) {
    userfunc u;
    u.id = sym->name;
    u.address = sym->taddress;
    u.localSize = sym->totalLocals;
    userFuncs.push_back(u);
    currUserFunc++;

    return currUserFunc - 1;
}

inline void make_operand(expr* e, vmarg* arg) {
    if (!e) {
        return;
    }
    if (e->sym) {
        if (!e->sym->name.empty()) {
            if (e->sym->name[0] == '_') {
                e->type = var_e;
            }
        }
    }
    arg->empty = false;
    switch (e->type) {
        case var_e:
            cout << "type : " << e->sym->type << endl;
            arg->val = e->sym->offset;
            if (e->sym->type == USERFUNC) {
                arg->type = userfunc_a;
            } else if (e->sym->type == LIBFUNC) {
                arg->type = libfunc_a;
            } else {
                switch (e->sym->space) {
                    case programvar:
                        arg->type = global_a;
                        break;
                    case functionlocal:
                        arg->type = local_a;
                        break;
                    case formalarg:
                        arg->type = formal_a;
                        break;
                    default:
                        assert(0);
                        break;
                }
            }
            break;

        case tableitem_e:
            switch (e->sym->space) {
                case programvar:
                    arg->type = global_a;
                    break;
                case functionlocal:
                    arg->type = local_a;
                    break;
                case formalarg:
                    arg->type = formal_a;
                    break;
                default:
                    assert(0);
                    break;
            }
            break;

        case arithexpr_e:
            arg->val = e->sym->offset;
            switch (e->sym->space) {
                case programvar:
                    arg->type = global_a;
                    break;
                case functionlocal:
                    arg->type = local_a;
                    break;
                case formalarg:
                    arg->type = formal_a;
                    break;
                default:
                    assert(0);
                    break;
            }
            break;

        case boolexpr_e:
            break;

        case assignexpr_e: {
            assert(e->sym);
            arg->val = e->sym->offset;
            switch (e->sym->space) {
                case programvar:
                    arg->type = global_a;
                    break;
                case functionlocal:
                    arg->type = local_a;
                    break;
                case formalarg:
                    arg->type = formal_a;
                    break;
                default:
                    assert(0);
            }
            break;
        }

        case newtable_e:
            assert(e->sym);
            arg->val = e->sym->offset;
            switch (e->sym->space) {
                case programvar: 
                    arg->type = global_a; break;
                
                case functionlocal: 
                    arg->type = local_a; break;
                
                case formalarg: 
                    arg->type = formal_a; break;
                
                default: 
                    assert(0);
            }
            break;

        case constbool_e:
            arg->val = e->boolConst;
            arg->type = bool_a;
            break;

        case constnum_e:
            arg->val = consts_newnumber(e->numConst);
            arg->type = number_a;
            break;

        case conststring_e:
            arg->val = consts_newstring(e->strConst);
            arg->type = string_a;
            break;

        case nil_e:
            arg->type = nil_a;
            break;

        case programfunc_e:
            arg->type = userfunc_a;
            // arg->val = e->sym->iaddress; //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            arg->val = userfuncs_newfunc(e->sym);
            break;
        
        case libraryfunc_e:
            arg->type = libfunc_a;
            arg->val = libfuncs_newused(e->sym->name);
            break;
        
        default:
            assert(0);
    }
}

unsigned nextInstructionLabel() {
    return currInstruction;
}

inline void emitInstruction(instruction* temp) {
    instructions.push_back(*temp);
    currInstruction++;
}

inline void make_numberoperand(vmarg* arg, double val) {
    arg->val = consts_newnumber(val);
    arg->type = number_a;
    arg->empty = false;
}

inline void make_booloperand(vmarg* arg, unsigned val) {
    arg->val = val;
    arg->type = bool_a;
    arg->empty = false;
}

inline void make_retvaloperand(vmarg* arg) {
    arg->type = retval_a;
    arg->empty = false;
}

void reset_operand(vmarg* arg) {
    arg = nullptr;
}

typedef struct incomplete_jump {
    unsigned instrNo;
    unsigned iaddress;
    incomplete_jump* next;
} incomplete_jump;

incomplete_jump* ij_head = nullptr;
unsigned ij_total = 0;

inline void add_incomplete_jump(unsigned instrNo, unsigned iaddress) {
    incomplete_jump* inc_j = new incomplete_jump;
    inc_j->instrNo = instrNo;
    inc_j->iaddress = iaddress;

    inc_j->next = ij_head;
    ij_head = inc_j;
    ij_total++;
}

inline void patch_incomplete_jumps() {
    incomplete_jump* temp = ij_head;
    while (temp != NULL) {
        if (temp->iaddress == nextQuadLabel()) {
            instructions[temp->instrNo].result.val = nextInstructionLabel();
        } else {
            instructions[temp->instrNo].result.val = quads[temp->iaddress]->taddress;
        }

        temp = temp->next;
    }
}

// ------------------------------- generate ---------------------------------

inline void generate(vmopcode op, quad* q) {
    instruction* t = new instruction;
    t->opcode = op;
    t->scrLine = q->line;
    make_operand(q->arg1, &t->arg1);
    make_operand(q->arg2, &t->arg2);
    make_operand(q->result, &t->result);
    q->taddress = nextInstructionLabel();
    emitInstruction(t);
}

inline void generate_ADD(quad* q) {
    generate(add_v, q);
}

inline void generate_SUB(quad* q) {
    generate(sub_v, q);
}

inline void generate_MUL(quad* q) {
    generate(mul_v, q);
}

inline void generate_DIV(quad* q) {
    generate(div_v, q);
}

inline void generate_MOD(quad* q) {
    generate(mod_v, q);
}

inline void generate_NEWTABLE(quad* q) {
    generate(newtable_v, q);
}

inline void generate_TABLEGETELEM(quad* q) {
    generate(tablegetelem_v, q);
}

inline void generate_TABLESETELEM(quad* q) {
    generate(tablesetelem_v, q);
}

inline void generate_ASSIGN(quad* q) {
    generate(assign_v, q);
}

inline void generate_NOP(quad* q) {
    instruction* t = new instruction;
    t->opcode = nop_v;
    emitInstruction(t);
}

inline void generate_relational(vmopcode op, quad* q) {
    instruction* t = new instruction;
    t->opcode = op;
    t->scrLine = q->line;
    make_operand(q->arg1, &t->arg1);
    make_operand(q->arg2, &t->arg2);

    t->result.type = label_a;
    t->result.empty = false;

    auto itr = find(quads.begin(), quads.end(), q);
    if (q->label < std::distance(quads.begin(), itr)) {
        t->result.val = quads[q->label]->taddress;
    } else {
        add_incomplete_jump(nextInstructionLabel(), q->label);
    }
    q->taddress = nextInstructionLabel();
    emitInstruction(t);
}

inline void generate_JUMP(quad* q) {
    generate_relational(jump_v, q);
}

inline void generate_IF_EQ(quad* q) {
    generate_relational(jeq_v, q);
}

inline void generate_IF_NOTEQ(quad* q) {
    generate_relational(jne_v, q);
}

inline void generate_IF_GREATER(quad* q) {
    generate_relational(jgt_v, q);
}

inline void generate_IF_GREATEREQ(quad* q) {
    generate_relational(jge_v, q);
}

inline void generate_IF_LESS(quad* q) {
    generate_relational(jlt_v, q);
}

inline void generate_IF_LESSEQ(quad* q) {
    generate_relational(jle_v, q);
}

inline void generate_NOT(quad* q) {
    generate(not_v, q);
}   

inline void generate_AND(quad* q) {
    generate(and_v, q);
}

inline void generate_OR(quad* q) {
    generate(or_v, q);
}

inline void generate_PARAM(quad* q) {
    q->taddress = nextInstructionLabel();
    instruction* t = new instruction;
    t->opcode = pusharg_v;
    // make_operand(q->arg1, &t->arg1);
    make_operand(q->result, &t->result);
    emitInstruction(t);
}

inline void generate_CALL(quad* q) {
    q->taddress = nextInstructionLabel();
    instruction* t = new instruction;
    t->opcode = call_v;
    // make_operand(q->arg1, &t->arg1);
    make_operand(q->result, &t->result);
    if (t->result.type != libfunc_a) {
        t->result.type = userfunc_a;
    }
    emitInstruction(t);
}

inline void generate_GETRETVAL(quad* q) {
    q->taddress = nextInstructionLabel();
    instruction* t = new instruction;
    t->opcode = assign_v;
    make_operand(q->result, &t->result);
    make_retvaloperand(&t->arg1);
    emitInstruction(t);
}

inline void generate_FUNCSTART(quad* q) {
    node* f = q->result->sym;
    f->taddress = nextInstructionLabel();
    q->taddress = nextInstructionLabel();

    funcStack.push(f);

    instruction* t = new instruction;
    t->opcode = funcenter_v;
    make_operand(q->result, &t->result);
    emitInstruction(t);
}

inline void generate_FUNCEND(quad* q) {
    node* f = funcStack.top();
    while (!f->returnStack.empty()) {
        instructions[f->returnStack.top()].result.val = nextInstructionLabel();
        f->returnStack.pop();
    }
    funcStack.pop();

    q->taddress = nextInstructionLabel();
    instruction* t = new instruction;
    t->opcode = funcexit_v;
    make_operand(q->result, &t->result);
    emitInstruction(t);
}   

inline void generate_RETURN(quad* q) {
    q->taddress = nextInstructionLabel();
    instruction* t = new instruction;
    t->opcode = assign_v;
    make_retvaloperand(&t->result);
    make_operand(q->arg1, &t->arg2);
    emitInstruction(t);

    node* f = funcStack.top();
    f->returnStack.push(nextInstructionLabel());

    t->opcode = jump_v;
    reset_operand(&t->arg1);
    reset_operand(&t->arg1);
    t->result.type = label_a;
    t->result.empty = false;
    emitInstruction(t);
}

typedef void (*generator_func_t)(quad*);

generator_func_t generators[] = {
    generate_ASSIGN,
    generate_ADD,
    generate_SUB,
    generate_MUL,
    generate_DIV,
    generate_MOD,
    generate_NOP,
    generate_AND,
    generate_OR,
    generate_NOT,
    generate_IF_EQ,
    generate_IF_NOTEQ,
    generate_IF_LESSEQ,
    generate_IF_GREATEREQ,
    generate_IF_LESS,
    generate_IF_GREATER,
    generate_CALL,
    generate_PARAM,
    generate_RETURN,
    generate_GETRETVAL,
    generate_FUNCSTART,
    generate_FUNCEND,
    generate_NEWTABLE,
    generate_JUMP,
    generate_TABLEGETELEM,
    generate_TABLESETELEM
};

inline string vmargToString(vmarg arg) {
    if (arg.empty == true) {
        return " ";
    } else {
        string s = to_string(arg.type) + "(" + vmarg_tToString[arg.type] + "), " + to_string(arg.val);
        return s;
    }
} 

inline void printInstructions() {
    for (int i = 0; i < instructions.size(); i++) {
        cout << setw(10) << left << to_string(i) + ":" << setw(20) << left << vmopcodeToString[instructions[i].opcode] << setw(30) << vmargToString(instructions[i].result) << setw(30) << left 
        << vmargToString(instructions[i].arg1) << setw(30) << left << vmargToString(instructions[i].arg2) << endl;
    }
}

inline void printTables() {
    cout << "------------ Number Table ------------\n";
    for (int i = 0; i < numConsts.size(); i++) {
        cout << i << ": " << numConsts[i] << endl;
    }

    cout << "\n------------ String Table ------------\n";
    for (int i = 0; i < currStrConst; i++) {
        cout << i << ": " << stringConsts[i] << endl;
    }
    cout << "\n\n";

    cout << "\n------------ User function Table ------------\n";
    for (int i = 0; i < currUserFunc; i++) {
        cout << i << ": " << userFuncs[i].id << endl;
    }
    cout << "\n\n";

    cout << "\n------------ Library function Table ------------\n";
    for (int i = 0; i < currLibFunc; i++) {
        cout << i << ": " << namedLibFuncs[i] << endl;
    }
    cout << "\n\n";
}

inline void create_binary_file() {
    ofstream myfile;
    myfile.open("avm_binary.abc");

    // magic number
    myfile << "340200501\n";

    myfile << programVarOffset << endl;

    // String table
    myfile << to_string(stringConsts.size());
    for (int i = 0; i < stringConsts.size(); i++) {
        myfile << " " + stringConsts[i]; 
    }
    myfile << endl;

    // Number table
    myfile << to_string(numConsts.size());
    for (int i = 0; i < numConsts.size(); i++) {
        myfile << " " + to_string(numConsts[i]); 
    }
    myfile << endl;

    // User functions table
    myfile << to_string(userFuncs.size());
    for (int i = 0; i < userFuncs.size(); i++) {
        myfile << " " + to_string(userFuncs[i].address) << "," << to_string(userFuncs[i].localSize) << "," << userFuncs[i].id; 
    }
    myfile << endl;

    // Library functions table
    for (int i = 0; i < namedLibFuncs.size(); i++) {
        myfile << namedLibFuncs[i] + " "; 
    }
    myfile << endl;

    // Code (instructions)
    myfile << instructions.size() << endl;
    for (int i = 0; i < instructions.size(); i++) {
        if (instructions[i].result.empty) {
            instructions[i].result.val = 0;
            instructions[i].result.type = number_a;
        }
        if (instructions[i].arg1.empty) {
            instructions[i].arg1.val = 0;
            instructions[i].arg1.type = number_a;
        }
        if (instructions[i].arg2.empty) {
            instructions[i].arg2.val = 0;
            instructions[i].arg2.type = number_a;
        }
        myfile << to_string(instructions[i].opcode) << " " 
               << to_string(instructions[i].result.type) << "," << to_string(instructions[i].result.val) << " "
               << to_string(instructions[i].arg1.type) << "," << to_string(instructions[i].arg1.val) << " "
               << to_string(instructions[i].arg2.type) << "," << to_string(instructions[i].arg2.val) << endl;
    }

    myfile.close();
}

void generateAll() {
    for (unsigned i = 0; i < total; i++) {
        (*generators[quads[i]->op])(quads[i]);
    }

    patch_incomplete_jumps();

    printTables();

    printInstructions();

    create_binary_file();
}