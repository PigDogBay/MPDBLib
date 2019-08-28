package com.pigdogbay.lib.usercontrols;

import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NumberPickerController implements DialogInterface.OnDismissListener
{
	private final Button minusBtn;
	private final Button plusBtn;
	private INumberPickerValue numberPickerValue;
	private INumberEditorDialog numberEditorDialog;
	private OnValueChangedListener onValueChangedListener;
	private Button setButton;

	Button getMinusBtn(){return minusBtn;}
	Button getPlusBtn(){return plusBtn;}

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
	public void setValue(double value){
		numberPickerValue.setValue(value);
		setButton.setText(numberPickerValue.getFormattedString());
	}
	public NumberPickerController(Button minus, Button set, Button plus)
	{
		minusBtn = minus;
		plusBtn = plus;
		minus.setOnClickListener(v -> minusButtonClicked());
		plus.setOnClickListener(v -> plusButtonClicked());
		setButton = set;
		setButton.setOnClickListener(v -> displayButtonClicked());
		
	}
	public NumberPickerController(View view, int minusButtonId, int setButtonId, int plusButtonId)
	{
		minusBtn = view.findViewById(minusButtonId);
		plusBtn = view.findViewById(plusButtonId);
		minusBtn.setOnClickListener(v -> minusButtonClicked());
		plusBtn.setOnClickListener(v -> plusButtonClicked());
		setButton = view.findViewById(setButtonId);
		setButton.setOnClickListener(v -> displayButtonClicked());
	}

	private void minusButtonClicked()
	{
		if (numberPickerValue!=null)
		{
			numberPickerValue.decrease();
			setButton.setText(numberPickerValue.getFormattedString());
			onValueChanged(numberPickerValue.getValue());
		}
	}
	private void plusButtonClicked()
	{
		if (numberPickerValue!=null)
		{
			numberPickerValue.increase();
			setButton.setText(numberPickerValue.getFormattedString());
			onValueChanged(numberPickerValue.getValue());
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
		onValueChanged(getValue());
	}


	private void onValueChanged(double newValue){
		if (onValueChangedListener!=null){
			onValueChangedListener.onValueChanged(newValue);
		}
	}
	public interface OnValueChangedListener {
		void onValueChanged(double newValue);
	}
	public OnValueChangedListener getOnValueChangedListener() {
		return onValueChangedListener;
	}
	public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
		this.onValueChangedListener = onValueChangedListener;
	}

}
