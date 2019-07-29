package com.pigdogbay.lib.utils;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.pigdogbay.lib.test.R;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PreferencesHelperTest
{

    /*
     * Basic case
     */
    @Test
    public void testGetInt1()
    {
        int expected = 42;
        int actual;
        int id = R.string.code_pref_weight_units_key;
        PreferencesHelper target = new PreferencesHelper(InstrumentationRegistry.getInstrumentation().getContext());
        target.setInt(id,expected);
        actual = target.getInt(id,0);
        assertEquals(expected, actual);
    }
    /*
     * Spanish locale
     */
    @Test
    public void testGetInt2()
    {
        Locale.setDefault(new Locale("es"));
        testGetInt1();
        Locale.setDefault(Locale.US);
    }
    /*
     * Badly formatted
     */
    @Test
    public void testGetInt3()
    {
        int expected = 88;
        int actual;
        int id = R.string.code_pref_weight_units_key;
        PreferencesHelper target = new PreferencesHelper(InstrumentationRegistry.getInstrumentation().getContext());
        target.setDouble(id,1.23);
        actual = target.getInt(id,expected);
        assertEquals(expected, actual);
    }
    /*
     * Basic case
     */
    @Test
    public void testGetDouble1()
    {
        double expected = 1.23;
        double actual;
        int id = R.string.code_pref_height_key;
        PreferencesHelper target = new PreferencesHelper(InstrumentationRegistry.getInstrumentation().getContext());
        target.setDouble(id,expected);
        actual = target.getDouble(id,0);
        assertEquals(expected, actual,0.0001D);
    }
    /*
     * Spanish locale
     */
    @Test
    public void testGetDouble2()
    {
        Locale.setDefault(new Locale("es"));
        testGetDouble1();
        Locale.setDefault(Locale.UK);
    }
    /*
     * Test API for locale behaviour
     * Expect locale to have no effect
     */
    @Test
    public void testDoubleCheck()
    {
        Locale.setDefault(new Locale("es"));
        assertEquals(1.23D,Double.parseDouble("1.23"),0.0001D);
        assertEquals("4.56",Double.toString(4.56D));
        Locale.setDefault(Locale.UK);
    }

    /*
     * Basic case
     */
    @Test
    public void testGetBoolean1()
    {
        int id = R.string.code_pref_welcome_shown_key;
        PreferencesHelper target = new PreferencesHelper(InstrumentationRegistry.getInstrumentation().getContext());
        target.setBoolean(id,true);
        assertTrue(target.getBoolean(id,false));
        target.setBoolean(id,false);
        assertFalse(target.getBoolean(id,false));
    }
    /*
     * Spanish locale
     */
    @Test
    public void testGetBoolean2()
    {
        Locale.setDefault(new Locale("es"));
        testGetBoolean1();
        Locale.setDefault(Locale.US);
    }



}
