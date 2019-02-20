package com.nlp.mails.controller.annotators;

import com.nlp.mails.model.Document;

public class MailAnnotator {
	
	private NLPAnnotator nlp;
	
	private NERAnnotator ner;
	
	private OERAnnotator oer;
	
	public MailAnnotator () {
		
		this.nlp = new NLPAnnotator();
		this.ner = new NERAnnotator();
		this.oer = new OERAnnotator();
		
	}
	
	public Document annotate (Document document) {
		
		document = nlp.annotateTagger(document);
		
		document = nlp.annotateLanguage(document);
		
		document = ner.annotate(document);
		
		// document = nlp.annotateParser(document);
		
		document = oer.annotate(document);
		
		SubjectAnnotator.annotate(document);
		
		ContentAnnotator.annotate(document);
		
		return document;
	}
}
