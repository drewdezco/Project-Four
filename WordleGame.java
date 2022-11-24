import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static java.lang.System.exit;
import static java.lang.Thread.sleep;

/**
 *Julia Barnes
 *Sarah Harrington
 *Andrew Hernandez
 *
 * The purpose of this class is to:
 * - Give a player six guesses to guess a five-letter word that is chosen at random from a list
 * - Check to see if the word guessed is a real five-letter word
 * - After each guess, tell the player if they got any letters exactly right as well as right in the wrong position
 * - Provide the real word at the end of guesses or if they get it correctly
 */
public class WordleGame implements KeyListener {

    private ArrayList<String> fiveLetterWords = new ArrayList<>();

    //take in target word as instance field
    private String targetWord;

    private WordleGraphics wordleGraphics;

    //default constructor
    public WordleGame(WordleGraphics wordleGraphics) {
        targetWord = null;
        this.wordleGraphics = wordleGraphics;
    }

    //parameterized constructor
    public WordleGame(String targetWord) {
        this.targetWord = targetWord;
    }

    /**
     * Generates a random number given a range
     * @param range
     * @return int
     */
    public static int generateNumber(int range) {

        return (int)(Math.random() * range);
    }

    /**
     * Initializes file words using word file given
     * Fills arraylist with words from the file
     * Catches file not found exception and returns error message as well as ends program
     * @throws FileNotFoundException
     */
    public void fillArrayList(File filename) {
        try {
            Scanner scan = new Scanner(filename);
            while (scan.hasNext()) {
                fiveLetterWords.add(scan.next());
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("ERROR: File not found.");
            exit(0);
        }
    }

    /**
     * Gets target word from the word list (specific range given in assignment)
     * Usually I would take the size of the arraylist for the random parameter
     * @return String
     */
    public String generateRandomWord() {
        return fiveLetterWords.get(generateNumber(4500));
    }

    public void setTargetWord() {
        targetWord = generateRandomWord();
    }

    public String getTargetWord() {
        return targetWord;
    }

    /**
     * Checks if current user guess in row is in the list of possible words
     * @return true if it is in the list, false if it is not
     */
    public boolean checkValidGuess() { //functional AFAIK
        wordleGraphics.setUserGuess(); //set user input in current row

        String checkArray[] = wordleGraphics.getUserGuess();//set array equal to user guess

        String checkInWords = ""; //instantiate empty string

        for (int i = 0; i < 5; i++) { //input each letter in array
            checkInWords += checkArray[i];
        }

        //System.out.println(checkInWords);

        if (!fiveLetterWords.contains(checkInWords.toLowerCase())) { //if list of words does not contain input
            return false;
        }
        else { //if it does contain input
            return true;
        }

    }

    public void checkColumn() throws InterruptedException {
        if (checkValidGuess() == true) { //if userguess is a valid input
            targetWord = "wares";
            String checkArray[] = wordleGraphics.getUserGuess();//set array equal to user guess

            String checkInWords = ""; //instantiate empty string

            for (int i = 0; i < 5; i++) { //input each letter in array
                checkInWords += checkArray[i];
            }

            for (int i = 0; i < 5; i ++) {
                //System.out.print(checkArray[i] + " " + targetWord.charAt(i));
                if (Character.toString(targetWord.charAt(i)).equalsIgnoreCase(checkArray[i])) {
                    //System.out.print(": Equals!\n");
                    wordleGraphics.setColumnFormat(1, i);//format as correct letter correct spot
                    continue;
                }
                if (targetWord.contains(checkArray[i].toLowerCase())) {
                    //System.out.print(": Here but not here!\n");
                    wordleGraphics.setColumnFormat(2, i);//format as correct letter incorrect spot
                }
                else {
                    //System.out.print(": Not here period!!\n");
                    wordleGraphics.setColumnFormat(3, i);//format as incorrect letter incorrect spot
                }
            }
        }
        else {
            //at some point put in message stating to input a five letter word
        }
    }

    /**
     * Empty override method that is unused
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Reads keycodes to determine which key is pressed.
     * Populates GUI with keypressed if A-Z
     * Removes characters if backspace is pressed
     * Moves to new row if row is filled
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println(e.getKeyCode());
        if (e.getKeyCode() == 8) { // if backspace is pressed

            if (wordleGraphics.getColumnValue()==5) { //if at the end of the current row
                wordleGraphics.setColumnValue(wordleGraphics.getColumnValue()-1);//subtract one to end up in correct column
            }
            if (wordleGraphics.getWordleLabels()[wordleGraphics.getRowValue()][wordleGraphics.getColumnValue()].getText().equals("") && wordleGraphics.getColumnValue() != 0) { //if column is empty and not the first column in the row
                wordleGraphics.setColumnValue(wordleGraphics.getColumnValue()-1);//move back one column
            }
            if (wordleGraphics.getWordleLabels()[wordleGraphics.getRowValue()][wordleGraphics.getColumnValue()].getText().length() > 0) { //if column has something in it
                wordleGraphics.setEmptyBoxFormat(wordleGraphics.getWordleLabels()[wordleGraphics.getRowValue()][wordleGraphics.getColumnValue()]); //set to empty box format for that indices
                wordleGraphics.getWordleLabels()[wordleGraphics.getRowValue()][wordleGraphics.getColumnValue()].setText(""); //set text to empty
            }
        }
        //System.out.println(e);

        else { //otherwise keypressed is not backspace

            if (wordleGraphics.getColumnValue() < 5) { //if its columns 0-4
                if (e.getKeyCode() >= 65 && e.getKeyCode() <= 90) { //get which key is pressed (A-Z || 65-90)
                    wordleGraphics.getWordleLabels()[wordleGraphics.getRowValue()][wordleGraphics.getColumnValue()].setText(Character.toString(Character.toUpperCase(e.getKeyChar()))); //populate label with uppercase version of that key
                    wordleGraphics.setUncheckedBoxFormat(wordleGraphics.getWordleLabels()[wordleGraphics.getRowValue()][wordleGraphics.getColumnValue()]); //Change format to reflect a letter is in the box
                    wordleGraphics.setColumnValue(wordleGraphics.getColumnValue()+1);//move forward one column
                }
                //System.out.println(columnValue);
            }

            if (wordleGraphics.getColumnValue() == 5 && wordleGraphics.getRowValue() != 5) { //if column is at 5
                if (e.getKeyCode()==10) { //if enter is pressed
                    //System.out.println("You pressed enter");
                    if (checkValidGuess() == true) {
                        try {
                            checkColumn();
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        wordleGraphics.setRowValue(wordleGraphics.getRowValue()+1); //move one row down
                        wordleGraphics.setColumnValue(0); //move column to first in the row
                    }
                    else {
                        //eventually display message stating to put in correct input
                        System.out.println("Input correct message");
                    }

                }
            }

            if (wordleGraphics.getColumnValue() == 5 && wordleGraphics.getRowValue() == 5) {
                if (e.getKeyCode()==10) {
                    if (checkValidGuess() == true) {
                        try {
                            checkColumn();
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }

                        //show scoreboard here
                    }
                }
            }
        }
    }

    /**
     * Empty override method that is unused
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }


    //method to run through wordle game
    public void playWordle() {

        //call GUI so it is called when the game play is called

        //iterate through 6 times (6 guesses)

        //take in user input as guess word

        //compare guess word to target word

        //return correct letters

        //return letters in the target but in the incorrect spot

        //return incorrect letters

        //handle guesses of incorrect length, do not count as a guess

    }

}