package com.pigdogbay.lib.utils;

import java.util.ArrayList;


public interface WordListCallback {
	void Update(String result);

	class FilterWrapper implements WordListCallback {

		ArrayList<String> matches = new ArrayList<>();
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
}