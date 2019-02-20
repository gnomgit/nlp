package com.nlp.mails.controller.annotators;

import java.util.ArrayList;
import java.util.List;

import com.nlp.mails.model.Document;
import com.nlp.mails.model.Sentence;
import com.nlp.mails.nlp.oer.CitiWordsFinderOER;
import com.nlp.mails.nlp.oer.OER;

public class OERAnnotator {

	private List<OER> oer;
	
	public OERAnnotator () {
		
		oer = new ArrayList<>();
		
		oer.add(new CitiWordsFinderOER());
		
	}
	
	public Document annotate (Document document) {
		
		List<Sentence> sentences = document.getContent();
		
		oer.forEach(n -> sentences.forEach(n::annotate));
		
		return document;
	}
}
