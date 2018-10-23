package com.taiger.kp.citimails.model;

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
	private Text text;
	private DataPoint datapoints;
	
	public Document () {
		this.path = "";
		this.mail = new Mail();
		this.text = new Text();
		this.datapoints = new DataPoint();
	}
	
}
