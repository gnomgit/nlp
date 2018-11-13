package com.taiger.kp.citimails.model;

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
	private List<Sentence> subject;
	private List<Sentence> content;
	
	public Document () {
		this.path = "";
		this.mail = new Mail();
		this.subject = new ArrayList<>();
		this.content = new ArrayList<>();
	}
	
}
