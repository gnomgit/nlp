package com.nlp.mails.nlp.splitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import com.nlp.mails.model.Constants;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

@Log4j2
@Getter
@Setter
public class MESplitter implements SentenceSplitter {

	private SentenceDetectorME detector;
	
	public MESplitter() {
		initialize();
	}
	
	public MESplitter initialize () {
		try {
			SentenceModel model = new SentenceModel(MESplitter.class.getClassLoader().getResourceAsStream(Constants.SENTENCE_EN));
			this.detector = new SentenceDetectorME(model);
		} 	catch (IOException e) {
			log.error(e.getMessage());
		}
		
		return this;
	}
	
	@Override
	public List<String> detect (String text) {
		Assert.hasText(text, "text shouldn't be null or empty");
		List<String> result = new ArrayList<>();
		
		//String toProcess = StringTools.reduceN(StringTools.convertR2N(text));
		String []splitted = text.split("\n");
		for (String line : splitted) {
			if (!line.trim().isEmpty()) {
				String[] sentences = detector.sentDetect(line);
				for (String sentence : sentences) {
					result.add(sentence);
				}
			}
		}
		
		return result;
	}

}
