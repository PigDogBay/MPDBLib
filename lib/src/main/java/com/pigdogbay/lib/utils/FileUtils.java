package com.pigdogbay.lib.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;

public final class FileUtils {
	private static final String DATE_STAMP_FORMAT = "yyyyMMdd";
	private static final int DATE_PART_LENGTH = 8;

	/**
	 * Checks if external storage is available for read and write
	 * @return true if writable
	 */
	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}

	/**
	 * Checks if external storage is available to at least read
	 * @return true if readable
	 */
	public static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		return (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
	}

	/**
	 * Creates the file in the external downloads directory
	 * @param filename - first part of filename, eg results.txt
	 * @return File pointing to the file in the downloads directory
	 * @throws IOException if cannot create the file
	 */
	public static File createDownloadsFile(String filename) throws IOException {
		File path = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		File file = new File(path, filename);
		if (!file.exists()) {
			if (!file.createNewFile()){
				throw new IOException("Unable to create file");
			}
		}
		return file;
	}
	public static void writeImage(File file, Bitmap bmp) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(file)) {
			bmp.compress(CompressFormat.PNG, 100, fos);
		}
		
	}

	/**
	 * Appends string to end of file
	 * @param file to write to, if exists string is appended.
	 * @param data string to write to file
	 * @throws IOException if anything went wrong, stream is closed.
	 */
	public static void writeTextFile(File file, String data) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
			writer.append(data);
			writer.newLine();
		}

	}
	/**
	 * Reads a text file
	 * @param file read from
	 * @return file contents read as string
	 */
	public static String readText(File file) throws IOException
	{
    	return readText(new FileReader(file));
	}

	public static String readText(Context ctx, Uri uri) throws IOException
	{
    	InputStream inputStream = ctx.getContentResolver().openInputStream(uri);
    	return readText(new InputStreamReader(Objects.requireNonNull(inputStream)));
	}	
	
	public static String readText(Reader baseReader) throws IOException
	{
		StringBuilder builder = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(baseReader)) {
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
				builder.append('\n');
			}
		}
		
		return builder.toString();
		
	}
	public static String appendDate(String prefix, String suffix)
	{
		String date = new SimpleDateFormat(DATE_STAMP_FORMAT,Locale.US).format(new Date());
		return prefix+"_"+date+suffix;
	}
	
	public static Date extractDate(File file) throws java.text.ParseException, IndexOutOfBoundsException
	{
		String name = file.getName();
		int startIndex = name.lastIndexOf('_');
		String datePart = name.substring(startIndex+1,startIndex+DATE_PART_LENGTH+1);
		return new SimpleDateFormat(DATE_STAMP_FORMAT,Locale.US).parse(datePart);
	}
	
	/**
	 * Read each line of the input stream and store in a list
	 * @param inputStream - source stream to read
	 * @return lines as a list of strings
	 */
	public static List<String> ReadLines(InputStream inputStream)
			throws IOException
	{
		ArrayList<String> list = new ArrayList<>();
		InputStreamReader isr = new InputStreamReader(inputStream);
		BufferedReader br = new BufferedReader(isr);
		String line;
		while ((line = br.readLine()) != null)
		{
			list.add(line);
		}

		return list;
	}

	public static void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while((read = in.read(buffer)) != -1){
			out.write(buffer, 0, read);
		}
	}
}
