package com.taiger.kp.citimails.nlp.spellchecker;

import com.taiger.kp.citimails.model.Sentence;

public interface SpellChecker {
	
	abstract public Sentence checkSentence (Sentence sentence);

}
