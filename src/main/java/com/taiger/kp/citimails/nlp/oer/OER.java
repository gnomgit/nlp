package com.taiger.kp.citimails.nlp.oer;

import com.taiger.kp.citimails.model.Sentence;

public interface OER {
	
	Sentence annotate (Sentence sentence);
	
}
