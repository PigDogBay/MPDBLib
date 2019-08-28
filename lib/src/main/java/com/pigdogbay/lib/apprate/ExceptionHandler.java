package com.pigdogbay.lib.apprate;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.SharedPreferences;

public class ExceptionHandler implements UncaughtExceptionHandler {

	private UncaughtExceptionHandler defaultExceptionHandler;
	private SharedPreferences preferences;

	// Constructor.
	ExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler, Context context)
	{
		preferences = context.getSharedPreferences(PrefsContract.SHARED_PREFS_NAME, 0);
		defaultExceptionHandler = uncaughtExceptionHandler;
	}

	public void uncaughtException(Thread thread, Throwable throwable) {

		preferences.edit().putBoolean(PrefsContract.PREF_APP_HAS_CRASHED, true).apply();

		// Call the original handler.
		defaultExceptionHandler.uncaughtException(thread, throwable);
	}
}
