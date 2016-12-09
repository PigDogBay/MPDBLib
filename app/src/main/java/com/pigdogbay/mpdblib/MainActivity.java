package com.pigdogbay.mpdblib;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.pigdogbay.lib.apprate.AppRate;
import com.pigdogbay.lib.usercontrols.CustomNumberPicker;
import com.pigdogbay.lib.usercontrols.GoProNagBox;
import com.pigdogbay.lib.usercontrols.NumberPickerController;

public class MainActivity extends AppCompatActivity implements GoProNagBox.IGoProResult, NumberPickerController.OnValueChangedListener {

    private static String TAG = "MPDB";
    private GoProNagBox goProNagBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Button) findViewById(R.id.btnGoProNagBox)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goProNagBoxTest();
            }
        });
        ((Button) findViewById(R.id.btnRateNow)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateNowTest();
            }
        });

        goProNagBox = new GoProNagBox(this);
        goProNagBox.setFrequency(5)
                .setResultListener(this)
                .setMessageId(R.string.go_pro_message)
                .setUrlId(R.string.market_cleverdic);

        CustomNumberPicker customNumberPicker = (CustomNumberPicker) findViewById(R.id.numPick);
        customNumberPicker.setOnValueChangedListener(this);
    }

    private void rateNowTest() {
        AppRate.reset(this);
        new AppRate(this).setMinDaysUntilPrompt(0).setMinLaunchesUntilPrompt(0).init();
    }

    private void goProNagBoxTest(){
        goProNagBox.save();
        goProNagBox.check();
    }

    @Override
    public void noGoPro() {
        Log.v(TAG,"No Go Pro");
    }

    @Override
    public void yesGoPro() {
        Log.v(TAG,"Yes Go Pro");
    }

    @Override
    public void onValueChanged(double newValue) {
        Log.v(TAG, String.format("Num Picker New Value: %.0f",newValue));
    }
}
