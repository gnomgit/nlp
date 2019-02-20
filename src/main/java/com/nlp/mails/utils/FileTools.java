package com.nlp.mails.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.springframework.util.Assert;

import com.nlp.mails.model.Constants;
import com.nlp.mails.nlp.ner.LocationFinderNER;

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
	
	/**/
	
	public static StringBuilder loadTextFile (String path) {
		StringBuilder result = new StringBuilder();
		try {
			Files.lines(Paths.get(path)).collect(Collectors.toList()).forEach(s -> result.append(s + "\n"));
		} 	catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
