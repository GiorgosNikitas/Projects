/* Assignment 1 - shellLib.c */
/* Giorgos Nikitas - csd4695 */

#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <fcntl.h>
#include "shellLib.h"

void welcome() {
    printf("%c]0;%s%c", '\033', "csd4695-cs345sh", '\007'); /* change shell window nanme */

    system("clear");
    printf("\t|------------------------------------|\n");
    printf("\t|                                    |\n");
    printf("\t|            Assignment 1            |\n");
    printf("\t|      Giorgos Nikitas - CSD4695     |\n");
    printf("\t|                                    |\n");
    printf("\t|         Welcome to my shell        |\n");
    printf("\t|                                    |\n");
    printf("\t|------------------------------------|\n");
}

void type_prompt() {
    
    int size = pathconf(".", _PC_PATH_MAX);
    
    char* path = (char *)malloc(size);

    printf("csd4695-cs345sh@%s:%s ", getlogin(), getcwd(path,size));    /* shell line information */
} 

/* Returns TRUE if one of the symbols <, >, >> is found. */
int check_special(char* str) {
    return (strcmp(str, "<") == 0 || strcmp(str, ">") == 0 || strcmp(str, ">>") == 0);
}

/* Reads input line, enters commands and their parameters into parameters[][]. */
/* Assigns 0 for normal execution, 1 for pipe into outer_funct[i]. */
/* Assigns 0-3 into inner_funct[i] for basic commands, output redirection, */
/* input redirection, output append-redirection. */
int read_input() {
    char *token, *out_save = NULL, *in_save = NULL;
    char input[100];
    int i = 0;      /* command counter */
    int j;          /* parameters counter for each command */

    fgets(input, 100, stdin);
    j = 0;
    while(input[j] != '\0') {
        if (input[j] == '\n') {
            input[j] = '\0';
            break;
        }
        j++;
    } 

    token = strtok_r(input, ";", &out_save);    /* tokenize by ";" */
    while(token != NULL) {
        parameters[i] = (char **)calloc(1, sizeof(char *));
        if (parameters[i] == NULL) {
            perror("Memory allocation failed\n");
            exit(2);
        }

        char* in_token = strtok_r(token, " ", &in_save);    /* tokemize by " " */
        j = 0;
        outer_funct[i] = 0;
        inner_funct[i] = 0;
        while(in_token != NULL) {
            if (strcmp(in_token, "|") == 0) {   /* check for pipe */
                in_token = strtok_r(NULL, " ", &in_save);
                j = 0;
                outer_funct[i] = 1;
                i++;
                outer_funct[i] = 1;
                
                parameters[i] =(char **)calloc(1, sizeof(char *));
                if (parameters[i] == NULL) {
                    perror("Memory allocation failed\n");
                    exit(2);
                }

                continue;
            } else if (check_special(in_token)) {   /* check for I/O redirection */
                i++;
                outer_funct[i] = outer_funct[i-1];
                
                int funtion;
                if (strcmp(in_token, ">") == 0) {
                    funtion = 1;
                } else if (strcmp(in_token, "<") == 0) {
                    funtion = 2;
                } else {
                    funtion = 3;
                }
                inner_funct[i-1] = funtion;
                inner_funct[i] = funtion;
                inner_funct[i+1] = funtion;

                parameters[i] = (char **)calloc(1, sizeof(char *));
                if (parameters[i] == NULL) {
                    perror("Memory allocation failed\n");
                    exit(2);
                }
                
                parameters[i][0] = (char *)calloc(1, strlen(in_token) + 1);
                if (parameters[i][0] == NULL) {
                    perror("Memory allocation failed\n");
                    exit(2);
                }
                strcpy(parameters[i][0], in_token);

                in_token = strtok_r(NULL, " ", &in_save);
                j = 0;
                i++;
                parameters[i] = (char **)calloc(1, sizeof(char *));
                if (parameters[i] == NULL) {
                    perror("Memory allocation failed\n");
                    exit(2);
                }
                continue;
            }

            parameters[i][j] = (char *)calloc(1, strlen(in_token) + 1);
            if (parameters[i][j] == NULL) {
                perror("Memory allocation failed\n");
                exit(2);
            }

            strcpy(parameters[i][j], in_token);

            in_token = strtok_r(NULL, " ", &in_save);
            j++;
            parameters[i][j] = 0;
        }
        i++;
        parameters[i] = 0;

        token = strtok_r(NULL, ";", &out_save);
    }

    return i;   /* number of commands read */
}

void free_malloc(int count) {
    int i, j;
    for (i = 0; i < count; i++) {
        j = 0;
        while (parameters[i][j] != 0) {
            printf("freeing: %s\n", parameters[i][j]);
            free(parameters[i][j]);
            j++;
        }
        free(parameters[i]);
    }
}

char* complete_command(char* str) {
    char* command = (char *)calloc(1, strlen(str) + strlen(CMD_PATH) + 1);
    if (command == NULL) {
        perror("Memory allocation failed\n");
        exit(2);
    }
    strcpy(command, CMD_PATH);
    strcat(command, str);
    return command;
}

/* executes piped commands */
int exec_pipe(int i) {
    int fd[2];
	int pid;
	int fd_save = 0;
    int k = i;

	while (parameters[i] != NULL && outer_funct[i] == 1) {
		pipe(fd);
        
        pid = fork();
		if (pid < 0) {
			perror("Fork failed\n");
			exit(1);
		} else if (pid == 0) {
			dup2(fd_save, STDIN_FILENO);
			if (parameters[i+1] != NULL) {  /* next command */
				dup2(fd[1], STDOUT_FILENO);
			}
			close(fd[0]);
			execute[inner_funct[i]](i);
            exit(0);
		} else {
			waitpid(pid, NULL, 0);
			close(fd[1]);
			fd_save = fd[0];
            if (inner_funct[i] > 0) {
                i += 2;
            }
			i++;
		}
	}

    return i - k - 1;
}

/* execution of basic commands */
int exec_0(int i) {
    if (strcmp(parameters[i][0], "chdir") == 0) {   /* check for chdir command */
        if(chdir(parameters[i][1]) != 0) {
            perror("chdir failed");
            exit(1);
        }
    } else {    /* any other commnd */
        char* command = complete_command(parameters[i][0]);

        int pid = fork();
        if (pid == 0) {
            if(execve(command, parameters[i], 0) == -1) {
                perror("Bad syntax");
                exit(1);
            }
            exit(0);
        } else if (pid > 0) {
            waitpid(pid, NULL, 0);
            return 0;
        } else {
            perror("Fork failed\n");
            exit(1);
        }
    }
}

/* execution of output redirection (>) */
int exec_1(int i) {
    int pid = fork();
    if (pid == 0) {
        char* command = complete_command(parameters[i][0]);
        
        int file1 = open(parameters[i+2][0], O_RDWR | O_CREAT | O_TRUNC, 0777); 
        if (file1 == -1) {
            perror("Could not open file\n");
            exit(1);
        }

        int file2 = dup2(file1, STDOUT_FILENO);
        close(file1);
        if(execve(command, parameters[i], 0) == -1) {
            perror("Bad syntax");
            exit(1);
        }
        exit(0);
    } else if (pid > 0) {
        waitpid(pid, NULL, 0);
        return 2;
    } else {
        perror("Fork failed\n");
        exit(1);
    }  
    
    return 2;
}

/* execution of input redirection (<) */
int exec_2(int i) {
    int pid = fork();
    if (pid == 0) {
        int file1 = open(parameters[i+2][0], O_RDWR | O_CREAT, 0777);
        if (file1 == -1) {
            exit(1);
        }

        int file2 = dup2(file1, STDIN_FILENO);
        close(file1);

        char* command = complete_command(parameters[i][0]);
        if(execve(command, parameters[i], 0) == -1) {
            perror("Bad syntax");
            exit(1);
        }
        exit(0);
    } else if (pid > 0) {
        waitpid(pid, NULL, 0);
        return 2;
    } else {
        perror("Fork failed\n");
        exit(1);
    }    

    return 2;
}

/* execution of redirection of output (>>) */
int exec_3(int i) {
    int pid = fork();
    if (pid == 0) {
        int file1 = open(parameters[i+2][0], O_RDWR | O_APPEND, 0777);
        if (file1 == -1) {
            exit(1);
        }

        int file2 = dup2(file1, STDOUT_FILENO);
        close(file1);

        char* command = complete_command(parameters[i][0]);
        if(execve(command, parameters[i], 0) == -1) {
            perror("Bad syntax");
            exit(1);
        }
        exit(0);
    } else if (pid > 0) {
        waitpid(pid, NULL, 0);
        return 2;
    } else {
        perror("Fork failed\n");
        exit(1);
    }   
    
    return 2;
}

int (*execute[])(int i) = {exec_0, exec_1, exec_2, exec_3};

/* determines the type of execution (normal or pipe) */
int control(int i) {
    int ret;
    if (outer_funct[i] == 0) {      /* normal execution */
        ret = execute[inner_funct[i]](i);
    } else {                        /* pipe */
        ret = exec_pipe(i);
    }

    return ret;
}