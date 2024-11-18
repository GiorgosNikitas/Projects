#include <iostream>
#include <cstdlib>
#include <string>
#include <vector>
#include <map>
#include <assert.h>
#include <stack>
#include <iomanip>
#include <fstream>
#include "symTable.h"

using namespace std;

typedef enum iopcode {
    assign,         add,            sub,
    mul,            div_,           mod,
    uminus,         and_,           or_,
    not_,           if_eq,          if_noteq,
    if_lesseq,      if_greatereq,   if_less,
    if_greater,     call,           param,
    ret,            getretval,      funcstart, 
    funcend,        tablecreate,    jump, 
    tablegetelem,   tablesetelem
} iopcode;

map<iopcode, string> opToString = {  { assign, "ASSIGN" }, { add, "ADD" }, { sub, "SUB" }, 
                                     { mul, "MUL" }, { div_, "DIV" }, { mod, "MOD" },
                                     { uminus, "UMINUS" }, { and_, "AND" }, { or_, "OR" },
                                     { not_, "NOT" }, { if_eq, "IF_EQ" }, { if_noteq, "IF_NOTEQ" },
                                     { if_lesseq, "IF_LESSEQ" }, { if_greatereq, "IF_GREATEREQ" }, { if_less, "IF_LESS" },
                                     { if_greater, "IF_GREATER" }, { call, "CALL" }, { param, "PARAM" },
                                     { ret, "RETURN" }, { getretval, "GETRETVAL" }, { funcstart, "FUNCSTART" },
                                     { funcend, "FUNCEND" }, { tablecreate, "TABLECREATE" }, { jump, "JUMP" },
                                     { tablegetelem, "TABLEGETELEM" }, { tablesetelem, "TABLESETELEM" } };

typedef enum expr_t {
    var_e,
    tableitem_e,

    programfunc_e,
    libraryfunc_e,

    arithexpr_e,
    boolexpr_e,
    assignexpr_e,
    newtable_e,

    constnum_e,
    constbool_e,
    conststring_e,

    nil_e
} expr_t;

typedef struct expr {
    expr_t type;
    node* sym;
    struct expr* index;
    double numConst;
    string strConst;
    bool boolConst;

    int trueList;
    int falseList;

    struct expr* next; 
} expr;

typedef struct call_ {
    expr* elist;
    unsigned char method;
    string name;
} call_;

typedef struct for_t {
    int test;
    int enter;
} for_t;

typedef struct stmt_t {
    int breakList;
    int contList;
} stmt_t;

typedef struct lc_stack_t{
    struct lc_stack_t* next;
    unsigned int counter;
} lc_stack_t;

lc_stack_t *lcs_top = nullptr, *lcs_bottom = nullptr;

typedef struct quad {
    iopcode op;
    expr* result;
    expr* arg1;
    expr* arg2;
    unsigned label = 0;
    unsigned line;
    unsigned taddress;
} quad;

vector<quad*> quads;
unsigned int total = 0;
unsigned int currQuad = 0; 

unsigned int programVarOffset = 0;
unsigned int functionLocalOffset = 0;
unsigned int formalArgOffset = 0;
unsigned int scopeSpaceCounter = 1;

stack<int> scopeOffsetStack;

// scopespace_t currScopeSpace() {
//     if (currScope == 1) {
//         return programvar;
//     } else if (currScope % 2 == 0) {
//         return formalarg;
//     } else {
//         return functionlocal;
//     }
// }

scopespace_t currScopeSpace() {
    if (scopeSpaceCounter == 1) {
        return programvar;
    } else if (scopeSpaceCounter % 2 == 0) {
        return formalarg;
    } else {
        return functionlocal;
    }
}

unsigned int currScopeOffset() {
    switch (currScopeSpace()) {
        case programvar:
            return programVarOffset;
        
        case functionlocal:
            return functionLocalOffset;

        case formalarg:
            return formalArgOffset;

        default:
            assert(0);
    }
}

void incCurrScopeOffset() {
    switch (currScopeSpace()) {
        case programvar:
            ++programVarOffset;
            break;
        
        case functionlocal:
            ++functionLocalOffset;
            break;

        case formalarg:
            ++formalArgOffset;
            break;

        default:
            assert(0);
    }
}

void enterScopeSpace() {
    ++scopeSpaceCounter;
}

void exitScopeSpace() {
    assert(scopeSpaceCounter > 1);
    --scopeSpaceCounter;
}

void resetFormalArgsOffset() {
    formalArgOffset = 0;
}

void resetFunctionLocalsOffset() {
    functionLocalOffset = 0;
}

void restoreCurrScopeOffset(unsigned int n) {
    switch (currScopeSpace()) {
        case programvar:
            programVarOffset = n;
            break;

        case functionlocal:
            functionLocalOffset = n;
            break;

        case formalarg:
            formalArgOffset = n;
            break;

        default:
            assert(0);
    }
}

node* fillExtra(node* n) {
    n->space = currScopeSpace();
    n->offset = currScopeOffset();
    incCurrScopeOffset();
    return n;
}

unsigned int nextQuadLabel() {
    return currQuad;
}

void patchLabel(int quadNo, int label) {
    cout << quadNo << " " << currQuad << endl;
    assert(quadNo < currQuad);
    // assert(!quads[quadNo]->label);
    quads[quadNo]->label = label;
}

void emit(iopcode op, expr* arg1, expr* arg2, expr* result, unsigned label, unsigned line) {
    quad* newQuad = new quad;
    currQuad++;
    newQuad->op = op;
    newQuad->arg1 = arg1;
    newQuad->arg2 = arg2;
    newQuad->result = result;
    newQuad->label = label;
    newQuad->line = line;
    quads.push_back(newQuad);
    total++;
}

expr* newexpr(expr_t type) {
    expr* e = new expr;

    e->type = type;

    return e;
}

expr* newexpr_conststring(string name) {
    expr* e = newexpr(conststring_e);
    e->strConst = name;

    return e;
}

expr* newexpr_constbool(bool b) {
    expr* e = newexpr(constbool_e);
    e->boolConst = b;

    return e;
}

expr* newexpr_constnum(double n) {
    expr* e = newexpr(constnum_e);
    e->numConst = n;

    return e;
}

unsigned int tempCounter = 0;

string newTempName() {
    return "_t" + to_string(tempCounter++);
}

void resetTemp() {
    tempCounter = 0;
}

node* newTemp() {
    string name = newTempName();
    node* sym = lookupInScope(name, currScope);

    if (sym == nullptr) {
        sym = insertNode(0, currScope, name, LOCALV);
        fillExtra(sym);
        sym->type = var_e;
        return sym;
    } else {
        fillExtra(sym);
        sym->type = var_e;
        return sym;
    }
}

expr* emitIfTableItem(expr* e) {
    if (e->type != tableitem_e) {
        return e;
    } else {
        expr* result = newexpr(var_e);
        result->sym = newTemp();
        
        emit(tablegetelem, e, e->index, result, 0, 0);

        return result;
    }
}

expr* emitArith(expr* e, expr* e1, expr* e2, iopcode op, int yylineno) {
    e = newexpr(arithexpr_e);
    e->sym = newTemp();
    // if (e1->type == constnum_e && e2->type == constnum_e) {
    //     e->type = constnum_e;
    // }
    emit(op, e1, e2, e, 0, yylineno);

    return e;
}

expr* emitRelop(expr* e, expr* e1, expr* e2, iopcode op, int yylineno) {
    e = newexpr(boolexpr_e);
    e->sym = newTemp();

    emit(op, e1, e2, nullptr, nextQuadLabel()+3, yylineno);
    emit(assign, newexpr_constbool(0), nullptr, e, 0, yylineno);
    emit(jump, nullptr, nullptr, nullptr, nextQuadLabel()+2, yylineno);
    emit(assign, newexpr_constbool(1), nullptr, e, 0, yylineno);

    return e;
}

// void emitRelop(iopcode op, expr* e, expr* e1, expr* e2, int yylineno) {
//     e->trueList = nextQuadLabel(); 
//     e->falseList = nextQuadLabel()+1; 
//     emit(op, e1, e2, nullptr, 0, yylineno); 
//     emit(jump, nullptr, nullptr, nullptr, 0, yylineno);
// }

// void emitRelop(expr * result, expr * arg1, expr * arg2, iopcode op, int line){
//     /*pername sto result toy emit ena neo expresion me numConst = nextquadlabel()+3 gia to "jump" tis entolis*/
//     emit(op, newexpr_constnum(nextquadlabel()+3), arg1, arg2, currQuad,line);
//     emit(assign, result, newexpr_constbool(0), (expr*) 0, currQuad, line);
//     emit(jump, newexpr_constnum(nextquadlabel()+2), (expr*) 0, (expr*) 0, currQuad, line);
//     emit(assign, result, newexpr_constbool(1), (expr*) 0, currQuad, line);
    
// }

expr* memberItem(expr* lvalue, string name) {
    lvalue = emitIfTableItem(lvalue);
    
    expr* item = newexpr(tableitem_e);

    item->sym = lvalue->sym;
    item->index = newexpr_conststring(name);

    return item;
}

expr* lvalue_expr(node* sym) {
    assert(sym);

    expr* e =  (expr*)malloc(sizeof(expr));

    e->next = nullptr;
    e->sym = sym;

    switch (sym->type) {
        case GLOBAL:
            e->type = var_e;
            break;

        case LOCALV:
            e->type = var_e;
            break;

        case FORMAL:
            e->type = var_e;
            break;
        
        case USERFUNC:
            e->type = programfunc_e;
            break;

        case LIBFUNC:
            e->type = libraryfunc_e;
            break;

        default:
            assert(0);
    }

    return e;
}

expr* reverseList(expr* elist) {
    expr* prev = nullptr;
    expr* current = elist;
    expr* next = nullptr;

    while (current != nullptr) {
        next = current->next;
        current->next = prev;
        prev = current;      
        current = next;
    }
    elist = prev;
    return elist;
}

expr* make_call(expr* lv, expr* reversed_elist) {
    expr* func = emitIfTableItem(lv);
    
    reversed_elist = reverseList(reversed_elist);
    while(reversed_elist) {
        emit(param, nullptr, nullptr, reversed_elist, 0, 0);
        reversed_elist = reversed_elist->next;
    }

    emit(call, nullptr, nullptr, func, 0, 0);
    expr* result = newexpr(var_e);
    result->sym = newTemp();
    emit(getretval, nullptr, nullptr, result, 0, 0);

    return result;
}

expr* get_last(expr* e) {
    expr* iter = e;
    while(iter != nullptr) {
        iter = iter->next;
    }

    return iter;
}

void check_arith(expr* e, string context) {
    if (e->type == constbool_e ||
        e->type == conststring_e ||
        e->type == nil_e ||
        e->type == newtable_e ||
        e->type == programfunc_e ||
        e->type == libraryfunc_e ||
        e->type == boolexpr_e) {
            cout << "Illegal expr used in " << context << "!\n";
        }
}

bool isTempName(string s) {
    return s[0] == '_';
}

bool isTempExpr(expr* e) {
    return e->sym && isTempName(e->sym->name);
}

stmt_t* make_stmt(stmt_t* s) {
    s = new stmt_t;
    s->breakList = 0;
    s->contList = 0;

    return s;
}

int newList(int i) {
    quads[i]->label = 0;
    return i;
}

int mergeList(int l1, int l2) {
    if (!l1) {
        return l2;
    } else {
        if (!l2) {
            return l1;
        } else {
            int i = l1;
            while (quads[i]->label) {
                i = quads[i]->label;
            }

            quads[i]->label = l2;
            return l1;
        }
    }
}

void patchList(int list, int label) {
    while(list) {
        int next = quads[list]->label;
        patchLabel(quads[list]->label, label);
        // cout << "n:" << next;
        list = next;
    }
    // cout << endl;
}

string argToPrint(expr* e) {
    if (e == nullptr) {
        return " ";
    }else if (e->type == constnum_e) {
        if (e->numConst == (int)e->numConst) {
            return to_string((int)e->numConst);
        } else {
            return to_string(e->numConst);
        }
    } else if(e->type == conststring_e) {
        return e->strConst;
    } else if (e->type == constbool_e) {
        return e->boolConst ? "TRUE" : "FALSE";
    } else if (e->type == nil_e) {
        return "nil";
    } else {
        return e->sym->name;
    }
}

void printQuads(vector<string> errorMsgs) {
    ofstream outFile("quads.txt");

    if (!outFile) {
        cerr << "Failed to open file" << endl;
        return;
    }

    for (auto it = errorMsgs.begin(); it != errorMsgs.end(); ++it) {
        outFile << *it;
    }
    outFile << endl;

    outFile << setw(10) << left << "Quad#" << setw(15) << left << "opcode" << setw(10) << left << "result" 
            << setw(10) << left << "arg1" << setw(10) << left << "arg2" << setw(10) << left << "label" << endl;
    outFile << "----------------------------------------------------------------" << endl;

    for (int i = 0; i < total; i++) {
        outFile << setw(10) << left << to_string(i)+":" << setw(15) << left << opToString[quads[i]->op] << setw(10) << left << argToPrint(quads[i]->result)
                << setw(10) << left << argToPrint(quads[i]->arg1) << setw(10) << left << argToPrint(quads[i]->arg2) << setw(10) << left 
                << (quads[i]->label == 0 ? " " : to_string(quads[i]->label)) << setw(10) << left << "[line " + to_string(quads[i]->line) + "]" << endl;
    } 

    outFile << "----------------------------------------------------------------" << endl;

    outFile.close();
}
