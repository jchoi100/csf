# If you have a 32-bit Lubuntu installed, the -m32 stuff is not
# needed, but it shouldn't hurt either. I hope. If you get some
# kind of error message about -m32, best to ask on Piazza.

CFLAGS=-std=c11 -pedantic -Wall -Wextra -O0 -save-temps -m32
LDFLAGS=-m32
ASFLAGS=-m32

all: sort cat

# sort: sort.o quick.o
sort: sort.o

cat: cat.S

mcat: mcat.S
	$(CC) -m32 $^ -o $@

qcat: qcat.S
	$(CC) -m32 -nostdlib $^ -o $@

clean:
	rm -rf *.o *.i *.s sort cat mcat qcat x86.tar.gz

archive:
	tar zcvf x86.tar.gz sort.c quick.S cat.c Makefile

submission:
	tar cvzf cs233-assignment-8-jchoi100.tar.gz Makefile sort.c cat.c quick.S
