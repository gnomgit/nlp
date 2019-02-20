package com.nlp.mails.controller.annotators;

import java.util.List;

import com.nlp.mails.model.Document;
import com.nlp.mails.model.Sentence;
import com.nlp.mails.nlp.languagedetector.Detector;
import com.nlp.mails.nlp.languagedetector.SentenceLanguageDetector;
import com.nlp.mails.nlp.languagedetector.TikaLanguageDetector;
import com.nlp.mails.nlp.parser.DeepParser;
import com.nlp.mails.nlp.parser.SyntaxParser;
import com.nlp.mails.nlp.tagger.METagger;
import com.nlp.mails.nlp.tagger.POSTagger;

public class NLPAnnotator {
	
	POSTagger tagger;
	
	SyntaxParser parser;
	
	Detector languageDetector;
	
	public NLPAnnotator () {
		
		tagger = new METagger();
		parser = new DeepParser();
		languageDetector = new SentenceLanguageDetector();
		//languageDetector = new TikaLanguageDetector();
		
	}
	
	public Document annotateTagger (Document document) {
		
		List<Sentence> sentences = document.getContent();
		
		sentences.forEach(tagger::annotate);
		
		return document;
	}
	
	public Document annotateParser (Document document) {
		
		List<Sentence> sentences = document.getContent();
		
		sentences.forEach(parser::annotate);
		
		return document;
	}
	
	public Document annotateLanguage (Document document) {
		
		List<Sentence> sentences = document.getContent();
		
		sentences.forEach(s -> s.setLanguage(languageDetector.detect(s.getOriginal())));
		
		return document;
	}
}
