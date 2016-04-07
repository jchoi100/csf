# Shows that li and la are pseudo instructions: The assembler
# generates the appropriate machine instructions for us.

	.text
main:
	li	$v0, 1
	li	$a0, 326482378
	syscall

	la	$a0, lf
	li	$v0, 4
	syscall

	jr	$ra

	.data
lf:
	.asciiz	"\n"
