.PHONY: clean

VMTranslator: parser.o VMTranslator.o
	gcc -g -o VMTranslator parser.o VMTranslator.o

parser.o: parser.c parser.h
	gcc -g -c parser.c

VMTranslator.o: VMTranslator.c
	gcc -g -c VMTranslator.c

clean:
	rm *.o
