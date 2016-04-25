	.text
	.globl	plus
plus:
	pushl	%ebp
	movl	%esp, %ebp
	subl	$16, %esp
	movl	8(%ebp), %edx
	movl	12(%ebp), %eax
	addl	%edx, %eax
	movl	%eax, -4(%ebp)
	movl	-4(%ebp), %eax
	leave
	ret

	.globl	main
main:
	pushl	%ebp
	movl	%esp, %ebp
	pushl	$10
	pushl	$37
	call	plus
	addl	$8, %esp
	leave
	ret
