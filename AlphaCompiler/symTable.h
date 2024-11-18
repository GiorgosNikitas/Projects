#include <iostream>
#include <string>
#include <cstdlib>
#include <vector>
#include <stack>

#define HASH_SIZE 50

using namespace std;

void insertLibfuncts();

unsigned int currScope;

enum SymbolType {
 GLOBAL, LOCALV, FORMAL,  USERFUNC, LIBFUNC
}; 

typedef enum scopespace_t{ programvar, functionlocal, formalarg }scopespace_t;

typedef enum symbol_t { var_s, programfunc_s, libraryfunc_s } symbol_t;

typedef struct node {
    unsigned int line;
    unsigned int scope;
    string name;
    int type;
    bool isHidden;
    scopespace_t space;
    unsigned int offset;
    unsigned int iaddress;
    unsigned int taddress;
    unsigned int totalLocals;
    stack<unsigned> returnStack;
    struct node* next;
    struct node* nextInScope;
} node;

typedef struct hashTable {
    int count;
    node* hash[HASH_SIZE]; 
} symbolTable;

symbolTable* mySymTab;

vector<node*> scopeHeads;

int hashIndex(int index) {
    return index % HASH_SIZE;
}

symbolTable* initHash() {
    mySymTab = (symbolTable*)malloc(sizeof(symbolTable));

    mySymTab->count = 0;
    for (int i = 0; i < HASH_SIZE; i++) {
        mySymTab->hash[i] = nullptr;
    }

    insertLibfuncts();
    return mySymTab;
}

node* getScopeHead(int scope) {
    for (auto it = scopeHeads.begin(); it != scopeHeads.end(); ++it) {
        if ((*it)->scope == scope) {
            return *it;
        }
    }

    return nullptr;
}

node* insertNode(int line, int scope, string name, int type) {
    int index = hashIndex(mySymTab->count);
    mySymTab->count++;

    node* newNode = new node;
    newNode->line = line;
    newNode->scope = scope;
    cout << name << endl;
    newNode->name = name;
    newNode->type = type;
    newNode->isHidden = false;

    /* insert into hash table */
    if (mySymTab->hash[index] == nullptr) {
        mySymTab->hash[index] = newNode;
    } else {
        newNode->next = mySymTab->hash[index];
        mySymTab->hash[index] = newNode;
    } 
    

    /* insert into correct scope list */
    if (scopeHeads.size() == 0) {
        newNode->nextInScope = nullptr;
        scopeHeads.push_back(newNode);
    } else {
        node* currentScope = getScopeHead(scope);
        if (currentScope == nullptr) {
            newNode->nextInScope = nullptr;
            scopeHeads.push_back(newNode);
        } else {
            node* iter = currentScope;
            while (iter->nextInScope != nullptr) {
                iter = iter->nextInScope;
            }

            iter->nextInScope = newNode; 
        }
    }

    return newNode;
}

node* lookupAll(string name) {
    for (int i = 0; i < HASH_SIZE; i++) {
        if (mySymTab->hash[i] != nullptr) {
            node *iter = mySymTab->hash[i];
            while(iter != nullptr) {
                if (iter->name == name && iter->isHidden == false) {
                    return iter;
                }
                iter = iter->next;
            }
        }
    }

    return nullptr;
}

node* lookupInScope(string name, int scope) {
    node* currentScope = getScopeHead(scope);

    if(currentScope == nullptr) {
        return nullptr;
    } else {
        node* iter = currentScope;
        while(iter != nullptr) {
            if (iter->name == name && iter->isHidden == false) {
                return iter;
            }
            iter = iter->nextInScope;
        }
    }

    return nullptr;
}

node* lookupAboveScopes(string name, int scope) {
    do {
        node* currentScope = getScopeHead(scope);

        if(currentScope != nullptr) {
            node* iter = currentScope;
            while(iter != nullptr) {
                cout << "l1" << endl;
                if (iter->name == name && iter->isHidden == false) {
                    return iter;
                }
                iter = iter->nextInScope;
            }
        }
        scope--;
        cout << "l2" << endl;
    } while(scope >= 0);  
    cout << "l3" << endl;  

    return nullptr;
}

void hideScope(int scope) {
    node* currentScope = getScopeHead(scope);

    if(currentScope != nullptr) {
        node* iter = currentScope;
        while(iter != nullptr) {
            iter->isHidden = true;
            iter = iter->nextInScope;
        } 
    }
}

// node* checkLocal(node* newNode) {
//     node* result = lookupInScope(newNode->name, newNode->scope);

//     if (result == nullptr) {
//         if (newNode->scope == 0) {
//             newNode->type = GLOBAL;
//         }

//         return newNode;
//     } else {
//         if (result->type = LIBFUNC) {
//             // cerr << 
//         }
//     }
// }

string typeToString(int type) {
    switch (type) {
    case GLOBAL:
        return "global variable";
        break;

    case LOCALV:
        return "local variable";
        break;

    case FORMAL:
        return "formal argument";
        break;

    case USERFUNC:
        return "user function";
        break;

    default:
        return "library function";
        break;
    }
}

void insertLibfuncts() {
    insertNode(0, 0, "print", LIBFUNC);
    insertNode(0, 0, "input", LIBFUNC);
    insertNode(0, 0, "objectmemberkeys", LIBFUNC);
    insertNode(0, 0, "objecttotalmembers", LIBFUNC);
    insertNode(0, 0, "objectcopy", LIBFUNC);
    insertNode(0, 0, "totalarguments", LIBFUNC);
    insertNode(0, 0, "argument", LIBFUNC);
    insertNode(0, 0, "typeof", LIBFUNC);
    insertNode(0, 0, "strtonum", LIBFUNC);
    insertNode(0, 0, "sqrt", LIBFUNC);
    insertNode(0, 0, "cos", LIBFUNC);
    insertNode(0, 0, "sin", LIBFUNC);
}

void printSyntax() {
    for (auto it = scopeHeads.begin(); it != scopeHeads.end(); ++it) {
        cout << "\n-------------\tScope #" << (*it)->scope << "\t-------------" << endl;
        node* iter = (*it);
        while (iter != nullptr) {
            cout << "\"" << iter->name << "\" [" << typeToString(iter->type)
                 << "] (line " << iter->line << ") (scope " << iter->scope << ")\n";

            iter = iter->nextInScope;
        }
        
    }
}

void printScopeLists() {
    cout << "Scope lists" << endl;
    for (auto it = scopeHeads.begin(); it != scopeHeads.end(); ++it) {
        node *iter = *it;
        cout << "Scope " << iter->scope << ":\t";
        while(iter != nullptr) {
            cout << iter->name << " - ";
            iter = iter->nextInScope;
        }
    }
}

void printHash() {
    cout << "\nHash table" << endl;
    for (int i = 0; i < HASH_SIZE; i++) {
        if (mySymTab->hash[i] != nullptr) {
            node *iter = mySymTab->hash[i];
            cout << "Scope " << iter->scope << ":\t";
            while(iter != nullptr) {
                cout << iter->name << " - ";
                iter = iter->next;
            }
        }
    }
}

// int main() {
//     initHash();

//     insertNode(1, 0, "a", 0);
//     insertNode(1, 0, "b", 3);
//     insertNode(1, 1, "c", 2);
//     insertNode(1, 1, "d", 4);
//     insertNode(1, 0, "e", 1);

//     // printScopeLists();
//     // printHash();

//     printSyntax();

//     return 0;
// }