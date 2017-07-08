%{
#include <stdio.h>
#include <stdlib.h>
#include "tree.h"

int yylex();
int yyparse();
FILE *yyin;

void yyerror(const char *s);
extern int line_num;
%}

%union {
    int ival;
    char *sval;
    tree_t *tree;
}

%type <tree> vmprogram
%type <tree> body_lines
%type <tree> body_line
%type <tree> command
%type <tree> arithmetic_command

%token <ival> INT
%token <sval> STRING
%token <tree> ENDL
%token <tree> ADD SUB NEG EQ GT LT AND OR NOT
%token PUSH POP
%token ARGUMENT LOCAL STATIC CONSTANT THIS THAT POINTER TEMP
%token LABEL GOTO IFGOTO FUNCTION CALL RETURN


%%

vmprogram:
    body_lines { $$ = tree_init("body_lines"); tree_addChild($$, $1); puts("found body_lines, printing tree"); tree_print($$); }
    ;
body_lines: /* empty */ { $$ = tree_init("empty body_lines"); }
    | body_lines body_line { $$ = tree_init("body_lines body_line"); tree_addChild($$, $1); tree_addChild($$, $2); puts("found body_lines body_line, printing tree"); tree_print($$); }
    ;
body_line:
    command ENDL { $$ = tree_init("command ENDL"); tree_addChild($$, $1); tree_addChild($$, $2); puts("found command ENDL, printing tree"); tree_print($$); }
    | ENDL { $$ = tree_init("ENDL"); puts("found ENDL, printing tree"); tree_print($$); }
    ;
command:
    arithmetic_command { $$ = tree_init("arithmetic_command"); tree_addChild($$, $1); puts("found arithmetic_command, printing tree"); tree_print($$); }
    | memory_access_command
    | program_flow_command
    ;
arithmetic_command:
    ADD { $$ = tree_init("ADD"); puts("found ADD, printing tree"); tree_print($$); }
    | SUB { $$ = tree_init("SUB"); puts("found SUB, printing tree"); tree_print($$); }
    | NEG { $$ = tree_init("NEG"); puts("found NEG, printing tree"); tree_print($$); }
    | EQ { $$ = tree_init("EQ"); puts("found EQ, printing tree"); tree_print($$); }
    | GT { $$ = tree_init("GT"); puts("found GT, printing tree"); tree_print($$); }
    | LT { $$ = tree_init("LT"); puts("found LT, printing tree"); tree_print($$); }
    | AND { $$ = tree_init("AND"); puts("found AND, printing tree"); tree_print($$); }
    | OR { $$ = tree_init("OR"); puts("found OR, printing tree"); tree_print($$); }
    | NOT { $$ = tree_init("NOT"); puts("found NOT, printing tree"); tree_print($$); }
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
