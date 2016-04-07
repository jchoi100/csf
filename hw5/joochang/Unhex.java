/**
 * Unhexing the number.
 * CS.600.233, Assignment 5
 * @author James
 * jlee381@jhu.edu
 * unhex.java
 */
public final class Unhex {
    /**
     * FOUR = 4.
     */
    public static final int FOUR = 4;
    /**
     * EIGHT = 8.
     */
    public static final int EIGHT = 8;
    /**
     * Hexadecimal representation.
     */
    public static final int HEXADECIMAL = 16;

    private Unhex() {
    }

    /**
     * Main.
     * @param args arguments
     */
    public static void main(String[] args) {
        final int zero = 0;
        final int minusOne = -1;
        final int twoSixteenMinusOne = 65535;
        final int twoToSixteen = 65536;

        convert(zero);
        convert(minusOne);
        convert(twoSixteenMinusOne);
        convert(twoToSixteen);
    }

    /**
     * Converts integer to hexadecimal String and prints the string.
     * @param x the integer to convert
     */
    public static void convert(int x) {
        int a = x;
        String sequence = "0123456789abcdef";
        String hex = "";
        for (int i = 0; i < EIGHT; i++) {
            int digit = (a % HEXADECIMAL);
            if (digit < 0) {
                digit += HEXADECIMAL;
            }
            hex = sequence.charAt(digit) + hex;
            a = a >> FOUR;
        }
        System.out.println("Hexadecimal value of " + x + " is: " + hex);
    }
}
