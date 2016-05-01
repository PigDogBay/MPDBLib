package com.pigdogbay.lib.usercontrols;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

public class NumberPicker extends LinearLayout{

	private NumberPickerController _Controller;
	public NumberPickerController getController()
	{
		return _Controller;
	}
	
	public NumberPicker(Context context) {
		this(context,null);
	}
	public NumberPicker(Context context, AttributeSet attributeSet) {
		super(context,attributeSet);
		
		this.setOrientation(HORIZONTAL);
		
		Button minusButton = new Button(context);
		minusButton.setText("-");
		minusButton.setMinEms(2);
		minusButton.setTextAppearance(context,
				android.R.style.TextAppearance_Large);
		minusButton.setTypeface(null, Typeface.BOLD);
		LinearLayout.LayoutParams minBtnLayParams = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		
		Button plusButton = new Button(context);
		plusButton.setText("+");
		plusButton.setMinEms(2);
		plusButton.setTextAppearance(context,
				android.R.style.TextAppearance_Large);
		plusButton.setTypeface(null, Typeface.BOLD);
		
		Button setButton = new Button(context);
		setButton.setText("42.0");
		setButton.setMinEms(6);
		setButton.setTextAppearance(context,
				android.R.style.TextAppearance_Large);
		addView(minusButton, minBtnLayParams);
		addView(setButton, minBtnLayParams);
		addView(plusButton,minBtnLayParams);
		
		_Controller = new NumberPickerController(minusButton, setButton, plusButton);
		_Controller.setNumberPickerValue(new NumberPickerValue());
		_Controller.setNumberEditorDialog(new NumberEditorDialog(context, "Enter Value", "Enter Value"));
	
	}
	


}
