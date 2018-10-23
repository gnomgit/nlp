package com.taiger.kp.citimails.nlp.parser;

import com.taiger.kp.citimails.model.Sentence;

public interface SyntaxParser {

	Sentence annotate (Sentence sentence);
	
}
