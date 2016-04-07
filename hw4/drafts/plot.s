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
	; your code here
	rts
