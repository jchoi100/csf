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
	ldx #$1f	;outer for loop counter
outer:	;outer for loop
	txa
	pha ;push x value to stack
	ldx #$1f ;put #$1f in x register
	clc ;clear bit
	sec ;set bit
	ldy x
	jsr inner	;go to inner for loop
	clc ;clear bit
	pla
	tax ;load back x value from stack
	dex ;decrement counter
	cpx #$00	;terminate outer for loop when x == 0
	bne outer ;otherwise, iterate again
	rts

inner:	;inner for loop
	clc
	dex ;decrement x
	lda $20,x	;load contents at address $(20+x) to accumulator
	inx		;increment x
	cmp $20,x	;compare accumulator to $(20+x)
	dex 	;decrement x, this doesn't affect carry flag
	bcc swap	;if the lower element is larger, call swap.
inner_stub: 
	clc
	cpx #$00
	bne inner	;inner loop ends when x = 0
	rts

swap:
	stx $10	;first index to swap
	inx			;increment x
	stx $11	;second index to swap
	jsr swap_slot	;swap
	cpx #$00 ;compare the x value to 0
	dex 	;decrement x
	bne inner_stub	;return to where we left off
	rts ;otherwise, return from subroutine



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
