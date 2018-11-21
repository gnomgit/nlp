package com.taiger.kp.citimails.nlp.tokenizer;

import com.taiger.kp.citimails.model.Constants;
import com.taiger.kp.citimails.model.Sentence;
import com.taiger.kp.citimails.model.Word;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import opennlp.tools.stemmer.PorterStemmer;
import smile.nlp.tokenizer.SimpleTokenizer;

@Log4j2
@Getter
@Setter
public class SmileSimpleTokenizer implements Tokenizer {
	
	private SimpleTokenizer tokenizer;
	
	private PorterStemmer porterStemmer;
	
	public SmileSimpleTokenizer () {
		initialize ();
	}

	public SmileSimpleTokenizer initialize () {
		this.tokenizer = new SimpleTokenizer();
		this.porterStemmer = new PorterStemmer();
		
		return this;
	}

	@Override
	public Sentence tokenize(String sentence) {
		if (sentence == null || sentence.isEmpty()) return new Sentence();
		
		Sentence result = new Sentence();
		result.setOriginal(sentence);
	
		String[] otokens = tokenizer.split (sentence);

		int min = 0;
		int max = sentence.length();
		for (int i = 0; i < otokens.length; i++) {
			int offset = sentence.substring(min, max).indexOf(otokens[i]) + min;
			min = otokens[i].length() + offset;
			Word word = new Word (otokens[i], Constants.O, 0.1, i+1, offset);
			word.setStem(porterStemmer.stem(otokens[i]).toLowerCase());
			result.addWord (word);
		}
		
		return result;
	}

}
