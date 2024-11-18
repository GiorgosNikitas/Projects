%{  
    #include <iostream>
    #include <string>
    #include <vector>
    #include "targetCode.h"
    // #include "symTable.h"
    // #define YY_DECL int alpha_yylex(void* yylval)

    #define loopcounter (lcs_top->counter)

    using namespace std;

    node* tmp;
    int lvalueType;
    int funCounter = 0;
    int functionArgs = 0;
    int inFunction = 0;
    int blockCounter = 0;
    int lastFuncScope = -1;
    bool isBool = false;
    vector<string> errorMsgs;

    int yylex(void);
    int yyerror(string yaccProvidedMessage);
    extern int yylineno;
    extern char* yytext;
    extern FILE* yyin;
    extern unsigned int currScope;
    extern void push_loopcounter(void);
    extern void pop_loopcounter(void);
%}

%start program

%union {
	char* stringValue;
	int	intValue;
	float doubleValue;
    struct node* exprNode;
    struct expr* expr;
    struct call_* callO;
    struct stmt_t* stmt_t;
    struct for_t* for_t;
}

%token <intValue> INTCONST
%token <doubleValue> REALCONST
%token <stringValue> STRING
%token <stringValue> IDENTIFIER
%token LINE_COMMENT
%token BLOCK_COMMENT

%token IF
%token ELSE
%token WHILE
%token FOR
%token FUNCTION
%token RETURN
%token BREAK
%token CONTINUE
%token AND
%token NOT
%token OR
%token LOCAL
%token TRUE_
%token FALSE_
%token NIL

%token LEFT_BRACE
%token RIGHT_BRACE
%token LEFT_SQUARE_BRACE
%token RIGHT_SQUARE_BRACE
%token LEFT_PARENTHESES
%token RIGHT_PARENTHESES
%token SEMICOLON
%token COMMA
%token COLON
%token DOUBLE_COLON
%token DOT
%token DOT_DOT

%token ASSIGN
%token PLUS
%token MINUS
%token MULTIPLY
%token DIVIDE
%token MOD
%token EQUAL
%token NOT_EQUAL
%token PLUS_PLUS
%token MINUS_MINUS
%token GREATER
%token LESS
%token GREATER_EQUAL
%token LESS_EQUAL

%right ASSIGN
%left OR
%left AND
%nonassoc EQUAL NOT_EQUAL
%nonassoc GREATER GREATER_EQUAL LESS LESS_EQUAL
%left PLUS
%right MINUS
%left MULTIPLY DIVIDE MOD
%right NOT PLUS_PLUS MINUS_MINUS
%left DOT DOT_DOT
%left LEFT_SQUARE_BRACE RIGHT_SQUARE_BRACE
%left LEFT_PARENTHESES RIGHT_PARENTHESES

%type <expr> lvalue
%type <expr> member
%type <expr> primary
%type <expr> assignexpr
%type <expr> call
%type <expr> term
%type <expr> objectdef
%type <expr> indexed
%type <expr> indexed_r
%type <expr> indexedelem
%type <expr> expr
%type <expr> elist
%type <expr> elist_r
%type <expr> const_

%type <stringValue> funcname

%type <intValue> funcbody
%type <intValue> ifprefix
%type <intValue> elseprefix
%type <intValue> whilestart
%type <intValue> whilecond
%type <intValue> N
%type <intValue> M

%type <exprNode> funprefix
%type <exprNode> funcdef

%type <callO> callsuffix
%type <callO> normcall
%type <callO> methodcall

%type <stmt_t> stmt;
%type <stmt_t> stmt_r;
%type <stmt_t> breakstmt;
%type <stmt_t> contstmt;
%type <stmt_t> loopstmt;

%type <for_t> forprefix;

%%

program:    stmt_r
            ;

stmt:		expr SEMICOLON{$$ = make_stmt($$);}
			|ifstmt{$$ = make_stmt($$);}
			|whilestmt{$$ = make_stmt($$);}
			|forstmt{$$ = make_stmt($$);}
			|returnstmt{$$ = make_stmt($$);}
			|breakstmt{$$ = make_stmt($$);}
			|contstmt{$$ = make_stmt($$);}
			|block {$$ = make_stmt($$);}
			|funcdef {$$ = make_stmt($$);}
			|SEMICOLON {$$ = make_stmt($$);}
            |other {$$ = make_stmt($$);}
			;

breakstmt:  BREAK SEMICOLON { 
                make_stmt($breakstmt); 
                $breakstmt->breakList = newList(nextQuadLabel());
                emit(jump, nullptr, nullptr, nullptr, 0, yylineno);
            }
            ;

contstmt:   CONTINUE SEMICOLON{
                make_stmt($contstmt);
                $contstmt->contList = newList(nextQuadLabel());
                emit(jump, nullptr, nullptr, nullptr, 0, yylineno);
            }
            ;

stmt_r:     stmt{ resetTemp(); } stmt_r 
            | /* empty */ {  }
            ;    

// X:          /* empty */ { $$ = nextQuadLabel(); } ;  

expr:       assignexpr
            |expr PLUS expr {
                check_arith($1, "expr1 + expr2");
                check_arith($3, "expr1 + expr2");
                $$ = emitArith($$, $1, $3, add, yylineno);
            }
            |expr MINUS expr {
                check_arith($1, "expr1 - expr2");
                check_arith($3, "expr1 - expr2");
                $$ = emitArith($$, $1, $3, sub, yylineno);
            }
            |expr MULTIPLY expr {
                check_arith($1, "expr1 * expr2");
                check_arith($3, "expr1 * expr2");
                $$ = emitArith($$, $1, $3, mul, yylineno);
            }
            |expr DIVIDE expr {
                check_arith($1, "expr1 / expr2");
                check_arith($3, "expr1 / expr2");
                $$ = emitArith($$, $1, $3, div_, yylineno);
            }
            |expr MOD expr {
                check_arith($1, "expr1 \% expr2");
                check_arith($3, "expr1 \% expr2");
                $$ = emitArith($$, $1, $3, mod, yylineno);
            }
            // |boolexpr{
            //     if (isBool) {
            //         expr* e = new expr;
            //         e->sym = newTemp();
            //         // e->sym = newexpr_constbool(1);
            //         emit(assign, newexpr_constbool(true), nullptr, e, 0, yylineno);
            //         emit(jump, nullptr, nullptr, nullptr, nextQuadLabel()+2, yylineno);
            //         emit(assign, newexpr_constbool(false), nullptr, e, 0, yylineno);
            //         emit(jump, nullptr, nullptr, nullptr, nextQuadLabel()+1, yylineno);
            //         isBool = false;
            //     }
            // }
            |expr GREATER expr{ $$ = emitRelop($$, $1, $3, if_greater, yylineno); }
            |expr GREATER_EQUAL expr{ $$ = emitRelop($$, $1, $3, if_greatereq, yylineno); }
            |expr LESS expr{ $$ = emitRelop($$, $1, $3, if_less, yylineno); }
            |expr LESS_EQUAL expr{ $$ = emitRelop($$, $1, $3, if_lesseq, yylineno); }
            |expr EQUAL expr{ $$ = emitRelop($$, $1, $3, if_eq, yylineno); }
            |expr NOT_EQUAL expr{ $$ = emitRelop($$, $1, $3, if_noteq, yylineno); }
            |expr AND expr {
                $$ = newexpr(boolexpr_e);
                $$->sym = newTemp();
                emit(and_, $1, $3, $$, 0, yylineno);
            }
            |expr OR expr{
                $$ = newexpr(boolexpr_e);
                $$->sym = newTemp();
                emit(or_, $1, $3, $$, 0, yylineno);
            }
            |term
            ;

// boolexpr:   expr GREATER expr{emitRelop(if_greater, $$, $1, $3, yylineno);}
//             |expr GREATER_EQUAL expr{emitRelop(if_greatereq, $$, $1, $3, yylineno);}
//             |expr LESS expr{emitRelop(if_less, $$, $1, $3, yylineno);}
//             |expr LESS_EQUAL expr{emitRelop(if_lesseq, $$, $1, $3, yylineno);}
//             |expr EQUAL expr{emitRelop(if_eq, $$, $1, $3, yylineno);}
//             |expr NOT_EQUAL expr{emitRelop(if_noteq, $$, $1, $3, yylineno);}
//             |expr AND X expr {
//                 patchLabel($1->trueList, $X);
//                 $$->trueList = $4->trueList;
//                 $$->falseList = mergeList($1->falseList, $4->falseList);
//                 // $$ = newexpr(boolexpr_e);
//                 // $$->sym = newTemp();
//                 // emit(and_, $1, $4, $$, 0, yylineno);
//                 isBool = true;
//             }
//             |expr OR X expr{
//                 patchLabel($1->falseList, $X);
//                 $$->trueList = mergeList($1->trueList, $4->trueList);
//                 $$->falseList = $4->falseList;
//                 isBool = true;
//                 // $$ = newexpr(boolexpr_e);
//                 // $$->sym = newTemp();
//                 // emit(or_, $1, $4, $$, 0, yylineno);
//             }
//             ;

term:       LEFT_PARENTHESES expr RIGHT_PARENTHESES { $term = $expr; }
            |MINUS expr {
                check_arith($expr, "unary minus");
                $term = newexpr(arithexpr_e);
                $term->sym = isTempExpr($expr) ? $expr->sym : newTemp();
                emit(mul, newexpr_constnum(-1), $expr, $term, 0, yylineno);
            }
            |NOT expr{
                $$->trueList = $2->falseList;
                $$->falseList = $2->trueList;
                $term = newexpr(boolexpr_e);
                $term->sym = newTemp();
                emit(not_, $expr, nullptr, $term, 0, yylineno);
            }
            |PLUS_PLUS lvalue{
                if ($2->sym->type == LIBFUNC || $2->sym->type == USERFUNC) {
                    yyerror("Cannot use operator '++' in function " + $2->sym->name);
                }

                check_arith($lvalue, "++lvalue");
                if ($lvalue->type == tableitem_e) {
                    $term = emitIfTableItem($lvalue);
                    emit(add, $term, newexpr_constnum(1), $term, 0, yylineno);
                    emit(tablesetelem, $lvalue, $lvalue->index, $term, 0, yylineno);
                } else {
                    emit(add, $lvalue, newexpr_constnum(1), $lvalue, 0, yylineno);
                    $term = newexpr(arithexpr_e);
                    $term->sym = newTemp();
                    emit(assign, $lvalue, nullptr, $term, 0, yylineno);
                }
            }
            |lvalue{
                if ($1->sym->type == LIBFUNC || $1->sym->type == USERFUNC) {
                    yyerror("Cannot use operator '++' in function " + $1->sym->name);
                }
            } PLUS_PLUS {
                check_arith($lvalue, "lvalue++");
                $term = newexpr(var_e);
                $term->sym = newTemp();
                if ($lvalue->type == tableitem_e) {
                    expr* val = emitIfTableItem($lvalue);
                    emit(assign, val, nullptr, $term, 0, yylineno);
                    emit(add, val, newexpr_constnum(1), val, 0, yylineno);
                    emit(tablesetelem, $lvalue, $lvalue->index, val, 0, yylineno);
                } else {
                    emit(assign, $lvalue, nullptr, $term, 0, yylineno);
                    emit(add, $lvalue, newexpr_constnum(1), $lvalue, 0, yylineno);
                }
            }
            |MINUS_MINUS lvalue{
                if ($2->sym->type == LIBFUNC || $2->sym->type == USERFUNC) {
                    yyerror("Cannot use operator '--' in function " + $2->sym->name);
                }

                check_arith($lvalue, "--lvalue");
                if ($lvalue->type == tableitem_e) {
                    $term = emitIfTableItem($lvalue);
                    emit(sub, $term, newexpr_constnum(1), $term, 0, yylineno);
                    emit(tablesetelem, $lvalue, $lvalue->index, $term, 0, yylineno);
                } else {
                    emit(sub, $lvalue, newexpr_constnum(1), $lvalue, 0, yylineno);
                    $term = newexpr(arithexpr_e);
                    $term->sym = newTemp();
                    emit(assign, $lvalue, nullptr, $term, 0, yylineno);
                }
            }
            |lvalue{
                if ($1->sym->type == LIBFUNC || $1->sym->type == USERFUNC) {
                    yyerror("Cannot use operator '--' in function " + $1->sym->name);
                }
            } MINUS_MINUS{
                check_arith($lvalue, "lvalue--");
                $term = newexpr(var_e);
                $term->sym = newTemp();
                if ($lvalue->type == tableitem_e) {
                    expr* val = emitIfTableItem($lvalue);
                    emit(assign, val, nullptr, $term, 0, yylineno);
                    emit(sub, val, newexpr_constnum(1), val, 0, yylineno);
                    emit(tablesetelem, $lvalue, $lvalue->index, val, 0, yylineno);
                } else {
                    emit(assign, $lvalue, nullptr, $term, 0, yylineno);
                    emit(sub, $lvalue, newexpr_constnum(1), $lvalue, 0, yylineno);
                }
            }
            |primary { $term = $primary; }
            ;

assignexpr: lvalue{
                if ($1->sym->type == LIBFUNC || $1->sym->type == USERFUNC) {
                    yyerror("Cannot assign value to function " + $1->sym->name);
                }
            } ASSIGN expr {  
                if ($lvalue->type == tableitem_e) {
                    emit(tablesetelem, $lvalue->index, $expr, $lvalue, 0, yylineno); // lvalue[index] = expr
                    $assignexpr = emitIfTableItem($lvalue);
                    $assignexpr->type = assignexpr_e;
                } else {
                    emit(assign, $expr, nullptr, $lvalue, 0, yylineno); 
                    $assignexpr = newexpr(assignexpr_e);
                    $assignexpr->sym = newTemp();
                    emit(assign, $lvalue, nullptr, $assignexpr, 0, yylineno);                                                                                                    
                }
            } 
            ;

primary:    lvalue { $primary = emitIfTableItem($lvalue); }
            |call { $primary = $call; }
            |objectdef { $primary = $objectdef; }
            |LEFT_PARENTHESES funcdef RIGHT_PARENTHESES { 
                    $primary = newexpr(programfunc_e);
                    $primary->sym = $funcdef; 
                }
            |const_ { $primary = $const_; }
            ;

lvalue:     IDENTIFIER{
                if ((tmp = lookupAboveScopes(yylval.stringValue, currScope)) != nullptr) {
                    if ((tmp->type == LOCALV && tmp->scope <= lastFuncScope && inFunction != 0) || 
                    (tmp->type == FORMAL && (currScope-blockCounter+inFunction) != tmp->scope)) {
                        yyerror("Cannot access " + tmp->name + " in this scope");
                        $$ = lvalue_expr(tmp);    
                    } else {
                        $$ = lvalue_expr(tmp);
                    }
                } else {
                    if (currScope == 0) {
                        tmp = insertNode(yylineno, currScope, yylval.stringValue, GLOBAL);
                    } else {
                        tmp = insertNode(yylineno, currScope, yylval.stringValue, LOCALV);
                    }
                    tmp = fillExtra(tmp);
                    $$ = lvalue_expr(tmp);
                }
            }
            |LOCAL IDENTIFIER{
                if ((tmp = lookupInScope(yylval.stringValue, currScope)) != nullptr) {
                    if (tmp->type == LIBFUNC) {
                        yyerror("Trying to shadow library funtion " + tmp->name);
                    } else if (tmp->type == USERFUNC) {
                        // ?????????????????????????????
                    } else {
                        $$ = lvalue_expr(tmp);
                    }
                } else {
                    if (currScope == 0) {
                        tmp = insertNode(yylineno, currScope, yylval.stringValue, GLOBAL);
                    } else {
                        tmp = insertNode(yylineno, currScope, yylval.stringValue, LOCALV);
                    }
                    tmp = fillExtra(tmp);
                    $$ = lvalue_expr(tmp);
                }
            }
            |DOUBLE_COLON IDENTIFIER{
                if ((tmp = lookupInScope(yylval.stringValue, 0)) != nullptr) {
                    $$ = lvalue_expr(tmp);   
                } else {
                    yyerror("Global variable not found");
                }
            }
            |member { $lvalue = $member; }
            ;

member:     lvalue DOT IDENTIFIER { $member = memberItem($lvalue, $IDENTIFIER); }
            |lvalue LEFT_SQUARE_BRACE expr RIGHT_SQUARE_BRACE {
                $lvalue = emitIfTableItem($lvalue);
                $member = newexpr(tableitem_e);
                $member->sym = $lvalue->sym;
                $member->index = $expr;
            }
            |call DOT IDENTIFIER
            |call LEFT_SQUARE_BRACE expr RIGHT_SQUARE_BRACE
            ;

call:       call LEFT_PARENTHESES elist RIGHT_PARENTHESES {$$ = make_call($1, $elist);}
            |lvalue callsuffix{
                $lvalue = emitIfTableItem($lvalue);
                if($callsuffix->method) {
                    expr* e = newexpr(nil_e);
                    e = $lvalue;
                    e->next = $callsuffix->elist;
                    $callsuffix->elist = e;
                    $lvalue = emitIfTableItem(memberItem($lvalue, $callsuffix->name));
                }
                $call = make_call($lvalue, $callsuffix->elist);
            }
            |LEFT_PARENTHESES funcdef RIGHT_PARENTHESES LEFT_PARENTHESES elist RIGHT_PARENTHESES{
                expr* func = newexpr(programfunc_e);
                func->sym = $funcdef;
                $$ = make_call(func, $elist);
            }
            ;

callsuffix:     normcall{ $callsuffix = $normcall; }
                |methodcall { $callsuffix = $methodcall; }
                ;

normcall:   LEFT_PARENTHESES elist RIGHT_PARENTHESES {
                $$ = new call_;
                $normcall->elist = $elist;
                $normcall->method = 0;
                $normcall->name = " ";
            }
            ;

methodcall: DOT_DOT IDENTIFIER LEFT_PARENTHESES elist RIGHT_PARENTHESES{
                $$ = new call_;
                $methodcall->elist = $elist;
                $methodcall->method = 1;
                $methodcall->name = $IDENTIFIER;
            } 
            ;

elist:      expr elist_r{ $elist = $expr; $elist->next = $elist_r; }
            | /* empty */{ $elist = nullptr; }
            ;

elist_r:    COMMA expr elist_r { $$ = $expr; $$->next = $3; }
            | /* empty */{ $$ = nullptr; }
            ;

objectdef:  LEFT_SQUARE_BRACE elist RIGHT_SQUARE_BRACE {
                expr* t = newexpr(newtable_e);
                t->sym = newTemp();
                emit(tablecreate, nullptr, nullptr, t, 0, yylineno);

                expr* iter = $2;
                int i = 0;
                while(iter != NULL){
                    emit(tablesetelem, newexpr_constnum(i++), iter, t, 0, yylineno);
                    iter = iter->next;
                }
                $$ = t;
            }
            |LEFT_SQUARE_BRACE indexed RIGHT_SQUARE_BRACE {
                expr* t = newexpr(newtable_e);
                t->sym = newTemp();
                emit(tablecreate, nullptr, nullptr, t, 0, yylineno);
                while($2 != nullptr){
                    emit(tablesetelem, $2->index, $2, t, 0, yylineno);
                    $2 = $2->next;
                }
                $$ = t;
            }
            ;                           

indexed:    indexedelem indexed_r{$$ = $1; $$->next = $2;} ;

indexed_r:  COMMA indexedelem indexed_r{$$ = $2; $$->next = $3;}
            | /* empty */{$$ = nullptr;}
            ;

indexedelem:    LEFT_BRACE expr COLON expr RIGHT_BRACE{$$ = $4; $$->index = $2;} ;

block:      LEFT_BRACE{ currScope++; if(inFunction != 0) {blockCounter++;} } 
            stmt_r 
            RIGHT_BRACE{ hideScope(currScope); currScope--; if(inFunction != 0) {blockCounter--;} } ;

funcname:   IDENTIFIER { $funcname = $IDENTIFIER; }
            | /* empty */ { string s = "$f" + to_string(funCounter++); $funcname = (char*)s.c_str();}
            ;

funprefix:  FUNCTION{lastFuncScope = currScope;} funcname { if ($funcname[0] != '$' && (tmp = lookupInScope(yylval.stringValue, 0)) != nullptr) {
                                                                    if (tmp->type == LIBFUNC) {
                                                                        yyerror("Trying to shadow library funtion " + tmp->name);
                                                                    } else if (tmp->type == USERFUNC) {
                                                                        yyerror("User function " + tmp->name + " has already been defined");
                                                                    } else {
                                                                        yyerror("Trying to shadow variable " + tmp->name);
                                                                    }
                                                            } else {
                                                                $funprefix = insertNode(yylineno, currScope, $funcname, USERFUNC);
                                                                fillExtra($funprefix);
                                                                $funprefix->type = USERFUNC;
                                                                emit(jump, nullptr, nullptr, nullptr, 0, yylineno);
                                                                $funprefix->iaddress = nextQuadLabel();
                                                                $funprefix->taddress = nextQuadLabel();
                                                                emit(funcstart, nullptr, nullptr, lvalue_expr($funprefix), 0, yylineno);
                                                                scopeOffsetStack.push(currScopeOffset());
                                                                enterScopeSpace();
                                                                resetFunctionLocalsOffset();
                                                            } };    

funcargs:   LEFT_PARENTHESES { enterScopeSpace(); resetFunctionLocalsOffset(); functionArgs++; } idlist RIGHT_PARENTHESES {functionArgs--; inFunction++;} ;

funcbody:   block { $funcbody = currScopeOffset(); exitScopeSpace(); inFunction--;} ;

funcdef:    funprefix funcargs funcbody {
                                            exitScopeSpace();
                                            $funprefix->totalLocals = $funcbody;
                                            int offset = scopeOffsetStack.top();
                                            scopeOffsetStack.pop();
                                            restoreCurrScopeOffset(offset);
                                            $funcdef = $funprefix;
                                            emit(funcend, nullptr, nullptr, lvalue_expr($funcdef), 0, yylineno);
                                            quads[$funprefix->iaddress - 1]->label = nextQuadLabel();
                                        } ;

const_:      INTCONST { $const_ = newexpr(constnum_e); $const_->numConst = yylval.intValue; }
            |REALCONST { $const_ = newexpr(constnum_e); $const_->numConst = yylval.doubleValue; }
            |STRING { $const_ = newexpr(conststring_e); $const_->strConst = yylval.stringValue; }
            |NIL { $const_ = newexpr(nil_e); } 
            |TRUE_  { $const_ = newexpr(constbool_e); $const_->boolConst = true; }
            |FALSE_ { $const_ = newexpr(constbool_e); $const_->boolConst = false; }
            ;

idlist:     IDENTIFIER{
				if ((tmp = lookupInScope(yylval.stringValue, 0)) != nullptr && tmp->type == LIBFUNC) {
                    yyerror("Trying to shadow library funtion " + tmp->name);
                } else {
                    if((tmp = lookupInScope(yylval.stringValue, currScope + functionArgs)) != nullptr) {
                        yyerror("Redeclaration of argument" + tmp->name);
                    } else {
                        insertNode(yylineno, currScope + functionArgs, yylval.stringValue, FORMAL);
                    }  
                }
			} idlist_r
            | /* empty */
            ;

idlist_r:   COMMA IDENTIFIER{
                if ((tmp = lookupInScope(yylval.stringValue, 0)) != nullptr && tmp->type == LIBFUNC) {
                    yyerror("Trying to shadow library funtion " + tmp->name);
                } else {
                    if((tmp = lookupInScope(yylval.stringValue, currScope + functionArgs)) != nullptr) {
                        yyerror("Redeclaration of argument " + tmp->name);
                    } else {
                        insertNode(yylineno, currScope + functionArgs, yylval.stringValue, FORMAL);
                    }  
                }
			} idlist_r
            | /* empty */
            ;

ifprefix:   IF LEFT_PARENTHESES expr RIGHT_PARENTHESES{
                emit(if_eq, $expr, newexpr_constbool(1), nullptr, nextQuadLabel()+2, yylineno);
                $ifprefix = nextQuadLabel();
                emit(jump, nullptr, nullptr, nullptr, 0, yylineno);
            }

ifstmt:     ifprefix stmt{ patchLabel($ifprefix, nextQuadLabel()); } elseprefix{ if($elseprefix != -1) {patchLabel($ifprefix, $elseprefix+1); patchLabel($elseprefix, nextQuadLabel());} }
            ;

elseprefix: ELSE{emit(jump, nullptr, nullptr, nullptr, 0, yylineno);} M stmt{$$ = $M-1;}
            |/* empty */ {$$ = -1;}
            ;

loopstart:  /* empty */{ /*++loopcounter;*/ };

loopend:    /* empty */{ /*--loopcounter;*/ };

loopstmt:   loopstart stmt loopend{ $loopstmt = $stmt; };

whilestart: WHILE {
                $whilestart = nextQuadLabel();
            }

whilecond:  LEFT_PARENTHESES expr RIGHT_PARENTHESES{
                emit(if_eq, $expr, newexpr_constbool(1), nullptr, nextQuadLabel()+2, yylineno);
                $whilecond = nextQuadLabel();
                emit(jump, nullptr, nullptr, nullptr, 0, yylineno);
            }

whilestmt:  whilestart whilecond loopstmt{
                emit(jump, nullptr, nullptr, nullptr, $whilestart, yylineno);
                patchLabel($whilecond, nextQuadLabel());
                patchList($loopstmt->breakList, nextQuadLabel());
                // patchList($loopstmt->contList, $whilestart);
                // quads[$whilestart]->label
            } 
            ;

N:          { $N = nextQuadLabel(); emit(jump, nullptr, nullptr, nullptr, 0, yylineno); } ;

M:          { $M = nextQuadLabel(); } ;

forprefix:  FOR LEFT_PARENTHESES elist SEMICOLON M expr SEMICOLON {
                $forprefix = new for_t;
                $forprefix->test = $M;
                $forprefix->enter = nextQuadLabel();

                emit(if_eq, $expr, newexpr_constbool(true),  nullptr, 0, yylineno);
            }

forstmt:    forprefix N elist RIGHT_PARENTHESES N loopstmt N {
                patchLabel($forprefix->enter, $5+1);
                patchLabel($2, nextQuadLabel());
                patchLabel($5, $forprefix->test);
                patchLabel($7, $2+1);

                // patchList($loopstmt->breakList, nextQuadLabel());
                // patchList($loopstmt->contList, $2+1);
            } 
            ;

returnstmt: RETURN expr SEMICOLON { emit(ret, nullptr, nullptr, $expr, 0, yylineno); }
            |RETURN SEMICOLON { emit(ret, nullptr, nullptr, nullptr, 0, yylineno); }
            ;

other:      LINE_COMMENT { ; } 
            |BLOCK_COMMENT { ; } ;

%%

int yyerror(string yaccProvidedMessage) {   
    errorMsgs.push_back("Error in line " + to_string(yylineno) + ": " +  yaccProvidedMessage + "\n");

    return 0;
}

int main(int argc, char** argv) {
    initHash();

    if(argc > 1){
		if(!(yyin = fopen(argv[1], "r"))){
			fprintf(stderr, "Cannot open file: %s\n", argv[1]);
			return 1;
		}
	}else{
		yyin = stdin;
	}
	
	yyparse();

    system("clear");

    printSyntax();

    printQuads(errorMsgs);

    generateAll();

    return 0;
}