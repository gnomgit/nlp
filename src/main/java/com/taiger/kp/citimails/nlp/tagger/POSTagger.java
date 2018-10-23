package com.taiger.kp.citimails.nlp.tagger;

import com.taiger.kp.citimails.model.Sentence;

public interface POSTagger {

	Sentence annotate (Sentence sentence);
	
}
