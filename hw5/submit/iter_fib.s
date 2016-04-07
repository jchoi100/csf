# 600.233 CSF Assignment 5
# Name: Joon Hyuck Choi
# JHED: jchoi100
# email: jchoi100@jhu.edu
# iter_fib.s

# Starter code for the iterative fibonacci function.
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

# Defined for n >= 0.
# Do not enter negative numbers as arguments!
# The program will return -1 to indicate error.
fib:
#	blt  $a0, $zero, edone # negative number check
	li   $t0, 0         	 # Set fib0 = 0
	li   $t1, 1            # Set fib1 = 1
	li   $t2, 0         	 # Set i = 0
	ble  $a0, 1, done   	 # if n == 1: done
	move $t3, $a0       	 # Set cond = n
	addu $t3, $t3, -1   	 # cond -= 1
	li   $v0, 0         	 # Set res = 0
loop:
	bge  $t2, $t3, done 	 # while i < cond
	addu $v0, $t0, $t1  	 # 		res = fibo + fib1
										  	 # 				With add, might get arithmetic 
										  	 # 				overflow exceptions. So use addu
	move $t0, $t1       	 # 		fibo0 = fibo1
	move $t1, $v0       	 # 		fibo1 = res
	addi $t2, $t2, 1    	 # 		i++
	j loop              	 # go back to loop
done:
	jr	 $ra            	 # return to main
edone:                	 # negative number exit
	li   $v0, -1           # return -1
	jr $ra

	.data
lf:
	.asciiz	"\n"
	.end
