package com.pigdogbay.lib.utils;

import java.util.ArrayList;
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
    public void newSearch(String query, WordSearch.SearchType searchType){
        matches.clear();
        this.query = query;
        this.searchType = searchType;
        switch (searchType){
            case Anagram:
                formatter = new WordMatches.SubAnagramFormatting();
                break;
            case Blanks:
            case Supergram:
                formatter = new WordMatches.BlankFormatting(query);
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
            this.originalWord = originalWord.replace("+", "");
            missingLetters = new MissingLetters(query);
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
}
