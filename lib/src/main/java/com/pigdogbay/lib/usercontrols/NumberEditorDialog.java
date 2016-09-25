package com.pigdogbay.lib.usercontrols;

import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.text.InputType;
import android.widget.EditText;

public class NumberEditorDialog implements INumberEditorDialog {

	Context _Context;
	String _Title, _Hint;
	OnDismissListener _OnDismissListener;
	public NumberEditorDialog(Context context, String hint, String title)
	{
		_Context = context;
		_Hint = hint;
		_Title  = title;
	}
	@Override
	public void show(final NumberPickerValue numberPickerValue) {
		final AlertDialog.Builder alert = new AlertDialog.Builder(_Context);
		final EditText input = new EditText(_Context);
		input.setText(String.format(Locale.US, "%.1f",numberPickerValue.getValue()));
		input.setHint(_Hint);
		input.setInputType(InputType.TYPE_CLASS_NUMBER
				| InputType.TYPE_NUMBER_FLAG_DECIMAL);
		alert.setTitle(_Title);
		alert.setView(input);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				try
				{
					String value = input.getText().toString().trim();
					numberPickerValue.setValue(Double.parseDouble(value));
					onDismiss();
				}
				catch (NumberFormatException ex)
				{
				}
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				dialog.cancel();
				onDismiss();
			}
		});
		alert.show();
		
	}
	@Override
	public void setOnDismissListener(OnDismissListener listener) {
		_OnDismissListener = listener;
	}
	void onDismiss()
	{
		if (_OnDismissListener!=null)
		{
			_OnDismissListener.onDismiss(null);
		}
	}

}
