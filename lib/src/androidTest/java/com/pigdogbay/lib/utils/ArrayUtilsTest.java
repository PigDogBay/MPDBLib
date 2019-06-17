package com.pigdogbay.lib.utils;

import androidx.test.filters.SmallTest;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class ArrayUtilsTest {
    @Test
    public void fromString1(){
        int[] actual = ArrayUtils.fromString("[1, 2, 3]");
        assertThat(actual, is(new int[]{1,2,3}));
    }
    @Test
    public void fromString2(){
        int[] actual = ArrayUtils.fromString("4,5,6");
        assertThat(actual, is(new int[]{4,5,6}));
    }
    @Test
    public void fromString3(){
        int[] actual = ArrayUtils.fromString(Arrays.toString(new int[]{7,8,9}));
        assertThat(actual, is(new int[]{7,8,9}));
    }
}
