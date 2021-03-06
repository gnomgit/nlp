package com.nlp.mails.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.nlp.mails.model.Constants;
import com.nlp.mails.model.Sentence;
import com.nlp.mails.model.Word;

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
		Assert.notNull(s.getWords(), "sentence shloudn't be null");
		String[] tokens = new String [s.getWords().size()]; 
		for (int i = 0; i < s.getWords().size(); i++) {
			Word w = s.getWords().get(i);
			//tokens[i] = w.getPosition() + "\t" + w.getW() + "\t" + w.getW() + "\t" + w.getPosUTag() +  "\t" + w.getPosUTag() + "_\t0\t_\t_\t_";
			tokens[i] = w.getPosition() + "\t" + w.getW() + "\t" + w.getW() + "\t" + w.getPosTag().charAt(0) +  "\t" + w.getPosTag() + "_\t0\t_\t_\t_";
		}
		return tokens;
	}
	
	public static int countWords (String sentence) {
		if (sentence == null || sentence.isEmpty()) return 0;
		else return sentence.trim().split(" ").length;
	}
	
	public static Map<String, String> sortDesc (Map<String, String> map) {
		List<Map.Entry<String, String>> ll = new LinkedList<Map.Entry<String, String>>(map.entrySet());
		Collections.sort(ll, new Comparator<Map.Entry<String, String>>() {
			public int compare(Map.Entry<String, String> s1, Map.Entry<String, String> s2) {
				Integer o1 = new Integer(countWords(s1.getKey()));
				Integer o2 = new Integer(countWords(s2.getKey()));
				return o2.compareTo(o1);
			}
		});
		Map<String, String> result = new LinkedHashMap<>();
		for (Map.Entry<String, String> entry : ll) {
			result.put(entry.getKey(), entry.getValue());
		}
		
		return result;
	}
	
	public static int costOfSubstitution (char a, char b) {
        return a == b ? 0 : 1;
    }
	
	public static int calculateLevenshtein (String a, String b) {
		if (a == null || b == null) return -1;
		String x = a.toLowerCase();
		String y = b.toLowerCase();
	    int[][] dp = new int[x.length() + 1][y.length() + 1];
	 
	    for (int i = 0; i <= x.length(); i++) {
	        for (int j = 0; j <= y.length(); j++) {
	            if (i == 0) {
	                dp[i][j] = j;
	            }	else if (j == 0) {
	                dp[i][j] = i;
	            }	else {
	                dp[i][j] = min(dp[i - 1][j - 1] + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)), dp[i - 1][j] + 1, dp[i][j - 1] + 1);
	            }
	        }
	    }
	 
	    return dp[x.length()][y.length()];
	}
	
	public static int min (int a, int b, int c) {
		return Math.min(a, Math.min(b, c));
	}
	
	public static boolean isMyNumber (String token) {
		if (token == null || token.isEmpty()) return false;
		if (token.length() == 1 && !Character.isDigit(token.charAt(0))) return false;
		if (StringUtils.countOccurrencesOf(token, "-") > 1) return false;
		for (Character ch : token.toCharArray()) {
			if (Character.isLetter(ch)) return false;
			if (ch == '?' || ch == '!' || ch == '/' || ch == ':') return false;
		}
		
		return true;
	}
	
	public static boolean isStopWord (String token) {
		if (token == null) return true;
		if (token.equals(Constants.colon)) return true;
		
		return false;
	}
	
	public static int countOccurrences (String str, String substr) {
		return str.length() - str.replace(substr, "").length();
	}
}
