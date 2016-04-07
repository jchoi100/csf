# 600.233 CSF Assignment 5
# Name: Joon Hyuck Choi
# JHED: jchoi100
# email: jchoi100@jhu.edu
# selection.s

# selection sort an array read from stdin to stdout
#
# we read up to 128 positive integers from stdin;
# if there are fewer than 128 integers, a negative
# integer can be used to mark the end of the input;
# we sort the array using selection sort and finally
# print the sorted array back to stdout

sys_print_int = 1
sys_print_string = 4
sys_read_int = 5

SLOTS = 128	# at most 128 values
BYTES = 512	# need 128*4 bytes of space

	.text
main:
	addi	$sp, $sp, -4
	sw	$ra, 0($sp)

	li	$a0, SLOTS
	la	$a1, buffer
	jal	read_array
	ble	$v0, $zero, exit
	sw	$v0, used

	lw	$a0, used
	la	$a1, buffer
	jal	selection_sort

	lw	$a0, used
	la	$a1, buffer
	jal	print_array
exit:
	lw	$ra, 0($sp)
	addi	$sp, $sp, 4

	jr	$ra

# length = read_array(size, address)
# $v0                 $a0   $a1
read_array:
	addi	$sp, $sp, -16		# save used registers
	sw	$s2, 12($sp)
	sw	$s1, 8($sp)
	sw	$s0, 4($sp)
	sw	$ra, 0($sp)

	move	$s0, $zero		# slots used
	move	$s1, $a1		# address in array
	move	$s2, $a0		# slots available
ra_loop:
	li	$v0, sys_read_int	# read next value
	syscall

	blt	$v0, $zero, ra_done	# break if we read < 0

	sw	$v0, ($s1)		# store value in array
	addi	$s0, $s0, 1		# used++
	addi	$s1, $s1, 4		# address++

	blt	$s0, $s2, ra_loop	# while used < available
ra_done:
	move	$v0, $s0		# return slots used

	lw	$ra, 0($sp)		# restore used registers
	lw	$s0, 4($sp)
	lw	$s1, 8($sp)
	lw	$s2, 12($sp)
	addi	$sp, $sp, 16

	jr	$ra			# return

# selection_sort(size, address)
#                 $a0   $a1
# a0 = array length
# a1 = array start address
# t0 = i 					: address  : outer loop counter
# t1 = j 				  : address  : inner loop counter 
# t2 = 4(n - 2)   : value    : outer loop break
# t3 = 4(n - 1)   : value    : inner loop break
# t4 = min				: address  : min element address
# t5 = jAdr       : address  : curr j element address
# a2 = a[j] 			: argument : used for swap calls
# a3 = a[min] 		: argument : used for swap calls
selection_sort:
	addi $sp, $sp, -8			# save used registers
	sw	 $s1, 4($sp)
	sw	 $ra, 0($sp)

	move $t2, $a0     		      # t2 = n
	add  $t2, $t2, -2  					# t2 -= 2: condition to break out of outer loop
	mul  $t2, $t2, 4            # t2 *= 4

	move $t3, $a0     		      # t3 = n
	add  $t3, $t3, -1  					# t3 -= 1: condition to break out of inner loop
	mul  $t3, $t3, 4            # t3 *= 4

	move $t0, $zero   					# i = 0
	move $t4, $a1               # min = a1
outer:
	bgt  $t0, $t2, breakOuter 	# while (i <= 4(n - 2))
	add  $t4, $a1, $t0          #     min = min + i
	addi $t5, $t4, 4            #     jAdr = min + 1 (but really + 4)
	add  $t1, $t0, 4 		        #     j = i + 1 (but really + 4)
inner:
	bgt  $t1, $t3, breakInner 	# 		while (j <= 4(n - 1))
	lw   $a2, 0($t5)            #     		a2 = a[j]
	lw   $a3, 0($t4)						#					a3 = a[min]
ifBlock:                      #         (this label here for readability
                            	#          --not really needed)
	bge  $a2, $a3, elseBlock    #  				if (a[j] < a[min])
	move $t4, $t5   		 				#							min = j
elseBlock:										#					else
	addi $t5, $t5, 4 						#							jAdr++ (but really + 4)
	addi $t1, $t1, 4 						#							j++ (but really + 4)
 	j inner 										#					back to inner loop
breakInner:
	add $t5, $a1, $t0
	move $a2, $t5               #     put value to swap in arg
	move $a3, $t4               #     put value to swap in arg
	jal  swap 									#			swap(a2, a3)
	addi $t0, $t0, 4 					  # 		i++ (but really + 4)
	j outer 										# back to outer loop
breakOuter:
	lw	  $ra, 0($sp)						# restore used registers
	lw	  $s1, 4($sp)
	addi	$sp, $sp, 8
	jr	  $ra										# return

# swap( i , j )
#      $a2 #a3
swap:
	addi	$sp, $sp, -8		# save used registers
	sw		$s2, 4($sp)
	sw		$s3, 0($sp)
	
	lw    $s2, 0($a2)     # s2 = (a2)
	lw    $s3, 0($a3)  		# s3 = (a3)

	sw    $s2, 0($a3)   	# (a3) = s2
	sw    $s3, 0($a2)   	# (a2) = s3
		
	lw		$s3, 0($sp)			# restore used registers
	lw		$s2, 4($sp)
	addi  $sp, $sp, 8
	jr	  $ra							# return


# print_array(size, address)
#             $a0   $a1
print_array:
	addi	$sp, $sp, -16		# save used registers
	sw	$s2, 12($sp)
	sw	$s1, 8($sp)
	sw	$s0, 4($sp)
	sw	$ra, 0($sp)

	move	$s0, $zero		# current slot
	move	$s1, $a1		# address in array
	move	$s2, $a0		# slots used
pa_loop:
	bge	$s0, $s2, pa_done	# while current < used

	lw	$a0, ($s1)		# load value in array
	jal	print_int		# and print it

	addi	$s0, $s0, 1			# current++
	addi	$s1, $s1, 4			# address++

	b	pa_loop
pa_done:
	lw	$ra, 0($sp)		# restore used registers
	lw	$s0, 4($sp)
	lw	$s1, 8($sp)
	lw	$s2, 12($sp)
	addi	$sp, $sp, 16

	jr	$ra			# return

# print_int(value)
#           $a0
print_int:
	li	$v0, sys_print_int
	syscall

	la	$a0, lf
	li	$v0, sys_print_string
	syscall

	jr	$ra

# print_string(string)
#              $a0
print_string:
	li	$v0, sys_print_string
	syscall

	la	$a0, lf
	li	$v0, sys_print_string
	syscall

	jr	$ra

	.data
buffer:
	.space	BYTES		# array of 4-byte integers
used:
	.word	0		# used slots in array
lf:
	.asciiz	"\n"

	.end
