package com.nlp.mails.controller.extractors;

import com.nlp.mails.model.Mail;

public interface SimpleParser {
	
	public void parse (String path, String name, Mail mail) throws Exception; 
	
}
