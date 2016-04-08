/*
 * 600.233 Computer System Fundamentals
 * Name: Joon Hyuck Choi
 * Assignment 6
 * JHED: jchoi100
 * email: jchoi100@jhu.edu
 * BranchSim.java
 */

import java.util.Scanner;
import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * Branch Simulator class.
 * @author Joon Hyuck Choi
 *
 */
public final class BranchSim {

    /** Total number of lines in .xz file. */
    private static double numLines = 0;

    /** Number of correct predictions made. */
    private static double correctPredictions = 0;

    /** Number of wrong predictions made. */
    private static double wrongPredictions = 0;

    /** Size of the branch predictor in bits. */
    private static double size = 0;

    /** Used to calculate percentage. */
    private static final double PERCENTAGE = 100.0;

    /** Three. */
    private static final int THREE = 3;

    /** Four. */
    private static final int FOUR = 4;

    /** Five. */
    private static final int FIVE = 5;

    /** Sixteen. */
    private static final int SIXTEEN = 16;

    /** 2^16. */
    private static final int TWOSIXTEEN = 65536;

    /**
     * Dummy constructor.
     */
    private BranchSim() {

    }

    /**
     * Branch simulator with the "always taken" heuristic.
     *
     * @param xzScanner Scanner for input .xz file.
     */
    private static void alwaysTaken(Scanner xzScanner) {
        while (xzScanner.hasNextLine()) {
            String oneLine = xzScanner.nextLine();
            Scanner lineScanner = new Scanner(oneLine);
            String result = null;
            try {
                Integer.parseInt(lineScanner.next().trim(), SIXTEEN);
                Integer.parseInt(lineScanner.next().trim(), SIXTEEN);
                result = lineScanner.next().trim();
            } catch (NumberFormatException nfe) {
                System.err.println("Invalid input number format!");
                System.exit(1);
            } catch (NoSuchElementException nee) {
                System.err.println("Wrong number of inputs in a line!");
                System.exit(1);
            }

            numLines++;

            if (result.equalsIgnoreCase("T")) {
                correctPredictions++;
            } else if (result.equalsIgnoreCase("N")) {
                wrongPredictions++;
            } else {
                System.err.println("Wrong result format! Either T or N!");
                System.exit(1);
            }

            lineScanner.close();
        }

    }

    /**
     * Branch predictor for the "never taken" heuristic.
     * @param xzScanner Scanner for the input .xz file.
     */
    private static void neverTaken(Scanner xzScanner) {
        while (xzScanner.hasNextLine()) {
            String oneLine = xzScanner.nextLine();
            Scanner lineScanner = new Scanner(oneLine);
            String result = null;
            try {
                Integer.parseInt(lineScanner.next().trim(), SIXTEEN);
                Integer.parseInt(lineScanner.next().trim(), SIXTEEN);
                result = lineScanner.next().trim();
            } catch (NumberFormatException nfe) {
                System.err.println("Invalid input number format!");
                System.exit(1);
            } catch (NoSuchElementException nee) {
                System.err.println("Wrong number of inputs in a line!");
                System.exit(1);
            }

            numLines++;

            if (result.equalsIgnoreCase("N")) {
                correctPredictions++;
            } else if (result.equalsIgnoreCase("T")) {
                wrongPredictions++;
            } else {
                System.err.println("Wrong result format! Either T or N!");
                System.exit(1);
            }

            lineScanner.close();
        }

    }

    /**
     * Branch predictor for the "backwards take forwards not take" heuristic.
     * @param xzScanner Scanner for the input .xz file.
     */
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
                sourceLong = Long.parseLong(source, SIXTEEN);
                destinLong = Long.parseLong(destin, SIXTEEN);

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

    }

    /**
     * Dynamic bimodal branch predictor with given number of slots for the
     * prediction table and the given number of steps for saturation counter.
     * @param xzScanner Scanner for the input .xz file.
     * @param args The input arguments to the program.
     */
    private static void bimodalPredictor(Scanner xzScanner, String[] args) {
        boolean slotValid = checkSlots(args[1]);
        boolean stepsValid = checkSteps(args[2]);
        if (!slotValid || !stepsValid) {
            System.err.println("Invalid inputs to bimodal predictor!");
            System.exit(1);
        }

        int numSlots = Integer.parseInt(args[1]);
        int numSteps = Integer.parseInt(args[2]);

        byte[] table = makeByteTable(numSlots);

        boolean prediction = true;
        boolean actual = true;

        while (xzScanner.hasNextLine()) {
            String oneLine = xzScanner.nextLine();
            Scanner lineScanner = new Scanner(oneLine);
            String source = null;
            String result = null;
            int sourceInt = 0;
            try {
                source = lineScanner.next().trim();
                sourceInt = Integer.parseInt(source, SIXTEEN);
                Integer.parseInt(lineScanner.next().trim());
                result = lineScanner.next().trim();
            } catch (NumberFormatException nfe) {
                System.err.println("Invalid input format!");
                break;
            } catch (NoSuchElementException nee) {
                System.err.println("Wrong number of inputs in a line!");
                break;
            }

            int tableLocation = ((int) sourceInt) % numSlots;
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
                System.err.println("Wrong result format! Either T or N!");
                break;
            }

            evalPredication(prediction, actual);
            lineScanner.close();
        }
        size = numSlots * (int) (Math.log(numSteps) / Math.log(2));

    }

    /**
     * Dynamic two-level predictor with the given number of slots for the
     * history table, the given number of possible history patterns, either
     * many local or one global prediction table(s), and the given number of
     * steps for the saturation counter.
     *
     * @param xzScanner Scanner for the .xz file.
     * @param args arguments for the program.
     */
    private static void twolevelPredictor(Scanner xzScanner, String[] args) {
        boolean slotValid = checkSlots(args[1]);
        boolean historyValid = checkHistory(args[2]);
        boolean typeValid = checkType(args[THREE]);
        boolean stepsValid = checkSteps(args[FOUR]);
        boolean isLocal = checkLocal(args[THREE]);

        int numSlots = Integer.parseInt(args[1]);
        int historyWidth = Integer.parseInt(args[2]);
        int numSteps = Integer.parseInt(args[FOUR]);

        checkTwoLevelValid(slotValid, historyValid, typeValid, stepsValid);

        int[] history = makeHistoryTable(numSlots);

        size += numSlots * (int) (Math.log(historyWidth) / Math.log(2));

        HashMap<Long, Byte> map = new HashMap<>();
        String oneLine = "";
        int factor = 0;
        boolean prediction = true;
        boolean actual = true;

        while (xzScanner.hasNextLine()) {
            oneLine = xzScanner.nextLine();
            if (oneLine.trim().equals("")) {
                break;
            }
            Scanner lineScanner = new Scanner(oneLine);
            String source = "";
            int sourceInt = 0;
            String result = "";
            try {
                source = lineScanner.next().trim();
                sourceInt = Integer.parseInt(source, SIXTEEN);
                Integer.parseInt(lineScanner.next().trim(), SIXTEEN);
                result = lineScanner.next().trim();
            } catch (NumberFormatException nfe) {
                System.err.println("Invalid input format!");
                break;
            } catch (NoSuchElementException nse) {
                System.err.println("Wrong number of inputs in a line!");
                break;
            }

            int location = sourceInt % numSlots;
            int lookupVal = history[location];

            factor = setFactor(isLocal, location);

            long key = factor * historyWidth + lookupVal;
            byte b = 0;
            if (map.containsKey(key)) {
                b = map.get(key);
            } else {
                map.put(key, (byte) 0);
                size += (Math.log(numSteps) / Math.log(2));
                b = 0;
            }

            prediction = setTaken(b, numSteps);

            numLines++;

            if (result.equalsIgnoreCase("T")) {
                actual = true;
                if (b < (numSteps - 1)) {
                    b++;
                }
                map.put(key, b);
                lookupVal <<= 1;
                lookupVal++;
                history[location] = lookupVal % historyWidth;
            } else if (result.equalsIgnoreCase("N")) {
                actual = false;
                if (b > 0) {
                    b--;
                }
                map.put(key, b);
                lookupVal <<= 1;
                history[location] = lookupVal % historyWidth;
            } else {
                System.err.println("Invalid result type! Either T or N!");
                break;
            }

            evalPredication(prediction, actual);

            lineScanner.close();

        }

    }

    /*********************************************************************/
    /*************************** Helper Methods **************************/
    /*********************************************************************/

    /**
     * Checks if the inputs to the two-level predictor are valid.
     * @param slotValid boolean of whether slot numbers is valid.
     * @param historyValid boolean of whether history table width is valid.
     * @param typeValid boolean of whether type specification is valid.
     * @param stepsValid boolean of whether number of steps is valid.
     */
    private static void checkTwoLevelValid(boolean slotValid,
                                           boolean historyValid,
                                           boolean typeValid,
                                           boolean stepsValid) {
        if (!slotValid || !historyValid || !typeValid || !stepsValid) {
            System.err.println("Invalid inputs to twolevel predictor!");
            System.exit(1);
        }
    }

    /**
     * Evaluate if the prediction was correct or not.
     * @param prediction The simulator's prediction.
     * @param actual The actual outcome.
     */
    private static void evalPredication(boolean prediction, boolean actual) {
        if (prediction == actual) {
            correctPredictions++;
        } else {
            wrongPredictions++;
        }
    }

    /**
     * Creates a history table given the number of slots.
     * @param numSlots The number of slots for the table.
     * @return the history table initialized with zeros.
     */
    private static int[] makeHistoryTable(int numSlots) {
        int[] history = new int[numSlots];
        for (int i = 0; i < numSlots; i++) {
            history[i] = 0; // initialize with 0s
        }
        return history;
    }

    /**
     * Creates a byte table given the number of slots.
     * @param numSlots The number of slots for the table.
     * @return the byte table initialized with zeros.
     */
    private static byte[] makeByteTable(int numSlots) {
        byte[] table = new byte[numSlots];
        for (int i = 0; i < numSlots; i++) {
            table[i] = 0;
        }
        return table;
    }

    /**
     * Check if the given argument is "local" or "global".
     * @param type The type of two-level predictor we want.
     * @return true or false.
     */
    private static boolean checkLocal(String type) {
        return type.equalsIgnoreCase("local");
    }

    /**
     * Sets the factor based on the simulator being local or global.
     * @param isLocal whether or not the simulator is local.
     * @param location the location to be used if local.
     * @return local location or global 0.
     */
    private static int setFactor(boolean isLocal, int location) {
        return isLocal ? location : 0;
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

    /**
     * Checks validity of type input by user.
     * @param type "local" or "global."
     * @return true if either of above in any case, false else.
     */
    private static boolean checkType(String type) {
        return type.equalsIgnoreCase("local")
            || type.equalsIgnoreCase("global");
    }

    /**
     * Checks validity of number of slots input by user.
     * @param arg String of number of slots.
     * @return True if valid.
     */
    private static boolean checkSlots(String arg) {
        int numSlots = 0;
        //Check if numeric.
        try {
            numSlots = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            System.err.println("Slots not a number!");
            System.exit(1);
        }

        boolean res = true;
        //Check if between 4 and 16.
        if (numSlots < FOUR || numSlots > TWOSIXTEEN) {
            res = false;
        }
        //Check if power of 2.
        if (!isTwosPower(numSlots)) {
            res = false;
        }
        return res;
    }

    /**
     * Checks if the width of the history table input by user is valid.
     * @param arg String representation of history table width.
     * @return True if valid.
     */
    private static boolean checkHistory(String arg) {
        int numHistory = 0;
        //Check if numeric.
        try {
            numHistory = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            System.err.println("Historynum not a number!");
            System.exit(1);
        }
        boolean res = true;
        //Check range.
        if (numHistory < FOUR || numHistory > TWOSIXTEEN) {
            res = false;
        }
        //Check power of two.
        if (!isTwosPower(numHistory)) {
            res = false;
        }
        return res;
    }

    /**
     * Checks if the numSteps for saturation counter input by user is valid.
     * @param arg String rep of number of steps for saturation counter.
     * @return True if valid.
     */
    private static boolean checkSteps(String arg) {
        int numSteps = 0;
        //Check if numeric.
        try {
            numSteps = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            System.err.println("Steps not a number!");
            System.exit(1);
        }
        boolean res = true;
        //Check range.
        if (numSteps < 2 || numSteps > SIXTEEN) {
            res = false;
        }
        //Check power of two.
        if (!isTwosPower(numSteps)) {
            res = false;
        }
        return res;
    }

    /**
     * Checks if a number is a power of 2.
     * @param num the number to be tested.
     * @return true if power of 2.
     */
    private static boolean isTwosPower(int num) {
        return (num & (num - 1)) == 0;
    }

    /**
     * Prints the result of prediction simulation on stdout.
     */
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

    /**
     * Checks if the given args has the correct length.
     * @param args The arguments for this program.
     * @param len The correct length.
     */
    private static void checkValidArgLen(String[] args, int len) {
        if (args.length != len) {
            System.err.println("Wrong number of arguments!");
            System.exit(1);
        }
    }

    /**
     * Determines which simulator to call based on argument.
     * @param args The array of input arguments by user.
     * @param command The type of simulator == args[0].
     * @param xzScanner The scanner for the .xz file.
     */
    private static void executePredictor(String[] args, String command,
                                         Scanner xzScanner) {
        if (command.equalsIgnoreCase("at")) {
            checkValidArgLen(args, 1);
            alwaysTaken(xzScanner);
        } else if (command.equalsIgnoreCase("nt")) {
            checkValidArgLen(args, 1);
            neverTaken(xzScanner);
        } else if (command.equalsIgnoreCase("btfn")) {
            checkValidArgLen(args, 1);
            btfnPredictor(xzScanner);
        } else if (command.equalsIgnoreCase("bimodal")) {
            checkValidArgLen(args, THREE);
            bimodalPredictor(xzScanner, args);
        } else if (command.equalsIgnoreCase("twolevel")) {
            checkValidArgLen(args, FIVE);
            twolevelPredictor(xzScanner, args);
        } else {
            System.err.println("Wrong predictor type!");
            System.exit(1);
        }

        printRes();

    }

    /**
     * Main driver for this program.
     * @param args Input arguments for this program.
     */
    public static void main(String[] args) {
        if (args.length <= 0) {
            System.err.println("You should specify at least one argument!");
            System.exit(1);
        }

        Scanner xzScanner = new Scanner(System.in);
        executePredictor(args, args[0], xzScanner);
        xzScanner.close();
    }
}