/*------------------------------------------*/
/* Georgios Nikitas AM: 4695                */
/* Assignment 3                             */
/* File name: symtablehash.c                */
/*------------------------------------------*/

#include "symtable.h"

#define HASH_MULTIPLIER 65599

typedef struct binding {
    char *key;
    void *value;
    struct binding * next;
} binding;

struct SymTable {
    binding** table;
    size_t buckets;
    size_t length;
};

static unsigned int SymTable_hash(const char *pcKey) {
	size_t ui;
    unsigned int uiHash = 0U;

	for (ui = 0U; pcKey[ui] != '\0'; ui++) 

		uiHash = uiHash * HASH_MULTIPLIER + pcKey[ui];

	return uiHash % 509;
}

SymTable_T SymTable_new() {
    SymTable_T new_hash = malloc(sizeof(SymTable_T*));

    new_hash->buckets = 509;
    new_hash->length = 0;
    new_hash->table = malloc(509 * sizeof(binding*));
    return new_hash;
}

void SymTable_free(SymTable_T oSymTable) {
    struct binding *tmp;
    struct binding *current;
    int i;
    for (i = 0; i < oSymTable->length; i++) {
        if (oSymTable->table[i] != NULL) {
            for (current = oSymTable->table[i]; current != NULL; current = current->next) {
                tmp = current;
                free(tmp->key);
                free(tmp);
            }
        }
    }   
    free(oSymTable->table);
}

unsigned int SymTable_getLength(SymTable_T oSymTable) {
    return oSymTable->length;
}

int SymTable_put(SymTable_T oSymTable, const char *pcKey, const void *pvValue) {
    size_t index;
    binding *symtab;

    assert(oSymTable);
    assert(pcKey);

    if (SymTable_contains(oSymTable, pcKey)) {return 0;}
    
    index = SymTable_hash(pcKey);
    symtab = (binding *) malloc(sizeof(binding));
    symtab->key = (char *) malloc(strlen(pcKey) * sizeof(char) + 1);
    strcpy(symtab->key, pcKey);
    symtab->value = (void *) pvValue;
    symtab->next = oSymTable->table[index];
    oSymTable->table[index] = symtab;
    oSymTable->length++;

    return 1;
}

int SymTable_remove(SymTable_T oSymTable, const char *pcKey){
    struct binding *prev,*curr;
    assert(oSymTable && pcKey);

    curr = oSymTable->table[SymTable_hash(pcKey)];
    prev = NULL;
    while(curr){
        if(strcmp(curr->key,pcKey) == 0) break;
        prev = curr;
        curr = curr->next;
    }
    if(curr==NULL) return 0; /*was not found*/

    if(prev == NULL) oSymTable->table[SymTable_hash(pcKey)] = curr->next;
    else prev->next = curr->next;
    free(curr->key);
    free(curr);
    oSymTable->length--;
    return 1;
}

int SymTable_contains(SymTable_T oSymTable, const char *pcKey) {
    size_t index = SymTable_hash(pcKey);
    binding *symtab;

    assert(oSymTable != NULL);
    assert(pcKey != NULL);

    for (symtab = oSymTable->table[index]; symtab != NULL; symtab = symtab->next) {
        if (strcmp(symtab->key, pcKey) == 0) {return 1;}
    }
    
    return 0;
}

void *SymTable_get(SymTable_T oSymTable, const char *pcKey) {
    size_t index = SymTable_hash(pcKey);
    binding *symtab;

    assert(oSymTable != NULL);
    assert(pcKey != NULL);

    if (!SymTable_contains(oSymTable, pcKey)) {return NULL;}

    for (symtab = oSymTable->table[index]; symtab != NULL; symtab = symtab->next) {
        if (strcmp(symtab->key, pcKey)) {break;}
    }

    return symtab->value;
}

void SymTable_map(SymTable_T oSymTable, void (*pfApply)(const char *pcKey, void *pvValue, void *pvExtra),const void *pvExtra) {
    binding *symtab;
    int i;

    assert(oSymTable != NULL);
    assert(pfApply != NULL);

    for (i = 0; i < oSymTable->buckets; i++) {
        for (symtab = oSymTable->table[i]; symtab != NULL; symtab = symtab->next) {
            pfApply(symtab->key, symtab->value, (void *) pvExtra);
        }
    }
}