package com.nlp.mails.nlp.ner;

import com.nlp.mails.model.Sentence;

public interface NER {
	
	Sentence annotate (Sentence sentence);
	
}
