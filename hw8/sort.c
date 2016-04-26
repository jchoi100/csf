/* Do NOT change any code here. */

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>

#define NELEM(x)  (sizeof(x) / sizeof((x)[0]))

int tiny[] = {10};
int random[] = {30, 40, 10, 20, 50, 60, -2, 70};
int ascending[] = {10, 20, 30, 40, 50, 60};
int descending[] = {60, 50, 40, 30, 20, 10};

void quicksort(int a[], int n);

int sorted(int a[], int n)
{
	for (int i = 0; i < n-1; i++) {
		if (a[i] > a[i+1]) {
			return 0;
		}
	}
	return 1;
}

void print(int a[], int n)
{
	for (int i = 0; i < n; i++) {
		printf("[%d]", a[i]);
	}
	if (sorted(a, n)) {
		printf(" (sorted)\n");
	} else {
		printf(" (unsorted)\n");
	}
}

int partition(int a[], int l, int u)
{
  	assert(l < u);

	int p = a[l];
	int i = l + 1;
	int j = u;

	while (i <= j) {
		while (i <= j && a[i] < p) {
			i++;
		}
		while (i <= j && a[j] > p) {
			j--;
		}
		if (i <= j) {
			int t = a[i]; a[i] = a[j]; a[j] = t;
			i++; j--;
		}
	}
	int t = a[l]; a[l] = a[j]; a[j] = t;
	return j;
}

// void realsort(int a[], int lo, int hi) {
// 	if (lo < hi) {
// 		int p = partition(a, lo, hi);
// 		realsort(a, lo, p);
// 		realsort(a, p + 1, hi);
// 	}
// }

// void quicksort(int a[], int n) {
// 	realsort(a, 0, n - 1);
// }


void test(int a[], int n)
{
	print(a, n);
	quicksort(a, n);
	print(a, n);
	printf("\n");
}

int main(void)
{
	test(tiny, NELEM(tiny));
	test(random, NELEM(random));
	test(ascending, NELEM(ascending));
	test(descending, NELEM(descending));

	return EXIT_SUCCESS;
}
