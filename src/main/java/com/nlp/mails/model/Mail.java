package com.nlp.mails.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@Log4j2
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Mail {
	
	private String subject = "";
	private String content = "";
	private String from = "";
	private String to = "";
	
	private List<Attachment> attachments = new ArrayList<>();
	
	public void save (File file) {
		Assert.notNull(file, "file shouldn't be null");
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(file, this);
		} 	catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
}
