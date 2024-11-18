/*------------------------------------------*/
/* Georgios Nikitas AM: 4695                */
/* Assignment 3                             */
/* File name: testmytab.c                   */
/*------------------------------------------*/

#include "symtable.h"

int main(){
    SymTable_T symtab = SymTable_new();

    if (SymTable_put(symtab, "key0", "elem0")) {
        printf("OK: Element added.\n");
    } else {
        printf("FAIL: Element not added.\n");
    }
    
    if (SymTable_put(symtab, "key1", "elem1")) {
        printf("OK: Element added.\n");
    } else {
        printf("FAIL: Element not added.\n");
    }

    if (!SymTable_put(symtab, "key1", "elem1")) {
        printf("OK: Didin't duplicate element.\n");
    } else {
        printf("FAIL: Duplicated element.\n");
    }

    if (symtab->length == 2) {
        printf("OK: Symatble's size is correct.\n");
    } else {
        printf("FAIL: Symatble's size is wrong.\n");
    }

    SymTable_free(symtab);

    return 0;
}