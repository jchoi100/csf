# If you have a 32-bit Lubuntu installed, the -m32 stuff is not
# needed, but it shouldn't hurt either. I hope.

CFLAGS=-std=c99 -pedantic -Wall -Wextra -O0 -save-temps -m32
LDFLAGS=-m32
ASFLAGS=-m32

all: sort move

sort: sort.o bubble.o
move: move.o minmax.o

qcat: qcat.s
	gcc $(CFLAGS) -nostdlib qcat.s -o qcat

clean:
	rm -rf *.o *.i sort move x86.tar.gz

archive:
	tar zcvf x86.tar.gz sort.c bubble.s move.c minmax.s Makefile
