package com.pigdogbay.lib.utils;

import java.util.ArrayList;


public interface WordListCallback {
	void Update(String result);

	public static class FilterWrapper implements WordListCallback {

		ArrayList<String> matches = new ArrayList<String>();
		WordListCallback _Callback;

		public FilterWrapper(WordListCallback callback) {
			_Callback = callback;
		}

		@Override
		public void Update(String result) {
			if (!matches.contains(result))
			{
				matches.add(result);
				_Callback.Update(result);
			}
		}

	}
	
	public static class MissingLettersWrapper implements WordListCallback {
		
		String _OriginalWord;
		WordListCallback _Callback;
		
		public MissingLettersWrapper(String originalWord, WordListCallback callback)
		{
			_Callback = callback;
			_OriginalWord = originalWord;
		}

		@Override
		public void Update(String result) {
			String missingLetters = StringUtils.SubtractChars(_OriginalWord, result);
			_Callback.Update(result + " (" + missingLetters + ")");
		}
	
	}

}