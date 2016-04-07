# Simple counter program in SCRAM assembly.

start:	LDA	count
	ADD	one
	STA	count

counter		:	DAT	0	# counter variable
one:	DAT	1	# constant 1
	ADD	one
	STA	count

count:	DAT	0	# counter variable
one:	DAT	1	# constant 1
	ADD	one
	STA	count

count:	DAT	0	# counter variable
one:	DAT	1	# constant 1
