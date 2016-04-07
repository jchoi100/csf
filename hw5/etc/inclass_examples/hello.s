# Just your average Hello World! program.

	.text
main:
	la	$a0, hello
	li	$v0, 4
	syscall

	jr	$ra

	.data
hello:
	.asciiz	"Hello World!\n"
