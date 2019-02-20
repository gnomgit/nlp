package com.nlp.mails.model;

import java.util.HashSet;
import java.util.Set;

import com.nlp.mails.utils.FileTools;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Dictionary {
	
	private Set<String> words;
	
	public Dictionary () {
		words = new HashSet<>();
		StringBuilder sb = FileTools.loadTextFile(Dictionary.class.getClassLoader().getResource(Constants.EN_GB).getPath());
		String[] lines = sb.toString().split("\n");
		for (String line : lines) {
			if (line != null && !line.isEmpty()) {
				if (line.contains("/")) {
					words.add(line.split("/")[0]);
				}	else {
					words.add(line);
				}
			}
			
		}
	}
	
	public String asLines () {
		StringBuilder sb = new StringBuilder();
		
		words.forEach(w -> sb.append(w + "\n"));
		
		return sb.toString();
	}
	
}
