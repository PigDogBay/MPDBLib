package com.pigdogbay.lib;

import androidx.test.filters.SmallTest;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class TemplateTest
{
    @Test
    public void method1(){
        assertThat(1, is(1));
    }
}
