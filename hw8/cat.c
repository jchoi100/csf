#include <stdio.h>
#include <stdlib.h>

int main(void)
{
	int c;
	while ((c = getchar()) != EOF) {
		putchar(c);
	}
	return EXIT_SUCCESS;
}
