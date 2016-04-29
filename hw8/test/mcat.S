# 600.233 Computer System Fundamentals
# Assignment 8
# Dodge the x86!
# Name: Joon Hyuck Choi
# JHED: jchoi100
# email: jchoi100@jhu.edu
# mcat.S

    .text
    .globl  main
main:
    leal    4(%esp), %ecx       # Put %esp + 4 into %ecx.
                                # This location contains the
                                # previous return address.
    andl    $-16, %esp          # %esp <-- %esp AND -16
                                # This clears the bottom 4 bits
                                # of the stack pointer.
                                # Aligning stack to 16 bytes.
    pushl   -4(%ecx)            # Push %ecx - 4 to stack.
                                # This contains the current
                                # return address.
    pushl   %ebp                # Push original %ebp to stack.
    movl    %esp, %ebp          # Move %esp to %ebp to set up 
                                # a new frame.
    pushl   %ecx                # Push %ecx to stack.
    subl    $20, %esp           # Subtract 20 from %esp.
                                # Making space for local variables.
    jmp .L2                     # Jump to .L2
.L3:                            # (inside the while loop in cat.c)
    subl    $12, %esp           # Subtract 12 from %esp
    pushl   -12(%ebp)           # Push %ebp - 12 into stack.
                                # This is the result from getchar.
    call    putchar             # Call putchar function on arg.
    addl    $16, %esp           # Add 16 to %esp. (Resetting what
                                # we did with subl$12 and pushl.)
.L2:
    call    getchar             # Call getchar function.
    movl    %eax, -12(%ebp)     # Move getchar result to %ebp - 12.
    cmpl    $-1, -12(%ebp)      # Compare EOF(-1) with getchar result.
    jne .L3                     # if result neq -1, jump to .L3
    movl    $0, %eax            # if result eq -1: move 0 into %eax.
                                # 0 for EXIT_SUCCESS.
    movl    -4(%ebp), %ecx      # Move %ebp - 4 into %ecx.
                                # This brings the original stack
                                # pointer to the %ecx register.
    leave                       # leave
    leal    -4(%ecx), %esp      # Put %ecx - 4 into %esp.
                                # Restore the original stack pointer.
    ret                         # return to caller
