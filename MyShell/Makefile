CC=gcc
CFLAGS=-I.
DEPS = hellomake.h

%.o: %.c $(DEPS)
	$(CC) -c -o $@ $< $(CFLAGS)

shell: shell.o shellLib.o
     $(CC) -o shell shell.o shellLib.o

clean:
    rm *.o shellLib