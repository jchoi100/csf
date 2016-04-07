# 600.233 CSF Assignment 5
# Name: Joon Hyuck Choi
# JHED: jchoi100
# email: jchoi100@jhu.edu
# recu_fib.s

# Starter code for the recursive fibonacci function.
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
	addi	$sp, $sp, -8
	sw	$ra, 4($sp)

	li	$v0, read_int
	syscall

	move	$a0, $v0
	jal	fib

	move	$a0, $v0
	li	$v0, print_int
	syscall

	la	$a0, lf
	li	$v0, print_string
	syscall

	lw	$ra, 4($sp)
	addi	$sp, $sp, 8

	jr	$ra

# Only defined for n >= 0
# If given a negative number, the function will
# output -1 to indicate error.
fib:
	blt  $a0, $zero, edone # negative number check
	addi	$sp, $sp, -8     # stack op
	sw	$ra, 4($sp)        # save $ra for later

	move $v0, $a0          # res = n
	blt  $a0, 2, done      # if n == 2: goto done
	sw   $a0, 0($sp)       # save current $a0
	addi $a0, $a0, -1      # n -= 1
	jal  fib               # fib(n - 1)
	lw   $a0, 0($sp)       # load back original n
	sw   $v0, 0($sp)       # fibo(n - 1)
	addi $a0, $a0, -2      # n -= 2
	jal  fib               # fib(n - 2)
	lw   $v1, 0($sp)            
	addu $v0, $v0, $v1     # res = fib(n - 1) + fib(n - 2)
done:
	lw	 $ra, 4($sp)       # retrieve return address
	addi $sp, $sp, 8       # stack op
	jr	 $ra               # return
edone:                   # negative number exit
	li   $v0, -1           # return -1
	jr $ra

	.data
lf:
	.asciiz	"\n"
	.end
