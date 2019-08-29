package com.pigdogbay.lib.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 03/12/2016.
 * Logic to parse queries and search through a list of words to find matches
 */

public class CodewordSolver {

    private static final int CROSSWORD_CHAR = '.';
    private static final int SAME_CHAR_1 = '1';
    private static final int SAME_CHAR_2 = '2';
    private static final int SAME_CHAR_3 = '3';
    private static final int SAME_CHAR_4 = '4';
    private static final int SAME_CHAR_5 = '5';
    private static final int SAME_CHAR_6 = '6';
    private static final int SAME_CHAR_7 = '7';

    private static class Letter {
        Letter(int character, int position){
            this.character = character;
            this.position = position;
        }
        int character;
        int position;
    }
    private List<Letter> unknowns, knowns, same1, same2, same3, same4, same5, same6, same7;
    private String foundLetters;
    private LetterSet letterSet;

    private int wordLength;

    public String getFoundLetters() {
        return foundLetters;
    }
    public void setFoundLetters(String foundLetters) {
        this.foundLetters = foundLetters;
    }
    public int getWordLength() {
        return wordLength;
    }

    public CodewordSolver(){
        unknowns = new ArrayList<>();
        knowns = new ArrayList<>();
        same1 = new ArrayList<>();
        same2 = new ArrayList<>();
        same3 = new ArrayList<>();
        same4 = new ArrayList<>();
        same5 = new ArrayList<>();
        same6 = new ArrayList<>();
        same7 = new ArrayList<>();
        foundLetters = "";
        letterSet = new LetterSet("");
    }

    public void parse(String query){
        unknowns.clear();
        knowns.clear();
        same1.clear();
        same2.clear();
        same3.clear();
        same4.clear();
        same5.clear();
        same6.clear();
        same7.clear();
        wordLength = query.length();

        for (int i = 0; i<query.length(); i++){
            int c = query.charAt(i);
            if (CROSSWORD_CHAR == c){
                unknowns.add(new Letter(c,i));
            } else if (SAME_CHAR_1 == c){
                same1.add(new Letter(c,i));
            } else if (SAME_CHAR_2 == c){
                same2.add(new Letter(c,i));
            } else if (SAME_CHAR_3 == c){
                same3.add(new Letter(c,i));
            } else if (SAME_CHAR_4 == c){
                same4.add(new Letter(c,i));
            } else if (SAME_CHAR_5 == c) {
                same5.add(new Letter(c, i));
            } else if (SAME_CHAR_6 == c) {
                same6.add(new Letter(c, i));
            } else if (SAME_CHAR_7 == c) {
                same7.add(new Letter(c, i));
            }
            else {
                knowns.add(new Letter(c,i));
            }
        }
    }

    public boolean isMatch(String word){
        //check if known letters match
        for (Letter letter : knowns){
            if (letter.character != word.charAt(letter.position)) return false;
        }
        //check if numbered letters are the same and different to the rest
        if (checkSameLetters(word, same1)) return false;
        if (checkSameLetters(word, same2)) return false;
        if (checkSameLetters(word, same3)) return false;
        if (checkSameLetters(word, same4)) return false;
        if (checkSameLetters(word, same5)) return false;
        if (checkSameLetters(word, same6)) return false;
        if (checkSameLetters(word, same7)) return false;

        //check knowns do not equal unknown
        for (Letter unknown : unknowns){
            int c = word.charAt(unknown.position);
            for (Letter known : knowns){
                if (known.character == c) return false;
            }
            //store char for later use
            unknown.character = c;
        }

        //check no other groups of letters in the unknowns
        letterSet.clear();
        letterSet.add(word);
        for (Letter letter : unknowns){
            if (letterSet.getCount(letter.character)>1) return false;
            //check unknown character has not already been found
            if (foundLetters.indexOf(letter.character)!=-1) return false;
        }
        return true;
    }

    /**
     * Check that the pattern of same letters are actually the same
     * @param word to test
     * @param sameLetters index of each letter that are the same
     * @return true - word does not match
     */
    private boolean checkSameLetters(String word, List<Letter> sameLetters) {
        if (sameLetters.size()>1){
            int c = word.charAt(sameLetters.get(0).position);
            //check that letter has not already been found
            if (foundLetters.indexOf(c)!=-1) return true;
            //check if numbered letters are the same
            for (int i = 1; i< sameLetters.size(); i++){
                if (c!=word.charAt(sameLetters.get(i).position)) return true;
            }
            //check that the other letters in the word are different
            for (int i=0; i<word.length();i++){
                if (!containsPosition(i,sameLetters)){
                    if (c==word.charAt(i)) return true;
                }
            }
            //store for later use
            sameLetters.get(0).character = c;
        }
        return false;
    }
    private boolean containsPosition(int pos, List<Letter> letters){
        for (Letter letter : letters){
            if (pos==letter.position) return true;
        }
        return false;
    }


}
