package com.pigdogbay.lib.utils;

import java.util.ArrayList;


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
		private final char[] letters;

		public ContainsFilter(WordListCallback wrappedCallback, String letters) {
			this.wrappedCallback = wrappedCallback;
			this.letters = letters.toCharArray();
		}

		@Override
		public void Update(String result) {
			for (char c : letters){
				if (result.indexOf(c)==-1){
					//letter not found
					return;
				}
			}
			//Contains all letters
			wrappedCallback.Update(result);
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
}