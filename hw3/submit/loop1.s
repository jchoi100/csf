a: b: c: 

LDA count

STA one
JMP count
JMZ a
STA b
STI c
STI one

count: DAT 0
one: DAT 1