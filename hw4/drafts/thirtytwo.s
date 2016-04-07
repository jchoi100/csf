;600.233 Assignment 4
;Name: Joon Hyuck Choi
;JHED: jchoi100
;email: jchoi100@jhu.edu
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
        clc
	lda $00
	adc $04
	sta $08
	lda $01
	adc $05
	sta $09
	lda $02
	adc $06
	sta $0a
	lda $03
	adc $07
	sta $0b
	rts

; subtract the 32 bit integer stored at $04..$07 from
; the 32 bit integer stores at $00..$03 and put the
; 32 but result into $0c..$0f
sub32:
	sec
	lda $00
	sbc $04
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
