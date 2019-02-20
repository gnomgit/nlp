package com.nlp.mails.controller.annotators;

import java.util.ArrayList;
import java.util.List;

import com.nlp.mails.model.Document;
import com.nlp.mails.model.Sentence;
import com.nlp.mails.nlp.ner.CUSIPFinderNER;
import com.nlp.mails.nlp.ner.CorrectorNER;
import com.nlp.mails.nlp.ner.DateFinderNER;
import com.nlp.mails.nlp.ner.ISINFinderNER;
import com.nlp.mails.nlp.ner.MoneyFinderNER;
import com.nlp.mails.nlp.ner.NER;
import com.nlp.mails.nlp.ner.OrganizationFinderNER;
import com.nlp.mails.nlp.ner.PercentageFinderNER;
import com.nlp.mails.nlp.ner.PersonFinderNER;
import com.nlp.mails.nlp.ner.TimeFinderNER;

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
