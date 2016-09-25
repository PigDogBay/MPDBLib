package com.pigdogbay.lib.usercontrols;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.pigdogbay.lib.R;

/**
 * https://code.tutsplus.com/tutorials/creating-compound-views-on-android--cms-22889
 */
public class CustomNumberPicker extends LinearLayout{

	/**
	 * Identifier for the state to save the selected index of
	 * the side spinner.
	 */
	private static String STATE_VALUE = "Value";

	/**
	 * Identifier for the state of the super class.
	 */
	private static String STATE_SUPER_CLASS = "SuperClass";

	private NumberPickerController controller;
	private NumberPickerValue numberPickerValue;
	private int minusBtnColor, plusBtnColor;

	public void setOnValueChangedListener(NumberPickerController.OnValueChangedListener onValueChangedListener) {
		controller.setOnValueChangedListener(onValueChangedListener);
	}

	public NumberPickerController.OnValueChangedListener getOnValueChangedListener() {
		return controller.getOnValueChangedListener();
	}

	public double getValue(){return controller.getValue();}
	
	public CustomNumberPicker(Context context) {
		super(context);
		init(context);
	}
	public CustomNumberPicker(Context context, AttributeSet attributeSet) {
		super(context,attributeSet);
		init(context);
		TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CustomNumberPicker);
		numberPickerValue = new NumberPickerValue();
		String displayFormat = typedArray.getString(R.styleable.CustomNumberPicker_displayFormat);
		if(displayFormat!=null) {
			numberPickerValue.setDisplayFormat(displayFormat);
		}
		numberPickerValue.setMin(typedArray.getFloat(R.styleable.CustomNumberPicker_min,0.0f));
		numberPickerValue.setMax(typedArray.getFloat(R.styleable.CustomNumberPicker_max,100.0f));
		numberPickerValue.setStep(typedArray.getFloat(R.styleable.CustomNumberPicker_step,1.0f));
		numberPickerValue.setValue(typedArray.getFloat(R.styleable.CustomNumberPicker_value,0.0f));
		minusBtnColor = typedArray.getColor(R.styleable.CustomNumberPicker_minusButtonColor,0xFF000000);
		plusBtnColor = typedArray.getColor(R.styleable.CustomNumberPicker_plusButtonColor,0xFF000000);
		typedArray.recycle();
	}

	private void init(Context context){
		this.setOrientation(HORIZONTAL);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.number_picker,this);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		controller = new NumberPickerController(this,R.id.pickerMinusBtn,R.id.pickerSetBtn, R.id.pickerPlusBtn);
		controller.setNumberPickerValue(numberPickerValue);
		controller.setNumberEditorDialog(new NumberEditorDialog(this.getContext(), "Enter Value", "Enter Value"));
		controller.getMinusBtn().setTextColor(minusBtnColor);
		controller.getPlusBtn().setTextColor(plusBtnColor);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		bundle.putParcelable(STATE_SUPER_CLASS,super.onSaveInstanceState());
		double d = controller.getValue();
		bundle.putDouble(STATE_VALUE,controller.getValue());
		return bundle;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle){
			Bundle bundle = (Bundle)state;
			super.onRestoreInstanceState(bundle.getParcelable(STATE_SUPER_CLASS));
			double d = controller.getValue();
			controller.setValue(bundle.getDouble(STATE_VALUE));
		}else {
			super.onRestoreInstanceState(state);
		}
	}
	@Override
	protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
		// Makes sure that the state of the child views in the side
		// spinner are not saved since we handle the state in the
		// onSaveInstanceState.
		super.dispatchFreezeSelfOnly(container);
	}

	@Override
	protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
		// Makes sure that the state of the child views in the side
		// spinner are not restored since we handle the state in the
		// onSaveInstanceState.
		super.dispatchThawSelfOnly(container);
	}

}
