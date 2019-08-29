package com.pigdogbay.lib.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public final class LineReader 
{
	public static List<String> Read(InputStream inputStream)
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
	
	public static List<String> Read(Context context, int resourceId) throws IOException
	{
		try (InputStream inputStream = context.getResources().openRawResource(resourceId)) {
			return Read(inputStream);
		}
	}
}
