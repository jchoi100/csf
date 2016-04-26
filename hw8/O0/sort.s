realsort:
	pushl	%ebp
	movl	%esp, %ebp
	subl	$24, %esp
	movl	12(%ebp), %eax
	cmpl	16(%ebp), %eax
	jge	.L25
	subl	$4, %esp
	pushl	16(%ebp)
	pushl	12(%ebp)
	pushl	8(%ebp)
	call	partition
	addl	$16, %esp
	movl	%eax, -12(%ebp)
	subl	$4, %esp
	pushl	-12(%ebp)
	pushl	12(%ebp)
	pushl	8(%ebp)
	call	realsort
	addl	$16, %esp
	movl	-12(%ebp), %eax
	addl	$1, %eax
	subl	$4, %esp
	pushl	16(%ebp)
	pushl	%eax
	pushl	8(%ebp)
	call	realsort
	addl	$16, %esp
.L25:
	nop
	leave
	ret

quicksort:
	pushl	%ebp
	movl	%esp, %ebp
	subl	$8, %esp
	movl	12(%ebp), %eax
	subl	$1, %eax
	subl	$4, %esp
	pushl	%eax
	pushl	$0
	pushl	8(%ebp)
	call	realsort
	addl	$16, %esp
	nop
	leave
	ret