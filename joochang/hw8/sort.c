/* Do NOT change any code here. */

#include <stdio.h>
#include <stdlib.h>

#define NELEM(x) (sizeof((x))/sizeof((x)[0]))

void bubble(int a[], int len);

static int data[] = {30, 40, 10, 20, 50, 60, -2};
static int tiny[] = {10};

void print_array(int a[], int len)
{
  for (int i = 0; i < len; i++) {
    printf("[%d]", a[i]);
  }
  printf("\n");
}

int main(void)
{
  print_array(data, NELEM(data));
  bubble(data, NELEM(data));
  print_array(data, NELEM(data));

  print_array(tiny, NELEM(tiny));
  bubble(tiny, NELEM(tiny));
  print_array(tiny, NELEM(tiny));

  return EXIT_SUCCESS;
}
