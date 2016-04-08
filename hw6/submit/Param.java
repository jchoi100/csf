/*
 * 600.233 Computer System Fundamentals
 * Name: Joon Hyuck Choi
 * Assignment 6
 * JHED: jchoi100
 * email: jchoi100@jhu.edu
 * Param.java
 */

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Param class for Problem 1.
 * @author Joon Hyuck Choi
 *
 */
public final class Param {

    /** Used to calculate percentage. */
    private static final double PERCENTAGE = 100.0;

    /** Sixteen. */
    private static final int HEX = 16;

    /**
     * Dummy constructor.
     */
    private Param() {

    }

    /**
     * Processes the input file and produces statistics.
     * @param xzScanner The scanner for the .xz file.
     */
    private static void parseInfo(Scanner xzScanner) {

        double numLines = 0;

        long numForward = 0;
        long numBackward = 0;

        long forwardDist = 0;
        long backwardDist = 0;

        while (xzScanner.hasNextLine()) {
            numLines++;
            String oneLine = xzScanner.nextLine();
            Scanner lineScanner = new Scanner(oneLine);
            String sourceAddress = null; //hex address
            String destinAddress = null; //hex address
            long sourceLong = 0; //decimal value
            long destinLong = 0; //decimal value

            try {
                sourceAddress = lineScanner.next().trim(); //hex address
                destinAddress = lineScanner.next().trim(); //hex address
            } catch (NoSuchElementException e) {
                System.err.println("Formatting error!");
            }

            try {
                sourceLong = Long.parseLong(sourceAddress, HEX); //decimal value
                destinLong = Long.parseLong(destinAddress, HEX); //decimal value
            } catch (NumberFormatException e) {
                System.err.println("Not a valid input!");
            }

            if (sourceLong > destinLong) {
                numBackward++;
                backwardDist += (sourceLong - destinLong);
            } else {
                numForward++;
                forwardDist += (destinLong - sourceLong);
            }
            lineScanner.close();
        }

        printRes(numLines, numForward, numBackward, forwardDist, backwardDist);
    }

    /**
     * Prints the result of running the program.
     */
    private static void printRes(double numLines, long numForward,
                                     long numBackward, long forwardDist,
                                     long backwardDist) {
        double forwardPerc = 0.0;
        double backwardPerc = 0.0;
        double forwardDistAvg = 0.0;
        double backwardDistAvg = 0.0;

        if (numLines != 0) {
            forwardPerc = (numForward / numLines) * PERCENTAGE;
            backwardPerc = (numBackward / numLines) * PERCENTAGE;
            if (numForward != 0) {
                forwardDistAvg = ((double) forwardDist / numForward);
            }
            if (numBackward != 0) {
                backwardDistAvg = ((double) backwardDist / numBackward);
            }
        }

        System.out.println("Total " + (long) numLines);
        System.out.println("Forward " + numForward);
        System.out.println("Backward " + numBackward);
        System.out.print("Forward% ");
        System.out.printf("%.2f\n", forwardPerc);
        System.out.print("Backward% ");
        System.out.printf("%.2f\n", backwardPerc);
        System.out.printf("Forward-distance %.2f\n", forwardDistAvg);
        System.out.printf("Backward-distance %.2f\n", backwardDistAvg);
    }

    /**
     * Main driver method for Parse class.
     * @param args arguments for main.
     */
    public static void main(String[] args) {
        Scanner xzScanner = new Scanner(System.in);
        parseInfo(xzScanner);
        xzScanner.close();
    }
}
