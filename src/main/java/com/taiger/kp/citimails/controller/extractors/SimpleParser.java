package com.taiger.kp.citimails.controller.extractors;

import com.taiger.kp.citimails.model.Mail;

public interface SimpleParser {
	
	public void parse (String path, String name, Mail mail) throws Exception; 
	
}
