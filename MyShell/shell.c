/* Assignment 1 - shell.c    */
/* Giorgos Nikitas - csd4695 */

#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <fcntl.h>
#include "shellLib.h"

char **parameters[20];
int outer_funct[20], inner_funct[20];

int main() {
    int k;

    welcome();

    while(TRUE) {   /* loop */
        type_prompt();  /* print shell line */

        int count = read_input();

        int i = 0;
        while(i < count) {
            if (strcmp(parameters[i][0], "quit") == 0) {    /* check for 'quit' command */
                exit(0);
            }  
            
            k = control(i);

            printf("%d\n", k);
            
            i = i + k + 1;  /* increment of i based on # of commands executed */
        }

        free_malloc(count);
    }

    return 0;
}