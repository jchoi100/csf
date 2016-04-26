realsort:
	pushl	%ebp
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	subl	$12, %esp
	movl	36(%esp), %ebx
	movl	40(%esp), %edi
	movl	32(%esp), %ebp
	cmpl	%edi, %ebx
	jge	.L41
.L45:
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
	cmpl	%edi, %ebx
	jl	.L45
.L41:
	addl	$12, %esp
	popl	%ebx
	popl	%esi
	popl	%edi
	popl	%ebp
	ret

quicksort:
	subl	$16, %esp
	movl	24(%esp), %eax
	subl	$1, %eax
	pushl	%eax
	pushl	$0
	pushl	28(%esp)
	call	realsort
	addl	$28, %esp
	ret