package com.pigdogbay.lib.utils;

import java.io.File;
import java.util.Comparator;

public class DateStampComparator implements Comparator<File>
{
	public int compare(File lhs, File rhs)
	{
		try{
			return FileUtils.extractDate(lhs).compareTo(FileUtils.extractDate(rhs));
		}catch(Exception ignored){
		}
		return -1;
	}
}