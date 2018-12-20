package com.taiger.kp.citimails.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Sentence {

	private String original;
	private List<Word> words;
	private String language;
	
	public Sentence () {
		this.words = new ArrayList<>();
		this.original = "";
	}
	
	public void addWord (Word word) {
		Assert.notNull(word, "word should not be null");
		this.words.add(word);
	}
}
