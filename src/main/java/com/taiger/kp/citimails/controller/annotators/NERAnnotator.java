package com.taiger.kp.citimails.controller.annotators;

import java.util.ArrayList;
import java.util.List;

import com.taiger.kp.citimails.model.Document;
import com.taiger.kp.citimails.model.Sentence;
import com.taiger.kp.citimails.nlp.ner.CUSIPFinderNER;
import com.taiger.kp.citimails.nlp.ner.CorrectorNER;
import com.taiger.kp.citimails.nlp.ner.DateFinderNER;
import com.taiger.kp.citimails.nlp.ner.ISINFinderNER;
import com.taiger.kp.citimails.nlp.ner.MoneyFinderNER;
import com.taiger.kp.citimails.nlp.ner.NER;
import com.taiger.kp.citimails.nlp.ner.OrganizationFinderNER;
import com.taiger.kp.citimails.nlp.ner.PercentageFinderNER;
import com.taiger.kp.citimails.nlp.ner.PersonFinderNER;
import com.taiger.kp.citimails.nlp.ner.TimeFinderNER;

public class NERAnnotator {
	
	private List<NER> ner;
	
	public NERAnnotator () {
		
		ner = new ArrayList<>();
		
		ner.add(new OrganizationFinderNER());
		ner.add(new PersonFinderNER());
		ner.add(new DateFinderNER());
		ner.add(new TimeFinderNER());
		ner.add(new PercentageFinderNER());
		ner.add(new MoneyFinderNER());
		ner.add(new ISINFinderNER());
		ner.add(new CUSIPFinderNER());
		ner.add(new CorrectorNER());
		
	}
	
	public Document annotate (Document document) {
		
		List<Sentence> sentences = document.getContent();
		
		ner.forEach(n -> sentences.forEach(n::annotate));
		
		return document;
	}
	
}
