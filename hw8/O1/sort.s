	.file	"sort.c"
	.text
	.globl	sorted
	.type	sorted, @function
sorted:
.LFB8:
	.cfi_startproc
	pushl	%ebx
	.cfi_def_cfa_offset 8
	.cfi_offset 3, -8
	movl	8(%esp), %edx
	movl	12(%esp), %ecx
	leal	-1(%ecx), %eax
	testl	%eax, %eax
	jle	.L5
	movl	4(%edx), %eax
	cmpl	%eax, (%edx)
	jg	.L6
	subl	$1, %ecx
	movl	$0, %eax
	jmp	.L3
.L4:
	movl	4(%edx,%eax,4), %ebx
	cmpl	%ebx, (%edx,%eax,4)
	jg	.L7
.L3:
	addl	$1, %eax
	cmpl	%ecx, %eax
	jne	.L4
	movl	$1, %eax
	jmp	.L2
.L5:
	movl	$1, %eax
	jmp	.L2
.L6:
	movl	$0, %eax
	jmp	.L2
.L7:
	movl	$0, %eax
.L2:
	popl	%ebx
	.cfi_restore 3
	.cfi_def_cfa_offset 4
	ret
	.cfi_endproc
.LFE8:
	.size	sorted, .-sorted
	.section	.rodata.str1.1,"aMS",@progbits,1
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
.LFB9:
	.cfi_startproc
	pushl	%edi
	.cfi_def_cfa_offset 8
	.cfi_offset 7, -8
	pushl	%esi
	.cfi_def_cfa_offset 12
	.cfi_offset 6, -12
	pushl	%ebx
	.cfi_def_cfa_offset 16
	.cfi_offset 3, -16
	movl	16(%esp), %edi
	movl	20(%esp), %esi
	testl	%esi, %esi
	jle	.L10
	movl	$0, %ebx
.L11:
	subl	$8, %esp
	.cfi_def_cfa_offset 24
	pushl	(%edi,%ebx,4)
	.cfi_def_cfa_offset 28
	pushl	$.LC0
	.cfi_def_cfa_offset 32
	call	printf
	addl	$1, %ebx
	addl	$16, %esp
	.cfi_def_cfa_offset 16
	cmpl	%ebx, %esi
	jne	.L11
.L10:
	subl	$8, %esp
	.cfi_def_cfa_offset 24
	pushl	%esi
	.cfi_def_cfa_offset 28
	pushl	%edi
	.cfi_def_cfa_offset 32
	call	sorted
	addl	$16, %esp
	.cfi_def_cfa_offset 16
	testl	%eax, %eax
	je	.L12
	subl	$12, %esp
	.cfi_def_cfa_offset 28
	pushl	$.LC1
	.cfi_def_cfa_offset 32
	call	puts
	addl	$16, %esp
	.cfi_def_cfa_offset 16
	jmp	.L9
.L12:
	subl	$12, %esp
	.cfi_def_cfa_offset 28
	pushl	$.LC2
	.cfi_def_cfa_offset 32
	call	puts
	addl	$16, %esp
	.cfi_def_cfa_offset 16
.L9:
	popl	%ebx
	.cfi_restore 3
	.cfi_def_cfa_offset 12
	popl	%esi
	.cfi_restore 6
	.cfi_def_cfa_offset 8
	popl	%edi
	.cfi_restore 7
	.cfi_def_cfa_offset 4
	ret
	.cfi_endproc
.LFE9:
	.size	print, .-print
	.section	.rodata.str1.1
.LC3:
	.string	"sort.c"
.LC4:
	.string	"l < u"
	.text
	.globl	partition
	.type	partition, @function
partition:
.LFB10:
	.cfi_startproc
	pushl	%ebp
	.cfi_def_cfa_offset 8
	.cfi_offset 5, -8
	pushl	%edi
	.cfi_def_cfa_offset 12
	.cfi_offset 7, -12
	pushl	%esi
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	pushl	%ebx
	.cfi_def_cfa_offset 20
	.cfi_offset 3, -20
	subl	$28, %esp
	.cfi_def_cfa_offset 48
	movl	48(%esp), %ecx
	movl	52(%esp), %edx
	movl	56(%esp), %eax
	cmpl	%eax, %edx
	jl	.L17
	pushl	$__PRETTY_FUNCTION__.1896
	.cfi_remember_state
	.cfi_def_cfa_offset 52
	pushl	$38
	.cfi_def_cfa_offset 56
	pushl	$.LC3
	.cfi_def_cfa_offset 60
	pushl	$.LC4
	.cfi_def_cfa_offset 64
	call	__assert_fail
.L17:
	.cfi_restore_state
	leal	(%ecx,%edx,4), %esi
	movl	%esi, 12(%esp)
	movl	(%esi), %ebx
	addl	$1, %edx
	jmp	.L18
.L27:
	addl	$1, %edx
	cmpl	%eax, %edx
	jg	.L25
	cmpl	(%ecx,%edx,4), %ebx
	jg	.L27
	jmp	.L21
.L26:
	subl	$1, %eax
	cmpl	%eax, %edx
	jg	.L25
	cmpl	(%ecx,%eax,4), %ebx
	jl	.L26
	jmp	.L23
.L18:
	cmpl	%eax, %edx
	jg	.L25
	cmpl	(%ecx,%edx,4), %ebx
	jg	.L27
.L21:
	cmpl	(%ecx,%eax,4), %ebx
	jl	.L26
.L23:
	leal	(%ecx,%edx,4), %edi
	movl	(%edi), %esi
	movl	%esi, 8(%esp)
	leal	(%ecx,%eax,4), %esi
	movl	(%esi), %ebp
	movl	%ebp, (%edi)
	movl	8(%esp), %edi
	movl	%edi, (%esi)
	addl	$1, %edx
	subl	$1, %eax
	jmp	.L18
.L25:
	movl	12(%esp), %edi
	movl	(%edi), %ebx
	leal	(%ecx,%eax,4), %edx
	movl	(%edx), %ecx
	movl	%ecx, (%edi)
	movl	%ebx, (%edx)
	addl	$28, %esp
	.cfi_def_cfa_offset 20
	popl	%ebx
	.cfi_restore 3
	.cfi_def_cfa_offset 16
	popl	%esi
	.cfi_restore 6
	.cfi_def_cfa_offset 12
	popl	%edi
	.cfi_restore 7
	.cfi_def_cfa_offset 8
	popl	%ebp
	.cfi_restore 5
	.cfi_def_cfa_offset 4
	ret
	.cfi_endproc
.LFE10:
	.size	partition, .-partition
	.globl	realsort
	.type	realsort, @function
realsort:
.LFB11:
	.cfi_startproc
	pushl	%ebp
	.cfi_def_cfa_offset 8
	.cfi_offset 5, -8
	pushl	%edi
	.cfi_def_cfa_offset 12
	.cfi_offset 7, -12
	pushl	%esi
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	pushl	%ebx
	.cfi_def_cfa_offset 20
	.cfi_offset 3, -20
	subl	$12, %esp
	.cfi_def_cfa_offset 32
	movl	32(%esp), %edi
	movl	36(%esp), %esi
	movl	40(%esp), %ebx
	cmpl	%ebx, %esi
	jge	.L37
	subl	$4, %esp
	.cfi_def_cfa_offset 36
	pushl	%ebx
	.cfi_def_cfa_offset 40
	pushl	%esi
	.cfi_def_cfa_offset 44
	pushl	%edi
	.cfi_def_cfa_offset 48
	call	partition
	movl	%eax, %ebp
	addl	$12, %esp
	.cfi_def_cfa_offset 36
	pushl	%eax
	.cfi_def_cfa_offset 40
	pushl	%esi
	.cfi_def_cfa_offset 44
	pushl	%edi
	.cfi_def_cfa_offset 48
	call	realsort
	addl	$12, %esp
	.cfi_def_cfa_offset 36
	pushl	%ebx
	.cfi_def_cfa_offset 40
	addl	$1, %ebp
	pushl	%ebp
	.cfi_def_cfa_offset 44
	pushl	%edi
	.cfi_def_cfa_offset 48
	call	realsort
	addl	$16, %esp
	.cfi_def_cfa_offset 32
.L37:
	addl	$12, %esp
	.cfi_def_cfa_offset 20
	popl	%ebx
	.cfi_restore 3
	.cfi_def_cfa_offset 16
	popl	%esi
	.cfi_restore 6
	.cfi_def_cfa_offset 12
	popl	%edi
	.cfi_restore 7
	.cfi_def_cfa_offset 8
	popl	%ebp
	.cfi_restore 5
	.cfi_def_cfa_offset 4
	ret
	.cfi_endproc
.LFE11:
	.size	realsort, .-realsort
	.globl	quicksort
	.type	quicksort, @function
quicksort:
.LFB12:
	.cfi_startproc
	subl	$16, %esp
	.cfi_def_cfa_offset 20
	movl	24(%esp), %eax
	subl	$1, %eax
	pushl	%eax
	.cfi_def_cfa_offset 24
	pushl	$0
	.cfi_def_cfa_offset 28
	pushl	28(%esp)
	.cfi_def_cfa_offset 32
	call	realsort
	addl	$28, %esp
	.cfi_def_cfa_offset 4
	ret
	.cfi_endproc
.LFE12:
	.size	quicksort, .-quicksort
	.globl	test
	.type	test, @function
test:
.LFB13:
	.cfi_startproc
	pushl	%esi
	.cfi_def_cfa_offset 8
	.cfi_offset 6, -8
	pushl	%ebx
	.cfi_def_cfa_offset 12
	.cfi_offset 3, -12
	subl	$12, %esp
	.cfi_def_cfa_offset 24
	movl	24(%esp), %ebx
	movl	28(%esp), %esi
	pushl	%esi
	.cfi_def_cfa_offset 28
	pushl	%ebx
	.cfi_def_cfa_offset 32
	call	print
	addl	$8, %esp
	.cfi_def_cfa_offset 24
	pushl	%esi
	.cfi_def_cfa_offset 28
	pushl	%ebx
	.cfi_def_cfa_offset 32
	call	quicksort
	addl	$8, %esp
	.cfi_def_cfa_offset 24
	pushl	%esi
	.cfi_def_cfa_offset 28
	pushl	%ebx
	.cfi_def_cfa_offset 32
	call	print
	movl	$10, (%esp)
	call	putchar
	addl	$20, %esp
	.cfi_def_cfa_offset 12
	popl	%ebx
	.cfi_restore 3
	.cfi_def_cfa_offset 8
	popl	%esi
	.cfi_restore 6
	.cfi_def_cfa_offset 4
	ret
	.cfi_endproc
.LFE13:
	.size	test, .-test
	.globl	main
	.type	main, @function
main:
.LFB14:
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
	subl	$12, %esp
	pushl	$1
	pushl	$tiny
	call	test
	addl	$8, %esp
	pushl	$8
	pushl	$random
	call	test
	addl	$8, %esp
	pushl	$6
	pushl	$ascending
	call	test
	addl	$8, %esp
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
.LFE14:
	.size	main, .-main
	.section	.rodata
	.align 4
	.type	__PRETTY_FUNCTION__.1896, @object
	.size	__PRETTY_FUNCTION__.1896, 10
__PRETTY_FUNCTION__.1896:
	.string	"partition"
	.globl	descending
	.data
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
	.globl	tiny
	.align 4
	.type	tiny, @object
	.size	tiny, 4
tiny:
	.long	10
	.ident	"GCC: (GNU) 5.3.1 20151207 (Red Hat 5.3.1-2)"
	.section	.note.GNU-stack,"",@progbits
