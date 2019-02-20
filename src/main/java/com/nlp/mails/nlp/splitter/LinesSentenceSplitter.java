package com.nlp.mails.nlp.splitter;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.util.Assert;

import com.nlp.mails.model.Constants;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Setter
public class LinesSentenceSplitter implements SentenceSplitter {
	
	public LinesSentenceSplitter() {
		initialize();
	}
	
	public LinesSentenceSplitter initialize () {
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
			BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
			iterator.setText(line);
			int start = iterator.first();
			for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
				tmp.add(line.substring(start,end));
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
