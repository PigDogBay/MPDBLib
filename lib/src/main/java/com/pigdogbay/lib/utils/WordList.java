package com.pigdogbay.lib.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class WordList {
	private volatile boolean _Stop;
	private List<String> _WordList;

	/**
	 * Signal to stop any searches
	 */
	public void stop()
	{
		_Stop = true;
	}
	
	public void reset()
	{
		_Stop = false;
	}
	/*
	 * Word list must be sorted and all lower case
	 */
	public void SetWordList(List<String> wordList) {
		_WordList = wordList;
	}

	public WordList() {
		_WordList = null;
	}

	public void FindSupergrams(String anagram, WordListCallback callback, int length)
	{
		int anagramLength = anagram.length();
		LetterSet set = new LetterSet(anagram);
		for (String word : _WordList)
		{
			if (_Stop){break;}
			if ((length==0 && word.length()>anagramLength)
					||(word.length()==length))
			{
				if (set.isSupergram(word))
				{
					callback.Update(word);
				}
			}
		}
	}
	public void FindAnagrams(String anagram, int numberOfBlanks, WordListCallback callback)
	{
		int len = anagram.length();
		LetterSet set = new LetterSet(anagram);
		int too_big = len+numberOfBlanks+1;
		for (String word : _WordList)
		{
			if (_Stop){break;}
			if (word.length()<too_big)
			{
				if (set.isAnagram(word,numberOfBlanks))
				{
					callback.Update(word);
				}
			}
		}
	}
	public void FindAnagramsExactLength(String anagram, int numberOfBlanks, WordListCallback callback)
	{
		int len = anagram.length()+numberOfBlanks;
		LetterSet set = new LetterSet(anagram);
		for (String word : _WordList)
		{
			if (_Stop){break;}
			if (word.length()==len)
			{
				if (set.isAnagram(word,numberOfBlanks))
				{
					callback.Update(word);
				}
			}
		}
	}

	public void FindAnagrams(String anagram, WordListCallback callback) 
	{
		int len = anagram.length();
		LetterSet set = new LetterSet(anagram);
		for (String word : _WordList)
		{
			if (_Stop){break;}
			if (word.length()==len)
			{
				if (set.isAnagram(word))
				{
					callback.Update(word);
				}
			}
		}
	}

	public void FindSubAnagrams(String anagram, WordListCallback callback) 
	{
		int len = anagram.length();
		if (len==1)
		{
			return;
		}
		LetterSet set = new LetterSet(anagram);
		for (String word : _WordList)
		{
			if (_Stop){break;}
			int wordLen = word.length();
			if (wordLen<len)
			{
				if (set.isSubgram(word))
				{
					callback.Update(word);
				}
			}
		}
	}

	public void FindPartialWords(String partialWord, WordListCallback callback) 
	{
		int length = partialWord.length();
		Pattern pattern = CreatePattern(partialWord);
		for (String word : _WordList) 
		{
			if (_Stop){break;}
			if (word.length() == length) 
			{
				if (pattern.matcher(word).matches()) 
				{
					callback.Update(word);
				}
			}
		}
	}

	public void FindWildcardWords(String wildcard, WordListCallback callback) 
	{
		Pattern pattern = CreatePattern(wildcard);
		for (String word : _WordList) 
		{
			if (_Stop){break;}
			if (pattern.matcher(word).matches()) 
			{
				callback.Update(word);
			}
		}
	}

	private Pattern CreatePattern(String s) 
	{
		s = s.toLowerCase(Locale.US);
		s = s.replace(".", "[a-z]");
		s = s.replace("#", "[a-z]+");
		return Pattern.compile(s, Pattern.CASE_INSENSITIVE);
	}

	/**
        Filter the word list so that it contains letters that are same size as word1
        and is a subset of the letters contained in word1+word2
        
        Create another filtered list if word2 is different length to word1
    
        swap lists, we want the smallest to cut down on some processing
    
        for each word in the first list find the unused letters
        
        Then for each word in the second list see if it is an anagram of the unused letters,
        if it is the first and second words make a two word anagram	 
        */
	public void FindMultiwordAnagrams(String word1, String word2,
			WordListCallback callback) 
	{
		LetterSet superset = new LetterSet(word1+word2);
		List<String> listA = getFilteredList(superset, word1.length());
		List<String> listB = listA;
		if (word1.length() != word2.length()){
			listB = getFilteredList(superset, word2.length());
		}
		for (String first : listA)
		{
			superset.clear();
			superset.add(word1);
			superset.add(word2);
			superset.delete(first);
			for (String second : listB)
			{
				if (_Stop){break;}
				if (superset.isAnagram(second))
				{
					callback.Update(first+" "+second);
				}
			}
		}
	}
	public void FindMultiwordAnagrams(String word1, String word2, String word3,
									  WordListCallback callback)
	{
		LetterSet superset = new LetterSet(word1+word2+word3);
		List<String> listA = getFilteredList(superset, word1.length());
		List<String> listB = listA;
		List<String> listC;
		if (word1.length() != word2.length()){
			listB = getFilteredList(superset, word2.length());
		}

		if (word3.length() == word1.length()){
			listC = listA;
		} else if (word3.length() == word2.length()){
			listC = listB;
		} else {
			listC = getFilteredList(superset, word3.length());
		}
		ArrayList<String> sublistB = new ArrayList<>();
		boolean are2And3SameLength = word2.length() == word3.length();
		ArrayList<String> sublistC = are2And3SameLength ? sublistB : new ArrayList<>();

		for (String first : listA)
		{
			//Prune lists B and C of any words that are impossible with first
			superset.clear();
			superset.add(word1);
			superset.add(word2);
			superset.add(word3);
			superset.delete(first);
			sublistB.clear();
			filterList(superset,word2.length(),sublistB,listB);
			if (!are2And3SameLength) {
				sublistC.clear();
                filterList(superset, word3.length(), sublistC, listC);
            }

			for (String second : sublistB)
			{
				superset.clear();
				superset.add(word1);
				superset.add(word2);
				superset.add(word3);
				superset.delete(first);
				superset.delete(second);
				for (String third : sublistC){
					if (_Stop){return;}
					if (superset.isAnagram(third))
					{
						callback.Update(first+" "+second+" "+third);
					}
				}
			}
		}
 	}
	private void filterList(LetterSet set, int length, ArrayList<String> matches, List<String> wordList)
	{
		for (String word : wordList)
		{
			if (_Stop){break;}
			if (word.length() == length && set.isSubgram(word))
			{
				matches.add(word);
			}
		}
	}

	/*
		Tries to find all word size combinations
	 */
	public void FindMultiwordAnagrams(String letters, int startLen, WordListCallback callback){
		int len = letters.length();
		int middleWordSize = len/2;

		//First show the users requested word sizes
		findOtherMultiwordAnagrams(letters, callback, startLen);

		//Show other words sizes, but skip the ones already shown
		if (startLen>middleWordSize){
			startLen = len-startLen;
		}
		for (int i = middleWordSize; i>0 ; i--)
		{
			if (i==startLen) continue;
			if (_Stop){break;}
			findOtherMultiwordAnagrams(letters, callback, i);
		}
	}

	private void findOtherMultiwordAnagrams(String letters, WordListCallback callback, int i) {
		String word1 = letters.substring(0,i);
		String word2 = letters.substring(i);
		FindMultiwordAnagrams(word1,word2,callback);
	}

	private List<String> getFilteredList(LetterSet set, int length)
	{
		ArrayList<String> matches = new ArrayList<>();
		for (String word : _WordList) 
		{
			if (_Stop){break;}
			if (word.length() == length && set.isSubgram(word))
			{
				matches.add(word);
			}
		}
		return matches;
	}

	public void findCodewords(CodewordSolver codewordSolver, WordListCallback callback){
		int expectedLength = codewordSolver.getWordLength();
		for (String word : _WordList) {
			if (_Stop) {
				break;
			}
			int len = word.length();
			if (len!=expectedLength)continue;
			if (codewordSolver.isMatch(word)){
				callback.Update(word);
			}
		}
	}

}
