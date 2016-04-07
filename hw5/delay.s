# bare memory access, branch and load delay slots
#
# The following program works fine when executed with
# the "regular" SPIM; it will print "333" as a result.
#
# We can tell SPIM to be a lot less nice to us, and
# for this problem that's what we'll do. Running SPIM
# with the "-bare" option gives us access only to the
# "bare" MIPS machine, none of the niceties that the
# assembler and simulator provide for us. In particular:
#
# - pseudo instruction such as li, la, and move will
#   no longer work; you have to perform the equivalent
#   operations using basic MIPS instructions such as
#   lui and addiu/ori
#
# - loads from memory will only take effect after
#   an additional instruction has been processed
#   (delayed loads)
#
# - branches and jumps will always execute the next
#   instruction after the branch as if it had been
#   written before the branch (delayed branches)
#
# - addressing memory with labels no longer works as
#   expected, instead you'll have to load the base
#   address of the .data segment (0x10000000) into a
#   register and then access memory indirectly with
#   an offset
#
# Modify the program (keeping the existing structure
# intact of course!) so that it will run without errors
# and produce the same result (333) when we run it with
# the "-bare" flag.

sys_print_int = 1
sys_print_string = 4

	.text
main:
	addi	$sp, $sp, -8
	sw	$ra, 4($sp)

	lw	$t0, one
	lw	$t1, two
	add	$t2, $t0, $t1
	move	$a0, $t2
	jal	print_int

	lw	$ra, 4($sp)
	addi	$sp, $sp, 8

	jr	$ra

print_int:
	li	$v0, sys_print_int
	syscall

	la	$a0, lf
	li	$v0, sys_print_string
	syscall

	jr	$ra

	.data
one:
	.word	111
two:
	.word	222
lf:
	.asciiz	"\n"

	.end
