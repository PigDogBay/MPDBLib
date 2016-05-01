package com.pigdogbay.lib.utils;


import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class ObservablePropertyTest {
    public enum Veg
    {
        carrots, peppers, onions, chillis, mushrooms
    }

    public class Listener implements ObservableProperty.PropertyChangedObserver<Veg>
    {
        public Veg newValue;

        @Override
        public void update(ObservableProperty<Veg> sender, Veg update) {
            newValue = update;
        }

    }
    @Test
    public void enum1()
    {
        ObservableProperty<Veg> target = new ObservableProperty<ObservablePropertyTest.Veg>(Veg.onions);
        Listener listener = new Listener();
        target.addObserver(listener);
        target.setValue(Veg.chillis);
        assertThat(listener.newValue, is(Veg.chillis));
    }


}
