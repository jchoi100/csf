	.file	"sort.c"
	.section	.text.unlikely,"ax",@progbits
.LCOLDB0:
	.text
.LHOTB0:
	.p2align 4,,15
	.globl	sorted
	.type	sorted, @function
sorted:
.LFB8:
	.cfi_startproc
	pushl	%ebx
	.cfi_def_cfa_offset 8
	.cfi_offset 3, -8
	movl	12(%esp), %ecx
	movl	8(%esp), %edx
	cmpl	$1, %ecx
	jle	.L5
	movl	4(%edx), %eax
	cmpl	%eax, (%edx)
	jg	.L7
	subl	$1, %ecx
	xorl	%eax, %eax
	jmp	.L3
	.p2align 4,,10
	.p2align 3
.L4:
	movl	4(%edx,%eax,4), %ebx
	cmpl	%ebx, (%edx,%eax,4)
	jg	.L7
.L3:
	addl	$1, %eax
	cmpl	%ecx, %eax
	jne	.L4
.L5:
	movl	$1, %eax
	popl	%ebx
	.cfi_remember_state
	.cfi_restore 3
	.cfi_def_cfa_offset 4
	ret
	.p2align 4,,10
	.p2align 3
.L7:
	.cfi_restore_state
	xorl	%eax, %eax
	popl	%ebx
	.cfi_restore 3
	.cfi_def_cfa_offset 4
	ret
	.cfi_endproc
.LFE8:
	.size	sorted, .-sorted
	.section	.text.unlikely
.LCOLDE0:
	.text
.LHOTE0:
	.section	.rodata.str1.1,"aMS",@progbits,1
.LC1:
	.string	"[%d]"
.LC2:
	.string	" (sorted)"
.LC3:
	.string	" (unsorted)"
	.section	.text.unlikely
.LCOLDB4:
	.text
.LHOTB4:
	.p2align 4,,15
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
	movl	20(%esp), %edi
	xorl	%ebx, %ebx
	movl	16(%esp), %esi
	testl	%edi, %edi
	jle	.L15
	.p2align 4,,10
	.p2align 3
.L19:
	subl	$8, %esp
	.cfi_def_cfa_offset 24
	pushl	(%esi,%ebx,4)
	.cfi_def_cfa_offset 28
	addl	$1, %ebx
	pushl	$.LC1
	.cfi_def_cfa_offset 32
	call	printf
	addl	$16, %esp
	.cfi_def_cfa_offset 16
	cmpl	%ebx, %edi
	jne	.L19
.L15:
	cmpl	$1, %edi
	jle	.L13
	movl	4(%esi), %eax
	cmpl	%eax, (%esi)
	jg	.L16
	subl	$1, %edi
	xorl	%eax, %eax
	jmp	.L17
	.p2align 4,,10
	.p2align 3
.L18:
	movl	4(%esi,%eax,4), %edx
	cmpl	%edx, (%esi,%eax,4)
	jg	.L16
.L17:
	addl	$1, %eax
	cmpl	%edi, %eax
	jne	.L18
.L13:
	movl	$.LC2, 16(%esp)
	popl	%ebx
	.cfi_remember_state
	.cfi_restore 3
	.cfi_def_cfa_offset 12
	popl	%esi
	.cfi_restore 6
	.cfi_def_cfa_offset 8
	popl	%edi
	.cfi_restore 7
	.cfi_def_cfa_offset 4
	jmp	puts
	.p2align 4,,10
	.p2align 3
.L16:
	.cfi_restore_state
	movl	$.LC3, 16(%esp)
	popl	%ebx
	.cfi_restore 3
	.cfi_def_cfa_offset 12
	popl	%esi
	.cfi_restore 6
	.cfi_def_cfa_offset 8
	popl	%edi
	.cfi_restore 7
	.cfi_def_cfa_offset 4
	jmp	puts
	.cfi_endproc
.LFE9:
	.size	print, .-print
	.section	.text.unlikely
.LCOLDE4:
	.text
.LHOTE4:
	.section	.rodata.str1.1
.LC5:
	.string	"sort.c"
.LC6:
	.string	"l < u"
	.section	.text.unlikely
.LCOLDB7:
	.text
.LHOTB7:
	.p2align 4,,15
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
	movl	52(%esp), %edx
	movl	56(%esp), %eax
	cmpl	%eax, %edx
	jge	.L39
	movl	48(%esp), %ebx
	leal	(%ebx,%edx,4), %ebx
	addl	$1, %edx
	movl	(%ebx), %ebp
	movl	%ebx, 12(%esp)
	.p2align 4,,10
	.p2align 3
.L25:
	cmpl	%eax, %edx
	jg	.L32
	movl	48(%esp), %ebx
	leal	(%ebx,%edx,4), %ebx
	.p2align 4,,10
	.p2align 3
.L26:
	movl	(%ebx), %esi
	cmpl	%esi, %ebp
	jle	.L40
	addl	$1, %edx
	addl	$4, %ebx
	cmpl	%eax, %edx
	jle	.L26
.L32:
	movl	48(%esp), %ebx
	movl	12(%esp), %edi
	leal	(%ebx,%eax,4), %edx
	movl	(%edi), %ecx
	movl	(%edx), %ebx
	movl	%ebx, (%edi)
	movl	%ecx, (%edx)
	addl	$28, %esp
	.cfi_remember_state
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
	.p2align 4,,10
	.p2align 3
.L40:
	.cfi_restore_state
	cmpl	%edx, %eax
	jl	.L32
	movl	48(%esp), %edi
	leal	0(,%eax,4), %ecx
	addl	%ecx, %edi
	movl	%edi, 4(%esp)
	movl	(%edi), %edi
	cmpl	%edi, %ebp
	jge	.L29
	movl	48(%esp), %edi
	movl	%esi, 8(%esp)
	leal	-4(%edi,%ecx), %ecx
	.p2align 4,,10
	.p2align 3
.L30:
	subl	$1, %eax
	cmpl	%edx, %eax
	jl	.L32
	movl	%ecx, %esi
	subl	$4, %ecx
	movl	4(%ecx), %edi
	cmpl	%edi, %ebp
	jl	.L30
	movl	%esi, 4(%esp)
	movl	8(%esp), %esi
.L29:
	movl	%edi, (%ebx)
	movl	4(%esp), %ebx
	addl	$1, %edx
	subl	$1, %eax
	movl	%esi, (%ebx)
	jmp	.L25
.L39:
	pushl	$__PRETTY_FUNCTION__.1896
	.cfi_def_cfa_offset 52
	pushl	$38
	.cfi_def_cfa_offset 56
	pushl	$.LC5
	.cfi_def_cfa_offset 60
	pushl	$.LC6
	.cfi_def_cfa_offset 64
	call	__assert_fail
	.cfi_endproc
.LFE10:
	.size	partition, .-partition
	.section	.text.unlikely
.LCOLDE7:
	.text
.LHOTE7:
	.section	.text.unlikely
.LCOLDB8:
	.text
.LHOTB8:
	.p2align 4,,15
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
	subl	$44, %esp
	.cfi_def_cfa_offset 64
	movl	68(%esp), %ebp
	cmpl	72(%esp), %ebp
	movl	64(%esp), %esi
	jge	.L41
.L69:
	subl	$4, %esp
	.cfi_def_cfa_offset 68
	pushl	76(%esp)
	.cfi_def_cfa_offset 72
	pushl	%ebp
	.cfi_def_cfa_offset 76
	pushl	%esi
	.cfi_def_cfa_offset 80
	call	partition
	movl	%eax, 28(%esp)
	addl	$16, %esp
	.cfi_def_cfa_offset 64
	cmpl	%ebp, %eax
	jle	.L48
.L70:
	subl	$4, %esp
	.cfi_def_cfa_offset 68
	pushl	16(%esp)
	.cfi_def_cfa_offset 72
	pushl	%ebp
	.cfi_def_cfa_offset 76
	pushl	%esi
	.cfi_def_cfa_offset 80
	call	partition
	movl	%eax, 32(%esp)
	addl	$16, %esp
	.cfi_def_cfa_offset 64
	cmpl	%eax, %ebp
	jge	.L51
.L71:
	subl	$4, %esp
	.cfi_def_cfa_offset 68
	pushl	20(%esp)
	.cfi_def_cfa_offset 72
	pushl	%ebp
	.cfi_def_cfa_offset 76
	pushl	%esi
	.cfi_def_cfa_offset 80
	call	partition
	movl	%eax, 36(%esp)
	addl	$16, %esp
	.cfi_def_cfa_offset 64
	cmpl	%ebp, %eax
	jle	.L54
.L72:
	subl	$4, %esp
	.cfi_def_cfa_offset 68
	pushl	24(%esp)
	.cfi_def_cfa_offset 72
	pushl	%ebp
	.cfi_def_cfa_offset 76
	pushl	%esi
	.cfi_def_cfa_offset 80
	call	partition
	movl	%eax, 40(%esp)
	addl	$16, %esp
	.cfi_def_cfa_offset 64
	cmpl	%eax, %ebp
	jge	.L57
.L73:
	subl	$4, %esp
	.cfi_def_cfa_offset 68
	pushl	28(%esp)
	.cfi_def_cfa_offset 72
	pushl	%ebp
	.cfi_def_cfa_offset 76
	pushl	%esi
	.cfi_def_cfa_offset 80
	call	partition
	movl	%eax, 44(%esp)
	addl	$16, %esp
	.cfi_def_cfa_offset 64
	cmpl	%eax, %ebp
	jge	.L60
.L74:
	subl	$4, %esp
	.cfi_def_cfa_offset 68
	pushl	32(%esp)
	.cfi_def_cfa_offset 72
	pushl	%ebp
	.cfi_def_cfa_offset 76
	pushl	%esi
	.cfi_def_cfa_offset 80
	call	partition
	movl	%eax, 24(%esp)
	addl	$16, %esp
	.cfi_def_cfa_offset 64
	cmpl	%eax, %ebp
	jge	.L63
.L75:
	subl	$4, %esp
	.cfi_def_cfa_offset 68
	pushl	12(%esp)
	.cfi_def_cfa_offset 72
	pushl	%ebp
	.cfi_def_cfa_offset 76
	pushl	%esi
	.cfi_def_cfa_offset 80
	call	partition
	movl	%eax, 20(%esp)
	addl	$16, %esp
	.cfi_def_cfa_offset 64
	cmpl	%eax, %ebp
	jge	.L66
	.p2align 4,,10
	.p2align 3
.L76:
	subl	$4, %esp
	.cfi_def_cfa_offset 68
	pushl	8(%esp)
	.cfi_def_cfa_offset 72
	pushl	%ebp
	.cfi_def_cfa_offset 76
	pushl	%esi
	.cfi_def_cfa_offset 80
	call	partition
	addl	$16, %esp
	.cfi_def_cfa_offset 64
	cmpl	%ebp, %eax
	movl	%eax, %ebx
	jle	.L68
	.p2align 4,,10
	.p2align 3
.L77:
	subl	$4, %esp
	.cfi_def_cfa_offset 68
	pushl	%ebx
	.cfi_def_cfa_offset 72
	pushl	%ebp
	.cfi_def_cfa_offset 76
	pushl	%esi
	.cfi_def_cfa_offset 80
	call	partition
	addl	$12, %esp
	.cfi_def_cfa_offset 68
	movl	%eax, %edi
	pushl	%eax
	.cfi_def_cfa_offset 72
	pushl	%ebp
	.cfi_def_cfa_offset 76
	leal	1(%edi), %ebp
	pushl	%esi
	.cfi_def_cfa_offset 80
	call	realsort
	addl	$16, %esp
	.cfi_def_cfa_offset 64
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
	.section	.text.unlikely
.LCOLDE8:
	.text
.LHOTE8:
	.section	.text.unlikely
.LCOLDB9:
	.text
.LHOTB9:
	.p2align 4,,15
	.globl	quicksort
	.type	quicksort, @function
quicksort:
.LFB12:
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
	movl	36(%esp), %eax
	movl	32(%esp), %ebp
	leal	-1(%eax), %edi
	testl	%edi, %edi
	jle	.L96
	xorl	%ebx, %ebx
	.p2align 4,,10
	.p2align 3
.L100:
	subl	$4, %esp
	.cfi_def_cfa_offset 36
	pushl	%edi
	.cfi_def_cfa_offset 40
	pushl	%ebx
	.cfi_def_cfa_offset 44
	pushl	%ebp
	.cfi_def_cfa_offset 48
	call	partition
	addl	$12, %esp
	.cfi_def_cfa_offset 36
	movl	%eax, %esi
	pushl	%eax
	.cfi_def_cfa_offset 40
	pushl	%ebx
	.cfi_def_cfa_offset 44
	leal	1(%esi), %ebx
	pushl	%ebp
	.cfi_def_cfa_offset 48
	call	realsort
	addl	$16, %esp
	.cfi_def_cfa_offset 32
	cmpl	%ebx, %edi
	jg	.L100
.L96:
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
.LFE12:
	.size	quicksort, .-quicksort
	.section	.text.unlikely
.LCOLDE9:
	.text
.LHOTE9:
	.section	.text.unlikely
.LCOLDB10:
	.text
.LHOTB10:
	.p2align 4,,15
	.globl	test
	.type	test, @function
test:
.LFB13:
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
	xorl	%esi, %esi
	subl	$28, %esp
	.cfi_def_cfa_offset 48
	movl	52(%esp), %edi
	movl	48(%esp), %ebx
	testl	%edi, %edi
	jle	.L108
	.p2align 4,,10
	.p2align 3
.L125:
	subl	$8, %esp
	.cfi_def_cfa_offset 56
	pushl	(%ebx,%esi,4)
	.cfi_def_cfa_offset 60
	addl	$1, %esi
	pushl	$.LC1
	.cfi_def_cfa_offset 64
	call	printf
	addl	$16, %esp
	.cfi_def_cfa_offset 48
	cmpl	%esi, %edi
	jne	.L125
.L108:
	leal	-1(%edi), %esi
	testl	%esi, %esi
	jle	.L137
	movl	4(%ebx), %eax
	cmpl	%eax, (%ebx)
	jg	.L109
	xorl	%eax, %eax
	jmp	.L110
	.p2align 4,,10
	.p2align 3
.L111:
	movl	4(%ebx,%eax,4), %edx
	cmpl	%edx, (%ebx,%eax,4)
	jg	.L109
.L110:
	addl	$1, %eax
	cmpl	%eax, %esi
	jne	.L111
	subl	$12, %esp
	.cfi_def_cfa_offset 60
	pushl	$.LC2
	.cfi_def_cfa_offset 64
	call	puts
	addl	$16, %esp
	.cfi_def_cfa_offset 48
.L121:
	xorl	%ebp, %ebp
	.p2align 4,,10
	.p2align 3
.L113:
	subl	$4, %esp
	.cfi_def_cfa_offset 52
	pushl	%esi
	.cfi_def_cfa_offset 56
	pushl	%ebp
	.cfi_def_cfa_offset 60
	pushl	%ebx
	.cfi_def_cfa_offset 64
	call	partition
	addl	$12, %esp
	.cfi_def_cfa_offset 52
	pushl	%eax
	.cfi_def_cfa_offset 56
	movl	%eax, 20(%esp)
	pushl	%ebp
	.cfi_def_cfa_offset 60
	pushl	%ebx
	.cfi_def_cfa_offset 64
	call	realsort
	movl	28(%esp), %eax
	addl	$16, %esp
	.cfi_def_cfa_offset 48
	leal	1(%eax), %ebp
	cmpl	%esi, %ebp
	jl	.L113
	testl	%edi, %edi
	jle	.L124
.L123:
	xorl	%ebp, %ebp
	.p2align 4,,10
	.p2align 3
.L115:
	subl	$8, %esp
	.cfi_def_cfa_offset 56
	pushl	(%ebx,%ebp,4)
	.cfi_def_cfa_offset 60
	addl	$1, %ebp
	pushl	$.LC1
	.cfi_def_cfa_offset 64
	call	printf
	addl	$16, %esp
	.cfi_def_cfa_offset 48
	cmpl	%ebp, %edi
	jg	.L115
	testl	%esi, %esi
	jle	.L116
.L124:
	movl	(%ebx), %eax
	cmpl	%eax, 4(%ebx)
	jl	.L117
	xorl	%eax, %eax
	jmp	.L119
	.p2align 4,,10
	.p2align 3
.L120:
	movl	4(%ebx,%eax,4), %ecx
	cmpl	%ecx, (%ebx,%eax,4)
	jg	.L117
.L119:
	addl	$1, %eax
	cmpl	%esi, %eax
	jl	.L120
.L116:
	subl	$12, %esp
	.cfi_def_cfa_offset 60
	pushl	$.LC2
	.cfi_def_cfa_offset 64
	call	puts
	addl	$16, %esp
	.cfi_def_cfa_offset 48
.L122:
	movl	$10, 48(%esp)
	addl	$28, %esp
	.cfi_remember_state
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
	jmp	putchar
	.p2align 4,,10
	.p2align 3
.L109:
	.cfi_restore_state
	subl	$12, %esp
	.cfi_def_cfa_offset 60
	pushl	$.LC3
	.cfi_def_cfa_offset 64
	call	puts
	addl	$16, %esp
	.cfi_def_cfa_offset 48
	jmp	.L121
	.p2align 4,,10
	.p2align 3
.L117:
	subl	$12, %esp
	.cfi_def_cfa_offset 60
	pushl	$.LC3
	.cfi_def_cfa_offset 64
	call	puts
	addl	$16, %esp
	.cfi_def_cfa_offset 48
	jmp	.L122
.L137:
	subl	$12, %esp
	.cfi_def_cfa_offset 60
	pushl	$.LC2
	.cfi_def_cfa_offset 64
	call	puts
	addl	$16, %esp
	.cfi_def_cfa_offset 48
	testl	%edi, %edi
	jg	.L123
	jmp	.L116
	.cfi_endproc
.LFE13:
	.size	test, .-test
	.section	.text.unlikely
.LCOLDE10:
	.text
.LHOTE10:
	.section	.text.unlikely
.LCOLDB11:
	.section	.text.startup,"ax",@progbits
.LHOTB11:
	.p2align 4,,15
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
	popl	%eax
	popl	%edx
	pushl	$8
	pushl	$random
	call	test
	popl	%ecx
	popl	%eax
	pushl	$6
	pushl	$ascending
	call	test
	popl	%eax
	popl	%edx
	pushl	$6
	pushl	$descending
	call	test
	movl	-4(%ebp), %ecx
	.cfi_def_cfa 1, 0
	addl	$16, %esp
	xorl	%eax, %eax
	leave
	.cfi_restore 5
	leal	-4(%ecx), %esp
	.cfi_def_cfa 4, 4
	ret
	.cfi_endproc
.LFE14:
	.size	main, .-main
	.section	.text.unlikely
.LCOLDE11:
	.section	.text.startup
.LHOTE11:
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
