%{
	// #define YY_DECL int alpha_yylex(void* yylval)
    #include <iostream>
    #include <string>
	#include <cstdlib>
    #include "parser.h"

    using namespace std;

    int tokenCounter = 1;

    /* Helper variables for STRINGT handling */
    int STRINGTLength;
    int STRINGTStart;
    string currentSTRINGT;
    int first = 0;

    /* Helper variables for COMMENT handling */
    int nestedCommentCounter;
    int* blockCommentStart;
    int* blockCommentEnd;

	typedef struct alpha_token{
		int line;
        int id;
		string token;
        int category;
        string type;
        string description;
		struct alpha_token*	next;
	} alpha_token_t;

    alpha_token_t* head;
    
    void newSTRINGT(string newstr, int s);

    void insertAlpha(int line, string token, int category, string type, string description);

    string keywordToSTRINGT(string keyword);
    int keywordToEnum(string keyword);

    string operatorToSTRINGT(string op);
    int operatorToEnum(string op);

    string punctuationToSTRINGT(string punctuation);
    int punctuationToEnum(string punctuation);
%}

KEYWORD     "if"|"else"|"while"|"for"|"function"|"return"|"break"|"continue"|"and"|"not"|"or"|"local"|"true"|"false"|"nil"

OPERATOR    "="|"+"|"-"|"*"|"/"|"%"|"=="|"!="|"++"|"--"|">"|"<"|">="|"<="

INTCONST    [0-9]+    

REALCONST   [0-9]+\.[0-9]+

PUNCTUATION (\{)|(\})|(\[)|(\])|(\()|(\))|(\;)|(\,)|(\:)|(\:\:)|(\.)|(\.\.)

IDENTIFIER  [a-zA-Z]+[a-zA-Z0-9_]*

LINE_COMMENT \/\/[^\n]*  

whitespace  [ \t\v\f]+


%option noyywrap
%option yylineno

%x BLOCKCOMMENT    
%x STRINGT 

%% 

\"      {
            STRINGTStart = yylineno;
            currentSTRINGT.clear();
            STRINGTLength = 0;
            BEGIN(STRINGT);
        }

<STRINGT>\\\\    {   /* Handling / inside STRINGT */
                    currentSTRINGT += "\\";
                }

<STRINGT>\\\'    {   /* Handling ' inside STRINGT */
                    currentSTRINGT += "\'";
                }

<STRINGT>\\\"    {   /* Handling " inside STRINGT */
                    currentSTRINGT += "\"";
                }

<STRINGT>\\\?    {   /* Handling ? inside STRINGT */
                    currentSTRINGT += "\?";
                }

<STRINGT>\\n     {   /* Handling new line inside STRINGT */
                    currentSTRINGT += "\n";
                }

<STRINGT>\\t     {   /* Handling tab inside STRINGT */
                    currentSTRINGT += "\t";
                }

<STRINGT>\\a     {   /* Handling tab inside STRINGT */
                    currentSTRINGT += "\a";
                }

<STRINGT>\\b     {   /* Handling tab inside STRINGT */
                    currentSTRINGT += "\b";
                }

<STRINGT>\\e     {   /* Handling tab inside STRINGT */
                    currentSTRINGT += "\e";
                }

<STRINGT>\\f     {   /* Handling tab inside STRINGT */
                    currentSTRINGT += "\f";
                }

<STRINGT>\\r     {   /* Handling tab inside STRINGT */
                    currentSTRINGT += "\r";
                }

<STRINGT>\\v     {   /* Handling tab inside STRINGT */
                    currentSTRINGT += "\v";
                }

<STRINGT>\\.{1}  {
                    printf("Warning: Unknown escape character \"%s\" inside string\n", yytext);
                }

<STRINGT>\n      {   /* Handling line change inside STRINGT */
                    currentSTRINGT += "\n";
                }

<STRINGT>{whitespace}      {   /* Handling line change inside STRINGT */
                    currentSTRINGT += yytext;
                }

<STRINGT>\"  {   /* End of STRINGT */
                if (currentSTRINGT == "") {
                    currentSTRINGT = " ";
                } 
                insertAlpha(STRINGTStart, currentSTRINGT, 4, "char*", currentSTRINGT);
                yylval.stringValue = (char*)currentSTRINGT.c_str();
                first = 0;
                STRINGTLength = 0;
                BEGIN(INITIAL);
                return STRING;   
            }

<STRINGT><<EOF>>     {   /* STRINGT remained open */
                        printf("Error: no closing \" for string\n");
                        exit(1);
                    }

<STRINGT>.   {      
                currentSTRINGT += yytext;
            }



\/\*    {
            nestedCommentCounter = 0; 
            blockCommentStart = (int*)malloc(sizeof(int));
            blockCommentStart[nestedCommentCounter] = yylineno; 

            blockCommentEnd = (int*)malloc(sizeof(int));
            blockCommentEnd[nestedCommentCounter] = yylineno; 

            BEGIN(BLOCKCOMMENT);
        }

<BLOCKCOMMENT>\n   {
                        int i;
                        for (i = 0; i < nestedCommentCounter + 1; i++) {
                            blockCommentEnd[i]++;
                        }
                    } 

<BLOCKCOMMENT>.

<BLOCKCOMMENT>\/\*  { 
                            nestedCommentCounter++; 

                            blockCommentStart = (int*)realloc(blockCommentStart, nestedCommentCounter * sizeof(int));
                            blockCommentStart[nestedCommentCounter] = yylineno; 

                            blockCommentEnd = (int*)realloc(blockCommentEnd, nestedCommentCounter * sizeof(int));
                            blockCommentEnd[nestedCommentCounter] = yylineno;

                            BEGIN(BLOCKCOMMENT); 
                        }

<BLOCKCOMMENT>\*\/  {   
                            char str1[10], str2[10];
                            snprintf(str1, sizeof(str1), "%d", blockCommentStart[nestedCommentCounter]);
                            strcat(str1, "-");
                            snprintf(str2, sizeof(str2), "%d", blockCommentEnd[nestedCommentCounter]);
                            strcat(str1, str2);
                            if (nestedCommentCounter == 0) {
                                insertAlpha(blockCommentStart[nestedCommentCounter], str1, BLOCK_COMMENT, "enumerated", "BLOCKCOMMENT");
                                free(blockCommentStart);
                                free(blockCommentEnd);
                                BEGIN(INITIAL);
                                return BLOCK_COMMENT;
                            } else {
                                insertAlpha(blockCommentStart[nestedCommentCounter], str1, BLOCK_COMMENT, "enumerated", "NESTED_COMMENT");
                                nestedCommentCounter--;
                                return BLOCK_COMMENT;
                            }
                        }

<BLOCKCOMMENT><<EOF>>  {
                            printf("Error: Block comment was not closed\n"); 
                            exit(1);
                        }

{KEYWORD}   { insertAlpha(yylineno, yytext, keywordToEnum(yytext), "char*", keywordToSTRINGT(yytext)); return keywordToEnum(yytext);}

{OPERATOR}  { insertAlpha(yylineno, yytext, operatorToEnum(yytext), "enumerated", operatorToSTRINGT(yytext)); return operatorToEnum(yytext);}

{INTCONST}  { insertAlpha(yylineno, yytext, INTCONST, "integer", yytext); yylval.intValue = atoi(yytext); return INTCONST;}

{REALCONST} { insertAlpha(yylineno, yytext, REALCONST, "float", yytext); yylval.doubleValue = stof(yytext); return REALCONST;}

{LINE_COMMENT}   { insertAlpha(yylineno, " ", LINE_COMMENT, "enumerated", "LINE_COMMENT"); return LINE_COMMENT;}

{IDENTIFIER}    { insertAlpha(yylineno, yytext, IDENTIFIER, "char*", yytext); yylval.stringValue = strdup(yytext); return IDENTIFIER;}

{PUNCTUATION}   { insertAlpha(yylineno, yytext, punctuationToEnum(yytext), "enumerated", punctuationToSTRINGT(yytext)); return punctuationToEnum(yytext);}

{whitespace}    {}

\n          { }

.           cerr << "Undefined input \"" << yytext << "\" in line" << yylineno << endl;

%%

void insertAlpha(int line, string token, int category, string type, string description) {
    if (tokenCounter == 1) {
        head = (alpha_token_t*)malloc(sizeof(alpha_token_t));
        head->next = nullptr;
    }
    alpha_token_t* newToken = (alpha_token_t*)malloc(sizeof(alpha_token_t));
    
    newToken->line = line;

    newToken->id = tokenCounter++;
    
    newToken->token = token;

    newToken->category = category;

    newToken->type = type;

    newToken->description = description;

    if (head->next == nullptr) {
        head->next = newToken;
        newToken->next = nullptr;
        return;
    }
    alpha_token_t* iter = head->next;
    while(1) { 
        if (iter->next == nullptr) {
            break;
        }
        iter = iter->next;
    }  
    
    newToken->next = nullptr;
    iter->next = newToken;       
}

string keywordToSTRINGT(string keyword) {
    if (keyword == "if") {
        return "IF";
    } else if (keyword == "else") {
        return "ELSE";
    } else if (keyword == "while") {
        return "WHILE";
    } else if (keyword == "for") {
        return "FOR";
    } else if (keyword == "function") {
        return "FUNCTION";
    } else if (keyword == "return") {
        return "RETURN";
    } else if (keyword == "break") {
        return "BREAK";
    } else if (keyword == "continue") {
        return "CONTINUE";
    } else if (keyword == "and") {
        return "AND";
    } else if (keyword == "not") {
        return "NOT";
    } else if (keyword == "or") {
        return "OR";
    } else if (keyword == "local") {
        return "LOCAL";
    } else if (keyword == "true") {
        return "TRUE";
    } else if (keyword == "false") {
        return "FALSE";
    } else if (keyword == "nil") {
        return "NIL";
    }

    return NULL;
}

int keywordToEnum(string keyword) {
    if (keyword == "if") {
        return IF;
    } else if (keyword == "else") {
        return ELSE;
    } else if (keyword == "while") {
        return WHILE;
    } else if (keyword == "for") {
        return FOR;
    } else if (keyword == "function") {
        return FUNCTION;
    } else if (keyword == "return") {
        return RETURN;
    } else if (keyword == "break") {
        return BREAK;
    } else if (keyword == "continue") {
        return CONTINUE;
    } else if (keyword == "and") {
        return AND;
    } else if (keyword == "not") {
        return NOT;
    } else if (keyword == "or") {
        return OR;
    } else if (keyword == "local") {
        return LOCAL;
    } else if (keyword == "true") {
        return TRUE_;
    } else if (keyword == "false") {
        return FALSE_;
    } else if (keyword == "nil") {
        return NIL;
    }

    return -1;
}

string operatorToSTRINGT(string op) {
    if (op == "=") {
        return "ASSIGN";
    } else if (op == "+") {
        return "PLUS";
    } else if (op == "-") {
        return "MINUS";
    } else if (op == "*") {
        return "MULTIPLY";
    } else if (op == "/") {
        return "DIVIDE";
    } else if (op == "\%") {
        return "MOD";
    } else if (op == "==") {
        return "EQUAL";
    } else if (op == "!=") {
        return "NOT_EQUAL";
    } else if (op == "++") {
        return "PLUS_PLUS";
    } else if (op == "--") {
        return "MINUS_MINUS";
    } else if (op == ">") {
        return "GREATER";
    } else if (op == "<") {
        return "LESS";
    } else if (op == ">=") {
        return "GREATER_EQUAL";
    } else if (op == "<=") {
        return "LESS_EQUAL";
    }

    return NULL;
}

int operatorToEnum(string op) {
    if (op == "=") {
        return ASSIGN;
    } else if (op == "+") {
        return PLUS;
    } else if (op == "-") {
        return MINUS;
    } else if (op == "*") {
        return MULTIPLY;
    } else if (op == "/") {
        return DIVIDE;
    } else if (op == "\%") {
        return MOD;
    } else if (op == "==") {
        return EQUAL;
    } else if (op == "!=") {
        return NOT_EQUAL;
    } else if (op == "++") {
        return PLUS_PLUS;
    } else if (op == "--") {
        return MINUS_MINUS;
    } else if (op == ">") {
        return GREATER;
    } else if (op == "<") {
        return LESS;
    } else if (op == ">=") {
        return GREATER_EQUAL;
    } else if (op == "<=") {
        return LESS_EQUAL;
    }

    return -1;
}

string punctuationToSTRINGT(string punctuation) {
    if (punctuation == "{") {
        return "LEFT_BRACE";
    } else if (punctuation == "}") {
        return "RIGHT_BRACE";
    } else if (punctuation == "[") {
        return "LEFT_SQUARE_BRACE";
    } else if (punctuation == "]") {
        return "RIGHT_SQUARE_BRACE";
    } else if (punctuation == "(") {
        return "LEFT_PARENTHESES";
    } else if (punctuation == ")") {
        return "RIGHT_PARENTHESES";
    } else if (punctuation == ";") {
        return "SEMICOLON";
    } else if (punctuation == ",") {
        return "COMMA";
    } else if (punctuation == ":") {
        return "COLON";
    } else if (punctuation == "::") {
        return "DOUBLE_COLON";
    } else if (punctuation == ".") {
        return "DOT";
    } else if (punctuation == "..") {
        return "DOT_DOT";
    }

    return NULL;
}

int punctuationToEnum(string punctuation) {
    if (punctuation == "{") {
        return LEFT_BRACE;
    } else if (punctuation == "}") {
        return RIGHT_BRACE;
    } else if (punctuation == "[") {
        return LEFT_SQUARE_BRACE;
    } else if (punctuation == "]") {
        return RIGHT_SQUARE_BRACE;
    } else if (punctuation == "(") {
        return LEFT_PARENTHESES;
    } else if (punctuation == ")") {
        return RIGHT_PARENTHESES;
    } else if (punctuation == ";") {
        return SEMICOLON;
    } else if (punctuation == ",") {
        return COMMA;
    } else if (punctuation == ":") {
        return COLON;
    } else if (punctuation == "::") {
        return DOUBLE_COLON;
    } else if (punctuation == ".") {
        return DOT;
    } else if (punctuation == "..") {
        return DOT_DOT;
    }

    return -1;
}

