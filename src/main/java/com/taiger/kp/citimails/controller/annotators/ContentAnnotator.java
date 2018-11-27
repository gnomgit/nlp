package com.taiger.kp.citimails.controller.annotators;

import java.util.List;

import org.springframework.util.Assert;

import com.taiger.kp.citimails.model.Constants;
import com.taiger.kp.citimails.model.Document;
import com.taiger.kp.citimails.model.Sentence;
import com.taiger.kp.citimails.model.Word;

import lombok.extern.log4j.Log4j2;

public class ContentAnnotator {
	
	public static void annotate (Document doc) {
		Assert.notNull(doc, "document null");
		Assert.notNull(doc.getMail(), "mail null");
		Assert.notNull(doc.getMail().getFrom(), "'from' null");
		Assert.notNull(doc.getMail().getTo(), "'to' null");
		
		List<Sentence> sentences = doc.getContent();
		for (Sentence s : sentences) {
			for (Word w : s.getWords()) {
				//log.info(w);
				if (!w.getOerTag().isEmpty()) {
					String oClass = w.getOerTag().split("\\|")[0];
					if (oClass != null && oClass.contains("/")) {
						oClass = oClass.substring(oClass.lastIndexOf('/') + 1).toUpperCase().trim();
						/*w.getDatapoints().add(Constants.AT + oClass);
						w.getPrevTags().forEach(t -> w.getDatapoints().add(t));*/
						addDatapoint(w, oClass);
					}
				}
				for (String tag : w.getPrevTags()) {
					if (tag.equals(Constants.DATE)) {
						w.getDatapoints().add(tag);
						break;
					}
				}
			}
		}
	}
	
	private static void addDatapoint (Word word, String oClass) {
		String tag = "";
		if (word.getPrevTags().contains(Constants.DATE)) {
			tag = Constants.DATE;
		}	else if (word.getPrevTags().contains(Constants.CURRENCY)) {
			tag = Constants.CURRENCY;
		}	else if (word.getPrevTags().contains(Constants.AMOUNT)) {
			tag = Constants.AMOUNT;
		}
		word.getDatapoints().add(Constants.AT + oClass + "_" + tag);
		word.getPrevTags().forEach(t -> word.getDatapoints().add(t));
		
	}
	
}
