package com.pigdogbay.lib.usercontrols;

import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.text.InputType;
import android.widget.EditText;

public class NumberEditorDialog implements INumberEditorDialog {

	private Context _Context;
	private String _Title, _Hint;
	private OnDismissListener _OnDismissListener;
	public NumberEditorDialog(Context context, String hint, String title)
	{
		_Context = context;
		_Hint = hint;
		_Title  = title;
	}
	@Override
	public void show(final INumberPickerValue numberPickerValue) {
		final AlertDialog.Builder alert = new AlertDialog.Builder(_Context);
		final EditText input = new EditText(_Context);
		input.setText(String.format(Locale.US, "%.1f",numberPickerValue.getValue()));
		input.setHint(_Hint);
		input.setInputType(InputType.TYPE_CLASS_NUMBER
				| InputType.TYPE_NUMBER_FLAG_DECIMAL);
		alert.setTitle(_Title);
		alert.setView(input);
		alert.setPositiveButton("Ok", (dialog, whichButton) -> {
			try
			{
				String value = input.getText().toString().trim();
				numberPickerValue.setValue(Double.parseDouble(value));
				onDismiss();
			}
			catch (NumberFormatException ignored){}
		});

		alert.setNegativeButton("Cancel", (dialog, whichButton) -> {
			dialog.cancel();
			onDismiss();
		});
		alert.show();
		
	}
	@Override
	public void setOnDismissListener(OnDismissListener listener) {
		_OnDismissListener = listener;
	}
	private void onDismiss()
	{
		if (_OnDismissListener!=null)
		{
			_OnDismissListener.onDismiss(null);
		}
	}

}
