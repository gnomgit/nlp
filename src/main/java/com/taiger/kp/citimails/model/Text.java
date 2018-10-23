package com.taiger.kp.citimails.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Text {
	
	private List<Sentence> sentences;
	
	public Text() {
		this.sentences = new ArrayList<>();
	}
	
}
