import java.util.NoSuchElementException;
import java.util.Scanner;
import java.text.DecimalFormat;

/**
 * 600.233 Computer Systems Fundamentals.
 * Assignment 6
 * @author Joo Chang Lee
 * jlee381@jhu.edu
 * Param.java
 */
public final class Param {

    static final int HEX = 16;
    static final double PERCENTAGE = 100.0;

    private Param() {

    }

    /**
     * Main method.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = "";
        int count = 0;
        int forwardCount = 0;
        int backwardCount = 0;
        double forwardDistance = 0;
        double backwardDistance = 0;
        DecimalFormat df2 = new DecimalFormat("0.00");
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            if (line.trim().equals("")) {
                break;
            }

            int fromInt = 0;
            int toInt = 0;
            Scanner sc2 = new Scanner(line);
            try {
                String read = sc2.next();
                fromInt = Integer.parseInt(read, HEX);
                read = sc2.next();
                toInt = Integer.parseInt(read, HEX);
                read = sc2.nextLine();
            } catch (NumberFormatException num) {
                System.out.println("Not a valid number input");
                break;
            } catch (NoSuchElementException no) {
                System.out.println("Format error");
                break;
            }

            count++;
            if (fromInt > toInt) {
                backwardCount++;
                backwardDistance += (fromInt - toInt);
            } else {
                forwardCount++;
                forwardDistance += (toInt - fromInt);
            }
            sc2.close();
        }

        printCount(count, forwardCount, backwardCount);
        printPercentage(count, forwardCount, backwardCount, df2);
        printDistance(forwardCount, backwardCount,
                forwardDistance, backwardDistance, df2);
        sc.close();
    }

    /**
     * Prints count.
     * @param count number of inputs
     * @param forwardCount number of inputs that move forward
     * @param backwardCount number of inputs that move backward
     */
    public static void printCount(int count,
            int forwardCount, int backwardCount) {
        System.out.println("Total " + count);
        System.out.println("Forward " + forwardCount);
        System.out.println("Backward " + backwardCount);
    }

    /**
     * Prints percentage.
     * @param count number of inputs
     * @param forwardCount number of inputs that move forward
     * @param backwardCount number of inputs that move backward
     * @param df2 the 2 decimal format
     */
    public static void printPercentage(int count, int forwardCount,
            int backwardCount, DecimalFormat df2) {
        double forwardPercentage = 0;
        double backwardPercentage = 0;
        if (count != 0) {
            forwardPercentage = forwardCount * PERCENTAGE / count;
            backwardPercentage = backwardCount * PERCENTAGE / count;
        }
        System.out.println("Forward% "
                + df2.format(forwardPercentage));
        System.out.println("Backward% "
                + df2.format(backwardPercentage));
    }

    /**
     * Prints average distance.
     * @param forwardCount number of inputs that move forward
     * @param backwardCount number of inputs that move backward
     * @param forwardDistance the total distance the program moves forward
     * @param backwardDistance the total distance the program moves backward
     * @param df2 the 2 decimal format
     */
    public static void printDistance(int forwardCount, int backwardCount,
            double forwardDistance, double backwardDistance,
            DecimalFormat df2) {
        double avgForwardDistance = 0;
        double avgBackwardDistance = 0;
        if (forwardCount != 0) {
            avgForwardDistance = forwardDistance / forwardCount;
        }
        if (backwardCount != 0) {
            avgBackwardDistance = backwardDistance / backwardCount;
        }
        System.out.println("Forward-distance "
                + df2.format(avgForwardDistance));
        System.out.println("Backward-distance "
                + df2.format(avgBackwardDistance));
    }
}
