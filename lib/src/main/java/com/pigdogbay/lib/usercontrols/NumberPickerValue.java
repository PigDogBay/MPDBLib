package com.pigdogbay.lib.usercontrols;

import android.annotation.SuppressLint;

@SuppressLint("DefaultLocale")
public class NumberPickerValue implements INumberPickerValue{
	double _Value = 0D;
	private String displayFormat = "%.1f";

	@Override
	public void increase(){
		_Value++;
	}
	@Override
	public void decrease(){
		_Value--;
	}
	@Override
	public String getFormattedString(){
		return String.format(displayFormat,_Value);
	}
	@Override
	public double getValue() {
		return _Value;
	}
	@Override
	public void setValue(double value) {
		_Value = value;
	}

	@Override
	public String getDisplayFormat() {
		return displayFormat;
	}



	@Override
	public void setDisplayFormat(String displayFormat) {
		this.displayFormat = displayFormat;
	}
}
