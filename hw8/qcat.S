# 600.233 Computer System Fundamentals
# Assignment 8
# Dodge the x86!
# Name: Joon Hyuck Choi
# JHED: jchoi100
# email: jchoi100@jhu.edu
# qcat.S

    .bss
    .lcomm buffer, 128
    .text
    .globl  _start
_start:
    movl    $buffer, %ecx
    jmp GETEXIT                     # putchar
LOOP:                            
    movl    $4, %eax
    movl    $1, %ebx
    movl    $128, %edx
    int     $0x80
GETEXIT:                           # getchar
    movl    $3, %eax
    movl    $0, %ebx
    movl    $128, %edx
    int     $0x80
    cmpl    $-1, %eax     # Compare EOF(-1) with getchar result.
    jne LOOP                    # if result neq -1, jump to LOOP
    movl    $1, %eax
    movl    $0, %ebx         # if result eq -1: move 0 into %eax.
    int     $0x80