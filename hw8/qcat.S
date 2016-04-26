# 600.233 Computer System Fundamentals
# Assignment 8
# Dodge the x86!
# Name: Joon Hyuck Choi
# JHED: jchoi100
# email: jchoi100@jhu.edu
# qcat.S

.section .bss
    .lcomm buffer, 128   # declare buffer of size 128
    .text
    .globl  _start
_start:
    movl $buffer, %ecx   # ecx gets our buffer
    jmp GETEXIT          # go to read first
LOOP:                    # putchar
    movl %eax, %edx
    movl $4, %eax        # put SYS_WRITE = 4 into eax
    movl $1, %ebx        # write_file 1 for ebx
    int  $0x80           # syscall
GETEXIT:                 # getchar
    movl $3, %eax        # put SYS_READ = 3 into eax
    movl $0, %ebx        # read_file 0 for ebx
    movl $128, %edx      # edx gets 128 as expected size
    int  $0x80           # syscall
    cmpl $0, %eax        # Compare EOF(0) with getchar result.
    jne LOOP             # if result neq -1, jump to LOOP
    movl $1, %eax        # put SYS_EXIT = 1 into eax
    movl $0, %ebx        # ExIT_SUCESS into ebx
    int  $0x80           # syscall