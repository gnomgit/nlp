package com.taiger.kp.citimails.controller.annotators;

import java.util.List;

import com.taiger.kp.citimails.model.Document;
import com.taiger.kp.citimails.model.Sentence;
import com.taiger.kp.citimails.nlp.parser.DeepParser;
import com.taiger.kp.citimails.nlp.parser.SyntaxParser;
import com.taiger.kp.citimails.nlp.tagger.METagger;
import com.taiger.kp.citimails.nlp.tagger.POSTagger;

public class NLPAnnotator {
	
	POSTagger tagger;
	
	SyntaxParser parser;
	
	public NLPAnnotator () {
		
		tagger = new METagger();
		parser = new DeepParser();
		
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
}
