package com.nlp.mails.nlp.tokenizer;

import com.nlp.mails.model.Sentence;

public interface Tokenizer {

	Sentence tokenize (String sentence);
	
}
