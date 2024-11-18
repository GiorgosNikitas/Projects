#include <iostream>
#include <string>
#include <vector>
#include <assert.h>
#include <algorithm>
#include <map>
#include <iomanip>
#include <assert.h>
#include <math.h>
#include <sstream>
#include <cstring>
#include "targetCode.h"

#define PI 3.141592654

using namespace std;

#define AVM_WIPEOUT(m) memset(&(m), 0, sizeof(m))

unsigned char executionFinished = 0;
unsigned pc = 0;
unsigned currLine = 0;
unsigned codeSize = 0;
instruction* code = nullptr;
int magicNumber;

#define AVM_ENDING_PC codeSize

#define AVM_STACKENV_SIZE 4
#define AVM_STACKSIZE 4096

#define AVM_NUMACTUALS_OFFSET 4
#define AVM_SAVEDPC_OFFSET 3
#define AVM_SAVEDTOP_OFFSET 2
#define AVM_SAVEDTOPSP_OFFSET 1

#define execute_add execute_arithmetic
#define execute_sub execute_arithmetic
#define execute_mul execute_arithmetic
#define execute_div execute_arithmetic
#define execute_mod execute_arithmetic

avm_memcell ax, bx, cx;
avm_memcell retval;

inline void read_from_binary() {
    ifstream myfile;
    myfile.open("avm_binary.abc");

    if (!myfile.is_open()) {
        cerr << "File 'avm_binary.abc' not found!\n"; 
        return; 
    }

    int lineCounter = 0;
    string line;
    while (getline(myfile, line)) {
        if (lineCounter == 0) {
            magicNumber = stoi(line);
        } else if (lineCounter == 1) {
            programVarOffset = stoi(line);
        } else if (lineCounter == 2) {
            string in;
            stringstream space(line);
            int inCounter = 0;
            while (getline(space, in, ' ')) {
                if (inCounter == 0) {
                    currStrConst = stoi(in);
                } else {
                    stringConsts.push_back(in);
                }
                
                inCounter++;
            }
        } else if (lineCounter == 3) {
            string in;
            stringstream space(line);
            int inCounter = 0;
            while (getline(space, in, ' ')) {
                if (inCounter == 0) {
                    currNumConst = stoi(in);
                } else {
                    double d = stod(in);
                    numConsts.push_back(stod(in));
                }
                
                inCounter++;
            }
        } else if (lineCounter == 4) {
            string in;
            stringstream space(line);
            int inCounter = 0;
            while (getline(space, in, ' ')) {
                if (inCounter == 0) {
                    currUserFunc = stoi(in);
                } else {
                    stringstream comma(in);
                    string field;
                    userfunc u;

                    getline(comma, field, ',');
                    u.address = stoi(field);

                    getline(comma, field, ',');
                    u.localSize = stoi(field);

                    getline(comma, field, ',');
                    u.id = field;

                    userFuncs.push_back(u);
                }
                
                inCounter++;
            }
        } else if (lineCounter == 5) {
            string in;
            stringstream space(line);
            while (getline(space, in, ' ')) {
                namedLibFuncs.push_back(in);
            }
            currLibFunc = namedLibFuncs.size();
        } else if (lineCounter == 6) {
            codeSize = stoi(line);
        } else {
            string in;
            stringstream space(line);
            int inCounter = 0;
            instruction* t = new instruction;
            while (getline(space, in, ' ')) {
                if (inCounter == 0) {
                    t->opcode = static_cast<vmopcode>(stoi(in));
                } else {
                    stringstream comma(in);
                    string field;
                    vmarg* temp;
                    if (inCounter == 1) {
                        temp = &t->result;
                    } else if (inCounter == 2) {
                        temp = &t->arg1;
                    } else if (inCounter == 3) {
                        temp = &t->arg2;
                    } else {
                        assert(0);
                    }

                    getline(comma, field, ',');
                    temp->type = static_cast<vmarg_t>(stoi(field));

                    getline(comma, field, ',');
                    temp->val = stoi(field);

                    temp->empty = false;
                }
                
                inCounter++;
            }
            instructions.push_back(*t);
        }
        lineCounter++;
    }

    myfile.close();
}

string typeStrings[] = {
    "number",
    "string",
    "bool",
    "table",
    "userfunc",
    "libfunc",
    "nil",
    "undef"
};

unsigned top, topsp;

avm_memcell avmstack[AVM_STACKSIZE];

// Reverse translation for constants:
// getting constant value from index.
inline double consts_getnumber(unsigned index) {
    return numConsts[index];
}

inline string consts_getstring(unsigned index) {
    return stringConsts[index];
}

inline string libfuncs_getused(unsigned index) {
    return namedLibFuncs[index];
}

inline userfunc* userfuncs_getfunc(unsigned index) {
    return &userFuncs[index];
}

inline void avm_error(string format) {
    cerr << format << endl;
    executionFinished = 1;
}

inline void avm_warning(string format) {
    cerr << format << endl;
}

inline avm_memcell* avm_translate_operand(vmarg* arg, avm_memcell* reg) {
    string s;
    switch(arg->type) {
        case global_a:
            return &avmstack[AVM_STACKSIZE - 1 - arg->val];
            break;
        
        case local_a:
            return &avmstack[topsp - arg->val];
            break;

        case formal_a:
            return &avmstack[topsp + AVM_STACKENV_SIZE + 1 + arg->val];
            break;

        case retval_a:
            return &retval;
            break;

        case number_a:
            reg->type = number_m;
            reg->data.numVal = consts_getnumber(arg->val);
            return reg;
            break;

        case string_a:
            reg->type = string_m;
            s = consts_getstring(arg->val);
            reg->data.strVal = new char[s.size() + 1];
            strcpy(reg->data.strVal, s.c_str());
            return reg;
            break;

        case bool_a:
            reg->type = bool_m;
            reg->data.boolVal = arg->val;
            return reg;

        case nil_a:
            reg->type = nil_m;
            return reg;
            break;

        case userfunc_a:
            reg->type = userfunc_m;
            // reg->data.funcVal = arg->val;
            reg->data.funcVal = userfuncs_getfunc(arg->val)->address;
            return reg;
            break;
    
        case libfunc_a:
            reg->type = libfunc_m;
            s = libfuncs_getused(arg->val);
            reg->data.libfuncVal = new char[s.size() + 1];
            strcpy(reg->data.libfuncVal, s.c_str());
            return reg;
            break;

        default:
            assert(0);
            break;
    }

    return nullptr;
}

unsigned totalActuals = 0;

inline void avm_dec_top() {
    if (!top) {
        avm_error("Stack overflow");
        executionFinished = 1;
    } else {
        --top;
    }
}

inline void avm_push_envvalue(unsigned val) {
    avmstack[top].type = number_m;
    avmstack[top].data.numVal = val;
    avm_dec_top();
}

inline void avm_callenvironment(void) {
    avm_push_envvalue(totalActuals);
    // assert(code[pc].opcode == call_v);
    assert(instructions[pc].opcode == call_v);
    avm_push_envvalue(pc+1);
    avm_push_envvalue(top + totalActuals + 2);
    avm_push_envvalue(topsp);
}

inline userfunc* avm_getfuncinfo(unsigned address) {
    for (int i = 0; i < userFuncs.size(); i++) {
        if (userFuncs[i].address == address) {
            return &userFuncs[i];
        }
    }

    return nullptr;
}

inline unsigned avm_get_envvalue(unsigned i) {
    assert(avmstack[i].type == number_m);
    unsigned val = (unsigned) avmstack[i].data.numVal;
    assert(avmstack[i].data.numVal == ((double)val));
    return val;
}

inline unsigned avm_totalactuals() {
    return avm_get_envvalue(topsp + AVM_NUMACTUALS_OFFSET);
}

inline avm_memcell* avm_getactual(unsigned i) {
    assert(i < avm_totalactuals()); // danger
    return &avmstack[topsp + AVM_STACKENV_SIZE + i + 1];
}

//-----------------------------------------------------------------------
typedef string (*tostring_func_t)(avm_memcell*);

inline string avm_tostring(avm_memcell* m);

inline string number_tostring(avm_memcell* m) {
    return to_string(m->data.numVal);
}

inline string string_tostring(avm_memcell* m) {
    return string(m->data.strVal);
}

inline string bool_tostring(avm_memcell* m) {
    return m->data.boolVal == true ? "true" : "false";
}

inline string table_tostring(avm_memcell* m) {
    string s = "[";
    for (int i = 0; i < AVM_TABLE_HASHSIZE; i++) {
        avm_table_bucket* iter = m->data.tableVal->strIndexed[i];
        
        while (iter) {
            s += "{\"" + string(iter->key.data.strVal) + "\" : " + avm_tostring(&iter->value) + "}";
            iter = iter->next;
        }

        iter = m->data.tableVal->numIndexed[i];
        while (iter) {
            s += "{\"" + to_string(iter->key.data.numVal) + "\" : " + avm_tostring(&iter->value) + "}";
            iter = iter->next;
        }
    }

    return s + "]";
}

inline string userfunc_tostring(avm_memcell* m) {
    string s = "User function: " + to_string(userFuncs[m->data.funcVal].address);
    return s;
}

inline string libfunc_tostring(avm_memcell* m) {
    string s = "Library function: " + string(m->data.libfuncVal);
    return s;
}

inline string nil_tostring(avm_memcell* m) {
    return "nil";
}

inline string undef_tostring(avm_memcell* m) {
    return "undef";
}

tostring_func_t tostringFuncs[] = {
    number_tostring,
    string_tostring,
    bool_tostring,
    table_tostring,
    userfunc_tostring,
    libfunc_tostring,
    nil_tostring,
    undef_tostring
}; 

inline string avm_tostring(avm_memcell* m) {
    assert(m->type >= 0 && m->type <= undef_m);
    return (*tostringFuncs[m->type])(m);
}

// Libfuncs

inline void libfunc_print() {
    unsigned n = avm_totalactuals();
    for (unsigned i = 0; i < n; ++i) {
        string s = avm_tostring(avm_getactual(i));
        cout << s << endl;
    }
    avm_memcellclear(&retval);
}

inline void libfunc_input() {
    unsigned n = avm_totalactuals();
    if (n != 0) {
        avm_error("'input' takes 0 arguments!");
    }
    string input;
    getline(cin, input);

    if (input == "true") {
        retval.type = bool_m;
        retval.data.boolVal = true;
    } else if (input == "false") {
        retval.type = bool_m;
        retval.data.boolVal = false;
    } else if (input.empty()) {
        retval.type = nil_m;
    } else {
        retval.type = string_m;
        retval.data.strVal = new char[input.size() + 1];
        strcpy(retval.data.strVal, input.c_str());
    }
}

inline void libfunc_objectmemberkeys() {}

inline void libfunc_objecttotalmembers() {}

inline void libfunc_objectcopy() {}

inline void libfunc_totalarguments() {}

inline void libfunc_argument() {}

inline void libfunc_typeof() {
    unsigned n = avm_totalactuals();

    if (n != 1) {
        retval.type = nil_m;
        avm_error("One argument (not " + to_string(n) + ") expected in 'typeof'!" );
    } else {
        avm_memcellclear(&retval);
        retval.type = string_m;
        string s = typeStrings[avm_getactual(0)->type];
        retval.data.strVal = new char[s.size() + 1];
        strcpy(retval.data.strVal, s.c_str());
    }
}

inline void libfunc_strtonum() {
    unsigned n = avm_totalactuals();

    if (n != 1) {
        retval.type = nil_m;
        avm_error("One argument (not " + to_string(n) + ") expected in 'strtonum'!" );
    } else {
        if (stod(avm_getactual(0)->data.strVal)) {
            retval.type = number_m;
            retval.data.numVal = stod(avm_getactual(0)->data.strVal);
        }
    }
}

inline void libfunc_sqrt() {
    unsigned n = avm_totalactuals();

    if (n != 1) {
        retval.type = nil_m;
        avm_error("One argument (not " + to_string(n) + ") expected in 'sqrt'!" );
    } else {
        avm_memcell* temp = avm_getactual(0);
        if (temp->type != number_m) {
            retval.type = nil_m;
            avm_error("'sqrt' expected number as argument not " + typeStrings[temp->type] + "!");
        } else {
            if (temp->data.numVal < 0) {
                retval.type = nil_m;
                avm_error("'sqrt' expected number argument >= 0 but got" + to_string(temp->data.numVal) + "!");
            } else {
                retval.type = number_m;
                retval.data.numVal = sqrt(temp->data.numVal);
            }
        }
    }
}

inline void libfunc_cos() {
    unsigned n = avm_totalactuals();

    if (n != 1) {
        retval.type = nil_m;
        avm_error("One argument (not " + to_string(n) + ") expected in 'cos'!" );
    } else {
        avm_memcell* temp = avm_getactual(0);
        if (temp->type != number_m) {
            retval.type = nil_m;
            avm_error("'cos' expected number as argument not " + typeStrings[temp->type] + "!");
        } else {
            double rad = (temp->data.numVal * PI) / 180;
            retval.type = number_m;
            retval.data.numVal = cos(rad);
        }
    }
}

inline void libfunc_sin() {
    unsigned n = avm_totalactuals();

    if (n != 1) {
        retval.type = nil_m;
        avm_error("One argument (not " + to_string(n) + ") expected in 'sin'!" );
    } else {
        avm_memcell* temp = avm_getactual(0);
        if (temp->type != number_m) {
            retval.type = nil_m;
            avm_error("'sin' expected number as argument not " + typeStrings[temp->type] + "!");
        } else {
            double rad = (temp->data.numVal * PI) / 180;
            retval.type = number_m;
            retval.data.numVal = sin(rad);
        }
    }
}

typedef void (*library_func_t)();

// inline void avm_registerlibfunc(string id, library_func_t addr) {

// }

inline library_func_t avm_getlibraryfunc(string id) {
    if (id == "print") {
        return libfunc_print;
    } else if (id == "input") {
        return libfunc_input;
    } else if (id == "objectmemberkeys") {
        return libfunc_objectmemberkeys;
    } else if (id == "objecttotalmembers") {
        return libfunc_objecttotalmembers;
    } else if (id == "objectcopy") {
        return libfunc_objectcopy;
    } else if (id == "totalarguments") {
        return libfunc_totalarguments;
    } else if (id == "argument") {
        return libfunc_argument;
    } else if (id == "typeof") {
        return libfunc_typeof;
    } else if (id == "strtonum") {
        return libfunc_strtonum;
    } else if (id == "sqrt") {
        return libfunc_sqrt;
    } else if (id == "cos") {
        return libfunc_cos;
    } else if (id == "sin") {
        return libfunc_sin;
    } else {
        assert(0);
    }
}

inline void avm_push_table_arg(avm_table* t) {
    avmstack[top].type = table_m;
    avm_tableincrefcounter(avmstack[top].data.tableVal = t);
    ++totalActuals;
    avm_dec_top();
}

typedef unsigned char (*tobool_func_t) (avm_memcell*);

inline unsigned char number_tobool(avm_memcell* m) {
    return m->data.numVal != 0;
}

inline unsigned char string_tobool(avm_memcell* m) {
    return !(m->data.strVal == nullptr);
}

inline unsigned char bool_tobool(avm_memcell* m) {
    return m->data.boolVal;
}

inline unsigned char table_tobool(avm_memcell* m) {
    return 1;
}

inline unsigned char userfunc_tobool(avm_memcell* m) {
    return 1;
}

inline unsigned char libfunc_tobool(avm_memcell* m) {
    return 1;
}

inline unsigned char nil_tobool(avm_memcell* m) {
    return 0;
}

inline unsigned char undef_tobool(avm_memcell* m) {
    return 0;
}

tobool_func_t toboolFuncs[] = {
    number_tobool,
    string_tobool,
    bool_tobool,
    table_tobool,
    userfunc_tobool,
    libfunc_tobool,
    nil_tobool,
    undef_tobool
};

inline unsigned char avm_tobool(avm_memcell* m) {
    assert(m->type >= 0 && m->type < undef_m);
    return (*toboolFuncs[m->type])(m);
}

inline avm_memcell* avm_tablegetelem(avm_table* table, avm_memcell* index) {
    assert(index->type == number_m || index->type == string_m);
    if (index->type == number_m) {
        int intIndex = avm_intindex(index->data.numVal);
        avm_table_bucket* iter = table->numIndexed[intIndex];

        while (iter) {
            if (iter->key.data.numVal == index->data.numVal) {
                return &iter->value;
            }

            iter = iter->next;
        }

        avm_warning("Table item with index: " + to_string(index->data.numVal) + " not found!");
    } else {
        int strIndex = avm_stringindex(string(index->data.strVal));
        avm_table_bucket* iter = table->strIndexed[strIndex];

        while (iter) {
            if (string(iter->key.data.strVal) == string(index->data.strVal)) {
                return &iter->value;
            }

            iter = iter->next;
        }

        avm_warning("Table item with index: \"" + string(index->data.strVal) + "\" not found!");
    }

    return nullptr;
}

void avm_tablesetelem(avm_table* table, avm_memcell* index, avm_memcell* content) {
    avm_table_bucket* p = new avm_table_bucket;
    unsigned i = 0;
    if (index->type == number_m) {
        i = avm_intindex((int)index->data.numVal);
        p->next = table->numIndexed[i];
        table->numIndexed[i] = p;
    } else if (index->type == string_m) {
        i = avm_stringindex(index->data.strVal);
        p->next = table->strIndexed[i];
        table->strIndexed[i] = p;
    } else {
        avm_error("Table index has to be int or string!");
    }
    p->key = *index;
    memcpy(&p->value, content, sizeof(avm_memcell));
    ++table->total;
}


inline void avm_assign(avm_memcell* lv, avm_memcell* rv) {
    if (lv == rv) {
        return;
    }

    if (lv->type == table_m && 
        rv->type == table_m &&
        lv->data.tableVal == rv->data.tableVal) {
        return;
    }

    if (rv->type == undef_m) {
        avm_warning("Assigning from 'undef' content!");
    }   

    avm_memcellclear(lv);

    memcpy(lv, rv, sizeof(avm_memcell));

    if (lv->type == string_m) {
        strcpy(lv->data.strVal, rv->data.strVal);
    } else if (lv->type == table_m) {
        avm_tableincrefcounter(lv->data.tableVal);
    }
}

// DISPATCHER
typedef void (*execute_func_t) (instruction*);

#define AVM_MAX_INSTRUCTIONS nop_v;

inline void execute_assign(instruction* instr) {
    avm_memcell* lv = avm_translate_operand(&instr->result, nullptr);
    avm_memcell* rv = avm_translate_operand(&instr->arg1, &ax);

    // assert(lv && (&avmstack[AVM_STACKENV_SIZE-1] >= lv && lv > &avmstack[top] || lv == &retval));
    // assert(rv && (&avmstack[AVM_STACKENV_SIZE-1] >= rv && rv > &avmstack[top] || rv == &retval));

    avm_assign(lv, rv);
}

inline void execute_add(instruction* instr);

inline void execute_sub(instruction* instr);

inline void execute_mul(instruction* instr);

inline void execute_div(instruction* instr);

inline void execute_mod(instruction* instr);

inline void execute_uminus(instruction* t) {}

inline void execute_and(instruction* instr) {
    avm_memcell* lv = avm_translate_operand(&instr->result, nullptr);
    avm_memcell* rv1 = avm_translate_operand(&instr->arg1, &ax);
    avm_memcell* rv2 = avm_translate_operand(&instr->arg2, &bx);

    // assert(lv && (&avmstack[AVM_STACKENV_SIZE - 1] >= lv && lv > &avmstack[top] || lv == &retval));
    assert(rv1 && rv2);
    
    avm_memcellclear(lv);
    lv->type = bool_m;
    lv->data.boolVal = avm_tobool(rv1) && avm_tobool(rv2);
}

inline void execute_or(instruction* instr) {
    avm_memcell* lv = avm_translate_operand(&instr->result, nullptr);
    avm_memcell* rv1 = avm_translate_operand(&instr->arg1, &ax);
    avm_memcell* rv2 = avm_translate_operand(&instr->arg2, &bx);

    // assert(lv && (&avmstack[AVM_STACKENV_SIZE - 1] >= lv && lv > &avmstack[top] || lv == &retval));
    assert(rv1 && rv2);

    avm_memcellclear(lv);
    lv->type = bool_m;
    lv->data.boolVal = avm_tobool(rv1) || avm_tobool(rv2);
}

inline void execute_not(instruction* instr) {
    avm_memcell* lv = avm_translate_operand(&instr->result, nullptr);
    avm_memcell* rv1 = avm_translate_operand(&instr->arg1, &ax);

    // assert(lv && (&avmstack[AVM_STACKENV_SIZE - 1] >= lv && lv > &avmstack[top] || lv == &retval));
    assert(rv1);
    
    avm_memcellclear(lv);
    lv->type = bool_m;
    lv->data.boolVal = !avm_tobool(rv1);
}

inline void execute_jeq(instruction* instr) {
    assert(instr->result.type == label_a);

    avm_memcell* rv1 = avm_translate_operand(&instr->arg1, &ax);
    avm_memcell* rv2 = avm_translate_operand(&instr->arg2, &bx);

    bool result = 0;

    if (rv1->type == undef_m || rv2->type == undef_m) {
        avm_error("'undef' involved in equality!"); 
    } else {
        if (rv1->type == bool_m || rv2->type == bool_m) {
            result = (avm_tobool(rv1) == avm_tobool(rv2));
        } else if (rv1->type == nil_m || rv2->type == nil_m) {
            result = rv1->type == nil_m && rv2->type == nil_m;
        } else if (rv1->type != rv2->type) {
            avm_error(typeStrings[rv1->type] + " == " + typeStrings[rv2->type] + " is illegal!");
        } else {
            result = (toboolFuncs[rv1->type] == toboolFuncs[rv1->type]);
        }
    }
    if (!executionFinished && result) {
        pc = instr->result.val;
    }
}

inline void execute_jne(instruction* instr) {
    assert(instr->result.type == label_a);

    avm_memcell* rv1 = avm_translate_operand(&instr->arg1, &ax);
    avm_memcell* rv2 = avm_translate_operand(&instr->arg2, &bx);

    bool result = 0;

    if (rv1->type == undef_m || rv2->type == undef_m) {
        avm_error("'undef' involved in inequality!"); 
    } else {
        if (rv1->type == bool_m || rv2->type == bool_m) {
            result = (avm_tobool(rv1) != avm_tobool(rv2));
        } else if (rv1->type == nil_m || rv2->type == nil_m) {
            result = rv1->type == nil_m && rv2->type == nil_m; // ??????????????????????//
        } else 
        if (rv1->type != rv2->type) {
            avm_error(typeStrings[rv1->type] + " != " + typeStrings[rv2->type] + " is illegal!");
        } else {
            result = (toboolFuncs[rv1->type] != toboolFuncs[rv1->type]);
        }
    }
    if (!executionFinished && result) {
        pc = instr->result.val;
    }
}

inline void execute_jle(instruction* instr) {
    assert(instr->result.type == label_a);

    avm_memcell* rv1 = avm_translate_operand(&instr->arg1, &ax);
    avm_memcell* rv2 = avm_translate_operand(&instr->arg2, &bx);

    bool result = 0;

    if (rv1->type == undef_m || rv2->type == undef_m) {
        avm_error("'undef' involved in comparison!"); 
    } else {
        if (rv1->type != rv2->type) {
            avm_error(typeStrings[rv1->type] + " <= " + typeStrings[rv2->type] + " is illegal!");
        } else {
            if (rv1->type == number_m && rv2->type == number_m) {
                result = rv1->data.numVal <= rv2->data.numVal;
            } else {
                avm_error(typeStrings[rv1->type] + " <= " + typeStrings[rv2->type] + " is illegal!");
            }            
        }
    }
    if (!executionFinished && result) {
        pc = instr->result.val;
    }
}

inline void execute_jge(instruction* instr) {
    assert(instr->result.type == label_a);

    avm_memcell* rv1 = avm_translate_operand(&instr->arg1, &ax);
    avm_memcell* rv2 = avm_translate_operand(&instr->arg2, &bx);

    bool result = 0;

    if (rv1->type == undef_m || rv2->type == undef_m) {
        avm_error("'undef' involved in comparison!"); 
    } else {
        if (rv1->type != rv2->type) {
            avm_error(typeStrings[rv1->type] + " >= " + typeStrings[rv2->type] + " is illegal!");
        } else {
            if (rv1->type == number_m && rv2->type == number_m) {
                result = rv1->data.numVal >= rv2->data.numVal;
            } else {
                avm_error(typeStrings[rv1->type] + " >= " + typeStrings[rv2->type] + " is illegal!");
            }            
        }
    }
    if (!executionFinished && result) {
        pc = instr->result.val;
    }
}

inline void execute_jlt(instruction* instr) {
    assert(instr->result.type == label_a);

    avm_memcell* rv1 = avm_translate_operand(&instr->arg1, &ax);
    avm_memcell* rv2 = avm_translate_operand(&instr->arg2, &bx);

    bool result = 0;

    if (rv1->type == undef_m || rv2->type == undef_m) {
        avm_error("'undef' involved in comparison!"); 
    } else {
        if (rv1->type != rv2->type) {
            avm_error(typeStrings[rv1->type] + " < " + typeStrings[rv2->type] + " is illegal!");
        } else {
            if (rv1->type == number_m && rv2->type == number_m) {
                result = rv1->data.numVal < rv2->data.numVal;
            } else {
                avm_error(typeStrings[rv1->type] + " < " + typeStrings[rv2->type] + " is illegal!");
            }            
        }
    }

    if (!executionFinished && result) {
        pc = instr->result.val;
    }
}

inline void execute_jgt(instruction* instr) {
    assert(instr->result.type == label_a);

    avm_memcell* rv1 = avm_translate_operand(&instr->arg1, &ax);
    avm_memcell* rv2 = avm_translate_operand(&instr->arg2, &bx);
    bool result = 0;

    if (rv1->type == undef_m || rv2->type == undef_m) {
        avm_error("'undef' involved in comparison!"); 
    } else {
        if (rv1->type != rv2->type) {
            avm_error(typeStrings[rv1->type] + " > " + typeStrings[rv2->type] + " is illegal!");
        } else {
            if (rv1->type == number_m && rv2->type == number_m) {
                result = rv1->data.numVal > rv2->data.numVal;
            } else {
                avm_error(typeStrings[rv1->type] + " > " + typeStrings[rv2->type] + " is illegal!");
            }            
        }
    }
    if (!executionFinished && result) {
        pc = instr->result.val;
    }
}

inline void execute_funcexit(instruction instr); // ignore

inline void avm_calllibfunc(string id) {
    library_func_t f = avm_getlibraryfunc(id);
    if (!f) {
        avm_error("Unsupported libfunc " + id + " called!");
        executionFinished = 1;
    } else {
        avm_callenvironment();
        topsp = top;
        totalActuals = 0;
        (*f)();
        if (!executionFinished) {
            // execute_funcexit(nullptr);
        }
    }
}

inline void avm_callsaveenvironment() {
    avm_push_envvalue(totalActuals);
    avm_push_envvalue(pc + 1);
    avm_push_envvalue(top + totalActuals + 2);
    avm_push_envvalue(topsp);
}

inline void avm_call_functor(avm_table* t);

inline void execute_call(instruction* instr) {
    avm_memcell* func = avm_translate_operand(&instr->result, &ax);
    assert(func);
    if (instr->result.type != libfunc_a) {
        instr->result.type = userfunc_a;
    }
    switch (func->type) {
        case userfunc_m:
            avm_callsaveenvironment();
            pc = func->data.funcVal + 3;
            // assert(pc < AVM_ENDING_PC);
            // assert(code[pc].opcode == funcenter_v);
            assert(instructions[pc].opcode == funcenter_v);
            break;

        case string_m:
            avm_calllibfunc(string(func->data.strVal));
            break;

        case libfunc_m: 
            avm_calllibfunc(string(func->data.libfuncVal));
            break;

        case table_m:
            // avm_call_functor(func->data.tableVal);
            break;

        default: 
            string s = avm_tostring(func);
            // cerr << "Call: cannot bind " << s << " to function!";
            executionFinished = 1;
            break;
    }
}

inline void execute_pusharg(instruction* instr) {
    avm_memcell* arg = avm_translate_operand(&instr->result, &ax);
    assert(arg);

    avm_assign(&avmstack[top], arg);
    ++totalActuals;
    avm_dec_top();
}

inline void execute_funcenter(instruction* instr) {
    avm_memcell* func = avm_translate_operand(&instr->result, &ax);
    assert(func);
    assert(pc == func->data.funcVal);

    // Callee actions
    totalActuals = 0;
    userfunc* funcInfo = avm_getfuncinfo(pc);
    topsp = top;
    top = top - funcInfo->localSize;
}

inline void execute_funcexit(instruction* t) {
    unsigned oldTop = top;
    top = avm_get_envvalue(topsp + AVM_SAVEDTOP_OFFSET);
    pc = avm_get_envvalue(topsp + AVM_SAVEDPC_OFFSET);
    topsp = avm_get_envvalue(topsp + AVM_SAVEDTOPSP_OFFSET);

    while(++oldTop <= top) { // ignore first
        avm_memcellclear(&avmstack[oldTop]);
    }
}

inline void execute_newtable(instruction* instr) {
    avm_memcell* lv = avm_translate_operand(&instr->result, nullptr);
    // assert(lv && (&avmstack[AVM_STACKENV_SIZE - 1] >= lv && lv > &avmstack[top] || lv == &retval));

    avm_memcellclear(lv);

    lv->type = table_m;
    lv->data.tableVal = avm_tablenew();
    avm_tableincrefcounter(lv->data.tableVal);
}

inline void execute_tablegetelem(instruction* instr) {
    avm_memcell* lv = avm_translate_operand(&instr->result, nullptr);
    avm_memcell* t = avm_translate_operand(&instr->arg1, nullptr);
    avm_memcell* i = avm_translate_operand(&instr->arg2, &ax);

    // assert(lv && (&avmstack[AVM_STACKENV_SIZE-1] >= lv && lv > &avmstack[top] || lv == &retval));
    // assert(t && &avmstack[AVM_STACKENV_SIZE-1] >= t && t > &avmstack[top]);
    assert(i);

    avm_memcellclear(lv);
    lv->type = nil_m;

    if (t->type != table_m) {
        avm_error("Illegal use of type " + typeStrings[t->type] + " as table!");
    } else {
        avm_memcell* content = avm_tablegetelem(t->data.tableVal, i);
        if (content) {
            avm_assign(lv, content);
        } else {
            string ts = avm_tostring(t);
            string is = avm_tostring(i);
            avm_warning(ts + "[" + is + "] not found!");
        }
    }
}

inline void execute_tablesetelem(instruction* instr) {
    avm_memcell* t = avm_translate_operand(&instr->result,nullptr);
    avm_memcell* i = avm_translate_operand(&instr->arg1, &ax);
    avm_memcell* c = avm_translate_operand(&instr->arg2, &bx);

    // assert(t && &avmstack[AVM_STACKENV_SIZE-1] >= t && t > &avmstack[top]);
    assert(i && c);

    if (t->type != table_m) {
        avm_error("Illegal use of type " + typeStrings[t->type] + "as a table!");
    } else {
        avm_tablesetelem(t->data.tableVal, i, c);
    }
}

inline void execute_jump(instruction* instr) {
    pc = instr->result.val;
}

inline void execute_nop(instruction* instr) {}

typedef double (*arithmetic_func_t) (double x, double y);

inline double add_impl(double x, double y) {
    return x + y;
}

inline double sub_impl(double x, double y) {
    return x - y;
}

inline double mul_impl(double x, double y) {
    return x * y;
}

inline double div_impl(double x, double y) {
    return x / y;
}

inline double mod_impl(double x, double y) {
    return (unsigned)x % (unsigned)y;
}

arithmetic_func_t arithmeticFuncs[] = {
    add_impl,
    sub_impl,
    mul_impl,
    div_impl,
    mod_impl
};

inline void execute_arithmetic(instruction* instr) {
    avm_memcell* lv = avm_translate_operand(&instr->result, nullptr);
    avm_memcell* rv1 = avm_translate_operand(&instr->arg1, &ax);
    avm_memcell* rv2 = avm_translate_operand(&instr->arg2, &bx);

    // assert(lv && (&avmstack[AVM_STACKENV_SIZE - 1] >= lv && lv > &avmstack[top] || lv == &retval));
    assert(rv1 && rv2);

    if (rv1->type != number_m || rv2->type != number_m) {
        avm_error("Not a number in arithmetic!");
        executionFinished = 1;
    } else {
        arithmetic_func_t op = arithmeticFuncs[instr->opcode - add_v];
        avm_memcellclear(lv);
        lv->type = number_m;
        lv->data.numVal = (*op)(rv1->data.numVal, rv2->data.numVal);
    }
}

execute_func_t executeFuncs[] = {
    execute_assign,
    execute_add,
    execute_sub,
    execute_mul,
    execute_div,
    execute_mod,
    execute_uminus,
    execute_and,
    execute_or,
    execute_not,
    execute_jump,
    execute_jeq,
    execute_jne,
    execute_jle,
    execute_jge,
    execute_jlt,
    execute_jgt,
    execute_call,
    execute_pusharg,
    execute_funcenter,
    execute_funcexit,
    execute_newtable,
    execute_tablegetelem,
    execute_tablesetelem,
    execute_nop
};

inline void execute_cycle() {
    if (executionFinished) {
        return;
    }

    if (pc == AVM_ENDING_PC) {
        executionFinished = 1;
        return;
    } else {
        assert(pc < AVM_ENDING_PC);
        instruction* instr = &instructions[pc];
        assert(instr->opcode >= 0);
        assert(instr->opcode <= nop_v);

        // if (instr->scrLine) {
        //     currLine = instr->scrLine;
        // }
        unsigned oldPc = pc;
        (*executeFuncs[instr->opcode])(instr);
        if (pc == oldPc) {
            ++pc;
        }
    }
}