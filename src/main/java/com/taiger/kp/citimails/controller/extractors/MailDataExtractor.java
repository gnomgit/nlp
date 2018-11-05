package com.taiger.kp.citimails.controller.extractors;

import org.springframework.util.Assert;

import com.taiger.kp.citimails.model.Constants;
import com.taiger.kp.citimails.model.Document;

public class MailDataExtractor {
	
	public static void extract (Document doc) {
		Assert.notNull(doc, "document null");
		Assert.notNull(doc.getMail(), "mail null");
		Assert.notNull(doc.getMail().getFrom(), "'from' null");
		Assert.notNull(doc.getMail().getTo(), "'to' null");
		
		doc.getDatapoints().put(Constants.FROM, doc.getMail().getFrom());
		doc.getDatapoints().put(Constants.TO, doc.getMail().getTo());
	}
	
}
