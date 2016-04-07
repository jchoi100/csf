# Illustrates the need to use the stack for $ra when functions
# call other functions.

	.text
main:
	addi	$sp, $sp, -4	# decrement stack pointer \
	sw	$ra, ($sp)	# M[$sp] <- $ra            / "push"

	li	$a0, 10
	# something valuable in $t2? save before call!
	# something valuable in $s2? not my problem
	jal	sumn

	move	$a0, $v0
	li	$v0, 1		# print int
	syscall

	la	$a0, lf
	li	$v0, 4
	syscall

	lw	$ra, ($sp)	# $ra <- M[$sp]          \
	addi	$sp, $sp, 4	# increment stack pointer/ "pop"

	jr	$ra

# result: $v0 = sillyadd(a: $a0, b: $a1)
sillyadd:
	# trash $t0, $t1, ... without remorse
	# can't trash $s0, $s1, .... gotta save/restore
	add	$v0, $a0, $a1
	jr	$ra

# result: $v0 = sumn(n: $a0) "sum of ints from 0 to n"
sumn:
	move	$t0, $a0	# counter in t0
	li	$t1, 0		# sum in t1
loop:
	add	$t1, $t1, $t0
	addi	$t0, $t0, -1
	bne	$t0, $0, loop

	move	$v0, $t1

	jr	$ra

	.data
lf:
	.asciiz	"\n"
