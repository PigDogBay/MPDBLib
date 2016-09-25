package com.pigdogbay.lib.usercontrols;

import android.annotation.SuppressLint;

@SuppressLint("DefaultLocale")
public class NumberPickerValue{
	private double value = 0D;
	private double step = 1.0D;
	private double min = 0.0D;
	private double max = 0.0D;
	private String displayFormat = "%.1f";

	public void increase(){
		value = value+step;
		rangeCheck();
	}
	public void decrease(){
		value = value -step;
		rangeCheck();
	}
	public String getFormattedString(){
		return String.format(displayFormat, value);
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
		rangeCheck();
	}
	public String getDisplayFormat() {
		return displayFormat;
	}
	public void setDisplayFormat(String displayFormat) {
		this.displayFormat = displayFormat;
	}

	public double getStep() {
		return step;
	}

	public void setStep(double step) {
		this.step = step;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	private void rangeCheck(){
		if (value>max){
			value = max;
		}
		if (value<min){
			value = min;
		}

	}
}
