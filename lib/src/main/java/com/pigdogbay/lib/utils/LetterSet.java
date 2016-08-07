package com.pigdogbay.lib.utils;

/**
 * @author Mark
 * 
 * Class to help compare groups of lowercase letters
 *
 */
public class LetterSet 
{
	private int[] setA = new int[26];
	private int[] setB = new int[26];
	private int LOWEST_CHAR_VALUE = (int)'a';
	private char[] buffer = new char[64];

	
	public LetterSet(String word)
	{
		add(word);
	}
	
	public void clear()
	{
		for (int i=0; i<26; i++)
		{
			setA[i]=0;
		}
	}
	
	public void add(String word)
	{
		int len = word.length();
		word.getChars(0, len, buffer, 0);
		for (int i=0; i<len; i++)
		{
			int index = ((int)buffer[i])-LOWEST_CHAR_VALUE;
			setA[index]++;
		}
	}
	
	public void delete(String word)
	{
		int len = word.length();
		word.getChars(0, len, buffer, 0);
		for (int i=0; i<len; i++)
		{
			int index = ((int)buffer[i])-LOWEST_CHAR_VALUE;
			setA[index]--;
		}
	}
	
	public boolean isValid()
	{
		for (int i=0; i<26; i++)
		{
			if (setA[i]<0)
			{
				return false;
			}
		}
		return true;
	}
	public boolean isAnagram(String word, int numberOfBlanks){
		clearSetB();
		addToSetB(word);
		for (int i=0; i<26; i++)
		{
			setB[i] = setB[i]-setA[i];
			if (setB[i]<0)
			{
				setB[i] = 0;
			}
		}
		int count = countSet(setB);
		return count <=numberOfBlanks;
	}
	public boolean isAnagram(String word)
	{
		clearSetB();
		addToSetB(word);
		return isAIdenticalToB();
	}
	public boolean isSupergram(String word)
	{
		clearSetB();
		addToSetB(word);
		return isASubsetOfB();
	}
	public boolean isSubgram(String word)
	{
		clearSetB();
		addToSetB(word);
		return isASupersetOfB();
	}
	
	private boolean isASupersetOfB()
	{
		for (int i=0; i<26; i++)
		{
			if (setA[i]<setB[i])
			{
				return false;
			}
		}
		return true;
	}

	private boolean isASubsetOfB()
	{
		for (int i=0; i<26; i++)
		{
			if (setA[i]>setB[i])
			{
				return false;
			}
		}
		return true;
	}

	private boolean isAIdenticalToB()
	{
		for (int i=0; i<26; i++)
		{
			if (setA[i]!=setB[i])
			{
				return false;
			}
		}
		return true;
	}
	private void clearSetB()
	{
		for (int i=0; i<26; i++)
		{
			setB[i]=0;
		}
	}
	private void addToSetB(String word)
	{
		int len = word.length();
		word.getChars(0, len, buffer, 0);
		for (int i=0; i<len; i++)
		{
			int index = ((int)buffer[i])-LOWEST_CHAR_VALUE;
			setB[index]++;
		}
	}

	private int countSet(int[] set){
		int count = 0;
		for (int i=0; i<26;i++){
			count = count + set[i];
		}
		return count;
	}

}

