package com.taiger.kp.citimails.controller.annotators;

import com.taiger.kp.citimails.model.Document;

public class MailAnnotator {
	
	public MailAnnotator () {
		
		
	}
	
	public Document annotate (Document document) {
		
		
		
		SubjectAnnotator.extract(document);
		
		ContentAnnotator.extract(document);
		
		return document;
	}
}
