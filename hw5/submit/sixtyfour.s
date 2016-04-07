# 600.233 CSF Assignment 5
# Name: Joon Hyuck Choi
# JHED: jchoi100
# email: jchoi100@jhu.edu
# sixtyfour.s

# 64 bit addition and subtraction
#
# write the two subroutines below and make sure they
# compute the correct 64 bit result; both subroutines
# get the following arguments:
#
# a0/a1 = lo/hi word of 64 bit integer
# a2/a3 = lo/hi word of 64 bit integer
#
# both return the result of the 64-bit addition or
# subtraction as follows:
#
# v0/v1 = lo/hi of 64 bit integer sum

sys_print_int = 1
sys_print_string = 4

main:
	addi	$sp, $sp, -4
	sw	$ra, ($sp)

	li	$a0, 0xffffffff
	li	$a1, 0x0
	li	$a2, 0x1
	li	$a3, 0x0

	jal	add64

	move	$a0, $v0
	move	$a1, $v1
	jal	print64

	li	$a0, 0x0
	li	$a1, 0x1
	li	$a2, 0x1
	li	$a3, 0x0

	jal	sub64

	move	$a0, $v0
	move	$a1, $v1
	jal	print64

	lw	$ra, ($sp)
	addi	$sp, $sp, 4

	jr	$ra

# v0/v1 = a0/a1 + a2/a3
add64:
	addu $v0, $a0, $a2 # $v0 = $a0 + $a2 --> low byte of result
	sltu $v1, $v0, $a2 # check if $v0 < $a2 If so, save carry in $v1
	addu $v1, $v1, $a1 # add carry $v1 and one high byte $a1
	addu $v1, $v1, $a3 # add $a1 + carry and $a3 --> high byte of result
	jr	$ra

# v0/v1 = a0/a1 - a2/a3
sub64:
	subu $v0, $a0, $a2 # $v0 = $a0 - $ a2 --> low byte of result
	sltu $v1, $a0, $a2 # check if $a0 < $a2. if so, $v1 will get 1
	subu $a1, $a1, $v1 # $a1 = $a1 - $v1
	subu $v1, $a1, $a3 # $v1 = $a1 - $a3 --> high byte of result
	jr	$ra

# print the 64-bit number in a0/a1 in hexadecimal (base 16)
print64:
	addi	$sp, $sp, -4
	sw	$ra, ($sp)

	move	$s0, $a0
	move	$a0, $a1
	jal	print_hex
	move	$a0, $s0
	jal	print_hex

	li	$v0, sys_print_string
	la	$a0, lf
	syscall

	lw	$ra, ($sp)
	addi	$sp, $sp, 4

	jr	$ra

# print the 32-bit number in $a0 in hexadecimal (base 16)
print_hex:
	la	$t0, buffer	# pointer to store next digit in
	la	$t1, digits	# pointer to table of hex digits
	li	$t2, 8		# count down 8 digits worth

ph_loop:
	rol	$a0, $a0, 4	# rotate left, hi 4 bits at lo end now
	andi	$t3, $a0, 0xf	# mask out lo 4 bits
	add	$t4, $t1, $t3	# compute address of digit for these 4 bits
	lb	$t5, ($t4)	# load digit
	sb	$t5, ($t0)	# append digit
	addi	$t0, $t0, 1
	addi	$t2, $t2, -1	# one less digit
	bne	$t2, $zero, ph_loop

	sb	$zero, ($t0)	# append zero byte
	addi	$t0, $t0, 1

	la	$a0, buffer
	li	$v0, sys_print_string
	syscall

	jr	$ra

	.data
buffer:
	.space	16
digits:
	.asciiz	"0123456789abcdef"
lf:
	.asciiz	"\n"
