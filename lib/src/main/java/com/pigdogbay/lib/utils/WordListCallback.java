package com.pigdogbay.lib.utils;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public interface WordListCallback {
	void Update(String result);

	class FilterWrapper implements WordListCallback {

		ArrayList<String> matches = new ArrayList<>();
		WordListCallback wrappedCallback;

		public FilterWrapper(WordListCallback callback) {
			wrappedCallback = callback;
		}

		@Override
		public void Update(String result) {
			if (!matches.contains(result))
			{
				matches.add(result);
				wrappedCallback.Update(result);
			}
		}

	}

	class BiggerThanFilter implements WordListCallback{
		private final WordListCallback wrappedCallback;

		private final int size;

		public BiggerThanFilter(WordListCallback wrappedCallback, int size) {
			this.wrappedCallback = wrappedCallback;
			this.size = size;
		}

		@Override
		public void Update(String result) {
			if (result.length()>size){
				wrappedCallback.Update(result);
			}
		}
	}

	class LessThanFilter implements WordListCallback{
		private final WordListCallback wrappedCallback;

		private final int size;

		public LessThanFilter(WordListCallback wrappedCallback, int size) {
			this.wrappedCallback = wrappedCallback;
			this.size = size;
		}

		@Override
		public void Update(String result) {
			if (result.length()<size){
				wrappedCallback.Update(result);
			}
		}
	}
	class EqualToFilter implements WordListCallback{
		private final WordListCallback wrappedCallback;

		private final int size;

		public EqualToFilter(WordListCallback wrappedCallback, int size) {
			this.wrappedCallback = wrappedCallback;
			this.size = size;
		}

		@Override
		public void Update(String result) {
			if (result.length()==size){
				wrappedCallback.Update(result);
			}
		}
	}
	class StartsWithFilter implements WordListCallback{
		private final WordListCallback wrappedCallback;
		private final String letters;

		public StartsWithFilter(WordListCallback wrappedCallback, String letters) {
			this.wrappedCallback = wrappedCallback;
			this.letters = letters;
		}

		@Override
		public void Update(String result) {
			if(result.startsWith(letters)) {
				wrappedCallback.Update(result);
			}
		}
	}
	class EndsWithFilter implements WordListCallback{
		private final WordListCallback wrappedCallback;
		private final String letters;

		public EndsWithFilter(WordListCallback wrappedCallback, String letters) {
			this.wrappedCallback = wrappedCallback;
			this.letters = letters;
		}

		@Override
		public void Update(String result) {
			if(result.endsWith(letters)) {
				wrappedCallback.Update(result);
			}
		}
	}
	class ContainsFilter implements WordListCallback{
		private final WordListCallback wrappedCallback;
		private final LetterSet letterSet;

		public ContainsFilter(WordListCallback wrappedCallback, String letters) {
			this.wrappedCallback = wrappedCallback;
			letterSet = new LetterSet(letters);
		}

		@Override
		public void Update(String result) {
			if (letterSet.isSupergram(result)){
				//Contains all letters
				wrappedCallback.Update(result);
			}
		}
	}
	class ContainsWordFilter implements  WordListCallback {
		private final WordListCallback wrappedCallback;
		private final String word;

		public ContainsWordFilter(WordListCallback wrappedCallback, String word) {
			this.wrappedCallback = wrappedCallback;
			this.word = word;
		}

		@Override
		public void Update(String result) {
			if (result.contains(word)){
				wrappedCallback.Update(result);
			}
		}
	}
	class ExcludesFilter implements WordListCallback{
		private final WordListCallback wrappedCallback;
		private final char[] letters;

		public ExcludesFilter(WordListCallback wrappedCallback, String letters) {
			this.wrappedCallback = wrappedCallback;
			this.letters = letters.toCharArray();
		}

		@Override
		public void Update(String result) {
			for (char c : letters){
				if (result.indexOf(c)!=-1){
					//banned letter found
					return;
				}
			}
			//Does not contain banned letter
			wrappedCallback.Update(result);
		}
	}

	class CrosswordFilter implements WordListCallback {
		private final WordListCallback wrappedCallback;
		private final Pattern pattern;

		public CrosswordFilter(WordListCallback wrappedCallback, String letters) {
			this.wrappedCallback = wrappedCallback;
			letters = letters.toLowerCase(Locale.US);
			letters = letters.replace(".", "[a-z]");
			letters = letters.replace("?", "[a-z]");
			letters = letters.replace("@", "[a-z]+");
			pattern = Pattern.compile("\\b"+letters+"\\b");
		}

		@Override
		public void Update(String result) {
			if (pattern.matcher(result).matches())
			{
				wrappedCallback.Update(result);
			}
		}
	}
	class RegexFilter implements WordListCallback {
		private final WordListCallback wrappedCallback;
		private final Pattern pattern;

		public RegexFilter(WordListCallback wrappedCallback, String letters) {
			this.wrappedCallback = wrappedCallback;
			Pattern compiled;
			try {
				compiled = Pattern.compile(letters, Pattern.CASE_INSENSITIVE);
			} catch (PatternSyntaxException e){
				//blocks any match
				compiled = Pattern.compile("");
			}
			this.pattern = compiled;
		}

		@Override
		public void Update(String result) {
			if (pattern.matcher(result).matches())
			{
				wrappedCallback.Update(result);
			}
		}
	}

}