package com.taiger.kp.citimails.nlp.splitter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

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
	}
	
	
	

}
