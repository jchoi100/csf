import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;


/**
 * .
 * @author James
 *
 */
public final class CacheSimulator {

    /**
     * .
     */
    public static final int THREE = 3;
    /**
     * Four.
     */
    public static final int FOUR = 4;
    /**
     * Five.
     */
    public static final int FIVE = 5;
    /**
     * Six.
     */
    public static final int SIX = 6;
    /**
     * Sixteen.
     */
    public static final int SIXTEEN = 16;
    /**
     * Thirty-two.
     */
    public static final int THIRTYTWO = 32;
    /**
     * Hundred.
     */
    public static final int HUNDRED = 100;

    private static int loadCount = 0;
    private static int storeCount = 0;
    private static int loadHitCount = 0;
    private static int storeHitCount = 0;
    private static int cycles = 0;

    private CacheSimulator() {

    }


    /**
     * .
     * @param args command-line arguments
     * @throws IOException if file does not exist
     */
    public static void main(String[] args) throws IOException {

        if (args.length <= SIX) {
            System.err.println("insufficient arguments.");
            System.exit(1);
        }

        int numSets = Integer.parseInt(args[0]);
        int numBlocks = Integer.parseInt(args[1]);
        int numBytes = Integer.parseInt(args[2]);
        boolean writeAllocate = args[THREE].equals("1");
        boolean writeThrough = args[FOUR].equals("1");
        boolean leastRecentlyUsed = args[FIVE].equals("1");

        boolean load = false;
        String address = "";
        long addressNum;
        int cacheIndex;

        int[][] cache = new int[numSets][numBlocks];
        for (int i = 0; i < numSets; i++) {
            for (int j = 0; j < numBlocks; j++) {
                cache[i][j] = 0;
            }
        }

        int[][] count;
        int[] index;
        count = new int[numSets][numBlocks];
        for (int[] innerCount: count) {
            Arrays.fill(innerCount, 0);
        }
        index = new int[numSets];
        Arrays.fill(index, 0);

        File trace = new File(args[SIX]);
        Scanner sc = new Scanner(trace);
        while (sc.hasNextLine()) {
            String s = sc.next();
            load = s.equals("l");
            if (load) {
                loadCount++;
            } else {
                storeCount++;
            }

            address = sc.next();
            address = address.substring(2);
            addressNum = Long.parseLong(address, SIXTEEN);
            cacheIndex = (int)
                    ((addressNum % (numSets * numBytes)) / numBytes);
            int inputTag = (int) (addressNum / (numSets * numBytes));

            //check if hit
            int blockLocation = -1;
            for (int i = 0; i < numBlocks; i++) {
                int cacheTag = cache[cacheIndex][i] / FOUR;
                if (cacheTag == inputTag) {
                    blockLocation = i;
                    if (load) {
                        loadHitCount++;
                    } else {
                        storeHitCount++;
                    }
                }
            }

            // miss
            if (blockLocation < 0) {
                if (load) {
                    int p = 0; // location of least recently used block
                    int q = 0; // value of counter for least recently used block
                    //LRU
                    if (leastRecentlyUsed) {
                        for (int i = 0; i < numBlocks; i++) {
                            if (count[cacheIndex][i] > q) {
                                p = i;
                                q = count[cacheIndex][i];
                            }
                        }
                        if (!writeThrough && (cache[cacheIndex][p]
                                % FOUR == THREE)) {
                            cycles += numBytes * HUNDRED / FOUR;
                        }
                        cache[cacheIndex][p] = inputTag * FOUR + THREE;
                        cycles += numBytes * HUNDRED / FOUR;
                        for (int i = 0; i < numBlocks; i++) {
                            count[cacheIndex][i]++;
                        }
                        count[cacheIndex][p] = 0;
                    } else {
                        //FIFO
                        p = index[cacheIndex];
                        if (!writeThrough && (cache[cacheIndex][p]
                                % FOUR == THREE)) {
                            cycles += numBytes * HUNDRED / FOUR;
                        }
                        cache[cacheIndex][p] = inputTag * FOUR + 1;
                        cycles += numBytes * HUNDRED / FOUR;
                        index[cacheIndex]++;
                        if (index[cacheIndex] >= numBlocks) {
                            index[cacheIndex] = 0;
                        }
                    }
                    cycles++;
                } else {
                    //store
                    if (writeAllocate) {
                        int p = 0;
                        int q = 0;
                        //LRU
                        if (leastRecentlyUsed) {
                            for (int i = 0; i < numBlocks; i++) {
                                if (count[cacheIndex][i] > q) {
                                    p = i;
                                    q = count[cacheIndex][i];
                                }
                            }
                            if (!writeThrough && (cache[cacheIndex][p]
                                    % FOUR == THREE)) {
                                cycles += numBytes * HUNDRED / FOUR;
                            }
                            cache[cacheIndex][p] = inputTag * FOUR + THREE;
                            cycles += numBytes * HUNDRED / FOUR;
                            for (int i = 0; i < numBlocks; i++) {
                                count[cacheIndex][i]++;
                            }
                            count[cacheIndex][p] = 0;
                        } else {
                            //FIFO
                            p = index[cacheIndex];
                            cache[cacheIndex][p] = inputTag * FOUR + THREE;
                            cycles += numBytes * HUNDRED / FOUR;
                            index[cacheIndex]++;
                            if (index[cacheIndex] >= numBlocks) {
                                index[cacheIndex] = 0;
                            }
                            if (!writeThrough && (cache[cacheIndex][p]
                                    % FOUR == THREE)) {
                                cycles += numBytes * HUNDRED / FOUR;
                            }
                        }
                        if (writeThrough) {
                            cycles += HUNDRED;
                        }
                        cycles++;
                    }
                }
            } else {
                if (load) {
                    if (leastRecentlyUsed) {
                        for (int i = 0; i < numBlocks; i++) {
                            count[cacheIndex][i]++;
                        }
                        count[cacheIndex][blockLocation] = 0;
                    }
                    cycles++;
                } else {
                    if (writeThrough) {
                        cycles += HUNDRED;
                    } else {
                        int x = cache[cacheIndex][blockLocation];
                        x = x / FOUR;
                        x = x * FOUR + THREE;
                        cache[cacheIndex][blockLocation] = x;
                    }
                    cycles++;
                }
            }
            sc.nextLine();
        }

        for (int i = 0; i < numSets; i++) {
            for (int j = 0; j < numBlocks; j++) {
                if (cache[i][j] % FOUR == THREE) {
                    cycles += numBytes * HUNDRED / FOUR;
                }
            }
        }

        sc.close();

        System.out.println("Total loads: " + loadCount);
        System.out.println("Total stores: " + storeCount);
        System.out.println("Load hits: " + loadHitCount);
        System.out.println("Load misses: " + (loadCount - loadHitCount));
        System.out.println("Store hits: " + storeHitCount);
        System.out.println("Store misses: " + (storeCount - storeHitCount));
        System.out.println("Total cycles: " + cycles);
    }
}
