;600.233 Assignment 4
;Name: Joon Hyuck Choi
;JHED: jchoi100
;email: jchoi100@jhu.edu
;sorting.s

; animated array sorting in 6502 assembly
;
; write the subroutine below to sort the array in
; ascending order and make the animation work as
; shown in lecture; note that you don't have to
; write any graphics code, it's all there already
;
; zero page memory map
;
; $00/$01
;	pointer to current screen line for draw_array
; 	use ($00),y addressing to access pixel in line
; $10/$11
;	array indices to swap for swap_slot
; $20/$3f
;	array to be sorted

main:
	jsr	fill_array
	jsr	draw_array
	jsr	sort_array
	brk

; sort the array, this is the code you have to write
; make sure you use swap_slot to exchange elements
; in the array, otherwise the animation won't work
sort_array:
	ldx #$1f
start:
	txa
	pha
	ldx #$1f
	clc
	sec
	ldy x
	jsr inner
	clc
	pla
	tax
	dex
	cpx #$00
	bpl start
	rts

inner:
	clc
	lda $20,x
	dex
	cmp $20,x
	bcc swap
	cpx #$00
	bne inner
	rts

swap:
	clc
	stx $10
	inx
	stx $11
	jsr swap_slot
	rts


; swap the two array slots indicated by $10 and $11
; and perform the necessary graphics updates for the
; sorting animation; would be more efficient to only
; draw the modified slots...
swap_slot:
	pha		; save registers
	txa
	pha
	tya
	pha

	ldx	$10	; first index
	lda	$20,x	; array[first]
	ldx	$11	; second index
	ldy	$20,x	; array[second]
	sta	$20,x	; array[second] = array[first]
	ldx	$10	; first index
	sty	$20,x	; array[first] = array[second]
	jsr	draw_array

	pla		; restore registers
	tay
	pla
	tax
	pla
	rts

; fill array with random values between 0 and 31
fill_array:
	ldx	#31	; count array index down from 31
fa_loop:
	lda	$fe	; grab a random byte
	and	#$1f	; force between 0 and 31
	sta	$20,x	; store in array[index]
	dex		; decrement index
	bpl	fa_loop	; another round if still positive

	rts

; draw the global array to the screen, Y coordinate
; is array position, X coordinate is value at that
; position
draw_array:
	; set up pointer to first screen line
	lda	#$02
	sta	$01
	lda	#$00
	sta	$00

	ldx	#31	; array index

da_next:
	; clear line
	ldy	#$1f
	lda	#0
da_clear:
	sta	($00),y
	dey
	bpl	da_clear
	; draw line
	ldy	$20,x	; array value at index
	lda	#3
da_pixel:
	sta	($00),y
	dey
	bpl	da_pixel

	; update pointer to next screen line
	lda	$00		; load low byte
	clc
	adc	#$20		; increment low byte
	sta	$00		; doesn't affect flags
	bcc	da_easy_line	; no overflow

	inc	$01		; increment high byte

da_easy_line:

	dex		; next array index
	bpl	da_next ; branch if negative flag clear

	rts
