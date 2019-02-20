package com.nlp.mails.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Document {
	
	private String path;
	private Mail mail;
	private Sentence subject;
	private List<Sentence> content;
	
	public Document () {
		this.path = "";
		this.mail = new Mail();
		this.subject = new Sentence();
		this.content = new ArrayList<>();
	}
	
}
