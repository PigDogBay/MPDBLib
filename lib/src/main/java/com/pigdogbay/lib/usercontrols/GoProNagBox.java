package com.pigdogbay.lib.usercontrols;

import com.pigdogbay.lib.utils.ActivityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class GoProNagBox implements android.content.DialogInterface.OnClickListener, OnCancelListener{

	public interface IGoProResult{
		void noGoPro();
		void yesGoPro();
	}
	
	public static final String TAG = "GoProNagBox";
	public static final int DEFAULT_FREQUENCY = 30;
	public static final String SHARED_PREFS_NAME = "go_pro_nag_box_prefs";
	public static final String PREF_COUNT = "pref_count";

	private Activity _HostActivity;
	private int _Frequency = DEFAULT_FREQUENCY;
	private int _UrlId, _Count=0;
	private boolean isCountDifferent=false;
	private IGoProResult _IGoProResultListener;
	private int _MessageId = 0;
	
	
	public GoProNagBox setFrequency(int frequency)
	{
		_Frequency = frequency;
		return this;
	}
	public GoProNagBox setUrlId(int id)
	{
		_UrlId = id;
		return this;
	}
	public GoProNagBox setResultListener(IGoProResult listener)
	{
		_IGoProResultListener = listener;
		return this;
	}
	public GoProNagBox setMessageId(int id)
	{
		_MessageId = id;
		return this;
	}
	public GoProNagBox(Activity hostActivity) {
		_HostActivity = hostActivity;
		try{
		_Count = hostActivity
				.getSharedPreferences(SHARED_PREFS_NAME, 0)
				.getInt(PREF_COUNT, 0);
		}catch (Exception e)
		{
			Log.v(TAG,"Unable to load preferences");
		}
	}
	public void check()
	{
		_Count++;
		if (_Count>_Frequency)
		{
			showDialog();
			_Count=0;
		}
		isCountDifferent = true;
	}
	public void save()
	{
		if (isCountDifferent){
			isCountDifferent = false;
			try{
				SharedPreferences preferences = _HostActivity
						.getSharedPreferences(SHARED_PREFS_NAME, 0);
				Editor editor = preferences.edit();
				editor.putInt(PREF_COUNT, _Count);
				editor.commit();
			}catch(Exception e){
				Log.v(TAG,"Unable to store preferences");
			}
			
		}
	}
	private void showDialog(){
		new AlertDialog.Builder(_HostActivity)
		.setTitle("Go Pro!")
		.setMessage(_HostActivity.getString(_MessageId))
		.setPositiveButton("Go Pro", this)
		.setNegativeButton("Not Now", this)
		.setOnCancelListener(this)
		.create().show();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				ActivityUtils.ShowAppOnMarketPlace(_HostActivity, _UrlId);
				if (null!=_IGoProResultListener)
				{
					_IGoProResultListener.yesGoPro();
				}
			break;
			default:
				if (null!=_IGoProResultListener)
				{
					_IGoProResultListener.noGoPro();
				}
				break;
		}
		
	}
	@Override
	public void onCancel(DialogInterface dialog) {
		if (null!=_IGoProResultListener)
		{
			_IGoProResultListener.noGoPro();
		}
	}
}
