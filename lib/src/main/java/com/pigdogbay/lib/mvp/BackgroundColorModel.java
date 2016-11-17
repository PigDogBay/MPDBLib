package com.pigdogbay.lib.mvp;

import com.pigdogbay.lib.R;
import com.pigdogbay.lib.utils.PreferencesHelper;


public class BackgroundColorModel {
	public static final int SKY_BLUE_INDEX = 0;
	public static final int DARK_PINK_INDEX = 1;
	public static final int LIGHT_PINK_INDEX = 2;
	public static final int TURQUOISE_INDEX = 3;
	public static final int YELLOW_INDEX = 4;
	public static final int GREY_INDEX = 5;
	public static final int WHITE_INDEX = 6;
	PreferencesHelper _PreferencesHelper;
	private final int defaultValue;

	public BackgroundColorModel(PreferencesHelper prefHelper, int defaultValue)
	{
		_PreferencesHelper = prefHelper;
		this.defaultValue = defaultValue;
	}
	public int getBackgroundId()
	{
        int colorIndex =_PreferencesHelper.getInt(R.string.code_pref_background_colour, defaultValue);
		switch (colorIndex){
		case DARK_PINK_INDEX:
			return R.drawable.bgpink;
		case LIGHT_PINK_INDEX:
			return R.drawable.bglightpink;
		case TURQUOISE_INDEX:
			return R.drawable.bgturquoise;
		case YELLOW_INDEX:
			return R.drawable.bgsunshine;
		case GREY_INDEX:
			return R.drawable.bggrey;
		case WHITE_INDEX:
			return R.drawable.white;
		}
		return R.drawable.bgskyblue;
	}
	public int getBackgroundIndex()
	{
		return _PreferencesHelper.getInt(R.string.code_pref_background_colour, defaultValue);
	}
	public void setBackgroundIndex(int index)
	{
		_PreferencesHelper.setInt(R.string.code_pref_background_colour, index);
	}
	public String getColorName() {
		switch(getBackgroundIndex()){
		case BackgroundColorModel.DARK_PINK_INDEX:
			return "Dark Pink";
		case BackgroundColorModel.LIGHT_PINK_INDEX:
			return "Light Pink";
		case BackgroundColorModel.TURQUOISE_INDEX:
			return "Turquoise";
		case BackgroundColorModel.YELLOW_INDEX:
			return "Yellow";
		case BackgroundColorModel.GREY_INDEX:
			return "Grey";
		case BackgroundColorModel.WHITE_INDEX:
			return "White";
		}
		return "Sky Blue";
	}
	

}
