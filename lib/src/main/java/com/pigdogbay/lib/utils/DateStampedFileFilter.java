package com.pigdogbay.lib.utils;

import java.io.File;
import java.io.FileFilter;

/**
 * @author mark.bailey
 *
 */
public class DateStampedFileFilter implements FileFilter {

	private static String _Prefix,_Suffix;
	public DateStampedFileFilter(String prefix, String suffix)
	{
		_Prefix = prefix;
		_Suffix = suffix;
	}

	@Override
	public boolean accept(File file) {
		String name = file.getName();
		if (name.equals("")){return false;}
		if (name.startsWith(_Prefix) && name.endsWith(_Suffix)){
			try{
				FileUtils.extractDate(file);
				return true;
			}catch(Exception ignored){}
		}
		return false;
	}

}
