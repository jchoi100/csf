/*
 * 600.233 Computer System Fundamentals
 * Name: Joon Hyuck Choi, Neha Kulkarni
 * Assignment 7
 * JHED: jchoi100, nkulkar5
 * email: jchoi100@jhu.edu / nkulkar5@jhu.edu
 * CacheSimulator.java
 */

import java.util.Scanner;
import java.io.BufferedReader;
import java.util.NoSuchElementException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * CacheSimulator class.
 * @author Joon Hyuck Choi
 *
 */
public final class CacheSimulator {

    /** Number of cycels needed to load/save with cache. */
    private static final int CACHE_LS_CYCLE = 1;

    /** Number of cycles needed to load/save with memory. */
    private static final int MEMORY_LS_CYCLE = 100;

    /** Sixteen. */
    private static final int SIXTEEN = 16;

    /** Three. */
    private static final int THREE = 3;

    /** Four. */
    private static final int FOUR = 4;

    /** Five. */
    private static final int FIVE = 5;

    /** Six. */
    private static final int SIX = 6;

    /** Seven. */
    private static final int SEVEN = 7;

    /** Total number of loads. */
    private static int numLoads = 0;

    /** Total number of load hits. */
    private static int numLoadHits = 0;

    /** Total number of load misses. */
    private static int numLoadMisses = 0;

    /** Total number of stores. */
    private static int numStores = 0;

    /** Total number of store hits. */
    private static int numStoreHits = 0;

    /** Total number of store misses. */
    private static int numStoreMisses = 0;

    /** Total number of cycles. */
    private static int numCycles = 0;

    /**
     * Dummy constructor.
     */
    private CacheSimulator() {

    }

    /**
     * Executes simulation.
     * @param args The input arguments
     * @throws IOException if file cannot be opened.
     */
    private static void executeSimulation(String[] args) throws IOException {
        // args[0]: number of sets in the cache (a positive power-of-2)
        // args[1]: number of blocks in each set (a positive power-of-2)
        // args[2]: number of bytes in each block (a positive powof2, atleast 4)
        // args[3]: write-allocate (1) or not (0)
        // args[4]: write-through (1) or write-back (0)
        // args[5]: least-recently-used (1) or FIFO (0) evictions
        // args[6]: input trace file

        // 1. A cache with n sets of 1 block each is essentially direct-mapped.
        // 2. A cache with 1 set of n blocks is essentially fully associative.
        // 3. Cache with n sets of m blocks is essentially m-way set-associative

        // Let's first write the program for case 1 --> should be easiest

        BufferedReader br = null;
        File file = null;

        try {
            file = new File(args[SIX]);
            br = new BufferedReader(new FileReader(file));
        } catch (IOException ioe) {
            System.err.println("File read error!");
            System.exit(1);
        }

        int numSets = Integer.parseInt(args[0]);
        int blocksPerSet = Integer.parseInt(args[1]);
        int bytesPerBlock = Integer.parseInt(args[2]);
        int writeAllocate = Integer.parseInt(args[THREE]);
        int writeThrough = Integer.parseInt(args[FOUR]);
        int lru = Integer.parseInt(args[FIVE]);
        long cacheSize = numSets * blocksPerSet * bytesPerBlock;
        String oneLine = "";

        while ((oneLine = br.readLine()) != null) {
            Scanner lineScanner = new Scanner(oneLine);
            String type = "";
            String address = "";
            String thirdCol = "";
            long addressLong = 0;

            try {
                type = lineScanner.next().trim();
                address = lineScanner.next().trim().substring(2);
                thirdCol = lineScanner.next().trim();
                addressLong = Long.parseLong(address, SIXTEEN);
            } catch (NoSuchElementException nse) {
                System.err.println("Wrong number of fields in trace file!");
                System.exit(1);
            } catch (NumberFormatException nfe) {
                System.err.println("Wrong address format!");
                System.exit(1);
            }

            lineScanner.close();

        }

        br.close();
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
        // double goodPerc = 0;
        // double badPerc = 0;
        // if (numLines != 0) {
        //     goodPerc = (correctPredictions / numLines) * PERCENTAGE;
        //     badPerc = (wrongPredictions / numLines) * PERCENTAGE;
        // }
        // System.out.println("Total " + (long) numLines);
        // System.out.println("Good " + (long) correctPredictions);
        // System.out.println("Bad " + (long) wrongPredictions);
        // System.out.print("Good% ");
        // System.out.printf("%.2f\n", goodPerc);
        // System.out.print("Bad% ");
        // System.out.printf("%.2f\n", badPerc);
        // System.out.println("Size " + (long) size);
    }

    /**
     * Checks if the input file exists and opens.
     * @param fileName The input file name.
     * @return true if file exists.
     * @throws IOException If file does not exist.
     */
    private static boolean fileValid(String fileName) throws IOException {
        try {
            File file = new File(fileName);
            BufferedReader br = new BufferedReader(new FileReader(file));
            br.close();
        } catch (IOException ioe) {
            System.err.println("File does not exist!");
            System.exit(1);
        }
        return true;
    }

    /**
     * Checks if all the input parameters are valid.
     * @param args The input params.
     * @return True if all inputs valid.
     */
    private static boolean allInputValid(String[] args) {
        int numSets = 0;
        int blocksPerSet = 0;
        int bytesPerBlock = 0;
        int writeAlloc = 0;
        int writeThrough = 0;
        int lru = 0;

        try {
            numSets = Integer.parseInt(args[0]);
            blocksPerSet = Integer.parseInt(args[1]);
            bytesPerBlock = Integer.parseInt(args[2]);
            writeAlloc = Integer.parseInt(args[THREE]);
            writeThrough = Integer.parseInt(args[FOUR]);
            lru = Integer.parseInt(args[FIVE]);
        } catch (NumberFormatException nfe) {
            System.err.println("Input number format error!");
            System.exit(1);
        }

        boolean firstThirdValid = firstThirdCheck(numSets, blocksPerSet);
        boolean secondThirdValid = secondThirdCheck(bytesPerBlock, writeAlloc);
        boolean lastThirdValid = lastThirdCheck(writeThrough, lru);
        boolean noWriteAllocWriteBack = (writeAlloc == 0 && writeThrough == 0);

        // Didn't check if user entered lru or FIFO for direct mapped caches.
        // If the configuration is set for a direct mapped cache, but the
        // user entered lru or FIFO, just ignore.

        return firstThirdValid && secondThirdValid && lastThirdValid
                && !noWriteAllocWriteBack;

    }

    /**
     * Checks if the first third of the inputs are valid.
     * @param numSets number of sets in the cache.
     * @param blocksPerSet number of blocks in each set.
     * @return True if both valid. Else false.
     */
    private static boolean firstThirdCheck(int numSets, int blocksPerSet) {
        return isTwosPower(numSets) && isTwosPower(blocksPerSet);
    }

    /**
     * Checks if the second third of the inputs are valid.
     * @param bytesPerBlock number of bytes in each block.
     * @param writeAllocate write-allocate (1) or not (0).
     * @return True if all input valid. Else false.
     */
    private static boolean secondThirdCheck(int bytesPerBlock,
                                            int writeAllocate) {
        return isTwosPower(bytesPerBlock)
                && (writeAllocate == 1 || writeAllocate == 0);
    }

    /**
     * Checks if the last third of the inputs are valid.
     * @param writeThrough write-through (1) or write-back (0).
     * @param lru least-recently-used (1) or FIFO (0) evictions.
     * @return True if all input valid. Else false.
     */
    private static boolean lastThirdCheck(int writeThrough, int lru) {
        return (writeThrough == 1 || writeThrough == 0)
                && (lru == 1 || lru == 0);
    }

    /**
     * Main driver for this program.
     * @param args Input arguments for this program.
     * @throws IOException When file cannot be found.
     */
    public static void main(String[] args) throws IOException {

        // Sample command line input:
        // java CacheSimulator 256 4 16 1 0 1 gcc.trace

        // args[0]: number of sets in the cache (a positive power-of-2)
        // args[1]: number of blocks in each set (a positive power-of-2)
        // args[2]: number of bytes in each block (a pos power-of-2, at least 4)
        // args[3]: write-allocate (1) or not (0)
        // args[4]: write-through (1) or write-back (0)
        // args[5]: least-recently-used (1) or FIFO (0) evictions
        // args[6]: input trace file

        if (args.length != SEVEN) {
            System.err.println("Wrong number of arguments!");
            System.exit(1);
        }

        if (allInputValid(args) && fileValid(args[SIX])) {
            executeSimulation(args);
        } else {
            System.err.println("Invalid input!");
            System.exit(1);
        }
    }
}
