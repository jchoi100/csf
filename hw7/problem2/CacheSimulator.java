/*
 * 600.233 Computer System Fundamentals
 * Name: Joon Hyuck Choi, Neha Kulkarni
 * Assignment 7
 * JHED: jchoi100, nkulkar5
 * email: jchoi100@jhu.edu / nkulkar5@jhu.edu
 * CacheSimulator.java
 */

import java.util.Scanner;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.io.FileNotFoundException;

/**
 * CacheSimulator class.
 * @author Joon Hyuck Choi
 *
 */
public final class CacheSimulator {

    private static final int CACHE_LS_CYCLE = 1;

    private static final int MEMORY_LS_CYCLE = 100;

    private static final int SIXTEEN = 16;

    private static int numLoads = 0;

    private static int numLoadHits = 0;

    private static int numLoadMisses = 0;

    private static int numStores = 0;

    private static int numStoreHits = 0;

    private static int numStoreMisses = 0;

    private static int numCycles = 0;

    /**
     * Dummy constructor.
     */
    private CacheSimulator() {

    }

    private static void executeSimulation(String[] args) {
    	// args[0]: number of sets in the cache (a positive power-of-2)
    	// args[1]: number of blocks in each set (a positive power-of-2)
    	// args[2]: number of bytes in each block (a positive power-of-2, at least 4)
    	// args[3]: write-allocate (1) or not (0)
    	// args[4]: write-through (1) or write-back (0)
    	// args[5]: least-recently-used (1) or FIFO (0) evictions
    	// args[6]: input trace file

        // 1. A cache with n sets of 1 block each is essentially direct-mapped.
        // 2. A cache with 1 set of n blocks is essentially fully associative.
        // 3. A cache with n sets of m blocks is essentially m-way set-associative. 

        // Let's first write the program for case 1 --> should be easiest

        int numSets = Integer.parseInt(args[0]);
        int blocksPerSet = Integer.parseInt(args[1]);
        int bytesPerBlock = Integer.parseInt(args[2]);
        int writeAllocate = Integer.parseInt(args[3]);
        int writeThrough = Integer.parseInt(args[4]);
        int lru = Integer.parseInt(args[5]);
        Scanner traceScanner = new Scanner(args[6]);

        long cacheSize = numSets * blocksPerSet * bytesPerBlock;

        while (traceScanner.hasNextLine()) {
            String oneLine = traceScanner.nextLine();
            Scanner lineScanner = new Scanner(oneLine);
            String type = "";
            String address = "";
            String thirdCol = "";
            int addressInt = 0;

            try {
                type = lineScanner.next().trim();
                address = lineScanner.next().trim().substring(2);
                thirdCol = lineScanner.next().trim();
                System.out.println(type);
                System.out.println(address);
                addressInt = Integer.parseInt(address, SIXTEEN);
            } catch (NoSuchElementException nse) {
                System.err.println("Wrong number of fields in trace file!");
                System.exit(1);
            } catch (NumberFormatException nfe) {
                System.err.println("Wrong address format!");
                System.exit(1);
            }

            lineScanner.close();

        }

        traceScanner.close();    	
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

    private static boolean allInputValid(String[] args) throws FileNotFoundException {
    	int numSets = 0;
    	int blocksPerSet = 0;
    	int bytesPerBlock = 0;
    	int writeAllocate = 0;
    	int writeThrough = 0;
    	int lru = 0;

    	try {
    		numSets = Integer.parseInt(args[0]);
    		blocksPerSet = Integer.parseInt(args[1]);
    		bytesPerBlock = Integer.parseInt(args[2]);
    		writeAllocate = Integer.parseInt(args[3]);
    		writeThrough = Integer.parseInt(args[4]);
    		lru = Integer.parseInt(args[5]);
    		Scanner traceScanner = new Scanner(args[6]);
    	} catch (NumberFormatException nfe) {
    		System.err.println("Input number format error!");
    		System.exit(1);
    	}
    	
    	boolean numSetsPowTwo = isTwosPower(numSets);
    	boolean numBlocksPowTwo = isTwosPower(blocksPerSet);
    	boolean numBytesPowTwo = isTwosPower(bytesPerBlock);
    	boolean writeAllocateValid = (writeAllocate == 1 || writeAllocate == 0);
    	boolean writeThroughValid = (writeThrough == 1 || writeThrough == 0);
    	boolean noWriteAllocateAndWriteBack = (writeAllocate == 0 && writeThrough == 0);
    	boolean lruFIFOValid = (lru == 1 || lru == 0);

    	// Didn't check if user entered lru or FIFO for direct mapped caches.
    	// If the configuration is set for a direct mapped cache, but the 
    	// user entered lru or FIFO, just ignore.

        // System.out.println("numSetsPowTwo: " + numSetsPowTwo);
        // System.out.println("numBlocksPowTwo: " + numBlocksPowTwo);
        // System.out.println("numBytesPowTwo: " + numBytesPowTwo);
        // System.out.println("writeAllocateValid: " + writeAllocateValid);
        // System.out.println("writeThroughValid: " + writeThroughValid);
        // System.out.println("!noWriteAllocateAndWriteBack: " + !noWriteAllocateAndWriteBack);
        // System.out.println("lruFIFOValid: " + lruFIFOValid);



    	if (numSetsPowTwo && numBlocksPowTwo && numBytesPowTwo
    		&& writeAllocateValid && writeThroughValid 
    		&& !noWriteAllocateAndWriteBack && lruFIFOValid) {
    		return true;
    	} else {
    		return false;
    	}

    }

    /**
     * Main driver for this program.
     * @param args Input arguments for this program.
     */
    public static void main(String[] args) throws FileNotFoundException {

    	// Sample command line input:
    	// java CacheSimulator 256 4 16 1 0 1 gcc.trace

    	// args[0]: number of sets in the cache (a positive power-of-2)
    	// args[1]: number of blocks in each set (a positive power-of-2)
    	// args[2]: number of bytes in each block (a positive power-of-2, at least 4)
    	// args[3]: write-allocate (1) or not (0)
    	// args[4]: write-through (1) or write-back (0)
    	// args[5]: least-recently-used (1) or FIFO (0) evictions
    	// args[6]: input trace file
    
        if (args.length != 7) {
            System.err.println("Wrong number of arguments!");
            System.exit(1);
        }

        if (allInputValid(args)) {
            executeSimulation(args);
        } else {
            System.err.println("Invalid input!");
            System.exit(1);
        }
    }
}
