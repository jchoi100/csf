    .text
    .globl  main
main:
    leal    4(%esp), %ecx   # save second item in stack to ecx register
                # in this case, the second item holds
                # the previous return address.
    andl    $-16, %esp  # clear bottom 4 bits of the stack pointer
                # align stack to 16 bytes
    pushl   -4(%ecx)    # push current return address to stack
                # so that main will function normally
    pushl   %ebp        # save original base pointer
    movl    %esp, %ebp  # establish a new base pointer
                # to where the stack pointer is now
    pushl   %ecx        # save the stack pointer
    subl    $20, %esp   # make some space for local variables
    jmp .L2     # jump to .L2
.L3:
    subl    $12, %esp   # subtract 12 from stack pointer
    pushl   -12(%ebp)   # push the result from getchar to stack
    call    putchar     # call putchar function from c library
    addl    $16, %esp   # add 16 to stack pointer
                # which removes subtract 12 and push
.L2:
    call    getchar     # call getchar function from c library
    movl    %eax, -12(%ebp) # move the result of getchar function
                # to storage space for local variable
    cmpl    $-1, -12(%ebp)  # compare result to -1 (EOF)
    jne .L3     # jump to .L3 if not equal to EOF
    movl    $0, %eax    # move 0 to eax register
                # to indicate successful exit
    movl    -4(%ebp), %ecx  # get original stack pointer to ecx register
    leave
    leal    -4(%ecx), %esp  # restore the original stack pointer
    ret         # return to previous function
