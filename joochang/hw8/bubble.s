# 600.233 Computer Systems Fundamentals
# Name: Joo Chang Lee
# JHED ID: jlee381
# Email: jlee381@jhu.edu
# Assignment 8: Welcome to x86!
# bubble.s

# 4 local variables used
# -4: first integer in comparison
# -8: second integer in comparison
# -12: inner loop counter
# -16: outer loop counter

	.text
	.globl bubble

bubble:
	pushl	%ebp
	movl	%esp, %ebp	# enters function

	movl	$0, -16(%ebp)	# set local outer counter to 0
	movl	$0, -12(%ebp)	# set local inner counter to 0

.out:
	movl	-16(%ebp), %eax	# move current outer counter to eax
	addl	$1, %eax	# add 1 to outer counter
	cmpl	12(%ebp), %eax	# compare outer counter to numElements
	jge		.end	# end program if counter >= numElements
	movl	%eax, -16(%ebp)	# move added outer counter back.
	movl	$0, -12(%ebp)	# set local inner counter to 0

.inner:
	movl	-12(%ebp), %eax	# move current inner counter to eax
	addl	$1, %eax	# add 1 to inner counter
	cmpl	12(%ebp), %eax	# compare inner counter to numElements
	jge		.out	# end outer loop if counter >= numElements
	movl	%eax, -12(%ebp)	# move added inner counter back

	movl	-12(%ebp), %eax	# move current inner couner to eax
	imull	$4, %eax	# multiply by 4
	movl	8(%ebp), %edx	# move array index 0 pointer to edx
	addl	%edx, %eax	# add edx and eax, to eax
	movl	(%eax), %eax
	movl	%eax, -8(%ebp)	# move second item to local variable

	movl	-12(%ebp), %eax	# move current inner couner to eax
	subl	$1, %eax	# subtract by 1
	imull	$4, %eax	# multiply by 4
	movl	8(%ebp), %edx	# move array index 0 pointer to edx
	addl	%edx, %eax	# add edx and eax, to eax
	movl	(%eax), %eax
	mov	%eax, -4(%ebp)	# move first item to local variable
	movl	-8(%ebp), %edx	# move second item to edx
	cmpl	%eax, %edx	# compare edx and eax
	jg	.inner	# go back to inner loop if second item is larger

	movl	-12(%ebp), %eax	# move current inner couner to eax
	imull	$4, %eax	# multiply by 4
	movl	8(%ebp), %edx	# move array index 0 pointer to edx
	addl	%edx, %eax	# add edx and eax, to eax
	movl	-4(%ebp), %edx
	movl	%edx, (%eax)	# move first item to address of eax.
	subl	$4, %eax	# subtract 4 from eax
	movl	-8(%ebp), %edx
	movl	%edx, (%eax)	# move second item to address of eax.
	jmp	.inner	# go back to inner loop

.end:
	leave	# leaves function
	ret

