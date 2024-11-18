all: parser.cpp al.cpp 
	g++ -g -o parser.out al.cpp parser.cpp 
	g++ -o main vmMain.cpp vmLib.h

parser.cpp: parser.y
	bison --yacc --defines=parser.h --output=parser.cpp parser.y

al.cpp:	scanner.l
	flex -d --outfile=al.cpp scanner.l
	
clean:
	rm -f al.cpp parser.cpp al.h parser.h parser.output *.out quads.txt main avm_binary.abc