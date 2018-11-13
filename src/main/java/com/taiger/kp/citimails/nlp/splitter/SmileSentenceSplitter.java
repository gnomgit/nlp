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
	
	@Override
	public List<String> detect (String text) {
		Assert.hasText(text, "text shouldn't be null or empty");
		List<String> result = new ArrayList<>();
		List<String> tmp = new ArrayList<>();
		
		text = text.replace(Constants.WEIRD_SPACE, Constants.SPACE);
		//text = text.replace("\t", "\t\n");
		//text = text.replace("  ", "  \n");
		String []splitted = text.split("\n");
		for (String line : splitted) {
			if (!line.trim().isEmpty()) {
				String[] sentences = detector.split(line);
				for (String sentence : sentences) {
					tmp.add(sentence);
				}
			}
		}
		
		StringBuilder before = new StringBuilder();
		for (String sentence : tmp) {
			if (sentence.endsWith(Constants.colon)) {
				before.append(sentence);
			}	else {
				if (before.length() > 0) {
					result.add(before.toString() + sentence);
					before = new StringBuilder();
				}	else {
					result.add(sentence);
				}
			}
		}
		
		return result;
	}
	
	
	

}
