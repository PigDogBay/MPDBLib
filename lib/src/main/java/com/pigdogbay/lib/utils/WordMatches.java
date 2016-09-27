package com.pigdogbay.lib.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Holds the results for a word search
 */
public class WordMatches
{
    private interface IWordFormatter {
        String format(String word);
    }

    private List<String> matches;
    private WordSearch.SearchType searchType;
    private String query;
    private IWordFormatter formatter;
    private IWordFormatter defaultFormatter;
    private String blankPrefix="<b>", blankSuffix = "</b>";
    private String unusedLettersPrefix=" (";
    private String unusedLettersSuffix = ")";

    protected List<String> getMatches() {
        return matches;
    }
    public void setBlankPrefix(String blankPrefix) {
        this.blankPrefix = blankPrefix;
    }
    public void setBlankSuffix(String blankSuffix) {
        this.blankSuffix = blankSuffix;
    }
    public void setUnusedLettersPrefix(String unusedLettersPrefix) {
        this.unusedLettersPrefix = unusedLettersPrefix;
    }
    public void setUnusedLettersSuffix(String unusedLettersSuffix) {
        this.unusedLettersSuffix = unusedLettersSuffix;
    }
    public WordSearch.SearchType getSearchType() {
        return searchType;
    }
    public String getQuery() {
        return query;
    }

    public WordMatches(){
        matches = new ArrayList<>();
        defaultFormatter = new WordMatches.NoFormatting();
        formatter = defaultFormatter;
    }

    public String getWord(int position){
        return matches.get(position);
    }
    public String getFormattedWord(String word){
        return formatter.format(word);
    }
    public int getCount(){
        return matches.size();
    }
    public void newSearch(String query, WordSearch.SearchType searchType){
        matches.clear();
        this.query = query;
        this.searchType = searchType;
        switch (searchType){
            case Anagram:
                formatter = new WordMatches.SubAnagramFormatting();
                break;
            case Blanks:
                formatter = new WordMatches.BlankFormatting(query);
                break;
            case Supergram:
                formatter = new WordMatches.SupergramFormatting(query);
                break;
            default:
                formatter = defaultFormatter;
        }
    }

    private class NoFormatting implements IWordFormatter{
        @Override
        public String format(String word) {
            return word;
        }
    }
    private class SubAnagramFormatting implements IWordFormatter{
        @Override
        public String format(String word) {
            if (word.length()<query.length()) {
                String unusedLetters = StringUtils.SubtractChars(query, word);
                if (unusedLetters.length() > 0) {
                    return word + unusedLettersPrefix + unusedLetters + unusedLettersSuffix;
                }
            }
            return word;
        }
    }
    private class BlankFormatting implements IWordFormatter{
        private final String originalWord;
        private final MissingLetters missingLetters;
        public BlankFormatting(String originalWord) {
            //remove blanks
            this.originalWord = originalWord.replace(WordSearch.BLANK_STR, "");
            missingLetters = new MissingLetters(originalWord);
        }

        @Override
        public String format(String word) {
            String unusedLetters = StringUtils.SubtractChars(originalWord, word);
            String formatted = missingLetters.highlightMissingLetters(word,blankPrefix,blankSuffix);
            if (unusedLetters.length() > 0) {
                return formatted + unusedLettersPrefix + unusedLetters + unusedLettersSuffix;
            }
            return formatted;
        }
    }
    private class SupergramFormatting implements IWordFormatter{
        private final MissingLetters missingLetters;
        public SupergramFormatting(String originalWord) {
            //remove supergram chars
            originalWord = originalWord.replace(WordSearch.SUPERGRAM_STR,"");
            missingLetters = new MissingLetters(originalWord);
        }
        @Override
        public String format(String word) {
            return missingLetters.highlightMissingLetters(word,blankPrefix,blankSuffix);
        }
    }

    public void sortAZ(){
        Collections.sort(matches, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return s.compareTo(t1);
            }
        });
    }
    public void sortZA(){
        Collections.sort(matches, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return t1.compareTo(s);
            }
        });
    }
    public void sortLengthAsc(){
        Collections.sort(matches, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                int sLen = s.length();
                int tLen = t1.length();
                if (sLen==tLen){
                    return s.compareTo(t1);
                }
                return sLen-tLen;
            }
        });
    }
    public void sortLengthDesc(){
        Collections.sort(matches, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                int sLen = s.length();
                int tLen = t1.length();
                if (sLen==tLen){
                    return s.compareTo(t1);
                }
                return tLen-sLen;
            }
        });
    }

}
