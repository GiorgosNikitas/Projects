/*------------------------------------------*/
/* Georgios Nikitas AM: 4695                */
/* Assignment 3                             */
/* File name: symtablelist.c                */
/*------------------------------------------*/

#include "symtable.h"

typedef struct binding {
    char *key;
    void *value;
    struct binding *next;
} binding;

struct SymTable {
    binding *head;
    size_t length;
};

SymTable_T SymTable_new(void) {
    SymTable_T new_SymTable = malloc(sizeof(SymTable_T*));
    new_SymTable->head = NULL;
    new_SymTable->length = 0;
    return new_SymTable;
}

void SymTable_free(SymTable_T oSymTable) {
    if (oSymTable != NULL) {
        binding *index;
        binding *temp;

        index = oSymTable->head;
        while(index != NULL) {
            temp = index->next;
            free(index->key);
            free(index);
            index = temp;
        }
        free(oSymTable);
    }
}

unsigned int SymTable_getLength(SymTable_T oSymTable) {
    assert(oSymTable != NULL);

    return oSymTable->length;
}

int SymTable_put(SymTable_T oSymTable, const char *pcKey, const void *pvValue) {
    binding *new_binding;

    assert(pcKey != NULL);
    assert(oSymTable != NULL);

    if (SymTable_contains(oSymTable, pcKey)) {
      return 0;
    }

    new_binding = (binding*) malloc(sizeof(binding*));
    new_binding->next = oSymTable->head;
    oSymTable->head = new_binding;
    oSymTable->length++;
    new_binding->key = (char*) malloc(strlen(pcKey) * sizeof(char) + 1);
    strcpy(new_binding->key, pcKey);
    new_binding->value = (void*) pvValue;

    return 1;
}

int SymTable_remove(SymTable_T oSymTable, const char *pcKey) {
    binding *index;
    binding *prev;

    assert(oSymTable != NULL);
    assert(pcKey != NULL);

    if (!SymTable_contains(oSymTable, pcKey)) {return 0;}

    prev = NULL;
    for (index = oSymTable->head; index != NULL; index = index->next) {
        if (strcmp(index->key, pcKey) == 0) {
            break;
        }
        prev = index;
    }

    if(index == NULL) {
        return 0;
    }

    if(prev == NULL) {
        oSymTable->head = index->next;
    } else {
        prev->next = index->next;
    }
    free(index->key);
    free(index);
    oSymTable->length--;
    return 1;
}

int SymTable_contains(SymTable_T oSymTable, const char *pcKey) {
    binding *index;

    assert(oSymTable != NULL);
    assert(pcKey != NULL);

    index = oSymTable->head;
    while (index != NULL) {
      if (strcmp(index->key, pcKey) == 0) {
        return 1;
      }
      index = index->next;
    }
    return 0;
}

void *SymTable_get(SymTable_T oSymTable, const char *pcKey) {
    binding *index;

    assert(oSymTable != NULL);
    assert(pcKey != NULL);

    if (!SymTable_contains(oSymTable, pcKey)) {
      return NULL;
    }

    for (index = oSymTable->head; index != NULL; index = index->next) {
        if (strcmp(index->key, pcKey) == 0) {
            break;
        }
    }
    return index->value;
}

void SymTable_map(SymTable_T oSymTable, void (*pfApply)(const char *pcKey, void *pvValue, void *pvExtra), const void *pvExtra) {
    binding *index;

    assert(oSymTable != NULL);
    assert(pfApply != NULL);

    for (index = oSymTable->head; index != NULL; index = index->next) {
        pfApply(index->key, index->value, (void *) pvExtra);
    }
}