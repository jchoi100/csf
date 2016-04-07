# Iterative factorial function.

	.text
main:
	addi	$sp, $sp, -4
	sw	$ra, ($sp)

	li	$a0, 5
	jal	fact

	move	$a0, $v0
	li	$v0, 1
	syscall

	la	$a0, lf
	li	$v0, 4
	syscall

	lw	$ra, ($sp)
	addi	$sp, $sp, 4
	jr	$ra

# call with $a0 >= 1, $a0 <= 12
fact:
	move	$v0, $a0

loop:
	addi	$a0, $a0, -1
	beq	$a0, $0, exit

	mul	$v0, $v0, $a0
	j	loop

exit:
	jr	$ra

	.data
lf:
	.asciiz	"\n"
