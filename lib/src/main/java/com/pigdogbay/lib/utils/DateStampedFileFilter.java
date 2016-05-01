/**
 * 
 */
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
	/* (non-Javadoc)
	 * @see java.io.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(File file) {
		String name = file.getName();
		if (name==null || name.equals("")){return false;}
		if (name.startsWith(_Prefix) && name.endsWith(_Suffix)){
			try{
				FileUtils.extractDate(file);
				return true;
			}catch(Exception e){}
		}
		return false;
	}

}
