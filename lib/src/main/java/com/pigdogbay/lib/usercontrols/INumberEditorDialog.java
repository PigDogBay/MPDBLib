package com.pigdogbay.lib.usercontrols;

import android.content.DialogInterface;

public interface INumberEditorDialog {
	void show(INumberPickerValue value);
	void setOnDismissListener(DialogInterface.OnDismissListener listener);
}
