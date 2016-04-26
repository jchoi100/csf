	.globl	realsort
realsort:
	pushl	%ebp
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	subl	$44, %esp
	movl	68(%esp), %ebp
	cmpl	72(%esp), %ebp
	movl	64(%esp), %esi
	jge	.L41
.L69:
	subl	$4, %esp
	pushl	76(%esp)
	pushl	%ebp
	pushl	%esi
	call	partition
	movl	%eax, 28(%esp)
	addl	$16, %esp
	cmpl	%ebp, %eax
	jle	.L48
.L70:
	subl	$4, %esp
	pushl	16(%esp)
	pushl	%ebp
	pushl	%esi
	call	partition
	movl	%eax, 32(%esp)
	addl	$16, %esp
	cmpl	%eax, %ebp
	jge	.L51
.L71:
	subl	$4, %esp
	pushl	20(%esp)
	pushl	%ebp
	pushl	%esi
	call	partition
	movl	%eax, 36(%esp)
	addl	$16, %esp
	cmpl	%ebp, %eax
	jle	.L54
.L72:
	subl	$4, %esp
	pushl	24(%esp)
	pushl	%ebp
	pushl	%esi
	call	partition
	movl	%eax, 40(%esp)
	addl	$16, %esp
	cmpl	%eax, %ebp
	jge	.L57
.L73:
	subl	$4, %esp
	pushl	28(%esp)
	pushl	%ebp
	pushl	%esi
	call	partition
	movl	%eax, 44(%esp)
	addl	$16, %esp
	cmpl	%eax, %ebp
	jge	.L60
.L74:
	subl	$4, %esp
	pushl	32(%esp)
	pushl	%ebp
	pushl	%esi
	call	partition
	movl	%eax, 24(%esp)
	addl	$16, %esp
	cmpl	%eax, %ebp
	jge	.L63
.L75:
	subl	$4, %esp
	pushl	12(%esp)
	pushl	%ebp
	pushl	%esi
	call	partition
	movl	%eax, 20(%esp)
	addl	$16, %esp
	cmpl	%eax, %ebp
	jge	.L66
.L76:
	subl	$4, %esp
	pushl	8(%esp)
	pushl	%ebp
	pushl	%esi
	call	partition
	addl	$16, %esp
	cmpl	%ebp, %eax
	movl	%eax, %ebx
	jle	.L68
.L77:
	subl	$4, %esp
	pushl	%ebx
	pushl	%ebp
	pushl	%esi
	call	partition
	addl	$12, %esp
	movl	%eax, %edi
	pushl	%eax
	pushl	%ebp
	leal	1(%edi), %ebp
	pushl	%esi
	call	realsort
	addl	$16, %esp
	cmpl	%ebp, %ebx
	jg	.L77
.L68:
	leal	1(%ebx), %ebp
	cmpl	%ebp, 4(%esp)
	jg	.L76
.L66:
	movl	4(%esp), %ebp
	addl	$1, %ebp
	cmpl	%ebp, 8(%esp)
	jg	.L75
.L63:
	movl	8(%esp), %ebp
	addl	$1, %ebp
	cmpl	%ebp, 28(%esp)
	jg	.L74
.L60:
	movl	28(%esp), %ebp
	addl	$1, %ebp
	cmpl	%ebp, 24(%esp)
	jg	.L73
.L57:
	movl	24(%esp), %ebp
	addl	$1, %ebp
	cmpl	%ebp, 20(%esp)
	jg	.L72
.L54:
	movl	20(%esp), %ebp
	addl	$1, %ebp
	cmpl	%ebp, 16(%esp)
	jg	.L71
.L51:
	movl	16(%esp), %ebp
	addl	$1, %ebp
	cmpl	%ebp, 12(%esp)
	jg	.L70
.L48:
	movl	12(%esp), %ebp
	addl	$1, %ebp
	cmpl	72(%esp), %ebp
	jl	.L69
.L41:
	addl	$44, %esp
	popl	%ebx
	popl	%esi
	popl	%edi
	popl	%ebp
	ret

quicksort:
	pushl	%ebp
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	subl	$12, %esp
	movl	36(%esp), %eax
	movl	32(%esp), %ebp
	leal	-1(%eax), %edi
	testl	%edi, %edi
	jle	.L96
	xorl	%ebx, %ebx
.L100:
	subl	$4, %esp
	pushl	%edi
	pushl	%ebx
	pushl	%ebp
	call	partition
	addl	$12, %esp
	movl	%eax, %esi
	pushl	%eax
	pushl	%ebx
	leal	1(%esi), %ebx
	pushl	%ebp
	call	realsort
	addl	$16, %esp
	cmpl	%ebx, %edi
	jg	.L100
.L96:
	addl	$12, %esp
	popl	%ebx
	popl	%esi
	popl	%edi
	popl	%ebp
	ret