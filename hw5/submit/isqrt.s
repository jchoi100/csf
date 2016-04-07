# 600.233 CSF Assignment 5
# Name: Joon Hyuck Choi
# JHED: jchoi100
# email: jchoi100@jhu.edu
# isqrt.s

# Your code should certainly have more comments that
# explain what's going on.

print_int = 1
print_string = 4
read_int = 5

  .text
# Note that for main() we make no assumption about the
# caller having set aside memory for saving arguments.
# Luckily, in this case, we don't need to save them.
main:
  addi  $sp, $sp, -8
  sw  $ra, 4($sp)

  li  $v0, read_int
  syscall

  move  $a0, $v0
  jal isqrt

  move  $a0, $v0
  li  $v0, print_int
  syscall

  la  $a0, lf
  li  $v0, print_string
  syscall

  lw  $ra, 4($sp)
  addi  $sp, $sp, 8

  jr  $ra

# Defined for n >= 0.
# Do not enter negative numbers as arguments!
# The program will consider a negative number to be 
# a huge positive number in the unsigned convention.
isqrt:
  li  $v0, 0            # Set res = 0
  li  $t0, 1            # Set bit = 1
  sll $t0, $t0, 30      # bit <<= 30

loop1:                  
  beq $t0, $zero, done  # while (bit != 0)
  addu $t1, $v0, $t0    #     cond = res + bit
  bgtu $t1, $a0, elseBlk # if ($a0 >= $t1)
  subu $a0, $a0, $t1    #    $a0 -= $t1
  srl $v0, $v0, 1       #    $v0 >>= 1
  addu $v0, $v0, $t0    #    $v0 += t0
  srl $t0, $t0, 2       #    $t0 >>= 2
  j loop1               # back to loop2
elseBlk:                # else
  srl $v0, $v0, 1       #    $v0 >>= 1
  srl $t0, $t0, 2       #    t0 >>= 2
  j loop1               # back to loop2

done:
  jr  $ra              # return to main


  .data
lf:
  .asciiz "\n"
  .end
