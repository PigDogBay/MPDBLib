package com.pigdogbay.lib.usercontrols;

/**
 * Created by Mark on 08/09/2016.
 */
public class NumberPickerIntValue implements INumberPickerValue
{
    private int value = 0;
    private int minValue = 0;

    @Override
    public void increase(){
        value++;
    }
    @Override
    public void decrease(){
        if (value>minValue) {
            value--;
        }
    }
    @Override
    public String getFormattedString(){
        return String.valueOf(value);
    }
    @Override
    public double getValue() {
        return (double) value;
    }
    @Override
    public void setValue(double value) {
        this.value = (int)value;
    }

    @Override
    public String getDisplayFormat() {
        return null;
    }

    @Override
    public void setDisplayFormat(String format) {

    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }
}
