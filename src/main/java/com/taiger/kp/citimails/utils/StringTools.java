package com.taiger.kp.citimails.utils;

import org.springframework.util.Assert;

import com.taiger.kp.citimails.model.Sentence;
import com.taiger.kp.citimails.model.Word;

public class StringTools {

	public static String convertR2N (String text) {
		Assert.hasText(text, "text is empty or null");
		return text.replace("\r\n", "\n");
	}
	
	public static String reduceN (String text) {
		Assert.hasText(text, "text is empty or null");
		String result = text.replace("\t", "");
		result = result.replaceAll(" ", "\n");
		result = result.replaceAll(" \n", "\n");
		result = result.replaceAll("\n ", "\n");
		while (result.contains("\n\n")) {
			result = result.replace("\n\n", "\n");
		}
		return result;
	}
	
	public static String[] sentence2conllFormat (Sentence s) {
		Assert.notNull(s, "s shloudn't be null");
		Assert.notNull(s.getS(), "sentence shloudn't be null");
		String[] tokens = new String [s.getS().size()]; 
		for (int i = 0; i < s.getS().size(); i++) {
			Word w = s.getS().get(i);
			//tokens[i] = w.getPosition() + "\t" + w.getW() + "\t" + w.getW() + "\t" + w.getPosUTag() +  "\t" + w.getPosUTag() + "_\t0\t_\t_\t_";
			tokens[i] = w.getPosition() + "\t" + w.getW() + "\t" + w.getW() + "\t" + w.getPosTag().charAt(0) +  "\t" + w.getPosTag() + "_\t0\t_\t_\t_";
		}
		return tokens;
	}
	
}
