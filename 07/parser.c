#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "parser.h"

typedef struct {
    CommandType_t type;
    char *arg1;
    char *arg2;
} command_t;

struct parser {
    command_t curCommand;
    FILE *file;
    char *curLine;
    char *nextLine;
};

static char *readOneLine(FILE *fp);
static command_t parseLine(char *line);
static CommandType_t getCommandType(char *line);
static int isArithmeticCommand(char *line);

void parser_init(parser_t *parser, FILE *fp)
{
    *parser = malloc(sizeof **parser);
    if (!(*parser)) {
        puts("Couldn't malloc parser, aborting");
        exit(1);
    }
    (*parser)->file = fp;
    (*parser)->curLine = NULL;
    (*parser)->nextLine = readOneLine(fp);
}

int parser_hasMoreCommands(parser_t parser)
{
    return parser->nextLine != NULL;
}

void parser_advance(parser_t parser)
{
    if (parser->curLine)
        free(parser->curLine);
    parser->curLine = parser->nextLine;
    parser->nextLine = readOneLine(parser->file);
    if (parser->curLine)
        parser->curCommand = parseLine(parser->curLine);
    else
        puts("Error: tried to parse a NULL line");
}

CommandType_t parser_commandType(parser_t parser)
{
    return parser->curCommand.type;
}

void parser_cleanUp(parser_t *parser)
{
    if (parser && *parser) {
        if ((*parser)->curLine)
            free((*parser)->curLine);
        if ((*parser)->nextLine)
            free((*parser)->nextLine);
    }
    if (*parser)
        free(*parser);
}

static command_t parseLine(char *line)
{
    command_t cmd;
    cmd.type = getCommandType(line);
    /*
    cmd.arg1 = getArg1(line);
    cmd.arg2 = getArg2(line);
    */
    return cmd;
}

static CommandType_t getCommandType(char *line)
{
    CommandType_t type = C_ERROR;
    if (isArithmeticCommand(line))
        type = C_ARITHMETIC;
    /* // TODO implement
    else if (isPushCommand(line))
        type = C_PUSH;
    else if (isPopCommand(line))
        type = C_POP;
    else if (isLabelCommand(line))
        type = C_LABEL;
    else if (isGotoCommand(line))
        type = C_GOTO;
    else if (isIfCommand(line))
        type = C_IF;
    else if (isFunctionCommand(line))
        type = C_FUNCTION;
    else if (isReturnCommand(line))
        type = C_RETURN;
    else if (isCallCommand(line))
        type = C_CALL;
        */
    return type;
}

static int isArithmeticCommand(char *line)
{
    return (strncmp(line, "add", 3) == 0) || (strncmp(line, "sub", 3) == 0) ||
           (strncmp(line, "eq", 2) == 0) || (strncmp(line, "gt", 2) == 0) ||
           (strncmp(line, "lt", 2) == 0) || (strncmp(line, "and", 3) == 0) ||
           (strncmp(line, "or", 2) == 0) || (strncmp(line, "not", 3) == 0);
}

static char* readOneLine(FILE *fp)
{
    char *line = NULL;
    size_t lineCapp = 0;
    ssize_t charsInLine = getline(&line, &lineCapp, fp);
    if (charsInLine == -1) {
        free(line);
        if (feof(fp)) {
            return NULL;
        }
        else  {
            puts("error reading line, aborting");
            exit(1);
        }
    }
    else if (line && line[charsInLine - 1] == '\n') {
        line[charsInLine - 1] = 0;
    }
    return line;
}
