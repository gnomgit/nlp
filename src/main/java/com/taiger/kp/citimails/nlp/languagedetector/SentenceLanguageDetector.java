package com.taiger.kp.citimails.nlp.languagedetector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import com.optimaize.langdetect.DetectedLanguage;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObjectFactory;
import com.taiger.kp.citimails.model.Constants;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Setter
public class SentenceLanguageDetector implements Detector {
	
	private List<LanguageProfile> languageProfiles;
	private List<LanguageProfile> languageProfilesEnEs;
	private LanguageDetector languageDetector;
	private TextObjectFactory textObjectFactory;
	
	public SentenceLanguageDetector() {
		initialize();
	}
	
	public SentenceLanguageDetector initialize () {
		this.languageProfilesEnEs = new ArrayList<>();
		try {
			this.languageProfiles = new LanguageProfileReader().readAllBuiltIn();
			this.languageProfiles.forEach(lp -> {
				if (Constants.languages.contains(lp.getLocale().getLanguage()))
					this.languageProfilesEnEs.add(lp);
			});
			
			//build language detector:
			languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
			        .withProfiles(languageProfilesEnEs)
			        .build();
			
			//create a text object factory
			textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();
			
		} 	catch (IOException e1) {
			log.error(e1.getMessage());
		}
			
		return this;
	}
	
	@Override
	public String detect (String text) {
		Assert.hasText(text, "text shouldn't be null or empty");
		String result = "";
	
		//query:
		//TextObject textObject = textObjectFactory.forText(text);
		//TextObject textObject = textObjectFactory.forText("Best regards ,");
		//Optional<LdLocale> lang = languageDetector.detect(textObject);
		List<DetectedLanguage> results = languageDetector.getProbabilities(text);
		if (results != null && !results.isEmpty()) result = results.get(0).getLocale().getLanguage();
			
		return result;
	}

}
