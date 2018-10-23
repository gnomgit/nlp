package com.taiger.kp.citimails.nlp.tokenizer;

import com.taiger.kp.citimails.model.Sentence;

public interface Tokenizer {

	Sentence tokenize (String sentence);
	
}
