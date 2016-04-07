# 600.233 Computer Systems Fundamentals
# Name: Joo Chang Lee
# JHED ID: jlee381
# Email: jlee381@jhu.edu
# Assignment 5. MIPS and SPIM!
# popcount.s


sys_print_int = 1
sys_print_string = 4

	.text
main:
	sub	$sp, 4
	sw		$ra, 0($sp)			# store $ra

	jal		read_input		# read input and do the operation

	lw		$ra, 0($sp)
	add	$sp, 4				# restore $ra
	jr		$ra					# exit main


read_input:
	sub	$sp, 16	
	sw		$s2, 12($sp)		# store registers $ra, $s0, $s1 $s2
	sw		$s1, 8($sp)
	sw		$s0, 4($sp)
	sw		$ra, 0($sp)

	lw		$s0, 4($a1)		# load pointer of command line argument to $s0
	move	$s1, $zero			# clear $s1, will contain counter for 1's.
	lb		$s2, two			# set $s2 to 2 for modular arithmetic

	beq	$s0, $zero, end	# check if input exists

loop:
	lb		$a0, 0($s0)		# load the byte from the pointer for input
	add	$s0, 1				# increment address by 1 byte
	
	bne	$a0, $zero, valid_check	# if the input is not '\0', do the operation.

	move	$a0, $s1			# move the counter to $a0 for output
	li		$v0, sys_print_int # print the counter
	syscall

	la		$a0, lf					# line feed
	li		$v0, sys_print_string		
	syscall

end:
	lw		$ra, 0($sp)			# restore registers
	lw		$s0, 4($sp)
	lw		$s1, 8($sp)
	lw		$s2, 12($sp)
	add	$sp, 16
	jr		$ra					# exit read_input subroutine


valid_check:
	lb		$t3, num_floor	# load num_floor to register $t3
	blt		$a0, $t3, loop		# throw away input char if input < num_floor
	lb		$t3, num_ceil		# load num_ceil to register
	blt		$a0, $t3, add_num	# count 1's from the input if input < num_ceil

	lb		$t3, up_floor
	blt		$a0, $t3, loop		# throw away input char if input < up_floor
	lb		$t3, up_ceil
	blt		$a0, $t3, add_upper	# count 1's from the input if input < upper_ceil

	lb		$t3, low_floor
	blt		$a0, $t3, loop		# throw away input char if input < low_floor
	lb		$t3, low_ceil
	blt		$a0, $t3, add_lower	# count 1's from the input if input < low_ceil

	j	loop					# throw away input if input does not belong in valid range


add_num:
	lb		$t5, num_floor	# load conversion factor to $t5
	sub	$a0, $a0, $t5		# subtract conversion factor (so that 0 = 0)
add_num_loop:
	beq	$a0, $zero, add_num_done	# if $a0 is zero, we are done with this character
	div		$a0, $s2			# a0 mod 2
	mfhi	$t4					# move remainder to $t4
	add	$s1, $s1, $t4		# add remainder to counter (0 or 1)
	mflo	$a0				# move quotient to $a0
	j	add_num_loop		# return for further division by 2
add_num_done:
	j	loop					# return for next character


add_upper:
	lb		$t5, up_conv		# load conversion factor
	sub	$a0, $a0, $t5		# subtract conversion factor (so that A = 10)
add_upper_loop:
	beq	$a0, $zero, add_upper_done		# done when $a0 is zero
	div		$a0, $s2			# mod 2
	mfhi	$t4
	add	$s1, $s1, $t4		# add remainder to counter
	mflo	$a0				# move quotient to $a0
	j	add_upper_loop		# return for further division
add_upper_done:
	j	loop					# return for next character


add_lower:
	lb		$t5, low_conv		# load conversion factor
	sub	$a0, $a0, $t5		# subtract conversion factor (so that a = 10)
add_lower_loop:
	beq	$a0, $zero, add_lower_done		# done when $a0 is zero
	div		$a0, $s2			# mod 2
	mfhi	$t4
	add	$s1, $s1, $t4		# add remainder to counter
	mflo	$a0				# move quotient to $a0
	j	add_lower_loop		# return for further division
add_lower_done:
	j	loop					# return for next character


	.data
lf:						# line feed
		.asciiz "\n"
two:					# two
		.byte 0x02
num_floor:			# ascii equivalent of 0
		.byte 0x30
num_ceil:				# ascii equivalent of : (one after 9)
		.byte 0x3a
up_floor:				# ascii equivalent of A
		.byte 0x41
up_ceil:				# ascii equivalent of G (one after F)
		.byte 0x47
low_floor:				# ascii equivalent of a
		.byte 0x61
low_ceil:				# ascii equivalent of g (one after f)
		.byte 0x67
up_conv:				# lower case alphabet conversion factor (A - conv = 10)
		.byte 0x37
low_conv:				# lower case alphabet conversion factor (a - conv = 10)
		.byte 0x57

	.end
