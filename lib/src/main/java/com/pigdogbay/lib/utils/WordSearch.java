package com.pigdogbay.lib.utils;

import java.util.Locale;


/*
 * Query parser for WordList
 * 
 * The following steps are needed to perform a word search
 * 
 * query = _WordSearch.preProcessQuery(query);
 * SearchType type = _WordSearch.getQueryType(query); 
 * query = _WordSearch.postProcessQuery(query, type);
 * _WordSearch.runQuery(query, type, callback);
 */
public class WordSearch {
    public enum SearchType {
        Crossword,
        Anagram,
        TwoWordAnagram,
        Wildcard,
        WildcardAndCrossword,
        Blanks,
        Supergram,
        Codeword
    }

    public static final char CROSSWORD_CHAR = '.';
    public static final char CROSSWORD_CHAR_ALTERNATIVE = '?';
    public static final char TWOWORD_CHAR = ' ';
    public static final char TWOWORD_CHAR_ALTERNATIVE = '-';
    public static final char WILDCARD_CHAR = '#';
    public static final char WILDCARD_CHAR_ALTERNATIVE = '@';
    public static final char BLANK_CHAR = '+';
    public static final char SUPERGRAM_CHAR = '*';
    public static final char LOWEST_ASCII_VALUE = ' ';
    public static final char CODEWORD_CHAR = '1';
    public static final char HIGHEST_ASCII_VALUE = 'z';
    public static final String CROSSWORD_STR = ".";
    public static final String TWOWORD_STR = " ";
    public static final String WILDCARD_STR = "#";
    public static final String BLANK_STR = "+";
    public static final String SUPERGRAM_STR = "*";
    public static final String CODEWORD_STR = "1";


    public final static int MAX_WORD_LEN = 42;

    private static final char DEL_CHAR = 'X';
    private static final String DEL_STR = "X";
    private WordList _WordList;
    private boolean _FindSubAnagrams = true;
    private boolean isThreeWordAnagramsEnabled = true;
    private CodewordSolver codewordSolver;

    public void setFindSubAnagrams(boolean value) {
        _FindSubAnagrams = value;
    }
    public void setThreeWordAnagramsEnabled(boolean threeWordAnagramsEnabled) {
        this.isThreeWordAnagramsEnabled = threeWordAnagramsEnabled;
    }


    public WordSearch(WordList list) {
        _WordList = list;
    }

    public CodewordSolver getCodewordSolver() {
        if (codewordSolver==null){
            codewordSolver = new CodewordSolver();
        }
        return codewordSolver;
    }


    /**
     * Remove non-ascii chars
     * chop down to max length
     * trim whitespace
     * lowercase
     */
    public String clean(String raw) {
        if (raw.length() == 0) {
            return "";
        }
        StringBuilder sbuff = new StringBuilder();
        for (int i = 0; i < raw.length() && i <= MAX_WORD_LEN; i++) {
            char c = raw.charAt(i);
            if (c >= LOWEST_ASCII_VALUE && c <= HIGHEST_ASCII_VALUE) {
                sbuff.append(c);
            }
        }
        return sbuff
                .toString()
                .trim()
                .toLowerCase(Locale.US);
    }

    public String preProcessQuery(String query) {
        if (isCodeWord(query))
        {
            query = query
                    .toLowerCase(Locale.US)
                    .replace(CROSSWORD_CHAR_ALTERNATIVE, CROSSWORD_CHAR);
        }
        else {
            query = query
                    .toLowerCase(Locale.US)
                    .replace(WILDCARD_CHAR_ALTERNATIVE, WILDCARD_CHAR)
                    .replace(CROSSWORD_CHAR_ALTERNATIVE, CROSSWORD_CHAR)
                    .replace(TWOWORD_CHAR_ALTERNATIVE, TWOWORD_CHAR)
                    .replace("1", ".")
                    .replace("2", "..")
                    .replace("3", "...")
                    .replace("4", "....")
                    .replace("5", ".....")
                    .replace("6", "......")
                    .replace("7", ".......")
                    .replace("8", "........")
                    .replace("9", ".........");
        }
        if (query.length() > MAX_WORD_LEN) {
            query = query.substring(0, MAX_WORD_LEN);
        }
        return query;
    }

    public SearchType getQueryType(String query) {
        if (query.contains(WILDCARD_STR) && query.contains(CROSSWORD_STR)) {
            return SearchType.WildcardAndCrossword;
        } else if (query.contains(WILDCARD_STR)) {
            return SearchType.Wildcard;
        } else if (isCodeWord(query)) {
            return SearchType.Codeword;
        } else if (query.contains(CROSSWORD_STR)) {
            return SearchType.Crossword;
        } else if (query.contains(TWOWORD_STR)) {
            return SearchType.TwoWordAnagram;
        } else if (query.contains(BLANK_STR)) {
            return SearchType.Blanks;
        } else if (query.contains(SUPERGRAM_STR)) {
            return SearchType.Supergram;
        }
        return SearchType.Anagram;
    }

    public String postProcessQuery(String query, SearchType type) {
        switch (type) {
            case Anagram:
                //Keep a-z, remove any other char
                query = stripChars(query, DEL_CHAR, DEL_CHAR);
                break;
            case Crossword:
                //keep a-z and .
                query = stripChars(query, CROSSWORD_CHAR, DEL_CHAR);
                break;
            case Blanks:
                //keep a-z and +
                query = stripChars(query, BLANK_CHAR, DEL_CHAR);
                break;
            case Supergram:
                //keep a-z and *
                query = stripChars(query, SUPERGRAM_CHAR, DEL_CHAR);
                break;
            case TwoWordAnagram:
                //keep a-z and ' '
                query = stripChars(query, TWOWORD_CHAR, DEL_CHAR);
                break;
            case Wildcard:
                //keep a-z and #
                query = stripChars(query, WILDCARD_CHAR, DEL_CHAR);
                break;
            case WildcardAndCrossword:
                //keep a-z . and #
                query = stripChars(query, CROSSWORD_CHAR, WILDCARD_CHAR);
                break;
            case Codeword:
                query = stripCharsForCodeWord(query);
                break;
            default:
                query = "";
                break;
        }
        return query;
    }

    /**
     * Removed any cruft that might upset the word list algorithms
     */
    private String stripChars(String s, char except1, char except2) {
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if ((c < 'a' || c > 'z') && c != except1 && c != except2) {
                chars[i] = DEL_CHAR;
            }
        }
        return new String(chars).replace(DEL_STR, "");
    }
    private String stripCharsForCodeWord(String s) {
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if ((c < 'a' || c > 'z') && c != CROSSWORD_CHAR ) {
                if (c < '1' || c > '9') {
                    chars[i] = DEL_CHAR;
                }
            }
        }
        return new String(chars).replace(DEL_STR, "");
    }

    public void runQuery(String query, SearchType type, WordListCallback callback) {
        _WordList.reset();
        switch (type) {
            case Anagram:
                _WordList.FindAnagrams(query, callback);
                if (_FindSubAnagrams
                        && query.length() <= MAX_WORD_LEN) {
                    WordListCallback filterWrapper = new WordListCallback.FilterWrapper(
                            callback);
                    _WordList.FindSubAnagrams(query, filterWrapper);
                }
                break;
            case Crossword:
                _WordList.FindPartialWords(query, callback);
                break;
            case Blanks:
                int numberOfBlanks = query.length();
                query = query.replace("+", "");
                numberOfBlanks = numberOfBlanks - query.length();
                _WordList.FindAnagrams(query, numberOfBlanks, callback);
                break;
            case Supergram:
                query = query.replace("*", "");
                _WordList.FindSupergrams(query, callback, 0);
                break;
            case TwoWordAnagram:
                String[] words = query.split(" ");
                if (isThreeWordAnagramsEnabled && words.length > 2){
                    _WordList.FindMultiwordAnagrams(words[0],words[1],words[2],callback);
                } else {
                    _WordList.FindMultiwordAnagrams(words[0] + words[1], words[0].length(), callback);
                }
                break;
            case Wildcard:
                _WordList.FindWildcardWords(query, callback);
                break;
            case WildcardAndCrossword:
                _WordList.FindWildcardWords(query, callback);
                break;
            case Codeword:
                getCodewordSolver().parse(query);
                _WordList.findCodewords(getCodewordSolver(),callback);
                break;
            default:
                break;

        }
    }

    public static String toString(SearchType type) {
        switch (type) {
            case Anagram:
                return "anagram";
            case Crossword:
                return "crossword";
            case Blanks:
                return "supergram";
            case Supergram:
                return "supergram wild";
            case TwoWordAnagram:
                return "two word anagram";
            case Wildcard:
                return "wildcard";
            case WildcardAndCrossword:
                return "wildcard and crossword";
            case Codeword:
                return "codeword";
        }
        return "undefined";
    }

    private boolean isCodeWord(String query) {
        int index = query.indexOf(CODEWORD_CHAR);
        if (index != -1) {
            return query.indexOf(CODEWORD_CHAR, index+1) != -1;
        }
        return false;
    }
}
