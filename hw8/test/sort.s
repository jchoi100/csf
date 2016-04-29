	.file	"sort.c"
	.globl	tiny
	.data
	.align 4
	.type	tiny, @object
	.size	tiny, 4
tiny:
	.long	10
	.globl	random
	.align 32
	.type	random, @object
	.size	random, 32
random:
	.long	30
	.long	40
	.long	10
	.long	20
	.long	50
	.long	60
	.long	-2
	.long	70
	.globl	ascending
	.align 4
	.type	ascending, @object
	.size	ascending, 24
ascending:
	.long	10
	.long	20
	.long	30
	.long	40
	.long	50
	.long	60
	.globl	descending
	.align 4
	.type	descending, @object
	.size	descending, 24
descending:
	.long	60
	.long	50
	.long	40
	.long	30
	.long	20
	.long	10
	.text
	.globl	sorted
	.type	sorted, @function
sorted:
.LFB0:
	.cfi_startproc
	pushl	%ebp
	.cfi_def_cfa_offset 8
	.cfi_offset 5, -8
	movl	%esp, %ebp
	.cfi_def_cfa_register 5
	subl	$16, %esp
	movl	$0, -4(%ebp)
	jmp	.L2
.L5:
	movl	-4(%ebp), %eax
	leal	0(,%eax,4), %edx
	movl	8(%ebp), %eax
	addl	%edx, %eax
	movl	(%eax), %edx
	movl	-4(%ebp), %eax
	addl	$1, %eax
	leal	0(,%eax,4), %ecx
	movl	8(%ebp), %eax
	addl	%ecx, %eax
	movl	(%eax), %eax
	cmpl	%eax, %edx
	jle	.L3
	movl	$0, %eax
	jmp	.L4
.L3:
	addl	$1, -4(%ebp)
.L2:
	movl	12(%ebp), %eax
	subl	$1, %eax
	cmpl	-4(%ebp), %eax
	jg	.L5
	movl	$1, %eax
.L4:
	leave
	.cfi_restore 5
	.cfi_def_cfa 4, 4
	ret
	.cfi_endproc
.LFE0:
	.size	sorted, .-sorted
	.section	.rodata
.LC0:
	.string	"[%d]"
.LC1:
	.string	" (sorted)"
.LC2:
	.string	" (unsorted)"
	.text
	.globl	print
	.type	print, @function
print:
.LFB1:
	.cfi_startproc
	pushl	%ebp
	.cfi_def_cfa_offset 8
	.cfi_offset 5, -8
	movl	%esp, %ebp
	.cfi_def_cfa_register 5
	subl	$24, %esp
	movl	$0, -12(%ebp)
	jmp	.L7
.L8:
	movl	-12(%ebp), %eax
	leal	0(,%eax,4), %edx
	movl	8(%ebp), %eax
	addl	%edx, %eax
	movl	(%eax), %eax
	subl	$8, %esp
	pushl	%eax
	pushl	$.LC0
	call	printf
	addl	$16, %esp
	addl	$1, -12(%ebp)
.L7:
	movl	-12(%ebp), %eax
	cmpl	12(%ebp), %eax
	jl	.L8
	subl	$8, %esp
	pushl	12(%ebp)
	pushl	8(%ebp)
	call	sorted
	addl	$16, %esp
	testl	%eax, %eax
	je	.L9
	subl	$12, %esp
	pushl	$.LC1
	call	puts
	addl	$16, %esp
	jmp	.L11
.L9:
	subl	$12, %esp
	pushl	$.LC2
	call	puts
	addl	$16, %esp
.L11:
	nop
	leave
	.cfi_restore 5
	.cfi_def_cfa 4, 4
	ret
	.cfi_endproc
.LFE1:
	.size	print, .-print
	.section	.rodata
.LC3:
	.string	"sort.c"
.LC4:
	.string	"l < u"
	.text
	.globl	partition
	.type	partition, @function
partition:
.LFB2:
	.cfi_startproc
	pushl	%ebp
	.cfi_def_cfa_offset 8
	.cfi_offset 5, -8
	movl	%esp, %ebp
	.cfi_def_cfa_register 5
	subl	$40, %esp
	movl	12(%ebp), %eax
	cmpl	16(%ebp), %eax
	jl	.L13
	pushl	$__PRETTY_FUNCTION__.1862
	pushl	$40
	pushl	$.LC3
	pushl	$.LC4
	call	__assert_fail
.L13:
	movl	12(%ebp), %eax
	leal	0(,%eax,4), %edx
	movl	8(%ebp), %eax
	addl	%edx, %eax
	movl	(%eax), %eax
	movl	%eax, -20(%ebp)
	movl	12(%ebp), %eax
	addl	$1, %eax
	movl	%eax, -28(%ebp)
	movl	16(%ebp), %eax
	movl	%eax, -24(%ebp)
	jmp	.L14
.L17:
	addl	$1, -28(%ebp)
.L15:
	movl	-28(%ebp), %eax
	cmpl	-24(%ebp), %eax
	jg	.L18
	movl	-28(%ebp), %eax
	leal	0(,%eax,4), %edx
	movl	8(%ebp), %eax
	addl	%edx, %eax
	movl	(%eax), %eax
	cmpl	-20(%ebp), %eax
	jl	.L17
	jmp	.L18
.L20:
	subl	$1, -24(%ebp)
.L18:
	movl	-28(%ebp), %eax
	cmpl	-24(%ebp), %eax
	jg	.L19
	movl	-24(%ebp), %eax
	leal	0(,%eax,4), %edx
	movl	8(%ebp), %eax
	addl	%edx, %eax
	movl	(%eax), %eax
	cmpl	-20(%ebp), %eax
	jg	.L20
.L19:
	movl	-28(%ebp), %eax
	cmpl	-24(%ebp), %eax
	jg	.L14
	movl	-28(%ebp), %eax
	leal	0(,%eax,4), %edx
	movl	8(%ebp), %eax
	addl	%edx, %eax
	movl	(%eax), %eax
	movl	%eax, -16(%ebp)
	movl	-28(%ebp), %eax
	leal	0(,%eax,4), %edx
	movl	8(%ebp), %eax
	addl	%eax, %edx
	movl	-24(%ebp), %eax
	leal	0(,%eax,4), %ecx
	movl	8(%ebp), %eax
	addl	%ecx, %eax
	movl	(%eax), %eax
	movl	%eax, (%edx)
	movl	-24(%ebp), %eax
	leal	0(,%eax,4), %edx
	movl	8(%ebp), %eax
	addl	%eax, %edx
	movl	-16(%ebp), %eax
	movl	%eax, (%edx)
	addl	$1, -28(%ebp)
	subl	$1, -24(%ebp)
.L14:
	movl	-28(%ebp), %eax
	cmpl	-24(%ebp), %eax
	jle	.L15
	movl	12(%ebp), %eax
	leal	0(,%eax,4), %edx
	movl	8(%ebp), %eax
	addl	%edx, %eax
	movl	(%eax), %eax
	movl	%eax, -12(%ebp)
	movl	12(%ebp), %eax
	leal	0(,%eax,4), %edx
	movl	8(%ebp), %eax
	addl	%eax, %edx
	movl	-24(%ebp), %eax
	leal	0(,%eax,4), %ecx
	movl	8(%ebp), %eax
	addl	%ecx, %eax
	movl	(%eax), %eax
	movl	%eax, (%edx)
	movl	-24(%ebp), %eax
	leal	0(,%eax,4), %edx
	movl	8(%ebp), %eax
	addl	%eax, %edx
	movl	-12(%ebp), %eax
	movl	%eax, (%edx)
	movl	-24(%ebp), %eax
	leave
	.cfi_restore 5
	.cfi_def_cfa 4, 4
	ret
	.cfi_endproc
.LFE2:
	.size	partition, .-partition
	.globl	test
	.type	test, @function
test:
.LFB3:
	.cfi_startproc
	pushl	%ebp
	.cfi_def_cfa_offset 8
	.cfi_offset 5, -8
	movl	%esp, %ebp
	.cfi_def_cfa_register 5
	subl	$8, %esp
	subl	$8, %esp
	pushl	12(%ebp)
	pushl	8(%ebp)
	call	print
	addl	$16, %esp
	subl	$8, %esp
	pushl	12(%ebp)
	pushl	8(%ebp)
	call	quicksort
	addl	$16, %esp
	subl	$8, %esp
	pushl	12(%ebp)
	pushl	8(%ebp)
	call	print
	addl	$16, %esp
	subl	$12, %esp
	pushl	$10
	call	putchar
	addl	$16, %esp
	nop
	leave
	.cfi_restore 5
	.cfi_def_cfa 4, 4
	ret
	.cfi_endproc
.LFE3:
	.size	test, .-test
	.globl	main
	.type	main, @function
main:
.LFB4:
	.cfi_startproc
	leal	4(%esp), %ecx
	.cfi_def_cfa 1, 0
	andl	$-16, %esp
	pushl	-4(%ecx)
	pushl	%ebp
	.cfi_escape 0x10,0x5,0x2,0x75,0
	movl	%esp, %ebp
	pushl	%ecx
	.cfi_escape 0xf,0x3,0x75,0x7c,0x6
	subl	$4, %esp
	subl	$8, %esp
	pushl	$1
	pushl	$tiny
	call	test
	addl	$16, %esp
	subl	$8, %esp
	pushl	$8
	pushl	$random
	call	test
	addl	$16, %esp
	subl	$8, %esp
	pushl	$6
	pushl	$ascending
	call	test
	addl	$16, %esp
	subl	$8, %esp
	pushl	$6
	pushl	$descending
	call	test
	addl	$16, %esp
	movl	$0, %eax
	movl	-4(%ebp), %ecx
	.cfi_def_cfa 1, 0
	leave
	.cfi_restore 5
	leal	-4(%ecx), %esp
	.cfi_def_cfa 4, 4
	ret
	.cfi_endproc
.LFE4:
	.size	main, .-main
	.section	.rodata
	.align 4
	.type	__PRETTY_FUNCTION__.1862, @object
	.size	__PRETTY_FUNCTION__.1862, 10
__PRETTY_FUNCTION__.1862:
	.string	"partition"
	.ident	"GCC: (Ubuntu 5.2.1-22ubuntu2) 5.2.1 20151010"
	.section	.note.GNU-stack,"",@progbits
