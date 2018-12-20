package com.taiger.kp.citimails.controller.annotators;

import java.util.ArrayList;
import java.util.List;

import com.taiger.kp.citimails.model.Document;
import com.taiger.kp.citimails.model.Sentence;
import com.taiger.kp.citimails.nlp.oer.CitiWordsFinderOER;
import com.taiger.kp.citimails.nlp.oer.OER;

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
