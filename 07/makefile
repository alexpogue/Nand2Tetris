all: vmparser
vmparser: vmparser.tab.c lex.vmparser.c tree.c tree.h
	gcc -o vmparser vmparser.tab.c lex.vmparser.c tree.c
lex.vmparser.c: vmparser.l vmparser.tab.h
	flex -o lex.vmparser.c vmparser.l
vmparser.tab.h vmparser.tab.c: vmparser.y
	bison -d vmparser.y
clean:
	rm lex.vmparser.c vmparser.tab.c vmparser.tab.h
