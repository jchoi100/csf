import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Parser that takes in a .s file and creates
 * a stdout of SCRAM formatted output.
 * @author Joon Hyuck Choi
 *
 */
public class SAS {
    private static final int MAX_LINES = 16;
    private static final int MAX_DAT = 255;
    private static final int MIN_DAT = 0;
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;
    private static final int SIX = 6;
    private static final int SEVEN = 7;
    private static final int EIGHT = 8;

    private Scanner sc;

    /** HashMap of operations and their OPCODE. */
    private HashMap<String, Integer> operations;

    /** HashMap of label, entry pairs. */
    private HashMap<String, LineEntry> labelMap;

    /** Keeps an ordered record of all the entries. */
    private ArrayList<LineEntry> lineEntries;

    /** Tokenized inputs. */
    private ArrayList<OriginalToken> tokenizedInput;

    /**
     * Constructor for the class.
     * @param console the stdin scanner.
     */
    public SAS(Scanner console) {
        this.sc = console;
        this.labelMap = new HashMap<>();
        this.lineEntries = new ArrayList<>();
        this.initializeOperationMap();
        this.tokenizedInput = new ArrayList<>();
    }

    /**
     * Puts the operation name and its corresponding bitcode.
     */
    private void initializeOperationMap() {
        this.operations = new HashMap<>();
        this.operations.put("HLT", 0);
        this.operations.put("LDA", 1);
        this.operations.put("LDI", 2);
        this.operations.put("STA", THREE);
        this.operations.put("STI", FOUR);
        this.operations.put("ADD", FIVE);
        this.operations.put("SUB", SIX);
        this.operations.put("JMP", SEVEN);
        this.operations.put("JMZ", EIGHT);
    }

    /**
     * Runs the entire program.
     * Runs the first and second passes.
     *
     * @throws NumberFormatException when we try to parse an int from a String.
     * @throws IOException when createOutput cannot write to stdout.
     */
    public void runSAS() throws NumberFormatException, IOException {
        this.tokenizeInput();
        this.firstPass();
        this.inputValidationCheck();
        this.secondPass();
        this.createOutput();
    }

    private void tokenizeInput() {
        int originalLineNumber = 0;

        while (this.sc.hasNextLine()) {
            String line = this.sc.nextLine();
            originalLineNumber++;

            //Takes care of case when the entire line is a comment.
            //Skip and go to the next iteration of the outer while loop.
            if ((line.length() > 0 && line.charAt(0) == '#')
                    || line.length() == 0) {
                continue;
            }

            //ex) STA start #this is a comment. Takes only "STA start"
            //ex) STA start#this is a comment. Takes only "STA start"
            if (line.contains("#")) {
                line = line.substring(0, line.indexOf('#')).trim();
            }

            Scanner lineScanner = new Scanner(line); //scan entire line.
            String currWord = "";

            while (lineScanner.hasNext() && !currWord.contains("#")) {
                currWord = lineScanner.next().trim();
                if (currWord.contains(":") && currWord.length() == 1) {
                    System.err.println("==========ERROR MESSAGE==========");
                    System.err.println("A misplaced colon at line "
                            + originalLineNumber + "!");
                    System.exit(1);
                } else {
                    this.tokenizedInput.add(
                               new OriginalToken(currWord, originalLineNumber));
                }
            }
            lineScanner.close();
        }
    }

    /**
     * First pass to pre-process inputs.
     * @param sc Scanner for the file.
     * @throws FileNotFoundException when file not found
     * @throws NumberFormatException when we try to parse an int from a String.
     */
    private void firstPass() throws FileNotFoundException,
                                    NumberFormatException {

        //Line entry buffer to be used when creating a new entry.
        LineEntry entry = new LineEntry();

        int scramAddress = 0;
        int progressCount = 0;
        for (int i = 0; i < this.tokenizedInput.size(); i++) {

            OriginalToken currToken = this.tokenizedInput.get(i);
            String currWord = currToken.token;
            int currLine = currToken.lineNumber;

            if (currWord.contains(":")) {
                if (progressCount == 0) {
                    currWord = currWord.substring(0, currWord.indexOf(':'));
                    entry.labelTokens.add(new OriginalToken(currWord,
                                                            currLine));
                } else {
                    System.err.println("==========ERROR MESSAGE==========");
                    System.err.println("Misplaced colon in line "
                                    + currLine + "!");
                    System.exit(1);
                }
            } else {
                if (progressCount == 0) { //expecting an operation or DAT
                    if (this.operations.containsKey(currWord.toUpperCase())) {
                        //operation
                        entry.operationToken = currToken;
                        entry.isDAT = false;
                    } else if (currWord.toUpperCase().equals("DAT")) { //DAT
                        entry.operationToken = currToken;
                        entry.isDAT = true;
                    } else { //no match
                        System.err.println("==========ERROR MESSAGE==========");
                        System.err.println("No specified "
                                + "operation in line " + currLine + "!");
                        System.exit(1);
                    }
                    progressCount++;
                } else { //expecting an address pointer or integer number
                    if (!entry.isDAT) {
                        entry.addressPointerToken = currToken;
                    } else {
                        entry.dataToken = currToken;
                    }
                    entry.selfScramAddress = scramAddress++;

                    for (int j = 0; j < entry.labelTokens.size(); j++) {
                        if (this.labelMap.containsKey(
                                            entry.labelTokens.get(j).token)) {
                            System.err.println("==========ERROR MESSAGE"
                                                    + "==========");
                            System.err.println("The label you used in line "
                                    + entry.labelTokens.get(j).lineNumber
                                    + " has already been defined!");
                            System.exit(1);
                        }
                        this.labelMap.put(entry.labelTokens.get(j).token,
                                          entry);
                    }

                    this.lineEntries.add(entry);
                    progressCount = 0;
                    entry = new LineEntry();
                }
            }
        } // while file has next line.
    }

    /**
     * Checks validity of inputs.
     */
    private void inputValidationCheck() {
        this.lineNumberCheck();
        for (int i = 0; i < this.lineEntries.size(); i++) {
            if (this.lineEntries.get(i).isDAT) {
                int data = -1;
                if (!this.labelMap.containsKey(this.lineEntries.get(i).dataToken.token)) {
                    try {
                        data = Integer.parseInt(
                                this.lineEntries.get(i).dataToken.token);
                    } catch (NumberFormatException e) {
                        System.err.println("==========ERROR MESSAGE==========");
                        System.err.println("The data you specified in line "
                                + this.lineEntries.get(i).dataToken.lineNumber
                                + " is not numeric!");
                        System.exit(1);
                    }
                    this.dataRangeCheck(data, i);
                }
            } else {
                String addressPointer =
                        this.lineEntries.get(i).addressPointerToken.token;
                int addressPointerNum = -1;
                try {
                    addressPointerNum = Integer.parseInt(addressPointer);
                    if (addressPointerNum >= MAX_LINES
                        || addressPointerNum < 0
                        || addressPointerNum > this.lineEntries.size()) {
                        System.err.println("==========ERROR MESSAGE==========");
                        System.err.println("The address you specified in line "
                            + this.lineEntries.get(i)
                                .addressPointerToken.lineNumber
                            + " is out of bounds!");
                        System.exit(1);
                    }
                } catch (NumberFormatException e) {
                    if (!this.labelMap.containsKey(addressPointer)) {
                        System.err.println("==========ERROR MESSAGE==========");
                        System.err.println("The label you specified in line "
                                + this.lineEntries.get(i)
                                    .addressPointerToken.lineNumber
                            + " is not defined!");
                        System.exit(1);
                    }
                }
            }
        }
    }

    /**
     * Helper method for inputValidationCheck().
     * Checks if the line number does not surpass 16.
     */
    private void lineNumberCheck() {
        if (this.lineEntries.size() > MAX_LINES) {
            System.err.println("==========ERROR MESSAGE==========");
            System.err.println("You have surpassed the maximum number"
                    + " of lines you can "
                    + "enter for this program!");
            System.exit(1);
        }
    }

    /**
     * Helper method for inputValidationCheck().
     * Checks if the data is in range of 0 ~ 255.
     * @param data the data to be checked.
     */
    private void dataRangeCheck(int data, int index) {
        if (data > MAX_DAT || data < MIN_DAT) {
            System.err.println("==========ERROR MESSAGE==========");
            System.err.println("The data you specified in line "
                    + this.lineEntries.get(index).dataToken.lineNumber
                    + " is out of bounds!");
            System.exit(1);
        }
    }

    /**
     * Second pass where we replace the labels with real addresses.
     * @throws NumberFormatException when trying to parse int from a String.
     */
    public void secondPass() throws NumberFormatException {
        for (int i = 0; i < this.lineEntries.size(); i++) {
            LineEntry currEntry = this.lineEntries.get(i);
            if (this.labelMap.containsKey(
                                    currEntry.addressPointerToken.token)
                || (currEntry.isDAT 
                    && this.labelMap.containsKey(currEntry.dataToken.token))) {
                //case when address is a label or the data is a label
                LineEntry pointedEntry = null;
                if (currEntry.isDAT) {
                    pointedEntry = this.labelMap.get(currEntry.dataToken.token);
                } else {
                    pointedEntry = this.labelMap.get(currEntry.addressPointerToken.token);
                }
                int pointerAddress = pointedEntry.selfScramAddress;
                if (!currEntry.isDAT) { //pointing address case
                    this.lineEntries.get(i).pointingScramAddress = pointerAddress;
                } else { //data is a label case
                    this.lineEntries.get(i).dataToken.token = pointerAddress + "";
                }
            } else { //is already a numeric address (or data value)?
                       //If not, it's using an undefined label
                if (!currEntry.isDAT) {
                    try {
                        this.lineEntries.get(i).pointingScramAddress =
                            Integer.parseInt(
                             this.lineEntries.get(i).addressPointerToken.token);
                    } catch (NumberFormatException e) {
                        System.err.println("==========ERROR MESSAGE==========");
                        System.err.println("The label you specified in line "
                                + this.lineEntries.get(i)
                                    .addressPointerToken.lineNumber
                                + " is not defined!");
                        System.exit(1);
                    }
                }
            }
        } //end second pass
    }

    /**
     * Creates stdout output.
     * @throws IOException when write() cannot write to stdout.
     */
    private void createOutput() throws IOException {
        byte[] outputs = new byte[this.lineEntries.size()];
        for (int i = 0; i < this.lineEntries.size(); i++) {
            LineEntry entry = this.lineEntries.get(i);
            String operation = entry.operationToken.token;
            int res = -1;
            if (entry.isDAT) {
                int data = Integer.parseInt(entry.dataToken.token);
                res = data;
            } else {
                int intOpCode = this.operations.get(operation);
                int pointingScramAddress = entry.pointingScramAddress;
                intOpCode *= MAX_LINES;
                res = intOpCode + pointingScramAddress;
            }
            outputs[i] = (byte) res;
        }
        try {
            System.out.write(outputs);
        } catch (IOException e) {
            System.err.println("==========ERROR MESSAGE==========");
            System.err.println("Could not print to stdout!");
        }
    }

//    private void printRegularVersion() {
//        System.out.println();
//        System.out.println();
//        System.out.println("Regular readable version:");
//        //Just printing for our sakes --> to be removed later
//        for (int i = 0; i < this.lineEntries.size(); i++) {
//            String output = i + " "
//                        + this.lineEntries.get(i).operationToken.token + "  ";
//            if (!this.lineEntries.get(i).isDAT) {
//                output += this.lineEntries.get(i).pointingScramAddress;
//            } else {
//                output += this.lineEntries.get(i).dataToken.token;
//            }
//            System.out.println(output);
//        }
//        System.out.println();
//        System.out.println();
//    }
//
//    private void printBinaryVersion() {
//        System.out.println();
//        System.out.println();
//        System.out.println("Binary version:");
//        //Just printing for our sakes --> to be removed later
//        for (int i = 0; i < this.lineEntries.size(); i++) {
//            String output = "";
//            if (!this.lineEntries.get(i).isDAT) {
//                output += Integer.toBinaryString(this.operations.get(
//                        this.lineEntries.get(i).operationToken.token)) + "  ";
//            }
//            if (!this.lineEntries.get(i).isDAT) {
//                output += " " + Integer.toBinaryString(
//                        this.lineEntries.get(i).pointingScramAddress);
//            } else {
//                output += " " + Integer.toBinaryString(
//                   Integer.parseInt(this.lineEntries.get(i).dataToken.token));
//            }
//            System.out.println(output);
//        }
//        System.out.println();
//        System.out.println();
//    }

    /**
     * One single line entry.
     * The ultimate thing we want to get out of this class
     * is something that looks like "LDA 4" instead of "LDA count".
     *
     * @author Joon Hyuck Choi
     *
     */
    private class LineEntry {

        public ArrayList<OriginalToken> labelTokens;
        public OriginalToken operationToken;
        public OriginalToken addressPointerToken;
        public OriginalToken dataToken;
        public boolean isDAT;
        public int selfScramAddress;
        public int pointingScramAddress;

        /**
         * Constructor.
         */
        LineEntry() {
            this.labelTokens = new ArrayList<>();
            this.operationToken = new OriginalToken();
            this.addressPointerToken = new OriginalToken();
            this.dataToken = new OriginalToken();
            this.isDAT = false;
            this.selfScramAddress = -1;
        }

    }

    /**
     * Contains one token of data from the original .s file.
     * @author Joon Hyuck Choi
     *
     */
    private class OriginalToken {
        private String token;
        private int lineNumber;
        OriginalToken() {

        }
        OriginalToken(String t, int l) {
            this.token = t;
            this.lineNumber = l;
        }
    }

    /**
     * Main method for this program.
     * @param args arguments for this method.
     * @throws FileNotFoundException when file not found.
     * @throws NumberFormatException when trying to parse int from a String.
     * @throws IOException when createOutput cannot write to stdout.
     */
    public static void main(String[] args) throws FileNotFoundException,
                                                  NumberFormatException,
                                                  IOException {
        Scanner console = new Scanner(System.in);
        SAS myParser = new SAS(console);
        myParser.runSAS();
        console.close();
    }

}