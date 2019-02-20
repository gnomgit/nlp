package com.nlp.mails.nlp.spellchecker;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.Dictionary;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.util.Version;

import com.nlp.mails.model.Constants;
import com.nlp.mails.model.Sentence;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@Log4j2
public class LuceneSpellChecker implements SpellChecker {

	org.apache.lucene.store.RAMDirectory dir;
	org.apache.lucene.search.spell.SpellChecker checker;
	com.nlp.mails.model.Dictionary dictionary;
	
	public LuceneSpellChecker() {
		dir = new org.apache.lucene.store.RAMDirectory();
		dictionary = new com.nlp.mails.model.Dictionary();
		try {
			checker = new org.apache.lucene.search.spell.SpellChecker(dir);
			checker.indexDictionary(new PlainTextDictionary(new StringReader(dictionary.asLines())), new IndexWriterConfig(Version.LUCENE_36, new KeywordAnalyzer()), false);
		} 	catch (IOException e) {
			log.error(e.getMessage());
		}
		
	}

	@Override
	public Sentence checkSentence(Sentence sentence) {

		sentence.getWords().forEach(w -> {
			if (!dictionary.getWords().contains(w.getW())) {
				try {
					String[] suggestions = checker.suggestSimilar(w.getW(), 1);
					if (suggestions.length >= 1) {
						log.info("{} - {} ", w.getW(), suggestions[0]);
					}
				} 	catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		});
		
		return sentence;
	}

}
