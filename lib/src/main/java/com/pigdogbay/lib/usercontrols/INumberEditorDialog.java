package com.pigdogbay.lib.usercontrols;

import android.content.DialogInterface;

public interface INumberEditorDialog {
	void show(NumberPickerValue value);
	void setOnDismissListener(DialogInterface.OnDismissListener listener);
}
