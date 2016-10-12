package com.pigdogbay.lib.usercontrols;

public interface INumberPickerValue {
    void increase();
    void decrease();
    String getFormattedString();
    double getValue();
    void setValue(double value);

    String getDisplayFormat();

    void setDisplayFormat(String format);
}