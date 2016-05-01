package com.pigdogbay.lib.usercontrols;

import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NumberPickerController implements DialogInterface.OnDismissListener
{
	INumberPickerValue numberPickerValue;
	INumberEditorDialog numberEditorDialog;
	Button setButton;
	
	public void setNumberEditorDialog(INumberEditorDialog numberEditorDialog)
	{
		if (this.numberEditorDialog!=null)
		{
			this.numberEditorDialog.setOnDismissListener(null);
		}
		this.numberEditorDialog = numberEditorDialog;
		this.numberEditorDialog.setOnDismissListener(this);
	}
	public INumberPickerValue getNumberPickerValue()
	{
		return numberPickerValue;
	}	
	public void setNumberPickerValue(INumberPickerValue numberPickerValue)
	{
		this.numberPickerValue = numberPickerValue;
		setButton.setText(numberPickerValue.getFormattedString());
	}
	
	public double getValue()
	{
		return numberPickerValue.getValue();
	}
	public NumberPickerController(Button minus, Button set, Button plus)
	{
		minus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				minusButtonClicked();
			}
		});
		plus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				plusButtonClicked();
			}
		});
		setButton = set;
		setButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				displayButtonClicked();
			}
		});
		
	}
	public NumberPickerController(View view, int minusButtonId, int setButtonId, int plusButtonId)
	{
		((Button)view.findViewById(minusButtonId)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				minusButtonClicked();
			}
		});
		((Button)view.findViewById(plusButtonId)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				plusButtonClicked();
			}
		});
		setButton = (Button)view.findViewById(setButtonId);
		setButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				displayButtonClicked();
			}
		});
	}

	private void minusButtonClicked()
	{
		if (numberPickerValue!=null)
		{
			numberPickerValue.decrease();
			setButton.setText(numberPickerValue.getFormattedString());
		}
	}
	private void plusButtonClicked()
	{
		if (numberPickerValue!=null)
		{
			numberPickerValue.increase();
			setButton.setText(numberPickerValue.getFormattedString());
		}
	}
	private void displayButtonClicked()
	{
		if (numberEditorDialog!=null)
		{
			numberEditorDialog.show(numberPickerValue);
		}
		setButton.setText(numberPickerValue.getFormattedString());
	}
	@Override
	public void onDismiss(DialogInterface dialog) {
		setButton.setText(numberPickerValue.getFormattedString());
	}
	
}
