package com.nlp.mails.nlp.languagedetector;

import org.apache.tika.language.LanguageIdentifier;
import org.springframework.util.Assert;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Setter
public class TikaLanguageDetector implements Detector {
	
	public TikaLanguageDetector() {
		initialize();
	}
	
	public TikaLanguageDetector initialize () {
			
		return this;
	}
	
	@Override
	public String detect (String text) {
		Assert.hasText(text, "text shouldn't be null or empty");
		String result = "";
	
		LanguageIdentifier identifier = new LanguageIdentifier(text);
	    result = identifier.getLanguage();
			
		return result;
	}

}
