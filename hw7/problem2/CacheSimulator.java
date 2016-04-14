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
    private int numLoads;

    /** Total number of load hits. */
    private int numLoadHits;

    /** Total number of load misses. */
    private int numLoadMisses;

    /** Total number of stores. */
    private int numStores;

    /** Total number of store hits. */
    private int numStoreHits;

    /** Total number of store misses. */
    private int numStoreMisses;

    /** Total number of cycles. */
    private int numCycles;

    /** Offset and indexLength for parsing address. */
    private int offset, numSets;

    /** Input arguments. */
    private int blocksPerSet, bytesPerBlock, indexLength;

    /** Boolienized form for the three input arguments given as numbers. */
    private boolean writeThrough, writeAllocate, lru;

    /** Input file name. */
    private String inputFile;

    /** Our cache. */
    private HashMap<Long, ArrayList<CacheSlot>> cache = new HashMap<>();

    /**
     * Class to contain info about a cache slot.
     */
    private class CacheSlot {
        private long index;
        private boolean dirty;
        private long tag;

        /**
         * Constructor for the CacheSlot class.
         * @param index index of the address.
         * @param tag tag of the address.
         */
        CacheSlot(long i, long t) {
            this.index = i;
            this.dirty = false;
            this.tag = t;
        }
    }

    /**
     * Constructor for the CacheSimulator class.
     * Initializes member variables.
     * @param args Arguments given.
     */
    private CacheSimulator(String[] args) {
        this.parseArguments(args);
        this.offset = (int) (Math.log(this.bytesPerBlock) / Math.log(2));
        this.numLoadHits = 0;
        this.numLoadMisses = 0;
        this.numStoreHits = 0;
        this.numStoreMisses = 0;
        this.numCycles = 0;
        this.indexLength = (int) (Math.log(this.numSets) / Math.log(2));
    }

    /**
     * Executes simulation.
     * 1. A cache with n sets of 1 block each is essentially direct-mapped.
     * 2. A cache with 1 set of n blocks is essentially fully associative.
     * 3. Cache with n sets of m blocks is essentially m-way set-associative
     * @throws IOException if file cannot be opened.
     */
    private void executeSimulation() throws IOException {
        File file = new File(this.inputFile);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String oneLine = "";

        while ((oneLine = br.readLine()) != null) {
            Scanner lineScanner = new Scanner(oneLine);
            String type = "";
            String address = "";

            try {
                type = lineScanner.next().trim();
                address = lineScanner.next().trim();
            } catch (NoSuchElementException nse) {
                System.err.println("Wrong number of fields in trace file!");
                System.exit(1);
            } catch (NumberFormatException nfe) {
                System.err.println("Wrong address format!");
                System.exit(1);
            }
            address = String.format("%32s",
                   Long.toBinaryString(Long.decode(address))).replace(' ', '0');
            int cutoff = address.length() - this.offset;

            int index = this.getIndex(address, cutoff);
            int tag = this.getTag(address, cutoff);

            if (type.equalsIgnoreCase("l")) {
                this.loadExecute(index, tag);
            } else if (type.equalsIgnoreCase("s")) { // Store case.
                // Now, we have both the index and tag.
                if (this.writeAllocate && !this.writeThrough) {
                    // Write back, write allocate
                    this.storeExecute1(index, tag);
                } else if (this.writeAllocate && this.writeThrough) {
                    // Write through, write allocate
                    this.storeExecute2(index, tag);
                } else { //writeAllocate == 0 && writeThrough == 1
                    // Write through, no write allocate
                    this.storeExecute3(index, tag);
                }
            } else { // error case.
                System.err.println("Wrong command type: l or s!");
                System.exit(1);
            }
            lineScanner.close();
        }
        br.close();
    }

    /**
     * Parses the index from raw address.
     * @param address String address.
     * @param cutoff Cutoff to be used for parsing index.
     * @return index part of the address.
     */
    private int getIndex(String address, int cutoff) {
        return (this.indexLength == 0) ? 0 : Integer.parseInt(address.substring(
            cutoff - this.indexLength, cutoff), 2);
    }

    /**
     * Parses the tag from raw address.
     * @param address String address.
     * @param cutoff Cutoff to be used for parsing tag.
     * @return tag part of the address.
     */
    private int getTag(String address, int cutoff) {
        return Integer.parseInt(address.substring(0,
                cutoff - this.indexLength), 2);
    }

    /**
     * Checks if the given bucket list contains the slot with the tag.
     * @param bucketList list of slots.
     * @param tag the tag we want to see if it exists or not.
     * @return position of slot if tag match, -1 if not.
     */
    private int bucketListContainsSlot(ArrayList<CacheSlot> bucketList,
                                                  long tag) {
        int position = -1;
        for (int i = 0; i < bucketList.size(); i++) {
            if (bucketList.get(i).tag == tag) {
                position = i;
            }
        }
        return position;
    }

    /**
     * Execution for load case.
     * @param index index into cache.
     * @param tag tag in cache.
     */
    private void loadExecute(long index, long tag) {
        this.numLoads++;
        if (this.cache.containsKey(index)) {
            ArrayList<CacheSlot> bucketList = this.cache.get(index);
            int position = this.bucketListContainsSlot(bucketList, tag);
            if (position != -1) { // hit!
                if (this.lru) {
                    //Bring that slot to the front of the arraylist
                    CacheSlot beingLoaded = bucketList.get(position);
                    bucketList.remove(position);
                    bucketList.add(0, beingLoaded);
                } //Else FIFO: do nothing with loading in hit
                this.numLoadHits++;
            } else { // The tag doesn't match! But the bucket exists.
                this.numLoadMisses++;
                // Need to evict something and place our new data there.
                // In both LRU and FIFO, if the bucket is not full,
                // we just place the new data at the front.
                // If the bucket is full, we evict the one at the
                // last position of the arraylist.
                if (bucketList.size() < this.blocksPerSet) {
                    // Just add this new guy to the front
                    // Load contents to cache from memory.
                    bucketList.add(0, new CacheSlot(index, tag));
                    this.numCycles += MEMORY_CYCLE
                                            * (this.bytesPerBlock / FOUR);
                } else {
                    // The bucket is full. Evict least recently used.
                    if (!bucketList.get(bucketList.size() - 1).dirty) {
                        // Not dirty.
                        // Overwrite the guy to be evicted with new guy
                        this.numCycles += MEMORY_CYCLE
                                            * (this.bytesPerBlock / FOUR);
                    } else { // Dirty.
                        // Write this old guy to memory
                        this.numCycles += MEMORY_CYCLE
                                            * (this.bytesPerBlock / FOUR);
                        // Load new guy to memory
                        this.numCycles += MEMORY_CYCLE
                                            * (this.bytesPerBlock / FOUR);
                    }
                    bucketList.remove(bucketList.size() - 1);
                    bucketList.add(0, new CacheSlot(index, tag));
                }
            }
        } else { // No cache for the index whatsoever!
            this.numLoadMisses++;
            // Need to load data from memory from scratch.
            // First, create a new bucket.
            ArrayList<CacheSlot> bucket = new ArrayList<>();
            // Need to insert result into cache.
            this.numCycles += MEMORY_CYCLE * (this.bytesPerBlock / FOUR);
            CacheSlot toAdd = new CacheSlot(index, tag);
            toAdd.dirty = false;
            bucket.add(toAdd);
            this.cache.put(index, bucket);
            // Then return result in cache to CPU.
        }
        // Then load this to CPU from cache.
        this.numCycles += CACHE_CYCLE * (this.bytesPerBlock / FOUR);
    }

    /**
     * Execution for store case.
     * Write back, write allocate
     * @param index index into cache.
     * @param tag tag in cache.
     */
    private void storeExecute1(long index, long tag) {
        // Write-back with write-allocate
        // Use dirty bit.
        // Don't ignore write miss.
        this.numStores++;
        if (this.cache.containsKey(index)) { // Something is in cache.
            ArrayList<CacheSlot> bucketList = this.cache.get(index);
            int position = this.bucketListContainsSlot(bucketList, tag);
            if (position != -1) { // Hit!
                this.numStoreHits++;
                bucketList.get(position).dirty = true;
                if (this.lru) {
                    CacheSlot beingStored = bucketList.get(position);
                    bucketList.remove(position);
                    bucketList.add(0, beingStored);
                }
                // Write the result to thing in cache.
            } else { // Miss!
                this.numStoreMisses++;
                // First, check if the bucket is full
                // If not, load from memory this data
                // to the front and change it and mark dirty.
                // If so, evict one block and do the same.
                CacheSlot toAdd = new CacheSlot(index, tag);
                toAdd.dirty = true;
                if (bucketList.size() < this.blocksPerSet) {
                    bucketList.add(0, toAdd);
                    this.numCycles += MEMORY_CYCLE
                                        * (this.bytesPerBlock / FOUR);
                    // Write to new guy in cache
                } else {
                    // Need to evict something now.
                    // In both LRU and FIFO, remove the last one.
                    if (!bucketList.get(bucketList.size() - 1).dirty) {
                        // Just evict without further action.
                        // Load new guy from memory
                        this.numCycles += MEMORY_CYCLE
                                            * (this.bytesPerBlock / FOUR);
                        // Write to new guy in cache
                    } else { // dirty!
                        // Write this guy to memory first
                        this.numCycles += MEMORY_CYCLE
                                            * (this.bytesPerBlock / FOUR);
                        // Bring new guy into cache from memory
                        this.numCycles += MEMORY_CYCLE
                                            * (this.bytesPerBlock / FOUR);
                        // Write to new guy in cache
                    }
                    bucketList.remove(bucketList.size() - 1);
                    bucketList.add(0, toAdd);
                }
            }
        } else { // Miss: Cache has nothing corresponding to the given index.
            this.numStoreMisses++;
            // First, read from memory the correct block.
            // First, create a new bucket to reside in cache.
            ArrayList<CacheSlot> bucket = new ArrayList<>();
            // Fetch data from memory into cache.
            this.numCycles += MEMORY_CYCLE * (this.bytesPerBlock / FOUR);
            CacheSlot toAdd = new CacheSlot(index, tag);
            toAdd.dirty = true;
            bucket.add(toAdd);
            this.cache.put(index, bucket);
            // Write the result to thing in cache.
        }
        // Write the result to thing in cache.
        this.numCycles += CACHE_CYCLE * (this.bytesPerBlock / FOUR);
    }

    /**
     * Execution for store case.
     * Write through, write allocate
     * @param index index into cache.
     * @param tag tag in cache.
     */
    private void storeExecute2(long index, long tag) {
        // Write through with write allocate.
        // Don't use dirty bit --> write to both cache and RAM simultaneously.
        // Don't ignore write miss.
        this.numStores++;
        if (this.cache.containsKey(index)) { // Something is in cache.
            ArrayList<CacheSlot> bucketList = this.cache.get(index);
            int position = this.bucketListContainsSlot(bucketList, tag);
            if (position != -1) { // Hit!
                this.numStoreHits++;
                if (this.lru) {
                    CacheSlot beingStored = bucketList.get(position);
                    bucketList.remove(position);
                    bucketList.add(0, beingStored);
                }
            } else { // Miss!
                this.numStoreMisses++;
                CacheSlot toAdd = new CacheSlot(index, tag);
                // If bucektlist full, evict the last one.
                if (bucketList.size() >= this.blocksPerSet) {
                    bucketList.remove(bucketList.size() - 1);
                }
                // First, bring to cache from memory.
                this.numCycles += MEMORY_CYCLE * (this.bytesPerBlock / FOUR);
                bucketList.add(0, toAdd);
            }
        } else { // Miss: Cache has nothing corresponding to the given index.
            this.numStoreMisses++;
            // First, need to create the right bucket.
            ArrayList<CacheSlot> bucket = new ArrayList<>();
            // First, read from memory the correct block.
            this.numCycles += MEMORY_CYCLE * (this.bytesPerBlock / FOUR);
            CacheSlot toAdd = new CacheSlot(index, tag);
            bucket.add(toAdd);
            this.cache.put(index, bucket);
        }
        // First, write to cache.
        this.numCycles += CACHE_CYCLE * (this.bytesPerBlock / FOUR);
        // Then, write to memory
        this.numCycles += MEMORY_CYCLE * (this.bytesPerBlock / FOUR);
    }

    /**
     * Execution for store case.
     * Write through, no write allocate
     * @param index index into cache.
     * @param tag tag in cache.
     */
    private void storeExecute3(long index, long tag) {
        // Write through with no write allocate.
        // Don't use dirty bit --> write to both cache and RAM simultaneously.
        // Ignore write miss.
        // data at the missed-write location is not loaded to cache,
        // and is written directly to the backing store.
        this.numStores++;
        if (this.cache.containsKey(index)) { // Something is in cache.
            ArrayList<CacheSlot> bucketList = this.cache.get(index);
            int position = this.bucketListContainsSlot(bucketList, tag);
            if (position != -1) { // Hit!
                if (this.lru) {
                    CacheSlot beingStored = bucketList.get(position);
                    bucketList.remove(position);
                    bucketList.add(0, beingStored);
                }
                this.numStoreHits++;
                this.numCycles += CACHE_CYCLE * (this.bytesPerBlock / FOUR);
            } else { // Miss!
                this.numStoreMisses++;
            }
        } else { // Miss: Cache has nothing corresponding to the given index.
            this.numStoreMisses++;

        }
        // Write directly to memory.
        this.numCycles += MEMORY_CYCLE * (this.bytesPerBlock / FOUR);
    }

    /**
     * Prints the result of prediction simulation on std out.
     */
    private void printRes() {
        System.out.println("Total loads: " + this.numLoads);
        System.out.println("Total stores: " + this.numStores);
        System.out.println("Load hits: " + this.numLoadHits);
        System.out.println("Load misses: " + this.numLoadMisses);
        System.out.println("Store hits: " + this.numStoreHits);
        System.out.println("Store misses: " + this.numStoreMisses);
        System.out.println("Total cycles: " + this.numCycles);
    }

    /**
     * Checks if the input file exists and opens.
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
     * Parses all the given input arguments to the format we want.
     * @param args the input arguments.
     */
    private void parseArguments(String[] args) {
        try {
            this.numSets = Integer.parseInt(args[0]);
            this.blocksPerSet = Integer.parseInt(args[1]);
            this.bytesPerBlock = Integer.parseInt(args[2]);
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
     * @return True if all inputs valid.
     */
    private boolean allInputValid() {
        return this.numSets > 0 && this.blocksPerSet > 0
               && this.isTwosPower(this.numSets)
               && this.isTwosPower(this.blocksPerSet)
               && this.bytesPerBlock >= FOUR
               && this.isTwosPower(this.bytesPerBlock)
               && (this.writeAllocate || this.writeThrough);
    }

    /**
     * Used for write-allocate, write-through, lru.
     * It's easier for us to use these as boolean type than int.
     * @param s the argument to convert to boolean.
     * @return Boolean form of the input string.
     */
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
