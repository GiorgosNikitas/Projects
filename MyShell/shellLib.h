/* Assignment 1 - shellLib.h */
/* Giorgos Nikitas - csd4695 */

#define CMD_PATH "/usr/bin/"
#define TRUE 1

/* paramaters[i][0] contains command */
/* parameters[i][j], j > 0, contains */
/* parammeters of the command        */
extern char **parameters[];

/* Function type of each command         */
/* outer_funct[i] == 0: normal execution */
/* outer_funct[i] == 1: pipe execution   */
extern int outer_funct[];

/* Function type of each command           */
/* outer_funct[i] == 0: basic commands     */
/* outer_funct[i] == 1: output redirection */
/* outer_funct[i] == 2: input redirection  */
/* outer_funct[i] == 3: output append      */
extern int inner_funct[];

/* Prints a message once you enter the shell. */
void welcome(void);

/* Prints the name of the shell, the logged in user */
/* and the path of the currently working directory. */
void type_prompt(void);

/* Returns TRUE if one of the symbols <,>,>> is found. */
int check_special(char*);

/* Reads input from the command line, separates it to */
/* commands that will be executed (parameters[][])    */
/* and sets the type function for each command        */
/* (outer_funct[], inner_funct[]).                    */
/* Returns: Number of commands to be executed.        */
int read_input(void);

/* Frees memory allocated by malloc. */
void free_malloc(int);

/* Returns complete path of command. */
char* complete_command(char*);

/* Executes piped commands. */
int exec_pipe(int);

/* Execuets basic commands. */
int exec_0(int);

/* Executes output redirection. */
int exec_1(int);

/* Executes input redirection. */
int exec_2(int);

/* Executes append output redirection. */
int exec_3(int);

/* Array of pointers to exec_0-3 functions */
extern int (*execute[])(int); 

/* Determines what the type of execution */
/* will be, normal or pipe.              */
int control(int);