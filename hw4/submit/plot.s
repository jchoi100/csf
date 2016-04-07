;600.233 Assignment 4
;Name: Joon Hyuck Choi
;JHED: jchoi100
;email: jchoi100@jhu.edu
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
	lda $01	;load y into A
	lsr
	lsr
	lsr		        ;shift right 3 times, we get y div 8 as result
	sta $04	;store (y div 8) at $04
	lda $04	;load back (y div 8) to A
	asl
	asl
	asl		        ;shift left 3 times, we get floor of y as result
	sta $05	;store floor(y) at $05
	sec                 ;set carry before subtraction
	lda $01	;load back original y coordinate
	sbc $05	;do y - floor(y), we get y mod 8 as result
	clc ;clear carry
	asl
	asl
	asl
	asl
	asl		        ;shift left 5 times, we get (y mod 8 ) * 32 as result
	adc $00	;add x to 32 * (y mod 8)
	sta $03	;store result at $03
	lda $04	;load (y div 8)
	adc #$02    ;add 2 to the most significant byte (display in 6502 starts at $0200)
	sta $04	;store this most significant byte at $04
	lda $02	;load the color to A
	sta ($03, x) ;store the color at the 2 byte address 
				 ;starting from where $03 is pointing (little endian)
	rts
