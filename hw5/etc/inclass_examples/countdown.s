# Count down from 10 to show how loops are done.

	.text
main:
	li	$s0, 10

loop:
	move	$a0, $s0
	li	$v0, 1		# print int
	syscall

	la	$a0, lf
	li	$v0, 4
	syscall

	addi	$s0, $s0, -1
	bne	$s0, $0, loop	# $s0 == 0?

	jr	$ra

	.data
lf:
	.asciiz	"\n"
