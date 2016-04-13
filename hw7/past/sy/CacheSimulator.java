/**
 * 600.233 Computer Systems Fundamentals
 * Names: Jae Won Lee, Ju Young Ahn
 * JHED ID: jlee392, jahn29
 * Emails: jlee392@jhu.edu, jahn29@jhu.edu
 * Assignment 7: The Power of Caches
 * Problem 2: Cache Simulation
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * CacheSimulator class.
 */
public class CacheSimulator {
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;
    private static final int SIX = 6;
    private static final int SEVEN = 7;

    // Cache
    private ArrayList<HashMap<Integer, Integer>> cache;
    // List of queues to determine next block to evict
    private ArrayList<LinkedList<Integer>> queues;
    // number of sets, blocks, bytes
    // number of bits required for set indexing
    private int sets, blocks, bytes, index;
    // number of load hits, load misses, store hits, and store misses
    private int lHit, lMiss, sHit, sMiss;
    // number of cycles per memory access, total number of cycles
    private int memCycles, numCycles;
    // boolean of write-allocate write-through, and LRU (0 or 1)
    private boolean wAlloc, wThru, lru;


    // number of cycles necessary to transfer 4 bytes of data
    private final int cyclesMove4byte = 100;

    // A wrapper class Address.
    private class Address {
        private int set;
        private int tag;

        public Address(int setNum, int blockTag) {
            this.set = setNum;
            this.tag = blockTag;
        }

        public int getSet() {
            return this.set;
        }

        public int getTag() {
            return this.tag;
        }
    }

    /**
     * This function creates cache simulator and
     * initialize cache variables.
     * @param args program arguments: 0 is the number of sets,
            1 is the number of blocks in each set,
            2 is the number of bytes in each block,
            3 is write-allocate (1) or not (0),
            4 is write-through (1) or write-back(0),
            5 is least-recently-used (LRU, 1) or FIFO (0) evictions
     */
    public CacheSimulator(String[] args) {
        // Checks argument errors
        this.checkArguments(args);
        this.cache = new ArrayList<HashMap<Integer, Integer>>(this.sets);
        this.queues = new ArrayList<LinkedList<Integer>>(this.sets);

        // Initialize
        for (int i = 0; i < this.sets; i++) {
            this.cache.add(new HashMap<Integer, Integer>(this.blocks));
            this.queues.add(new LinkedList<Integer>());
        }

        // Initialize counters to all zero.
        this.lHit = 0;
        this.lMiss = 0;
        this.sHit = 0;
        this.sMiss = 0;
        this.numCycles = 0;
        this.index = (int) (Math.log(this.sets) / Math.log(2));
        this.memCycles = this.bytes / FOUR * this.cyclesMove4byte;
    }

    /**
     *  * This function is the main method.
     *  * @param args is the input program arguments
     */
    public static void main(String[] args) {
        CacheSimulator cacheSimulator = new CacheSimulator(args);
        cacheSimulator.simulate(args[SIX]);
        cacheSimulator.printStats();
    }

    /**
     * This function goes through memory access trace
     * and simulate according cache behavior.
     * @param fname is the name of the memory access trace file.
     */
    public void simulate(String fname) {
        Scanner tr = new Scanner("");
        try {
            tr = new Scanner(new File(fname));
        } catch (FileNotFoundException e) {
            System.out.println("No file found.");
            System.exit(1);
        }
        int line = 1;   // keeps track of the line number in the trace

        while (tr.hasNextLine()) {
            String cmd = tr.next();
            if (tr.hasNext()) {
                try {
                    Address address = this.parseAddress(tr.next());
                    if (cmd.equals("s")) {
                        this.store(address);
                    } else if (cmd.equals("l")) {
                        this.load(address);
                    } else {
                        System.out.println("At line "
                            + line + ", wrong trace format.");
                        System.exit(1);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("At line "
                        + line + ", wrong address.");
                    System.exit(1);
                }
            } else {
                System.out.println("At line" + line + ", wrong trace.");
                System.exit(1);
            }
            tr.nextLine();
            line++;
        }
    }

    /**
     * This function stores the input address to the memory
     * or to the cache by looking at the write-allocate and
     * write-through.
     * @param adr is the input address
     */
    private void store(Address adr) {
        // if address is found, store hit
        if (this.cache.get(adr.getSet()).containsKey(adr.getTag())) {
            this.sHit++;
            this.numCycles++;
            if (this.lru) {     // if LRU is enabled
                LinkedList<Integer> queue = this.queues.get(adr.getSet());
                // Transfer the relevant memory block to the end of queue
                queue.add(queue.remove(queue.indexOf(adr.getTag())));
            }
            // whether write-through or write-allocate
            // if write-through, immediately update the memory

            if (this.wThru) {
                this.numCycles = this.numCycles + this.memCycles;
            } else { // if not, just mark dirty block
                if (this.cache.get(adr.getSet()).get(adr.getTag()) == 0) {
                    this.cache.get(adr.getSet()).remove(adr.getTag());
                    this.cache.get(adr.getSet()).put(adr.getTag(), 1);
                }
            }
        // if address is not found, store miss
        } else {
            this.sMiss++;
            if (this.wAlloc && this.wThru) {
                // if write-allocate and write-through,
                // from memory into cache, and write to memory
                this.add(adr.getSet(), adr.getTag(), 0);
                this.numCycles = this.numCycles + (2 * this.memCycles + 1);
            } else if (this.wAlloc) {
                // if write-allocate
                // from memory into cache, and mark dirty
                this.add(adr.getSet(), adr.getTag(), 1);
                this.numCycles = this.numCycles + (this.memCycles + 1);
            } else { // else, only store to memory
                this.numCycles = this.numCycles + this.memCycles;
            }
        }
    }

    /**
     * This function loads the input address from the cache.
     * @param adr is the input address
     */
    private void load(Address adr) {
        this.numCycles++;
        // if this load if found, hit
        if (this.cache.get(adr.getSet()).containsKey(adr.getTag())) {
            this.lHit++;
            if (this.lru) {     // if LRU is enabled
                LinkedList<Integer> queue = this.queues.get(adr.getSet());
                // Transfer the relevant memory block to the end of queue
                queue.add(queue.remove(queue.indexOf(adr.getTag())));
            }
        // if this load is not found, miss
        } else {
            this.lMiss++;
            this.numCycles = this.numCycles + this.memCycles;
            this.add(adr.getSet(), adr.getTag(), 0);
        }
    }

    /**
     * This function adds the input address to the cache.
     * @param set is the set index to add to
     * @param tag is the tag of the address to be added
     * @param value is the value stored in the block.
     */
    private void add(int set, int tag, int value) {
        if (this.cache.get(set).size() == this.blocks) {
            LinkedList<Integer> queue = this.queues.get(set);
            // memory block in the head of queue and the cache is evicted
            if (this.cache.get(set).remove(queue.removeFirst()) == 1) {
                // write to memory first if the block is dirty
                this.numCycles = this.numCycles + this.memCycles;
            }
            //this.evict(set);
        }
        // Add the block to the end of queue
        this.queues.get(set).add(tag);
        // Add a new block to the cache
        this.cache.get(set).put(tag, value);
    }


    /**
     * This function separates the 32-bit address into offset, set index, and
     * tag identifiers.
     * @param adr is the address to be parsed
     * @return an Address object resulting from parsing the input address
     */
    private Address parseAddress(String adr) throws NumberFormatException {
        // Convert hex address to binary string
        adr = String.format("%32s",
                Long.toBinaryString(Long.decode(adr))).replace(' ', '0');
        int offset = (int) (Math.log(this.bytes) / Math.log(2));
        int cut = adr.length() - offset;
        int set = 0;
        // Determine set number
        if (this.index == 0) {
            set = 0;
        } else {
            set = Integer.parseInt(adr.substring(cut
                    - this.index, cut), 2);
        }
        // Determine tag
        int tag = Integer.parseInt(adr.substring(0, cut - this.index), 2);
        return new Address(set, tag);
    }

    /**
     * This function checks the input arguments for errors.
     * @param args is the input program arguments
     * @return an array of parsed arguments in correct order
     */
    private void checkArguments(String[] args) {
        if (args.length != SEVEN) {
            System.out.println("Wrong number of arguments.");
            System.exit(1);
        }
        try {
            this.sets = Integer.parseInt(args[0]);
            // Check whether the set is at least one
            atleastone(this.sets, "sets");
            // Check whether the set is a power of 2 number.
            poweroftwosets(this.sets);

            this.blocks = Integer.parseInt(args[1]);
            // Check whether the block is at least one
            atleastone(this.blocks, "blocks");
            // Check whether the block is a power of 2 number.
            poweroftwoblocks(this.blocks);

            this.bytes = Integer.parseInt(args[2]);
            if (this.bytes < FOUR) {
                System.out.println(
                    "Wrong parameter, number of bytes should be at least 4.");
                System.exit(1);
            }

            // Check whether the byte is a power of 2 number.
            poweroftwobytes(this.bytes);

            this.wAlloc = CacheSimulator.str2bool(args[THREE]);
            this.wThru = CacheSimulator.str2bool(args[FOUR]);
            this.lru = CacheSimulator.str2bool(args[FIVE]);
            if (!this.wAlloc && !this.wThru) {
                System.out.println(
                    "Wrong, you cannot select "
                    + "both no-write-allocate and write-back.");
                System.exit(1);
            }
        } catch (NumberFormatException e) {
            System.out.println(
                "Wrong parameter, the first six arguments should be integers.");
            System.exit(1);
        }
    }

    /**
     *
     * @param test is the number of sets or blocks
     * @param type is either sets or blocks
     */
    private static void atleastone(int test, String type) {
        if (test < 1) {
            System.out.println(
                "Wrong parameter, number of " + type
                + " should be at least 1.");
            System.exit(1);
        }
    }

    /**
     * This function checks whether the number of set is integer power-of-two.
     * @param test is the number of sets
     */
    private static void poweroftwosets(int test) {
        int remainder = 0;
        while (test != 1) {
            remainder = test % 2;
            if (remainder != 0) {
                System.out.println("The number of sets "
                        + "is not a power of 2.");
                System.exit(1);
            }
            test = test / 2;
        }
    }

    /**
     * This function checks whether the number of block is integer power-of-two.
     * @param test is the number of blocks
     */
    private static void poweroftwoblocks(int test) {
        int remainder = 0;
        while (test != 1) {
            remainder = test % 2;
            if (remainder != 0) {
                System.out.println("The number of blocks "
                        + "is not a power of 2.");
                System.exit(1);
            }
            test = test / 2;
        }
    }

    /**
     * This function checks whether the number of byte is integer power-of-two.
     * @param test is the number of bytes
     */
    private static void poweroftwobytes(int test) {
        int remainder = 0;
        while (test != 1) {
            remainder = test % 2;
            if (remainder != 0) {
                System.out.println("The number of bytes "
                        + "is not a power of 2.");
                System.exit(1);
            }
            test = test / 2;
        }
    }

    /**
     * This function converts an integer string (0 or 1) to a boolean.
     * @param str is the string of 0 or 1
     * @return the boolean (true or false) according to the input string
     */
    private static boolean str2bool(String str) {
        if (str.equals("0")) {
            return false;
        } else if (str.equals("1")) {
            return true;
        } else {
            System.out.println("Wrong parameter, write-allocate, "
                    + "write-through, and eviction "
                    + "parameters should be 0 or 1");
            System.exit(1);
            return false;
        }
    }

    /**
     * This function prints the output of the simulation in a correct format.
     */
    private void printStats() {
        int lTot = this.lHit + this.lMiss;
        int sTot = this.sHit + this.sMiss;

        System.out.println("Total loads: " + lTot);
        System.out.println("Total stores: " + sTot);
        System.out.println("Load hits: " + this.lHit);
        System.out.println("Load misses: " + this.lMiss);
        System.out.println("Store hits: " + this.sHit);
        System.out.println("Store misses: " + this.sMiss);
        System.out.println("Total cycles: " + this.numCycles);
    }
}
