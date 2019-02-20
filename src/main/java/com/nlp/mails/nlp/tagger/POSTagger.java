package com.nlp.mails.nlp.tagger;

import com.nlp.mails.model.Sentence;

public interface POSTagger {

	Sentence annotate (Sentence sentence);
	
}
