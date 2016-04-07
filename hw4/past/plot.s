;600.233 Computer Systems Fundamentals
;Name: Joo Chang Lee
;JHED ID: jlee381
;Email: jlee381@jhu.edu
;Assignment 4. Eight Bits of Madness!
;plot.s

; a plot subroutine
;
; write the subroutine below to make the "random
; dots" animation work as shown in lecture

main:
	lda	$fe	; random
	and	#$1f	; 0 <= x <= 31
	sta	$00	; coordinate

	lda	$fe	; random
	and	#$1f	; 0 <= y <= 31
	sta	$01	; coordinate

	lda	$fe	; random
	and	#$0f	; 0 <= y <= 15
	sta	$02	; color

	jsr	plot	; plot the pixel

	jmp	main	; and again, forever

; draw a pixel at the coordinates and with the color
; given: x is in $00, y is in $01, color is in $02
plot:
	; your code here
	lda $01	;load y coordinate.
	lsr
	lsr
	lsr			;shift right 3 times (integer division by 8)
	sta $04	;store to $04
	lda $04	;load $04
	asl
	asl
	asl			;shift left 3 times (floor of $01 / 8)
	sta $05	;store to $05
	clc
	lda $01	;load y coordinate
	sbc $05	;subtract by the floor value. (desired result: y mod 8)
	clc
	adc #$01	;add 1 (adjustment for correct value)
	asl
	asl
	asl
	asl
	asl			;shift left 5 times (x32)
	adc $00	;add x coordinate
	sta $03	;store to $03.
	lda $04	;load (y coordinate / 8)
	adc #$02	;add 2 (display starts at $200)
	sta $04	;store to $04 (high byte)
	lda $02	;load the value
	sta ($03, x)	;store to 16bit address starting from $03 (little endian)
	rts
