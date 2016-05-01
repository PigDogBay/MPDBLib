package com.pigdogbay.lib.diagnostics;

import android.content.Context;
import android.content.res.Configuration;
import android.widget.Toast;

public final class Capabilities
{
	/**
	 * Show a toast of the screen size
	 * @param context
	 */
	public static void ScreenSizeToast(Context context)
	{
	    if ((context.getResources().getConfiguration().screenLayout &      Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {     
	        Toast.makeText(context, "Large screen",Toast.LENGTH_LONG).show();

	    }
	    else if ((context.getResources().getConfiguration().screenLayout &      Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {     
	        Toast.makeText(context, "Normal sized screen" , Toast.LENGTH_LONG).show();

	    } 
	    else if ((context.getResources().getConfiguration().screenLayout &      Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {     
	        Toast.makeText(context, "Small sized screen" , Toast.LENGTH_LONG).show();
	    }
	    else {
	        Toast.makeText(context, "Screen size is neither large, normal or small" , Toast.LENGTH_LONG).show();
	    }		
	}
}
