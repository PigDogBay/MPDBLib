package com.pigdogbay.lib.utils;

import android.os.Environment;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DateStampedFileFilterTest
{
    private String typicalFilename = "WeightRecorder_20130114.csv";
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
