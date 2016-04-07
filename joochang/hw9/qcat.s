SYS_EXIT = 1
SYS_READ = 3
SYS_WRITE = 4
STDIN_FILENO = 0
STDOUT_FILENO = 1
SYSCALL = 0x80
BUFFER_SIZE = 128
EOF = 0
EXIT_SUCCESS = 0

.section .bss
	.lcomm buffer, BUFFER_SIZE
	.text
	.globl	_start
_start:
	movl	$buffer, %ecx		# move buffer to ecx register
.read:
	movl	$SYS_READ, %eax		# read syscall code
	movl	$STDIN_FILENO, %ebx	# integer for stdin
	movl	$BUFFER_SIZE, %edx	# default buffer size,
					# for number of bytes to read
	int	$SYSCALL		# syscall
	cmpl	$EOF, %eax		# compare result to EOF.
					# result of read syscall is
					# the number of bytes read,
					# with 0 indicating EOF.
	je .end				# jump to end if EOF
	movl	%eax, %edx		# move the number of bytes to edx,
					# which is number of bytes to print
	movl	$SYS_WRITE, %eax	# write syscall code
	movl	$STDOUT_FILENO, %ebx	# integer for stdout
	int	$SYSCALL		# syscall
	jmp .read			# go back to read input
.end:
	mov	$SYS_EXIT, %eax		# exit syscall code
	mov	$EXIT_SUCCESS, %ebx	# return value indicating success
	int	$SYSCALL		# syscall
