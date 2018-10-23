package com.taiger.kp.citimails.nlp.ner;

import com.taiger.kp.citimails.model.Sentence;

public interface NER {
	
	Sentence annotate (Sentence sentence);
	
}
