package com.pigdogbay.lib.utils;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.filters.SmallTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class DateStampedFileFilterTest
{
    String typicalFilename = "WeightRecorder_20130114.csv";
    private File createFile(String filename)
    {
        File path = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        return new File(path, filename);
    }

    @Test
    public void accept1()
    {
        File file = createFile(typicalFilename);
        DateStampedFileFilter target = new DateStampedFileFilter("WeightRecorder","csv");
        assertTrue(target.accept(file));
    }
}
