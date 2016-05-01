package com.pigdogbay.lib.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class StringUtils {
	public static final String LOWERCASE_ALPHABET = "abcdefghijklmnopqrstuvwxyz";

	public static String WordSort(String word) {
		char[] letters = word.toCharArray();
		Arrays.sort(letters);
		return new String(letters);
	}

	
	/**
	 * Returns all the words that are one letter less, eg ABCD returns BCD, ACD,ABD, ABC
	 * 
	 * @param word
	 * @return list of the subwords
	 */
	public static List<String> GetSubWords(String word) {
		word = WordSort(word);
		ArrayList<String> subwords = new ArrayList<String>();
		int len = word.length();
		if (len > 1) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < len; i++) {
				sb.append(word);
				sb.deleteCharAt(i);
				String newWord = sb.toString();
				sb.setLength(0);
				if (!subwords.contains(newWord)) {
					subwords.add(newWord);
				}
			}
		}
		return subwords;
	}

	/**
	 * Recusively finds subwords
	 * 
	 * If word is 9 letters and min length =6, expect to get a list of 9*8*7 + 9*8 + 9
	 * For example,  abcdefghi will take around 20ms and create 585 words.
	 * 
	 * @param word (not include)
	 * @param minLen min length of word to find, will not search for words smaller than this
	 * @return unsorted list of sub-words
	 */
	public static List<String> GetSubWords(String word, int minLen) {
		ArrayList<String> agregate = new ArrayList<String>();
		if (word.length() > minLen) {
			List<String> sublist = GetSubWords(word);
			agregate.addAll(sublist);
			for (String subword : sublist) {
				agregate.addAll(GetSubWords(subword, minLen));
			}
		}
		return agregate;
	}

	/*
	 * Performs the reverse operation to GetSubWords
	 */
	public static char GetMissingLetter(String word, String subWord) {
		String remainder = SubtractChars(word, subWord);
		return remainder.charAt(0);
	}

	/*
	 * Subtracts the letters of littleword from bigword Returns: a string of the
	 * remainder chars
	 */
	public static String SubtractChars(String bigword, String littleword) {
		StringBuilder sbBig = new StringBuilder(bigword);
		for (char c : littleword.toCharArray()) {
			for (int i = 0; i < sbBig.length(); i++) {
				if (sbBig.charAt(i) == c) {
					sbBig.deleteCharAt(i);
					break;
				}
			}
		}
		return sbBig.toString();
	}

	/*
	 * Checks if the word contains any of the banned letters.
	 * 
	 * Returns true if word does not contain any of the banned letters
	 */
	public static boolean DoesNotContainBannedLetters(String word,
			char[] bannedLetters) {
		char[] chars = word.toCharArray();
		for (char c : bannedLetters) {
			for (int i = 0; i < chars.length; i++) {
				if (c == chars[i]) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static void sortByLengthReverse(List<String> list){
		Collections.sort(list, new Comparator<String>() {
			@Override
			public int compare(String lhs, String rhs) {
				return rhs.length()-lhs.length();
			}
		});
	}

	/**
	 * Converts the string into an integer value, e.g. "123" gives 123 To
	 * convert string to int, use Integer.toString(value)
	 * 
	 * Uses US Locale
	 * 
	 * @param s
	 *            - string value to parse
	 * @param defaultValue
	 *            - value to return if an exception occurs
	 * @return integer value of the string
	 */
	public static int parseInt(String s, int defaultValue) {
		try {
			defaultValue = Integer.parseInt(s);
		} catch (Exception ex) {
		}
		return defaultValue;
	}

	/**
	 * Converts the string into a long value, e.g. "123" gives 123 To convert
	 * string to long, use Long.toString(value)
	 * 
	 * Uses US Locale
	 * 
	 * @param s
	 *            - string value to parse
	 * @param defaultValue
	 *            - value to return if an exception occurs
	 * @return long value of the string
	 */
	public static long parseLong(String s, long defaultValue) {
		try {
			defaultValue = Long.parseLong(s);
		} catch (Exception ex) {
		}
		return defaultValue;
	}

	/**
	 * Converts the string into a double value, e.g. "123.4" gives 123.4 To
	 * convert string to double, use Double.toString(value)
	 * 
	 * Uses US Locale
	 * 
	 * @param s
	 *            - string value to parse
	 * @param defaultValue
	 *            - value to return if an exception occurs
	 * @return double value of the string
	 */
	public static double parseDouble(String s, double defaultValue) {
		try {
			defaultValue = Double.parseDouble(s);
		} catch (Exception ex) {
		}
		return defaultValue;
	}

	/**
	 * Converts the string into a double value, e.g. "true" gives true To
	 * convert string to boolean, use Boolean.toString(value)
	 * 
	 * Uses US Locale
	 * 
	 * @param s
	 *            - string value to parse
	 * @param defaultValue
	 *            - value to return if an exception occurs
	 * @return boolean value of the string
	 */
	public static boolean parseBoolean(String s, boolean defaultValue) {
		try {
			defaultValue = Boolean.parseBoolean(s);
		} catch (Exception ex) {
		}
		return defaultValue;
	}

}
