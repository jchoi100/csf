#600.233 CSF Assignment 5
#Name: Joon Hyuck Choi
#JHED: jchoi100
#email: jchoi100@jhu.edu
#isqrt.s

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
# The program will return -1.
isqrt:
  li $v0, 0            # Set res = 0
  li $t0, 0            # Set $t0 = 0
loop:
  bgt $t0, $a0, done   # while ($t0 <= n)
  addi $v0, $v0, 1     #     res++
  mul $t0, $v0, $v0    #     $t0 = res * res
  j loop               # continue looping
done:
  addi $v0, $v0, -1    # res--
  jr  $ra              # return to main

  .data
lf:
  .asciiz "\n"
  .end
