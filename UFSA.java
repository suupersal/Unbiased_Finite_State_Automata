/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

/**
 *
 * @author Salvador
 */
public class UFSA {
    //Field to hold the total number of states

    int numStates;
    //field to hold the current state of the machine
    int state;
    //Field that holds the integer value of the dead state (trap state)
    final int DEAD_STATE = -1;
    //2D array that will be used as the transition table
    int[][] transitionTable;
    //boolean will be used to determine when to exit loop for reading input symbols
    boolean exit;
    //boolean that will determine if the end of the file has been reached
    boolean fileComplete = false;
    //boolean array will hold values that determin if a state (index) is a final state
    boolean[] finalStates;
    //input line, current string being analyzed
    String input;
    //This string will keep the original input 
    //string so it can be printed out with result
    String processedInput;
    //This array will hold all symbols of input
    String tempInputArray[];
    //Hols current symbol being analyzed
    String symbol;
    //end marker used to determine the end of input string
    final String END_MARKER = "~";
    //dilimeter used to determin the end of a machine
    final String DILIMETER = "...";
    //String array that will hold the entire alphabet that is available for use
    String[] alphabet;
    //Objects required for file Input
    FileInputStream fstream;
    DataInputStream in;
    BufferedReader br;

    public UFSA() {
        try {
            fstream = new FileInputStream("inputFile.txt");
            in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));
        } catch (FileNotFoundException ex) {
            System.out.println("Error: Input file not found. Please Check input.");
        }

    }
    //method that informs user of Acceptance

    public void Accept() {
        String S=processedInput + "  Accepted";
        System.out.println(String.format("%s", S));
    }
    //method that informs user that input is rejected

    public void Reject() {
        String S=processedInput + "  Rejected!";
        System.out.println(String.format("%s", S));
    }
    //this method returns the next symbol available in the input string.
    //Method will return the endmarker if input is of size 0.

    public String nextSymbol() {
        if (input.length() == 0) {
            return END_MARKER;
        }
        String temp = input.substring(0, input.length() - (input.length() - 1));
        if (input.length() > 0) {
            input = input.substring(1);
        }
        return temp;

    }

    //Method returns true if passed symbol is in the alphabet. 
    //Will return false if it is not.
    public boolean inAlphabet(String symbol) {
        int result = getSymbolNum(symbol);
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    //nextState() will determine the next state of the machine given the 
    //current state and a symbol. It uses the 2D array and current state as 
    //the first index and the index of the symbol in the alphabet array as the
    //second index; the resulting value is the next state.
    public int nextState(int currState, String nextInput) {
        int result = transitionTable[currState][getSymbolNum(nextInput)];
        return result;
    }

    //getSymbolNum() will take a symbol of type string as an argument and will
    //search for the given symbol in the alphabet array. if the symbol is found it
    //will return the index of where it was found; else it will display an error message 
    //and terminate program.
    public int getSymbolNum(String symbol) {

        for (int i = 0; i < alphabet.length; i++) {
            if (alphabet[i].equals(symbol)) {

                return i;
            }
        }
        return -1;
    }

    //initialize() will read the input file and read all neccessary values and symbols
    //needed to create the machine that will analyze all the inputs.
    public void initialize() throws IOException {

        input = br.readLine();

        //Read the total number of states
        numStates = Integer.parseInt(input);

        //create the boolean finalStates array with size=numStates
        finalStates = new boolean[numStates];

        //Read the final states line
        input = br.readLine();

        //remove any empty spaces and create a temporary array with input
        String[] tempFinalStates = input.split(" ");

        //Set of the finalStates boolean array using fianlstates as indexies
        for (int i = 0; i < tempFinalStates.length; i++) {
            finalStates[Integer.parseInt(tempFinalStates[i])] = true;
        }

        //get the alphabet string
        input = br.readLine();

        //split the input line into an array of symbols and set it equal to the
        //alphabet array
        alphabet = input.split(" ");

        //set up transition table

        //read the fist transition line
        input = br.readLine();

        //initialize the transition table using numStates and alphabet.length as
        //the demensions
        transitionTable = new int[numStates][alphabet.length];

        //fill table with default dead states.
        for (int k = 0; k < transitionTable.length; k++) {
            for (int l = 0; l < transitionTable[0].length; l++) {
                transitionTable[k][l] = DEAD_STATE;

            }
        }

        //While loop will continue to run and add transition states until there are
        //no more transitions to input
        while (input != null) {

            if (input.charAt(0) == '(') {

                //Remove the "(" and ")"
                input = input.substring(1, input.length() - 1);

                //Remove all " "
                String[] temp = input.split(" ");

                // enter the transition int othe transitions table using the first number in
                //the input line as the first index, the symbols position int he alphabet array
                //as the second index and the next state as the value.
                transitionTable[Integer.parseInt(temp[0])][getSymbolNum(temp[1])] = Integer.parseInt(temp[2]);

                input = br.readLine();



            } else {

                break;

            }


        }

    }

    public void run() throws IOException {
        
        /////Print out the read input and format it before analyzing string
        System.out.println("1) Numberof States: " + numStates);
        System.out.print("2) Final States: ");
        for (int i = 0; i < finalStates.length; i++) {
            if (finalStates[i] == true) {
                System.out.print(i + " ");
            }
        }
        System.out.println("");

        System.out.print("3) Alphabet: ");
        for (String tempA : alphabet) {
            System.out.print(tempA + " ");
        }
        System.out.println("");
        System.out.println("4) Transitions:");
        for (int k = 0; k < transitionTable.length; k++) {
            for (int l = 0; l < transitionTable[0].length; l++) {
                if (!(transitionTable[k][l] == -1)) {
                    System.out.print("(" + k + " " + alphabet[l] + " " + transitionTable[k][l] + ")");
                    System.out.println("");
                }
            }
        }
        System.out.println("5) Strings");
        ///////////end of printing and formating

        while (true) {
            //use this to remember the full input line, needed to display result
            processedInput = input;
            while (!exit) {

                symbol = nextSymbol();

                //check if its in the alphabet
                if (inAlphabet(symbol)) {
                    state = nextState(state, symbol);
                    if (state == DEAD_STATE) {
                        exit = true;
                        Reject();
                    }
                } else {
                    exit = true;

                    if (!symbol.equals(END_MARKER)) {
                        Reject();
                    } else if (finalStates[state]) {
                        Accept();
                    } else {
                        Reject();
                    }
                }

            }

            input = br.readLine();

            if (input == null) {
                in.close();
                fileComplete = true;
                return;
            } else if (input.equals(DILIMETER)) {
                fileComplete = false;
                state = 0;
                exit = false;
                break;
            }
            exit = false;
            state = 0;

        }
    }
}
