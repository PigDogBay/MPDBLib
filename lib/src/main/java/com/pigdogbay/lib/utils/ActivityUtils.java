package com.pigdogbay.lib.utils;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public final class ActivityUtils
{
	public static void ShowWebPage(Activity currentActivity, String url)
	{
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_BROWSABLE);
		intent.setData(Uri.parse(url));
		currentActivity.startActivity(intent);
	}
	public static void ShowAppOnMarketPlace(Activity currentActivity, int urlId)
	{
		try
		{
			ActivityUtils.ShowWebPage(currentActivity, currentActivity.getString(urlId));
		}
		catch (Exception e)
		{
			Toast.makeText(currentActivity, "Play Store Unavailable", Toast.LENGTH_LONG)
					.show();
		}
	}
	public static void SendEmail(Activity currentActivity, String[] recipients,
			String subject, String body)
	{
		SendEmail(currentActivity,recipients,subject,body,"Send mail...");
	}
	public static void SendEmail(Activity currentActivity, String[] recipients,
			String subject, String body, String chooserTitle)
	{
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL, recipients);
		i.putExtra(Intent.EXTRA_SUBJECT, subject);
		i.putExtra(Intent.EXTRA_TEXT, body);
		try
		{
			currentActivity.startActivity(Intent.createChooser(i,chooserTitle));
		} 
		catch (android.content.ActivityNotFoundException ex)
		{
			Toast.makeText(currentActivity,
					"There are no email clients installed.", Toast.LENGTH_SHORT)
					.show();
		}
	}
	public static void shareText(Activity activity, String subject, String text, int chooserTitleID) {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_EMAIL, "");
		i.putExtra(Intent.EXTRA_SUBJECT, subject);
		i.putExtra(Intent.EXTRA_TEXT, text);
		try {
			activity.startActivity(Intent.createChooser(i,
					activity.getString(chooserTitleID)));
		}
		catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(activity,
					"Unable to find app",
					Toast.LENGTH_SHORT).show();
		}

	}
	
	
	public static void enableOrientation(Activity activity, boolean enable)
	{
		if (enable)
		{
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
			Settings.System.putInt(activity.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);
		}
		else
		{
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			Settings.System.putInt(activity.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
			
		}
	}
	/**
	 * Also see
	 * http://stackoverflow.com/questions/10296711/androidtake-screenshot-and-share-it
	 * @param activity
	 * @return
	 */
	public static Bitmap takeScreenShot(Activity activity)
	{
	    View rootView = activity.getWindow().getDecorView();
		Bitmap bmp = Bitmap.createBitmap(rootView.getWidth(), rootView.getHeight(), Bitmap.Config.RGB_565);
		rootView.draw(new Canvas(bmp));
	    //Remove title bar
		Rect frame = new Rect();
	    activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
	    int statusBarHeight = frame.top;
	    bmp = Bitmap.createBitmap(bmp, 0, statusBarHeight, rootView.getWidth(), rootView.getHeight()  - statusBarHeight);
	    return bmp;
	}	

	public static void showInfoDialog(Context context, int titleID,
			int messageID, int okID) {
		String title = context.getResources().getString(titleID);
		String message = context.getResources().getString(messageID);
		new AlertDialog.Builder(context)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton(okID,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).show();

	}
	
	public static void showKeyboard(Activity activity, View view)
	{
		try {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm != null) {
				imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
			}
		}
		catch (Exception e) {
		}
	}
	public static void hideKeyboard(Activity activity, IBinder token)
	{
		try {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm != null) {
				imm.hideSoftInputFromWindow(token, 0);
			}
		}
		catch (Exception e) {
		}
	}	
	
}
