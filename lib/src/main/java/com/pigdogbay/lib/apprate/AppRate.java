package com.pigdogbay.lib.apprate;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Activity;
import androidx.appcompat.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.text.format.DateUtils;
import android.widget.Toast;

import com.pigdogbay.lib.R;

/* Based on code from
 * AppRate From TimotheeJeanin
 * http://github.com/TimotheeJeannin/AppRate
 */
public class AppRate implements android.content.DialogInterface.OnClickListener, OnCancelListener {

	private static final String TAG = "AppRater";

	private SharedPreferences preferences;
	private Activity hostActivity;
	private AlertDialog.Builder dialogBuilder = null;
	private int styleId = R.style.AppRateAlertDialogStyle;

	private long minLaunchesUntilPrompt = 10;
	private long minDaysUntilPrompt = 7;

	private boolean showIfHasCrashed = true;

	public AppRate(Activity hostActivity) {
		this.hostActivity = hostActivity;
		preferences = hostActivity.getSharedPreferences(PrefsContract.SHARED_PREFS_NAME, 0);
	}

	public AppRate setStyleId(int id){
		styleId = id;
		return this;
	}
	/**
	 * @param minLaunchesUntilPrompt The minimum number of days before showing the rate dialog.<br/>
	 *            Default value is 7 days.
	 * @return This {@link AppRate} object to allow chaining.
	 */
	public AppRate setMinLaunchesUntilPrompt(long minLaunchesUntilPrompt) {
		this.minLaunchesUntilPrompt = minLaunchesUntilPrompt;
		return this;
	}

	/**
	 * @param minDaysUntilPrompt The minimum number of times the user lunches the application before showing the rate dialog.<br/>
	 *            Default value is 10 times.
	 * @return This {@link AppRate} object to allow chaining.
	 */
	public AppRate setMinDaysUntilPrompt(long minDaysUntilPrompt) {
		this.minDaysUntilPrompt = minDaysUntilPrompt;
		return this;
	}

	/**
	 * @param showIfCrash If <code>false</code> the rate dialog will not be shown if the application has crashed once.<br/>
	 *            Default value is <code>false</code>.
	 * @return This {@link AppRate} object to allow chaining.
	 */
	public AppRate setShowIfAppHasCrashed(boolean showIfCrash) {
		showIfHasCrashed = showIfCrash;
		return this;
	}

	/**
	 * Use this method if you want to customize the style and content of the rate dialog.<br/>
	 * When using the {@link AlertDialog.Builder} you should use:
	 * <ul>
	 * <li>{@link AlertDialog.Builder#setPositiveButton} for the <b>rate</b> button.</li>
	 * <li>{@link AlertDialog.Builder#setNeutralButton} for the <b>rate later</b> button.</li>
	 * <li>{@link AlertDialog.Builder#setNegativeButton} for the <b>never rate</b> button.</li>
	 * </ul>
	 * @param customBuilder The custom dialog you want to use as the rate dialog.
	 * @return This {@link AppRate} object to allow chaining.
	 */
	public AppRate setCustomDialog(AlertDialog.Builder customBuilder) {
		dialogBuilder = customBuilder;
		return this;
	}

	/**
	 * Reset all the data collected about number of launches and days until first launch.
	 * @param context A context.
	 */
	public static void reset(Context context) {
		context.getSharedPreferences(PrefsContract.SHARED_PREFS_NAME, 0).edit().clear().apply();
	}

	/**
	 * Display the rate dialog if needed.
	 */
	public void init() {

		if (preferences.getBoolean(PrefsContract.PREF_DONT_SHOW_AGAIN, false) || (
				preferences.getBoolean(PrefsContract.PREF_APP_HAS_CRASHED, false) && !showIfHasCrashed)) {
			return;
		}

		if (!showIfHasCrashed) {
			initExceptionHandler();
		}

		Editor editor = preferences.edit();

		// Get and increment launch counter.
		long launch_count = preferences.getLong(PrefsContract.PREF_LAUNCH_COUNT, 0L) + 1L;
		editor.putLong(PrefsContract.PREF_LAUNCH_COUNT, launch_count);

		// Get date of first launch.
		long date_firstLaunch = preferences.getLong(PrefsContract.PREF_DATE_FIRST_LAUNCH, 0L);
		if (date_firstLaunch == 0) {
			date_firstLaunch = System.currentTimeMillis();
			editor.putLong(PrefsContract.PREF_DATE_FIRST_LAUNCH, date_firstLaunch);
		}

		// Show the rate dialog if needed.
		if (launch_count >= minLaunchesUntilPrompt) {
			if (System.currentTimeMillis() >= date_firstLaunch + (minDaysUntilPrompt * DateUtils.DAY_IN_MILLIS)) {

				if (dialogBuilder != null) {
					showDialog(dialogBuilder);
				} else {
					showDefaultDialog();
				}
			}
		}

		editor.apply();
	}

	/**
	 * Initialize the {@link ExceptionHandler}.
	 */
	private void initExceptionHandler() {

		UncaughtExceptionHandler currentHandler = Thread.getDefaultUncaughtExceptionHandler();

		// Don't register again if already registered.
		if (!(currentHandler instanceof ExceptionHandler)) {

			// Register default exceptions handler.
			Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(currentHandler, hostActivity));
		}
	}

	/**
	 * Shows the default rate dialog.
	 */
	private void showDefaultDialog() {

		String title = "Rate " + getApplicationName(hostActivity.getApplicationContext());
		String message = "If you enjoy using " + getApplicationName(hostActivity.getApplicationContext()) + ", please take a moment to rate it. Thanks for your support!";
		String rate = "Rate Now";
		String remindLater = "later";
		String dismiss = "No thanks";

		new AlertDialog.Builder(hostActivity, styleId)
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton(rate, this)
				.setNegativeButton(dismiss, this)
				.setNeutralButton(remindLater, this)
				.setOnCancelListener(this)
				.create().show();
	}

	/**
	 * Show the custom rate dialog.
	 */
	private void showDialog(AlertDialog.Builder builder) {

		AlertDialog dialog = builder.create();
		dialog.show();

		String rate = (String) dialog.getButton(AlertDialog.BUTTON_POSITIVE).getText();
		String remindLater = (String) dialog.getButton(AlertDialog.BUTTON_NEUTRAL).getText();
		String dismiss = (String) dialog.getButton(AlertDialog.BUTTON_NEGATIVE).getText();

		dialog.setButton(AlertDialog.BUTTON_POSITIVE, rate, this);
		dialog.setButton(AlertDialog.BUTTON_NEUTRAL, remindLater, this);
		dialog.setButton(AlertDialog.BUTTON_NEGATIVE, dismiss, this);

		dialog.setOnCancelListener(this);
	}

	@Override
	public void onCancel(DialogInterface dialog) {

		Editor editor = preferences.edit();
		editor.putLong(PrefsContract.PREF_DATE_FIRST_LAUNCH, System.currentTimeMillis());
		editor.putLong(PrefsContract.PREF_LAUNCH_COUNT, 0);
		editor.apply();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

		Editor editor = preferences.edit();

		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			try
			{
				hostActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + hostActivity.getPackageName())));
			}
			catch (ActivityNotFoundException e)
			{ 
					Toast.makeText(hostActivity, PrefsContract.PREF_UNABLE_TO_FIND_MARKET_APP, Toast.LENGTH_LONG)
						.show();
			}
			editor.putBoolean(PrefsContract.PREF_DONT_SHOW_AGAIN, true);
			break;

		case DialogInterface.BUTTON_NEGATIVE:
			editor.putBoolean(PrefsContract.PREF_DONT_SHOW_AGAIN, true);
			break;

		case DialogInterface.BUTTON_NEUTRAL:
			editor.putLong(PrefsContract.PREF_DATE_FIRST_LAUNCH, System.currentTimeMillis());
			editor.putLong(PrefsContract.PREF_LAUNCH_COUNT, 0);
			break;

		default:
			break;
		}

		editor.apply();
		dialog.dismiss();
	}
	/**
	 * @param context A context of the current application.
	 * @return The application name of the current application.
	 */
	private static String getApplicationName(Context context) {
		final PackageManager packageManager = context.getPackageManager();
		ApplicationInfo applicationInfo;
		try {
			applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
		} catch (final NameNotFoundException e) {
			applicationInfo = null;
		}
		return (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "(unknown)");
	}
}
