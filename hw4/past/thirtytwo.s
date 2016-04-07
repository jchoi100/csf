;600.233 Computer Systems Fundamentals
;Name: Joo Chang Lee
;JHED ID: jlee381
;Email: jlee381@jhu.edu
;Assignment 4. Eight Bits of Madness!
;thirtytwo.s

; 32 bit little endian addition and subtraction
;
; write the two subroutines below and make sure they
; compute the correct 32 bit little endian results

main:
	lda	#$12
	sta	$04
	sta	$05
	lda	#$13
	sta	$06
	sta	$07
	lda	#$3f
	sta	$00
	sta	$01
	sta	$02
	sta	$03
	jsr	add32
	jsr	sub32
	brk

; add the two 32 bit integers stored at $00..$03 and
; $04..$07 into a 32 bit result stored at $08..$0b
add32:
	clc			;clear carry
	lda $00
	adc $04	;add 00 and 04 (lowest)
	sta $08	;store at 08 (lowest)
	lda $01
	adc $05
	sta $09
	lda $02
	adc $06
	sta $0a
	lda $03
	adc $07	;add 03 and 07 (highest)
	sta $0b	;store at 0b (highest)
	rts

; subtract the 32 bit integer stored at $04..$07 from
; the 32 bit integer stores at $00..$03 and put the
; 32 but result into $0c..$0f
sub32:
	sec			;set carry (for subtraction)
	lda $00	;load $00
	sbc $04	;subtract $04
	sta $0c
	lda $01
	sbc $05
	sta $0d
	lda $02
	sbc $06
	sta $0e
	lda $03
	sbc $07
	sta $0f
	rts
