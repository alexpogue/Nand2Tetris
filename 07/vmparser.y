%{
#include <stdio.h>
#include <stdlib.h>
int yylex();
int yyparse();
FILE *yyin;

void yyerror(const char *s);
extern int line_num;
%}

%union {
    int ival;
    char *sval;
}

%token <ival> INT
%token <sval> STRING
%token ENDL
%token ADD SUB NEG EQ GT LT AND OR NOT
%token PUSH POP
%token ARGUMENT LOCAL STATIC CONSTANT THIS THAT POINTER TEMP
%token LABEL GOTO IFGOTO FUNCTION CALL RETURN

%%

vmprogram:
    body_lines { puts("Done parsing vm file\n"); }
    ;
body_lines:
    /* empty */
    | body_lines body_line
    ;
body_line:
    command ENDL
    | ENDL
    ;
command:
    arithmetic_command
    | memory_access_command
    | program_flow_command
    ;
arithmetic_command:
    ADD { puts("found add command"); }
    | SUB { puts("found sub command"); }
    | NEG
    | EQ
    | GT
    | LT
    | AND
    | OR
    | NOT
    ;
memory_access_command:
    PUSH segment index
    | POP segment index
    ;
segment:
    ARGUMENT
    | LOCAL
    | STATIC
    | CONSTANT
    | THIS
    | THAT
    | POINTER
    | TEMP
    ;
index:
    INT
    ;
program_flow_command:
    LABEL symbol
    | GOTO symbol
    | IFGOTO symbol
    | FUNCTION functionName nLocals
    | CALL functionName nArgs
    | RETURN
    ;
symbol:
    STRING
    ;
functionName:
    STRING
    ;
nLocals:
    INT
    ;
nArgs:
    INT
    ;
%%


int getParseTree(FILE *src)
{
    // TODO: parse tree logic
    yyin = src;
    do {
        yyparse();
    } while (!feof(yyin));
    return 0;
}

int main(int argc, char *argv[])
{
    puts("Hello world!");
    FILE *myfile = fopen("file.vm", "r");
    if (!myfile) {
        puts("I can't open file.vm!");
        return -1;
    }
    getParseTree(myfile);
    fclose(myfile);
}

void yyerror(const char *s)
{
    printf("EEK, parse error on line %d! Message: %s\n", line_num, s);
    exit(-1);
}
