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
import java.util.HashMap;

/**
 * CacheSimulator class.
 * @author Joon Hyuck Choi
 *
 */
public final class CacheSimulator {

    /** Number of cycels needed to load/save with cache. */
    private static final int CACHE_CYCLE = 1;

    /** Number of cycles needed to load/save with memory. */
    private static final int MEMORY_CYCLE = 100;

    /** Sixteen. */
    private static final int SIXTEEN = 16;

    /** Thirty two. */
    private static final int THIRTYTWO = 32;
    
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

    /** Our cache. */
    private static HashMap<Long, CacheSlot> cache =
            new HashMap<>();;

    /**
     * Dummy constructor.
     */
    private CacheSimulator() {

    }

    /**
     * Executes simulation.
     * 1. A cache with n sets of 1 block each is essentially direct-mapped.
     * 2. A cache with 1 set of n blocks is essentially fully associative.
     * 3. Cache with n sets of m blocks is essentially m-way set-associative
     * @param args The input arguments
     * @throws IOException if file cannot be opened.
     */
    private static void executeSimulation(String[] args) throws IOException {
        // Let's first write the program for case 1 --> should be easiest
        File file = new File(args[SIX]);
        BufferedReader br = new BufferedReader(new FileReader(file));

        int numSets = Integer.parseInt(args[0]);
        int blocksPerSet = Integer.parseInt(args[1]);
        int bytesPerBlock = Integer.parseInt(args[2]);
        int writeAllocate = Integer.parseInt(args[THREE]);
        int writeThrough = Integer.parseInt(args[FOUR]);
        int lru = Integer.parseInt(args[FIVE]);
        long cacheSize = numSets * blocksPerSet * bytesPerBlock;
        String oneLine = "";
        
        //Number of bits to take from the address to index to cache.
        int indexLength = (int) (Math.log(numSets) / Math.log(2));
        int tagLength = THIRTYTWO - indexLength;
        
        while ((oneLine = br.readLine()) != null) {
            Scanner lineScanner = new Scanner(oneLine);
            String type = "";
            String address = "";
            String thirdCol = "";
            long addressLong = 0;

            // Try parsing all the information.
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

            // Set mask for getting index.
            long mask = (1 << indexLength) - 1;
            long index = addressLong & mask;

            // Flip mask bits to get mask for getting tag.
            mask = ~mask;
            long tag = (addressLong & mask);
            tag >>= indexLength;

            // Now, we have both the index and tag.

            // Do the actual simulation.
            if (type.equalsIgnoreCase("l")) {
                loadExecute(index, tag, bytesPerBlock);
            } else if (type.equalsIgnoreCase("s")) { // Store case.
                storeExecute(index, tag, bytesPerBlock);
            } else { // error case.
                System.err.println("Wrong command type: l or s!");
                System.exit(1);
            }
            lineScanner.close();
        }
        br.close();
    }

    /**
     * Execution for load case.
     * @param index index into cache.
     * @param tag tag in cache.
     * @param bytesPerBlock Bytes per block.
     */
    private static void loadExecute(long index, long tag, int bytesPerBlock) {
        numLoads++;
        if (cache.containsKey(index)) {
            CacheSlot slot = cache.get(index);
            if (slot.valid && slot.tag == tag) { // hit!
                numLoadHits++;
                numCycles += CACHE_CYCLE * (bytesPerBlock / FOUR);
            } else { // The tag doesn't match!
                numLoadMisses++;
                if (!slot.dirty) {
                    // Not dirty. Just load the new stuff in.
                    cache.get(index).tag = tag;
                    cache.get(index).valid = true;
                    cache.get(index).dirty = false;
                    numCycles += CACHE_CYCLE * (bytesPerBlock / FOUR);
                    numCycles += MEMORY_CYCLE * (bytesPerBlock / FOUR);
                } else {
                    // Need to write the dirty data to memory.
                    // But we don't have data. Just pretend.
                    numCycles += MEMORY_CYCLE * (bytesPerBlock / FOUR);
                    // Now, load the new memory contents in cache.
                    // Change the tag and valid bit.
                    cache.get(index).tag = tag;
                    cache.get(index).valid = true;
                    cache.get(index).dirty = false;
                    numCycles += MEMORY_CYCLE * (bytesPerBlock / FOUR);
                    numCycles += CACHE_CYCLE * (bytesPerBlock / FOUR);
                }
            }
        } else { // No cache for the index whatsoever!
            numLoadMisses++;
            // Need to load data from memory from scratch.
            // Need to insert result into cache.
            numCycles += MEMORY_CYCLE * (bytesPerBlock / FOUR);
            CacheSlot toAdd = new CacheSlot(index, true, tag, 0);
            cache.put(index, toAdd);
            // Then return result in cache to CPU.
            numCycles += CACHE_CYCLE * (bytesPerBlock / FOUR);
        }
    }

    /**
     * Execution for store case.
     * @param index index into cache.
     * @param tag tag in cache.
     * @param bytesPerBlock Bytes per block.
     */
    private static void storeExecute(long index, long tag, int bytesPerBlock) {
        numStores++;
        if (cache.containsKey(index)) { // Something is in cache.
            CacheSlot slot = cache.get(index);
            if (slot.valid && slot.tag == tag) { // What we wanted.
                numStoreHits++;
                /*******DOES THIS SPEND ANY CYLCES??*********/
            } else { // Miss!
                numStoreMisses++;
                cache.get(index).valid = true;
                cache.get(index).dirty = true;
                if (!slot.dirty) { // not dirty
                    // Just read in the new index and tag from
                    // memory and write this new data.
                    // First, fetch the correct slot from memory
                    numCycles += MEMORY_CYCLE * (bytesPerBlock / FOUR);
                    cache.get(index).tag = tag;
                    // Write new data to the thing in cache.
                } else { // slot is dirty
                    // Write this guy to memory first, and then
                    // read block for our address, and then
                    // write the new data.
                    // First, write the dirty block to memory.
                    numCycles += MEMORY_CYCLE * (bytesPerBlock / FOUR);
                    // Second, read the correct block from memory.
                    cache.get(index).tag = tag;
                    numCycles += MEMORY_CYCLE * (bytesPerBlock / FOUR);
                    // Third, write the new stuff in cache.
                }
            }
        } else { // Cache has nothing corresponding to the given index.
            numStoreMisses++;
            // First, read from memory the correct block.
            CacheSlot toAdd = new CacheSlot(index, true, tag, 0);
            cache.put(index, toAdd);
            numCycles += MEMORY_CYCLE * (bytesPerBlock / FOUR);
            // Second, write this to cache.
            cache.get(index).dirty = true;
        }
    }

    /**
     * Prints the result of prediction simulation on stdout.
     */
    private static void printRes() {
         System.out.println("Total loads: " + numLoads);
         System.out.println("Total stores: " + numStores);
         System.out.println("Load hits: " + numLoadHits);
         System.out.println("Load misses: " + numLoadMisses);
         System.out.println("Store hits: " + numStoreHits);
         System.out.println("Store misses: " + numStoreMisses);
         System.out.println("Total cycles: " + numCycles);
    }

    /******************************************************************/
    /** Input validation helper methods. */

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
        return isTwosPower(bytesPerBlock) && bytesPerBlock >= FOUR
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
     * Checks if a number is a power of 2.
     * @param num the number to be tested.
     * @return true if power of 2.
     */
    private static boolean isTwosPower(int num) {
        return (num & (num - 1)) == 0;
    }


    /******************************************************************/
    /** Main method. */

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
            printRes();
        } else {
            System.err.println("Invalid input!");
            System.exit(1);
        }
    }

    private static class CacheSlot {
        private long index;
        private boolean valid;
        private boolean dirty;
        private long tag;
        private long data; //???

        /**
         * Constructor for the CacheSlot class.
         * @param index
         * @param valid
         * @param tag
         * @param data
         */
        CacheSlot(long index, boolean valid, long tag, long data) {
            this.index = index;
            this.dirty = false;
            this.valid = valid;
            this.tag = tag;
            this.data = data;
        }
    }
}
