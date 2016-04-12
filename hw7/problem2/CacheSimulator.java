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

/**
 * CacheSimulator class.
 * @author Joon Hyuck Choi
 *
 */
public final class CacheSimulator {

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
        Scanner traceScanner = new Scanner(args[6]);
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

    private static boolean allInputValid(String[] args) {
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
    	} catch (FileNotFoundException fnfe) {
     		System.err.println("Input file not found!");
    		System.exit(1);   		
    	}

    	
    	boolean numSetsPowTwo = isTwosPower(numSets);
    	boolean numBlocksPowTwo = isTwosPower(blocksPerSet);
    	boolean numBytesPowTwo = isTwosPower(bytesPerBlock);
    	boolean writeAllocateValid = (writeAllocate == 1 || writeAllocate == 0);
    	boolean writeThroughValid = (writeThrough == 1 || writeThrough == 0);
    	boolean writeAllocateAndWriteBack = (writeAllocate == 1 && writeThrough == 0);
    	boolean lruFIFOValid = (lru == 1 || lru == 0);

    	// Didn't check if user entered lru or FIFO for direct mapped caches.
    	// If the configuration is set for a direct mapped cache, but the 
    	// user entered lru or FIFO, just ignore.

    	if (numSetsPowTwo && numBlocksPowTwo && numBytesPowTwo
    		&& writeAllocateValid && writeThroughValid 
    		&& !writeAllocateAndWriteBack && lruFIFOValid) {
    		return true;
    	} else {
    		return false;
    	}

    }

    /**
     * Main driver for this program.
     * @param args Input arguments for this program.
     */
    public static void main(String[] args) {

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
            System.err.println("You should specify at least one argument!");
            System.exit(1);
        }

        if (allInputValid(args)) {
        	executeSimulation(args);
        } else {
        	System.err.println("You should specify at least one argument!");
        	System.exit(1);
        }
    }
}