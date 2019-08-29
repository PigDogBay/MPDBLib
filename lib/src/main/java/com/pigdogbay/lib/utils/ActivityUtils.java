package com.pigdogbay.lib.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

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
	private void sendMultipleFiles(Activity activity, String[] recipients,
								   String subject, String body, String chooserTitle, File[] files) {
		Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL, recipients);
		i.putExtra(Intent.EXTRA_SUBJECT, subject);
		i.putExtra(Intent.EXTRA_TEXT, body);
		ArrayList<Uri> uris = new ArrayList<>();
		for (File f : files){
			Uri u = Uri.fromFile(f);
			uris.add(u);
		}
		i.putParcelableArrayListExtra(Intent.EXTRA_STREAM,uris);
		try {
			activity.startActivity(Intent.createChooser(i,chooserTitle));
		}
		catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(activity,
					"No email app found",
					Toast.LENGTH_SHORT).show();
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
						(dialog, which) -> dialog.dismiss()).show();

	}
	
	public static void showKeyboard(Activity activity, View view)
	{
		try {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm != null) {
				imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
			}
		}
		catch (Exception ignored) {
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
		catch (Exception ignored) {
		}
	}

	public static void hideKeyboard(Activity activity)
	{
		//http://stackoverflow.com/questions/7789514/how-to-get-activitys-windowtoken-without-view
		//also use findViewById(android.R.id.content).getWind‌​owToken()
		hideKeyboard(activity,activity.getWindow().getDecorView().getRootView().getWindowToken());
	}

	public static String readResource(Context c, int id) throws IOException {
		try (InputStream is = c.getResources().openRawResource(id)) {
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
			int n;
			while ((n = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, n);
			}
			return writer.toString();
		}
	}
}
