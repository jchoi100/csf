# 600.233 Computer System Fundamentals
# Assignment 8
# Dodge the x86!
# Name: Joon Hyuck Choi
# JHED: jchoi100
# email: jchoi100@jhu.edu
# quick.S

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
    pushl %ebp                # push current ebp to stack
    movl  %esp, %ebp          # move esp to ebp
    movl  12(%ebp), %eax      # move lo into eax
    cmpl  16(%ebp), %eax      # compare lo, hi
    jge ELSE1                 # end if hi <= lo
    subl  $4, %esp            # leave space for int p
    pushl 16(%ebp)            # 3rd arg: hi
    pushl 12(%ebp)            # 2nd arg: lo
    pushl  8(%ebp)            # 1st arg: a[]
    call  partition           # call partition on a, lo, hi
    addl  $12, %esp           # move esp 12 up
    movl  %eax, -4(%ebp)      # move parition ret val to our local var
    pushl %eax                # 3rd arg: p
    pushl 12(%ebp)            # 2nd arg: lo
    pushl  8(%ebp)            # 1st arg: a[]
    call  realsort            # call realsort on a, lo, p
    addl  $12, %esp           # move esp 12 up
    pushl 16(%ebp)            # 3rd arg: hi
    addl  $1, -4(%ebp)        # p++
    pushl -4(%ebp)                # 2nd arg: p + 1
    pushl  8(%ebp)            # 1st arg: a[]
    call  realsort            # call realsort on a, p + 1, hi
    addl  $12, %esp           # move esp 12 up
ELSE1:                        # when hi < lo
    leave                     # leave
    ret                       # return to caller

quicksort:
    pushl %ebp                # push current ebp to stack
    movl  %esp, %ebp          # move esp to ebp
    movl  12(%ebp), %eax       # eax <- n
    subl  $1, %eax             # eax -= 1
    pushl %eax                # 3rd arg: n - 1
    pushl $0                  # 2nd arg: 0
    pushl 8(%ebp)             # 1st arg: a[]
    call  realsort             # call realsort on a, 0, n-1
    addl  $12, %esp
    leave                     # leave
    ret                       # return to caller
