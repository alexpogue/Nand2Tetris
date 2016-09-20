%option noyywrap
%{
#include "tree.h"
#include "vmparser.tab.h"
int line_num = 1;
%}
%%
"//"[^\n]*              /* consume comment */;
add                     return ADD;
sub                     return SUB;
neg                     return NEG;
eq                      return EQ;
gt                      return GT;
lt                      return LT;
and                     return AND;
or                      return OR;
not                     return NOT;

push                    return PUSH;
pop                     return POP;

argument                return ARGUMENT;
local                   return LOCAL;
static                  return STATIC;
constant                return CONSTANT;
this                    return THIS;
that                    return THAT;
pointer                 return POINTER;
temp                    return TEMP;

label                   return LABEL;
goto                    return GOTO;
if-goto                 return IFGOTO;
function                return FUNCTION;
call                    return CALL;
return                  return RETURN;

[0-9]+                  { yylval.ival = atoi(yytext); return INT; }
    /* TODO: add sval to a hash table or dynamic array to be freed later */
[a-zA-Z][a-zA-Z0-9]*    { yylval.sval = strdup(yytext); return STRING; }

\n                      { ++line_num; return ENDL; }
.                       ;
