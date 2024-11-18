#include <stdio.h>
#include <stdlib.h>
#include <string.h>

char **parameters[20];
int outer_funct[20], inner_funct[20];

int read2(int outer_funct[]) {
    char *token, *out_save = NULL, *in_save = NULL, *special_save = NULL;
    char input[100], temp[20];
    int i = 0, j;

    scanf("%[^\n]%*c", input);

    token = strtok_r(input, ";", &out_save);
    while(token != NULL) {
        parameters[i] = (char **)malloc(20);

        char* in_token = strtok_r(token, " ", &in_save);
        j = 0;
        outer_funct[i] = 0;
        inner_funct[i] = 0;
        while(in_token != NULL) {
            if (strcmp(in_token, "|") == 0) {
                in_token = strtok_r(NULL, " ", &in_save);
                j = 0;
                outer_funct[i] = 1;
                i++;
                outer_funct[i] = 1;
                parameters[i] = (char **)malloc(20);
                continue;
            } else if (strcmp(in_token, "<") == 0 || strcmp(in_token, ">") == 0 || strcmp(in_token, ">>") == 0) {
                i++;
                parameters[i] = (char **)malloc(20);
                parameters[i][0] = (char *)malloc(strlen(in_token) + 1);
                strcpy(parameters[i][0], in_token);
                parameters[i][1] = 0;

                in_token = strtok_r(NULL, " ", &in_save);
                j = 0;
                i++;
                parameters[i] = (char **)malloc(20);
                continue;
            }

            parameters[i][j] = (char *)malloc(strlen(in_token) + 1);
            strcpy(parameters[i][j], in_token);

            in_token = strtok_r(NULL, " ", &in_save);
            j++;
            parameters[i][j] = 0;
        }
        i++;

        token = strtok_r(NULL, ";", &out_save);
    }

    int k = i;
    for (i = 0; i < k; i++) {
        j = 0;

        while(parameters[i][j] != NULL) {
            printf("%s\n", parameters[i][j]);

            j++;
        }
    }

    return k;
}

void free_malloc(int count) {
    int i, j;
    for (i = 0; i < count; i++) {
        j = 0;
        while(parameters[i][j] != NULL) {
            free(parameters[i][j]);
            j++;
        }
        free(parameters[i]);
    }
}

int exec_0(int i) {
    printf("Executing simple command: %s\n", parameters[i][0]);
    return 0;
}

int exec_1(int i) {
    printf("Executing pipe: %s\n", parameters[i][0]);
    return 1;
}

int exec_2(int i) {
    return 0;
}

int (*execute[])(int i) = {exec_0, exec_1, exec_2};

int main() {
    int count = read2(outer_funct);

    printf("%d\n", count);

    int i = 0;
    while(i < count) {
        i = i + execute[outer_funct[i]](i) + 1;
    }

    //free_malloc(command, ar, count);

    return 0;
}