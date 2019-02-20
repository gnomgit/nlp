package com.nlp.mails.nlp.spellchecker;

import com.nlp.mails.model.Sentence;

public interface SpellChecker {
	
	abstract public Sentence checkSentence (Sentence sentence);

}
