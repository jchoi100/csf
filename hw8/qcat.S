# 600.233 Computer System Fundamentals
# Assignment 8
# Dodge the x86!
# Name: Joon Hyuck Choi
# JHED: jchoi100
# email: jchoi100@jhu.edu
# qcat.S

EOF -1
ZERO 0
SYS_EXIT 1
SYS_READ 3
SYS_WRTE 4
BUF_SIZE 128

    .bss
    .lcomm buffer, BUF_SIZE
    .text
    .globl  _start
_start:
    movl    $buffer, %ecx
    jmp GETEXIT                     # putchar
LOOP:                            
    movl    $SYS_WRTE, %eax
    movl    $1, %ebx
    movl    $BUF_SIZE, %edx
    int     $0x80
GETEXIT:                           # getchar
    movl    $SYS_READ, %eax
    movl    $0, %ebx
    movl    $BUF_SIZE , %edx
    int     $0x80
    cmpl    $EOF, %eax     # Compare EOF(-1) with getchar result.
    jne LOOP                    # if result neq -1, jump to LOOP
    movl    $SYS_EXIT, %eax
    movl    $ZERO, %ebx         # if result eq -1: move 0 into %eax.
    int     $0x80