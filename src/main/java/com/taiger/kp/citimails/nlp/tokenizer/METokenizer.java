package com.taiger.kp.citimails.nlp.tokenizer;

import java.io.IOException;

import org.springframework.util.Assert;

import com.taiger.kp.citimails.model.Constants;
import com.taiger.kp.citimails.model.Sentence;
import com.taiger.kp.citimails.model.Word;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

@Log4j2
@Getter
@Setter
public class METokenizer implements Tokenizer {
	
	private TokenizerME tokenizer;
	
	private PorterStemmer porterStemmer;
	
	public METokenizer () {
		initialize ();
	}

	public METokenizer initialize () {
		try {
			this.porterStemmer = new PorterStemmer();
			TokenizerModel model = new TokenizerModel(METokenizer.class.getClassLoader().getResourceAsStream(Constants.TOKEN_EN));
			this.tokenizer = new TokenizerME(model);
		} 	catch (IOException e) {
			log.error(e.getMessage());
		}
		
		return this;
	}

	@Override
	public Sentence tokenize(String sentence) {
		Assert.hasText(sentence, "sentence should has text");
		
		Sentence result = new Sentence();
		result.setOriginal(sentence);
	
		String[] otokens = tokenizer.tokenize (sentence);
		double[] tokenProbs = tokenizer.getTokenProbabilities ();

		int min = 0;
		int max = sentence.length();
		for (int i = 0; i < otokens.length; i++) {
			int offset = sentence.substring(min, max).indexOf(otokens[i]) + min;
			min = otokens[i].length() + offset;
			Word word = new Word (otokens[i], Constants.O, tokenProbs[i], i+1, offset);
			word.setStem(porterStemmer.stem(otokens[i]).toLowerCase());
			result.addWord (word);
		}
		
		return result;
	}

}
