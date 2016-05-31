#include <stdio.h>
#include <stdlib.h>
#include "parser.h"

void printUsage(char *cmd);

int main(int argc, char *argv[])
{
    if (argc < 2) {
        printUsage(argv[0]);
        exit(1);
    }
    parser_t parser;
    FILE *fp = fopen(argv[1], "r");
    if (!fp) {
        printf("Error opening file %s.. abort!\n", argv[1]);
        exit(1);
    }
    parser_init(&parser, fp);
    int moreCommands = parser_hasMoreCommands(parser);
    printf("moreCommands = %d\n", moreCommands);
    parser_advance(parser);
    //CommandType_t type = parser_commandType(parser);
    //printf("%d\n", type);
    moreCommands = parser_hasMoreCommands(parser);
    printf("moreCommands = %d\n", moreCommands);
    parser_advance(parser);
    moreCommands = parser_hasMoreCommands(parser);
    printf("moreCommands = %d\n", moreCommands);
    parser_advance(parser);
    moreCommands = parser_hasMoreCommands(parser);
    printf("moreCommands = %d\n", moreCommands);
    parser_advance(parser);
    moreCommands = parser_hasMoreCommands(parser);
    printf("moreCommands = %d\n", moreCommands);
    return 0;
}

void printUsage(char *cmd)
{
    puts("Usage:");
    puts("To compile a single <filename>.vm to a <filename>.asm, use");
    printf("  %s <source_file>.vm\n\n", cmd);
    puts("To compile a directory <dirname> full of .vm files and output <dirname>.asm, use");
    printf("  %s <dirname>\n", cmd);
}
