package com.pigdogbay.lib.utils;

import android.os.Environment;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

//These tests requires the following permissions to be granted
//adb shell pm grant com.pigdogbay.lib.test android.permission.READ_EXTERNAL_STORAGE
//adb shell pm grant com.pigdogbay.lib.test android.permission.WRITE_EXTERNAL_STORAGE
@RunWith(AndroidJUnit4.class)
@SmallTest
public class FileUtilsTest {
    static final String TYPICAL_FILENAME = "WeightRecorder_20130114.csv";
    private File createFile(String filename)
    {
        File path = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        return new File(path, filename);
    }
    @Test
    public void isExternalStorageWritable() {
        assertTrue(FileUtils.isExternalStorageWritable());
    }

    @Test
    public void isExternalStorageReadable() {
        assertTrue(FileUtils.isExternalStorageReadable());
    }

    //This test requires the following permissions to be granted
    //adb shell pm grant com.pigdogbay.lib.test android.permission.READ_EXTERNAL_STORAGE
    //adb shell pm grant com.pigdogbay.lib.test android.permission.WRITE_EXTERNAL_STORAGE
    @Test
    public void createDownloadsFile() {
        try {
            File file = FileUtils.createDownloadsFile("AndroidUtilsTest.txt");
            assertTrue(file.exists());
        }
        catch (IOException e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void readWriteTextFile() {
        try {
            File file = FileUtils.createDownloadsFile("AndroidUtilsTest.txt");
            if(file.exists())
            {
                file.delete();
            }
            String expected = "Gorf interrupting\nAll your base belong to us\nNo soup for you!";
            FileUtils.writeTextFile(file, expected);
            String actual = FileUtils.readText(file);
            assertThat(actual.trim(), is(expected.trim()));
        }
        catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void extractDate()
    {
        File file = createFile(TYPICAL_FILENAME);
        long expected = new Date(113,0,14).getTime();
        try{
            long actual = FileUtils.extractDate(file).getTime();
            assertEquals(expected, actual);}
        catch(Exception e){
            fail(e.getMessage());
        }
    }
    @Test
    public void readLines() throws IOException
    {
        ByteArrayInputStream bais = new ByteArrayInputStream("one\rtwo\rthree".getBytes());
        List<String> lines = FileUtils.ReadLines(bais);
        assertThat(lines.size(), is(3));
        assertThat(lines.get(1), is("two"));
    }



}
