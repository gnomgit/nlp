package com.taiger.kp.citimails.utils;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.springframework.util.Assert;

public class FileTools {
	
	public static String fileNameNoExt (String file) {
		Assert.hasText(file, "file shouldn't be null or empty");
		return FilenameUtils.removeExtension(file);
	}
	
	public static String fileNameNoPath (String file) {
		Assert.hasText(file, "file shouldn't be null or empty");
		return FilenameUtils.getBaseName(file);
	}
	
	public static String filePath (String file) {
		Assert.hasText(file, "file shouldn't be null or empty");
		return FilenameUtils.getFullPath(file);
	}
	
	public static String fileExtension (String file) {
		Assert.hasText(file, "file shouldn't be null or empty");
		return FilenameUtils.getExtension(file);
	}
	
	public static void createDir (String dir) {
		File directory = new File(dir);
	    if (! directory.exists()){
	        directory.mkdirs();
	        // If you require it to make the entire directory path including parents,
	        // use directory.mkdirs(); here instead, ioc use directory.mkdir();
	    }
	}
	
}
