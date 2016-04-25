# Write the quicksort function in i386 assembly. Write it
# FROM SCRATCH please, DON'T write it in C first and then
# copy the compiler's assembly here. We'll know...
#
# HINT: You will want to write *another* function that you
# can call recursively with lower and upper bounds. If you
# try calling quicksort itself recursively, which only has
# the array and its length as parameters, things will most
# likely go very wrong.
#
# HINT: So your quicksort function will call *that* *other*
# *function* which will (a) call on the partition function
# to do most of the actual work and (b) call itself to deal
# with sub-arrays recursively.
#
# See https://en.wikipedia.org/wiki/Quicksort#Algorithm if
# you need a refresher as to what partition actually does
# and how it relates to quicksort as a whole. There's even
# some pseudo-code for that recursive function you'll need
# to write...

	.text
	.globl	quicksort
    .globl  realsort

quicksort:
    pushl %ebp
    movl  %esp, %ebp
    pushl %ebx




    leave
	ret


realsort:
    pushl %ebp
    movl  %esp, %ebp
    pushl %ebx




    leave
    ret
