/* Do NOT change any code here. */

#include <stdio.h>
#include <stdlib.h>

int min(int a, int b);
int max(int a, int b);

int main(void)
{
  printf("%d\n", min(10, 1));
  printf("%d\n", min(2, 2));
  printf("%d\n", min(3, 7));

  printf("%d\n", max(4, 1));
  printf("%d\n", max(-2, 5));
  printf("%d\n", max(6, 6));

  return EXIT_SUCCESS;
}
