# Recursive factorial function.

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
	addi	$sp, $sp, -4
	sw	$ra, ($sp)
	addi	$sp, $sp, -4
	sw	$s0, ($sp)

	move	$s0, $a0	# n in $s0
	beq	$s0, $0, equalszero

	# note: n > 0
	addi	$a0, $s0, -1
	jal	fact
	mul	$v0, $v0, $s0
	j	exit

equalszero:
	li	$v0, 1
exit:
	lw	$s0, ($sp)
	addi	$sp, $sp, 4
	lw	$ra, ($sp)
	addi	$sp, $sp, 4
	jr	$ra

	.data
lf:
	.asciiz	"\n"
