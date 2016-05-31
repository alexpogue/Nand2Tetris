#include <stdio.h>

typedef struct parser *parser_t;

typedef enum { C_ERROR, C_ARITHMETIC, C_PUSH, C_POP, C_LABEL, C_GOTO, C_IF, 
               C_FUNCTION, C_RETURN, C_CALL } CommandType_t;

void parser_init(parser_t *parser, FILE *fp);
int parser_hasMoreCommands(parser_t parser);
void parser_advance(parser_t parser);
CommandType_t parser_commandType(parser_t parser);
void parser_cleanUp(parser_t *parser);
