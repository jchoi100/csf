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
    .globl  quicksort
    .globl  realsort
realsort:
    pushl   %ebp
    movl    %esp, %ebp
    subl    $24, %esp
    movl    12(%ebp), %eax
    cmpl    16(%ebp), %eax
    jge .L25
    subl    $4, %esp
    pushl   16(%ebp)
    pushl   12(%ebp)
    pushl   8(%ebp)
    call    partition
    addl    $16, %esp
    movl    %eax, -12(%ebp)
    subl    $4, %esp
    pushl   -12(%ebp)
    pushl   12(%ebp)
    pushl   8(%ebp)
    call    realsort
    addl    $16, %esp
    movl    -12(%ebp), %eax
    addl    $1, %eax
    subl    $4, %esp
    pushl   16(%ebp)
    pushl   %eax
    pushl   8(%ebp)
    call    realsort
    addl    $16, %esp
.L25:
    nop
    leave
    ret

quicksort:
    pushl   %ebp
    movl    %esp, %ebp
    subl    $8, %esp
    movl    12(%ebp), %eax
    subl    $1, %eax
    subl    $4, %esp
    pushl   %eax
    pushl   $0
    pushl   8(%ebp)
    call    realsort
    addl    $16, %esp
    nop
    leave
    ret