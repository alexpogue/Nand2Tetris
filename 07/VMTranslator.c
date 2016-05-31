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
    parser_t parser = NULL;
    FILE *fp = fopen(argv[1], "r");
    if (!fp) {
        printf("Error opening file %s.. abort!\n", argv[1]);
        exit(1);
    }
    parser_init(&parser, fp);
    while (parser_hasMoreCommands(parser)) {
        parser_advance(parser);
        CommandType_t type = parser_commandType(parser);
        printf("type = %d\n", type);
    }

    parser_cleanUp(&parser);
    fclose(fp);
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
