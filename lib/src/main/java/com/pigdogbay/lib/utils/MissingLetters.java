package com.pigdogbay.lib.utils;

/**
 * When doing anagram searches with blank letters, this class will help to highlight the letters in matching words that were used for the blank
 * I use a class instead of a static function, to cut down on object creation as an anagram search will usually return over 500 results
 */
public class MissingLetters
{
    //No word greater than 64 chars
    private static final int BUFFER_SIZE = 64;
    private final char[] buffer = new char[BUFFER_SIZE];
    private final int[] positions = new int[BUFFER_SIZE];
    private final int lettersLen;
    private final String letters;
    private final StringBuilder builder = new StringBuilder();

    /**
     * @param letters - letters of anagram, not including blanks
     */
    public MissingLetters(String letters){
        this.letters = letters;
        lettersLen = letters.length();
    }

    /**
     * Find the position of a missing letter in the word used in findPositions
     * @param index into array of letter positions
     * @return letter position in the word
     */
    public int getPositionAt(int index) {
        return positions[index];
    }

    /**
     * Missing letters = word letters - anagram letters
     * call getPositionAt() to access the positions of the letters in word
     *
     * @param word to find missing letters
     * @return number of missing letters found
     */
    public int findPositions(String word){
        int wordLen = word.length();
        letters.getChars(0, lettersLen, buffer, 0);

        int index=0;
        for (int i=0;i<wordLen;i++){
            int pos = ArrayUtils.indexOf(buffer,word.charAt(i), lettersLen);
            if (pos==-1) {
                positions[index] = i;
                index++;
            } else {
                //remove char to stop it being found again
                buffer[pos] = '\0';
            }
        }
        return index;
    }

    /**
     * Note adjacent missing letters are grouped into the same span
     * @param word string to highlight
     * @param prefix string to prefix each missing letter
     * @param suffix string to suffix each missing letter
     * @return string with any missing letters spanned with the prefix and suffix
     */
    public String highlightMissingLetters(String word, String prefix, String suffix){
        int wordLen = word.length();
        int numberOfMissingLetters = findPositions(word);
        builder.setLength(0);
        for (int i=0;i<wordLen;i++) {
            if (ArrayUtils.contains(positions,i,numberOfMissingLetters)){
                builder.append(prefix);
                builder.append(word.charAt(i));
                builder.append(suffix);
            }
            else
            {
                builder.append(word.charAt(i));
            }
        }
        //Span adjacent highlighted letters
        String span = suffix+prefix;
        int index;
        while((index = builder.indexOf(span)) != -1){
            builder.replace(index, index+span.length(),"");
        }
        return builder.toString();
    }

}
