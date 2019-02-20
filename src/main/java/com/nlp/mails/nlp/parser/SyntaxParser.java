package com.nlp.mails.nlp.parser;

import com.nlp.mails.model.Sentence;

public interface SyntaxParser {

	Sentence annotate (Sentence sentence);
	
}
