import java.util.NoSuchElementException;
import java.util.Scanner;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * 600.233 Computer Systems Fundamentals.
 * Assignment 6
 * @author Joo Chang Lee
 * jlee381@jhu.edu
 * BranchSim.java
 */
public final class BranchSim {

    static int count = 0;
    static int predictTrue = 0;
    static int predictFalse = 0;
    static long size = 0;

    static final int THREE = 3;
    static final int FOUR = 4;
    static final int FIVE = 5;
    static final double PERCENTAGE = 100.0;
    static final int HEX = 16;
    static final int TWOTOSIXTEEN = 65536;

    private BranchSim() {

    }

    /**
     * Main method.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        DecimalFormat df2 = new DecimalFormat("0.00");

        if (args.length == 0) {
            System.err.println("No arguments");
            System.exit(1);
        }
        String command = args[0];
        Scanner sc = new Scanner(System.in);

        execute(args, command, sc);

        printOutput(df2);

        sc.close();
    }

    /**
     * Executes BranchSim depending on input.
     * @param args command line arguments
     * @param command the execution type
     * @param sc the scanner for System.in
     */
    public static void execute(String[] args, String command, Scanner sc) {
        if (command.equals("at")) {
            at(sc);
        } else if (command.equals("nt")) {
            nt(sc);
        } else if (command.equals("btfn")) {
            btfn(sc);
        } else if (command.equals("bimodal")) {
            int slots = 0;
            int steps = 0;
            if (args.length < THREE) {
                System.err.println("Invalid arguments.");
                System.exit(1);
            }
            try {
                slots = Integer.parseInt(args[1]);
                steps = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.err.println("Not a number");
                System.exit(1);
            }
            bimodal(sc, slots, steps);
        } else if (command.equals("twolevel")) {
            if (args.length < FIVE) {
                System.err.println("Invalid arguments.");
                System.exit(1);
            }
            int slots = 0;
            int historypatterns = 0;
            String type = "";
            int steps = 0;
            try {
                slots = Integer.parseInt(args[1]);
                historypatterns = Integer.parseInt(args[2]);
                type = args[THREE];
                steps = Integer.parseInt(args[FOUR]);
            } catch (NumberFormatException e) {
                System.err.println("Not a number");
                System.exit(1);
            }
            twolevel(sc, slots, historypatterns, type, steps);
        } else {
            System.err.println("Invalid argument");
            System.exit(1);
        }
    }

    /**
     * Always taken method.
     * @param sc scanner for System.in
     */
    public static void at(Scanner sc) {
        String line = "";
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            if (line.trim().equals("")) {
                break;
            }
            Scanner sc2 = new Scanner(line);
            String read = "";
            try {
                read = sc2.next();
                read = sc2.next();
                read = sc2.nextLine();
            } catch (NumberFormatException num) {
                System.err.println("Not a valid number input");
                break;
            } catch (NoSuchElementException no) {
                System.err.println("Format error");
                break;
            }
            count++;
            if (read.trim().equals("T")) {
                predictTrue++;
            } else if (read.trim().equals("N")) {
                predictFalse++;
            } else {
                System.err.println("Should not reach here. Invalid T/F value.");
                break;
            }
            sc2.close();
        }
    }

    /**
     * Never taken method.
     * @param sc scanner for System.in
     */
    public static void nt(Scanner sc) {
        String line = "";
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            if (line.trim().equals("")) {
                break;
            }
            Scanner sc2 = new Scanner(line);
            String read = "";
            try {
                read = sc2.next();
                read = sc2.next();
                read = sc2.nextLine();
            } catch (NumberFormatException num) {
                System.err.println("Not a valid number input");
                break;
            } catch (NoSuchElementException no) {
                System.err.println("Format error");
                break;
            }
            count++;
            if (read.trim().equals("T")) {
                predictFalse++;
            } else if (read.trim().equals("N")) {
                predictTrue++;
            } else {
                System.err.println("Should not reach here. Invalid T/F value.");
                break;
            }
            sc2.close();
        }
    }

    /**
     * Backwards taken, forwards not taken method.
     * @param sc scanner for System.in
     */
    public static void btfn(Scanner sc) {
        String line = "";
        boolean taken = true;
        boolean actual = true;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            if (line.trim().equals("")) {
                break;
            }
            Scanner sc2 = new Scanner(line);
            String read = "";
            int fromInt = 0;
            int toInt = 0;
            try {
                read = sc2.next();
                fromInt = Integer.parseInt(read, HEX);
                read = sc2.next();
                toInt = Integer.parseInt(read, HEX);
                read = sc2.nextLine();
            } catch (NumberFormatException num) {
                System.err.println("Not a valid number input");
                break;
            } catch (NoSuchElementException no) {
                System.err.println("Format error");
                break;
            }

            if (fromInt > toInt) {
                taken = true;
            } else {
                taken = false;
            }
            count++;
            if (read.trim().equals("T")) {
                actual = true;
            } else if (read.trim().equals("N")) {
                actual = false;
            } else {
                System.err.println("Should not reach here.");
                break;
            }

            checkTrue(taken, actual);

            sc2.close();
        }
    }

    /**
     * Bimodal predictor method.
     * @param sc scanner for System.in
     * @param slots number of slots in the predictor
     * @param steps number of saturation counter
     */
    public static void bimodal(Scanner sc, int slots, int steps) {
        String line = "";

        boolean validSlots;
        boolean validSteps;
        validSlots = largeValid(slots);
        validSteps = smallValid(steps);

        validCheck(validSlots);
        validCheck(validSteps);

        byte[] table = new byte[slots];
        for (int i = 0; i < slots; i++) {
            table[i] = 0;
        }

        boolean taken = true;
        boolean actual = true;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            if (line.trim().equals("")) {
                break;
            }
            Scanner sc2 = new Scanner(line);
            String read = "";
            int fromInt = 0;
            try {
                read = sc2.next();
                fromInt = Integer.parseInt(read, HEX);
                read = sc2.next();
                read = sc2.nextLine();
            } catch (NumberFormatException num) {
                System.err.println("Not a valid number input");
                break;
            } catch (NoSuchElementException no) {
                System.err.println("Format error");
                break;
            }

            int location = fromInt % slots;
            byte b = table[location];
            taken = setTaken(b, steps);

            count++;
            if (read.trim().equals("T")) {
                actual = true;
                if (table[location] < (steps - 1)) {
                    table[location]++;
                }
            } else if (read.trim().equals("N")) {
                actual = false;
                if (table[location] > 0) {
                    table[location]--;
                }
            } else {
                System.err.println("Should not reach here. Invalid T/F value.");
                break;
            }

            checkTrue(taken, actual);

            sc2.close();
        }
        size = slots * (int) (Math.log(steps) / Math.log(2));
    }

    /**
     * Two-level predictor method.
     * @param sc scanner for System.in
     * @param slots number of slots in the predictor
     * @param historypatterns number of patterns to keep track of
     * @param type type of two level predictor (local / global)
     * @param steps number of saturation counter
     */
    public static void twolevel(Scanner sc, int slots,
            int historypatterns, String type, int steps) {
        String line = "";
        boolean local = checkLocal(type);

        boolean validSlots;
        boolean validHistory;
        boolean validSteps;
        validSlots = largeValid(slots);
        validHistory = largeValid(historypatterns);
        validSteps = smallValid(steps);

        validCheck(validSlots);
        validCheck(validHistory);
        validCheck(validSteps);

        int[] history = null;
        history = createHistory(history, slots, historypatterns);

        HashMap<Long, Byte> map = new HashMap<Long, Byte>();

        String read = "";
        int factor = 0;
        boolean taken = true;
        boolean actual = true;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            if (line.trim().equals("")) {
                break;
            }
            Scanner sc2 = new Scanner(line);
            int fromInt = 0;
            try {
                read = sc2.next();
                fromInt = Integer.parseInt(read, HEX);
                read = sc2.next();
                read = sc2.nextLine();
            } catch (NumberFormatException num) {
                System.err.println("Not a valid number input");
                break;
            } catch (NoSuchElementException no) {
                System.err.println("Format error");
                break;
            }

            int location = fromInt % slots;
            int lookup = history[location];

            factor = setFactor(local, location);

            long searchKey = factor * historypatterns + lookup;
            byte b = 0;
            if (map.containsKey(searchKey)) {
                b = map.get(searchKey);
            } else {
                map.put(searchKey, (byte) 0);
                size += (int) (Math.log(steps) / Math.log(2));
                b = 0;
            }

            taken = setTaken(b, steps);

            count++;
            if (read.trim().equals("T")) {
                actual = true;
                if (b < (steps - 1)) {
                    b++;
                }
                map.put(searchKey, b);
                lookup *= 2;
                lookup++;
                history[location] = lookup % historypatterns;
            } else if (read.trim().equals("N")) {
                actual = false;
                if (b > 0) {
                    b--;
                }
                map.put(searchKey, b);
                lookup *= 2;
                history[location] = lookup % historypatterns;
            } else {
                System.err.println("Should not reach here. Invalid T/F value.");
                break;
            }

            checkTrue(taken, actual);
            sc2.close();
        }

    }

    /**
     * Quits program if the command line input numbers are invalid.
     * @param valid the boolean value of validity of input numbers
     */
    public static void validCheck(boolean valid) {
        if (!valid) {
            System.err.println("Wrong value.");
            System.exit(1);
        }
    }

    /**
     * Checks if the number is power of two between 16 and 65536.
     * @param num the number to check
     * @return true if the number satisfies conditions
     */
    public static boolean largeValid(int num) {
        if (num >= FOUR && num <= TWOTOSIXTEEN) {
            if ((num & (num - 1)) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the number is power of two between 2 and 16.
     * @param steps the number to check
     * @return true if the number satisfies conditions
     */
    public static boolean smallValid(int steps) {
        if (steps >= 2 && steps <= HEX) {
            if ((steps & (steps - 1)) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the prediction was true or not
     * and increments counter accordingly.
     * @param taken the prediction on taken / not taken
     * @param actual the actual taken / not taken
     */
    public static void checkTrue(boolean taken, boolean actual) {
        if (taken == actual) {
            predictTrue++;
        } else {
            predictFalse++;
        }
    }

    /**
     * Set the factor value, which will be the adjustment factor
     * for global / local two level predictor.
     * @param local whether the predictor is local or not
     * @param location the location to search
     * @return 0 if global, location if local
     */
    public static int setFactor(boolean local, int location) {
        int factor = 0;
        if (local) {
            factor = location;
        }
        return factor;
    }

    /**
     * Set taken depending on the saturation counter.
     * @param b The saturation counter
     * @param steps the number of steps for the saturation counter
     * @return true if decide to take the branch
     */
    public static boolean setTaken(byte b, int steps) {
        boolean taken = true;
        if (b < (steps / 2)) {
            taken = false;
        }
        return taken;
    }

    /**
     * Check for the type of two level predictor.
     * @param type the type from command line input
     * @return true if the type specified for two level predictor is local
     */
    public static boolean checkLocal(String type) {
        if (type.equalsIgnoreCase("local")) {
            return true;
        } else if (type.equalsIgnoreCase("global")) {
            return false;
        } else {
            System.err.println("Wrong type.");
            System.exit(1);
            return false;
        }
    }

    /**
     * Creates a table of history.
     * @param array the array to initialize
     * @param slots the number of slots in the predictor
     * @param historypatterns number of patterns to keep track of
     * @return the array created
     */
    public static int[] createHistory(int[] array,
            int slots, int historypatterns) {
        int[] history = new int[slots];
        for (int i = 0; i < slots; i++) {
            history[i] = 0;
        }
        size += slots * (int) (Math.log(historypatterns) / Math.log(2));
        return history;
    }

    /**
     * Prints the output.
     * @param df2 the 2-decimal format
     */
    public static void printOutput(DecimalFormat df2) {
        double truePercentage = 0;
        double falsePercentage = 0;
        if (count != 0) {
            truePercentage = (predictTrue * PERCENTAGE) / count;
            falsePercentage = (predictFalse * PERCENTAGE) / count;
        }
        System.out.println("Total " + count);
        System.out.println("Good " + predictTrue);
        System.out.println("Bad " + predictFalse);
        System.out.println("Good% "
                + df2.format(truePercentage));
        System.out.println("Bad% "
                + df2.format(falsePercentage));
        System.out.println("Size " + size);
    }
}
