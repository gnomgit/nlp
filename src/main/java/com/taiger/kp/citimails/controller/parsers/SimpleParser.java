package com.taiger.kp.citimails.controller.parsers;

import com.taiger.kp.citimails.model.Mail;

public interface SimpleParser {
	
	public void parse (String path, String name, Mail mail) throws Exception; 
	
}
