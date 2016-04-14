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
import java.util.ArrayList;

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

    private String inputFile;

    /** Number of sets. */
    private int sets;

    /** Number of blocks. */
    private int numBlocks;

    /** Number of bytes. */
    private int bytes;

    /** Number of bits required for indexing. */
    private int indexLength;

    /** Booleanized int values from args. */
    private boolean writeAllocate, writeThrough, lru;

    /** Total number of load hits. */
    private int numLoadHits;

    /** Total number of load misses. */
    private int numLoadMisses;

    /** Total number of store hits. */
    private int numStoreHits;

    /** Total number of store misses. */
    private int numStoreMisses;

    /** Number of cycles per memory access. */
    private int memoryCycles;

    /** Total number of cycles. */
    private int numCycles;

    /** Our cache. */
    private ArrayList<HashMap<Integer, Boolean>> cache;

    /** Queues containing/keeping track of next block to evict. */
    private ArrayList<ArrayList<Integer>> evictionQueue;

    /**
     *
     */
    private CacheSimulator(String[] args) {
        this.parseArguments(args);
        this.cache = new ArrayList<>(this.sets);
        this.evictionQueue = new ArrayList<>(this.sets);

        for (int i = 0; i < this.sets; i++) {
            this.cache.add(new HashMap<Integer, Boolean>(this.numBlocks));
            this.evictionQueue.add(new ArrayList<Integer>());
        }

        this.numLoadHits = 0;
        this.numLoadMisses = 0;
        this.numStoreHits = 0;
        this.numStoreMisses = 0;
        this.numCycles = 0;
        this.indexLength = (int) (Math.log(this.sets) / Math.log(2));
        this.memoryCycles = this.bytes / 2 / 2 * MEMORY_CYCLE;
    }

    /**
     *
     * @param addressLong
     * @return
     */
    private Address parseAddress(int addressLong) {

        // Set mask for getting index.
        int mask = (1 << this.indexLength) - 1;
        int index = addressLong & mask;

        // Flip mask bits to get mask for getting tag.
        mask = ~mask;
        int tag = (addressLong & mask);
        tag >>= this.indexLength;

        return new Address(index, tag);
    }

    /**
     * Executes simulation.
     * 1. A cache with n sets of 1 block each is essentially direct-mapped.
     * 2. A cache with 1 set of n blocks is essentially fully associative.
     * 3. Cache with n sets of m blocks is essentially m-way set-associative
     * @param args The input arguments
     * @throws IOException if file cannot be opened.
     */
    private void executeSimulation() throws IOException {

        File file = new File(this.inputFile);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String oneLine = "";

        while ((oneLine = br.readLine()) != null) {
            Scanner lineScanner = new Scanner(oneLine);
            String type = "";
            String addressStr = "";
            String thirdCol = "";
            int addressLong = 0;

            // Try parsing all the information.
            try {
                type = lineScanner.next().trim();
                addressStr = lineScanner.next().trim().substring(2);
                thirdCol = lineScanner.next().trim();
                addressLong = Integer.parseInt(addressStr, SIXTEEN);
            } catch (NoSuchElementException nse) {
                System.err.println("Wrong number of fields in trace file!");
                System.exit(1);
            } catch (NumberFormatException nfe) {
                System.err.println("Wrong address format!");
                System.exit(1);
            }

            Address address = this.parseAddress(addressLong);

            // Do the actual simulation.
            if (type.equalsIgnoreCase("l")) {
                this.loadExecute(address);
            } else if (type.equalsIgnoreCase("s")) { // Store case.
                this.storeExecute(address);
            } else { // error case.
                System.err.println("Wrong command type: l or s!");
                System.exit(1);
            }
            lineScanner.close();
        }
        br.close();
    }

    /**
     *
     * @param toAdd
     * @param isDirty
     */
    private void addToCache(Address toAdd, boolean isDirty) {
        if (this.cache.get(toAdd.index).size() == this.numBlocks) {
            this.evict(toAdd.index);
        }
        this.evictionQueue.get(toAdd.index).add(toAdd.tag);
        this.cache.get(toAdd.index).put(toAdd.tag, isDirty);
    }

    /**
     *
     * @param la
     */
    private void updateLRUQueue(Address la) {
        ArrayList<Integer> q = this.evictionQueue.get(la.index);
        int position = q.indexOf(la.tag);
        q.add(q.remove(position));
    }

    /**
     *
     * @param index
     */
    private void evict(int index) {
        ArrayList<Integer> q = this.evictionQueue.get(index);
        boolean dirty = this.cache.get(index).remove(q.remove(0));
        if (dirty) {
            this.numCycles += this.memoryCycles;
        }
    }

    /**
     *
     * @param address
     */
    private void setDirty(Address address) {
        if (!this.cache.get(address.index).get(address.tag)) {
            this.cache.get(address.index).remove(address.tag);
            this.cache.get(address.index).put(address.tag, true);
        }
    }

    /**
     * Execution for load case.
     * @param index index into cache.
     * @param tag tag in cache.
     * @param bytesPerBlock Bytes per block.
     */
    private void loadExecute(Address la) {
        this.numCycles += CACHE_CYCLE;
        if (this.cache.get(la.index).containsKey(la.tag)) {
            this.numLoadHits++;
            if (this.lru) {
                this.updateLRUQueue(la);
            }
        } else {
            this.numLoadMisses++;
            this.numCycles += MEMORY_CYCLE;
            this.addToCache(la, false);
        }
    }

    /**
     * Execution for store case.
     * @param index index into cache.
     * @param tag tag in cache.
     * @param bytesPerBlock Bytes per block.
     */
    private void storeExecute(Address sa) {
        if (this.cache.get(sa.index).containsKey(sa.tag)) {
            this.numStoreHits++;
            this.numCycles += CACHE_CYCLE;
            if (this.lru) {
                this.updateLRUQueue(sa);
            }
            if (this.writeThrough) {
                this.numCycles += MEMORY_CYCLE;
            } else {
                this.setDirty(sa);
            }
        } else {
            this.numStoreMisses++;
            if (this.writeAllocate && this.writeThrough) {
                // write allocate, write through
                this.addToCache(sa, false);
                this.numCycles += this.memoryCycles * 2 + CACHE_CYCLE;
            } else if (this.writeAllocate && !this.writeThrough) {
                // write allocate, write back
                this.addToCache(sa, true); // mark dirty
                this.numCycles += this.memoryCycles + CACHE_CYCLE;
            } else {
                // no write allocate, write through
                this.numCycles += this.memoryCycles;
            }
        }
    }

    /**
     *
     * @author user01
     *
     */
    private class Address {
        private int index;
        private int tag;

        /**
         *
         * @param i
         * @param t
         */
        Address(int i, int t) {
            this.index = i;
            this.tag = t;
        }

    }

    /**
     * Prints the result of prediction simulation on stdout.
     */
    private void printRes() {
        int totalLoads = this.numLoadHits + this.numLoadMisses;
        int totalStores = this.numLoadHits + this.numLoadMisses;

        System.out.println("Total loads: " + totalLoads);
        System.out.println("Total stores: " + totalStores);
        System.out.println("Load hits: " + this.numLoadHits);
        System.out.println("Load misses: " + this.numLoadMisses);
        System.out.println("Store hits: " + this.numStoreHits);
        System.out.println("Store misses: " + this.numStoreMisses);
        System.out.println("Total cycles: " + this.numCycles);
    }

    /**
     * Checks if the input file exists and opens.
     * @param fileName The input file name.
     * @return true if file exists.
     * @throws IOException If file does not exist.
     */
    private boolean fileValid() throws IOException {
        try {
            File file = new File(this.inputFile);
            BufferedReader br = new BufferedReader(new FileReader(file));
            br.close();
        } catch (IOException ioe) {
            System.err.println("File does not exist!");
            System.exit(1);
        }
        return true;
    }

    /**
     *
     * @param args
     */
    private void parseArguments(String[] args) {
        try {
            this.sets = Integer.parseInt(args[0]);
            this.numBlocks = Integer.parseInt(args[1]);
            this.bytes = Integer.parseInt(args[2]);
            this.writeAllocate = this.stringToBool(args[THREE]);
            this.writeThrough = this.stringToBool(args[FOUR]);
            this.lru = this.stringToBool(args[FIVE]);
            this.inputFile = args[SIX];
        } catch (NumberFormatException nfe) {
            System.err.println("Input arguments number format error!");
            System.exit(1);
        }
    }

    /**
     * Checks if all the input parameters are valid.
     * If all the checks pass, the results will have
     * been saved in their corresponding member variables.
     * @param args The input params.
     * @return True if all inputs valid.
     */
    private boolean allInputValid() {
        return this.sets > 0 && this.numBlocks > 0
               && this.isTwosPower(this.sets)
               && this.isTwosPower(this.numBlocks)
               && this.bytes >= FOUR
               && this.isTwosPower(this.bytes)
               && (this.writeAllocate || this.writeThrough);
    }

    private boolean stringToBool(String s) {
        if (s.equals("1")) {
            return true;
        } else if (s.equals("0")) {
            return false;
        } else {
            System.err.println("Wrong number format for "
                                + "writeAllocate, writeThrough, "
                                + "and lru/FIFO!");
            System.exit(1);
            return false;
        }
    }

    /**
     * Checks if a number is a power of 2.
     * @param num the number to be tested.
     * @return true if power of 2.
     */
    private boolean isTwosPower(int num) {
        return (num & (num - 1)) == 0;
    }

    /**
     * Main driver for this program.
     * @param args Input arguments for this program.
     * @throws IOException When file cannot be found.
     */
    public static void main(String[] args) throws IOException {

        if (args.length != SEVEN) {
            System.err.println("Wrong number of arguments!");
            System.exit(1);
        }

        CacheSimulator cs = new CacheSimulator(args);

        if (cs.allInputValid() && cs.fileValid()) {
            cs.executeSimulation();
            cs.printRes();
        } else {
            System.err.println("Invalid input!");
            System.exit(1);
        }
    }

}