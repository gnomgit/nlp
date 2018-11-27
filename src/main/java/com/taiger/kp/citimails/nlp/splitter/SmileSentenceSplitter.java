package com.taiger.kp.citimails.nlp.splitter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import com.taiger.kp.citimails.model.Constants;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import smile.nlp.tokenizer.SimpleSentenceSplitter;

@Log4j2
@Getter
@Setter
public class SmileSentenceSplitter implements SentenceSplitter {

	private smile.nlp.tokenizer.SentenceSplitter detector;
	
	public SmileSentenceSplitter() {
		initialize();
	}
	
	public SmileSentenceSplitter initialize () {
		detector = SimpleSentenceSplitter.getInstance();
		return this;
	}
	
	/*
	@Override
	public List<String> detect (String text) {
		Assert.hasText(text, "text shouldn't be null or empty");
		List<String> result = new ArrayList<>();

		text = formatter(text);
		//text = text.replace("\t", "\t\n");
		//text = text.replace("  ", "  \n");
		String []splitted = text.split("\n");
		for (String line : splitted) {
			if (!line.trim().isEmpty()) {
				String[] sentences = detector.split(line);
				for (String sentence : sentences) {
					result.add(sentence);
				}
			}
		}
		
		return result;
	} //*/
	
	private String formatter (String text) {
		if (text == null) return "";
		String result = text;
		
		result = result.replace(Constants.WEIRD_SPACE + "", "");
		result = result.replace(": ", " : ");
		result = result.replace(", ", " , ");
		result = result.replace(". ", " . ");
		result = result.replace("; ", " ; ");
		result = result.replace("! ", " ! ");
		result = result.replace("? ", " ? ");
		
		return result;
	}
	
	//*
	@Override
	public List<String> detect (String text) {
		Assert.hasText(text, "text shouldn't be null or empty");
		List<String> result = new ArrayList<>();
		List<String> tmp = new ArrayList<>();
		
		text = formatter(text);
		
		String []splitted = text.split("\n");
		for (String line : splitted) {
			if (!line.trim().isEmpty()) {
				String[] sentences = detector.split(line);
				for (String sentence : sentences) {
					if (!sentence.trim().isEmpty()) {
						tmp.add(sentence);
					}
				}
			}
		}
		
		StringBuilder before = new StringBuilder();
		for (String sentence : tmp) {
			if (sentence.endsWith(Constants.colon)) {
				before.append(" " + sentence.substring(0, sentence.length() - 1) + " " + Constants.colon);
			}	else {
				if (before.length() > 0) {
					result.add(separateEndingSymbol(before.toString().trim() + " " + sentence));
					before = new StringBuilder();
				}	else {
					result.add(separateEndingSymbol(sentence));
				}
			}
		}
		if (before.length() > 0) {
			result.add(separateEndingSymbol(before.toString().trim()));
		}
		
		return result;
	} //*/
	
	private String separateEndingSymbol (String sentence) {
		String result = sentence;
		if (sentence.endsWith(Constants.dot)) {
			result = sentence.substring(0, sentence.length() - 1) + " " + Constants.dot;
		}	
		return result;
	}
	

}
