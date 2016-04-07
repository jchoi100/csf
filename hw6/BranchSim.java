/*
 * 600.233 Computer Systems Fundamentals
 * Name: Joon Hyuck Choi
 * JHED: jchoi100
 * email: jchoi100@jhu.edu
 * BranchSim.java
 */

import java.util.Scanner;
import java.util.NoSuchElementException;

/**
 * Branch Simulator class.
 * @author Joon Hyuck Choi
 *
 */
public final class BranchSim {

    private static double numLines = 0;

    private static double correctPredictions = 0;
    private static double wrongPredictions = 0;

    private static double size = 0;

    private static final double PERCENTAGE = 100.0;
    private static final int HEX = 16;
    private static final int THREE = 3;
    private static final int FIVE = 5;

    private BranchSim() {

    }

    private static void alwaysTaken(Scanner xzScanner) {
        while (xzScanner.hasNextLine()) {
            String oneLine = xzScanner.nextLine();
            Scanner lineScanner = new Scanner(oneLine);
            String source = null;
            String destin = null;
            String result = null;
            long sourceLong = 0;
            long destinLong = 0;
            try {
                source = lineScanner.next();
                destin = lineScanner.next();
                result = lineScanner.next();
                sourceLong = Long.parseLong(source, HEX);
                destinLong = Long.parseLong(destin, HEX);
            } catch (NumberFormatException nfe) {
                System.err.println("Invalid input format!");
                System.exit(1);
            } catch (NoSuchElementException nee) {
                System.err.println("Wrong number of inputs in a line!");
                System.exit(1);
            }

            numLines++;

            if (result.equals("T")) {
                correctPredictions++;
            } else if (result.equals("N")) {
                wrongPredictions++;
            } else {
                System.err.println("Wrong result format!");
                System.exit(1);
            }

            lineScanner.close();
        }

        printRes();
    }

    private static void neverTaken(Scanner xzScanner) {
        while (xzScanner.hasNextLine()) {
            String oneLine = xzScanner.nextLine();
            Scanner lineScanner = new Scanner(oneLine);
            String source = null;
            String destin = null;
            String result = null;
            long sourceLong = 0;
            long destinLong = 0;
            try {
                source = lineScanner.next();
                destin = lineScanner.next();
                result = lineScanner.next();
                sourceLong = Long.parseLong(source, HEX);
                destinLong = Long.parseLong(destin, HEX);
            } catch (NumberFormatException nfe) {
                System.err.println("Invalid input format!");
                System.exit(1);
            } catch (NoSuchElementException nee) {
                System.err.println("Wrong number of inputs in a line!");
                System.exit(1);
            }
            numLines++;
            if (result.equals("N")) {
                correctPredictions++;
            } else if (result.equals("T")) {
                wrongPredictions++;
            } else {
                System.err.println("Wrong result format!");
                System.exit(1);
            }

            lineScanner.close();
        }

        printRes();
    }

    private static void btfnPredictor(Scanner xzScanner) {
        while (xzScanner.hasNextLine()) {
            String oneLine = xzScanner.nextLine();
            Scanner lineScanner = new Scanner(oneLine);
            String source = null;
            String destin = null;
            String result = null;
            long sourceLong = 0;
            long destinLong = 0;
            String ourPrediction = null;
            try {
                source = lineScanner.next();
                destin = lineScanner.next();
                result = lineScanner.next();
                sourceLong = Long.parseLong(source, HEX);
                destinLong = Long.parseLong(destin, HEX);

                if (sourceLong < destinLong) {
                    ourPrediction = "N";
                } else {
                    ourPrediction = "T";
                }

            } catch (NumberFormatException nfe) {
                System.err.println("Invalid input format!");
                System.exit(1);
            } catch (NoSuchElementException nee) {
                System.err.println("Wrong number of inputs in a line!");
                System.exit(1);
            }

            numLines++;

            if (ourPrediction.equals(result)) {
                correctPredictions++;
            } else {
                wrongPredictions++;
            }

            lineScanner.close();
        }

        printRes();
    }

    private static void bimodalPredictor(Scanner xzScanner, String[] args) {
        boolean slotValid = checkSlots(args[1]);
        boolean stepsValid = checkSteps(args[2]);
        if (!slotValid || !stepsValid) {
            System.err.println("Invalid inputs to bimodal predictor!");
            System.exit(1);           
        }

        int numSlots = Integer.parseInt(args[1]);
        int numSteps = Integer.parseInt(args[2]);

        byte[] table = new byte[numSlots];
        for (int i = 0; i < numSlots; i++) {
            table[i] = 0;
        }

        boolean prediction = true;
        boolean actual = true;

        while (xzScanner.hasNextLine()) {
            String oneLine = xzScanner.nextLine();
            Scanner lineScanner = new Scanner(oneLine);
            String source = null;
            String destin = null;
            String result = null;
            long sourceLong = 0;
            long destinLong = 0;
            try {
                source = lineScanner.next();
                destin = lineScanner.next();
                result = lineScanner.next();
                sourceLong = Long.parseLong(source, HEX);
                destinLong = Long.parseLong(destin, HEX);
            } catch (NumberFormatException nfe) {
                System.err.println("Invalid input format!");
                System.exit(1);
            } catch (NoSuchElementException nee) {
                System.err.println("Wrong number of inputs in a line!");
                System.exit(1);
            }

            int tableLocation = ((int) sourceLong) % numSlots;
            byte b = table[tableLocation];
            prediction = setTaken(b, numSteps);

            numLines++;

            if (result.equals("T")) {
                actual = true;
                if (table[tableLocation] < (numSteps - 1)) {
                    table[tableLocation]++;
                }
            } else if (result.equals("N")) {
                actual = false;
                if (table[tableLocation] > 0) {
                    table[tableLocation]--;
                }
            } else {
                System.err.println("Wrong result format!");
                System.exit(1);
            }

            checkCorrectness(actual, prediction);
            lineScanner.close();
        }
        size = numSlots * (int) (Math.log(numSteps) / Math.log(2));

        printRes();

    }


    private static void twolevelPredictor(Scanner xzScanner, String[] args) {
        boolean slotValid = checkSlots(args[1]);
        boolean historyValid = checkHistory(args[2]);
        boolean typeValid = checkType(args[3]);
        boolean stepsValid = checkSteps(args[4]);

        int numSlots = Integer.parseInt(args[1]);
        int historyWidth = Integer.parseInt(args[2]);
        String type = args[3];
        int numSteps = Integer.parseInt(args[4]);

        if (!slotValid || !historyValid || !typeValid || !stepsValid) {
            System.err.println("Invalid inputs to twolevel predictor!");
            System.exit(1);            
        }







        printRes();

    }

    /**************************************/
    /*Helper Methods*/
    /**************************************/

    private static void checkCorrectness(boolean actual, boolean prediction) {
        if (actual == prediction) {
            correctPredictions++;
        } else {
            wrongPredictions++;
        }
    }

    /**
     * Set taken depending on the saturation counter.
     * @param b The saturation counter
     * @param steps the number of steps for the saturation counter
     * @return true if decide to take the branch
     */
    private static boolean setTaken(byte b, double steps) {
        boolean taken = true;
        if (b < (steps / 2)) {
            taken = false;
        }
        return taken;
    }

    private static boolean checkType(String type) {
        if (type.equalsIgnoreCase("local")
            || type.equalsIgnoreCase("global")) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean checkSlots(String arg) {
        int numSlots = 0;
        try {
            numSlots = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            System.err.println("Historynum not a number!");
            System.exit(1);
        }        
        boolean res = true;
        if (numSlots < 4 || numSlots > 65536) {
            res = false;
        }
        if (!isTwosPower(numSlots)) {
            res = false;
        }
        return res;
    }

    private static boolean checkHistory(String arg) {
        int numHistory = 0;
        try {
            numHistory = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            System.err.println("Historynum not a number!");
            System.exit(1);
        }
        boolean res = true;
        if (numHistory < 4 || numHistory > 65536) {
            res = false;
        }
        if (!isTwosPower(numHistory)) {
            res = false;
        }
        return res;
    }

    private static boolean checkSteps(String arg) {
        int numSteps = 0;
        try {
            numSteps = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            System.err.println("Steps not a number!");
            System.exit(1);
        }
        boolean res = true;
        if (numSteps < 4 || numSteps > 16) {
            res = false;
        }
        if (!isTwosPower(numSteps)) {
            res = false;
        }
        return res;
    }

    private static boolean isTwosPower(int num) {
        if ((num & (num - 1)) == 0) {
            return true;
        } else {
            return false;
        }
    }

    private static void printRes() {
        double goodPerc = 0;
        double badPerc = 0;
        if (numLines != 0) {
            goodPerc = (correctPredictions / numLines) * PERCENTAGE;
            badPerc = (wrongPredictions / numLines) * PERCENTAGE;
        }
        System.out.println("Total " + (long) numLines);
        System.out.println("Good " + (long) correctPredictions);
        System.out.println("Bad " + (long) wrongPredictions);
        System.out.print("Good% ");
        System.out.printf("%.2f\n", goodPerc);
        System.out.print("Bad% ");
        System.out.printf("%.2f\n", badPerc);
        System.out.println("Size " + (long) size);
    }

    private static void executePredictor(String[] args, String command,
                                         Scanner xzScanner) {        
        if (command.equals("at")) {
            alwaysTaken(xzScanner);
        } else if (command.equals("nt")) {
            neverTaken(xzScanner);
        } else if (command.equals("btfn")) {
            btfnPredictor(xzScanner);
        } else if (command.equals("bimodal")) {
            if (args.length == THREE) {
                bimodalPredictor(xzScanner, args);
            } else {
                System.err.println("Wrong arguments!");
                System.exit(1);
            }
        } else if (command.equals("twolevel")) {
            if (args.length == FIVE) {
                twolevelPredictor(xzScanner, args);
            } else {
                System.err.println("Wrong arguments!");
                System.exit(1);
            }
        } else {
            System.err.println("Wrong arguments!");
            System.exit(1);
        }

    }

    /**
     * Main driver for this program.
     *
     * @param args arguments for this program.
     */
    public static void main(String[] args) {
        if (args.length <= 0) {
            System.err.println("Wrong arguments!");
            System.exit(1);
        }

        Scanner xzScanner = new Scanner(System.in);
        executePredictor(args, args[0], xzScanner);
        xzScanner.close();
    }
}