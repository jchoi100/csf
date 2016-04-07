# 600.233 Computer Systems Fundamentals
# Name: Joo Chang Lee
# JHED ID: jlee381
# Email: jlee381@jhu.edu
# Assignment 8: Welcome to x86!
# minmax.s

	.text

	.globl min
min:
	movl	4(%esp), %eax	# move first value to eax register
	cmpl	8(%esp), %eax	# compare second value to first value
	cmovg	8(%esp), %eax	# move second value to eax if eax is greater
	ret

	.globl max
max:
	movl	4(%esp), %eax	# move first value to eax register
	cmpl	8(%esp), %eax	# compare second value to first value
	cmovl	8(%esp), %eax	# move second value to eax if eax is smaller
	ret
