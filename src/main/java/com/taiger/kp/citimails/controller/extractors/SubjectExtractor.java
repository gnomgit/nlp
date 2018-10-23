package com.taiger.kp.citimails.controller.extractors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.Assert;

import com.taiger.kp.citimails.model.Constants;
import com.taiger.kp.citimails.model.Document;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class SubjectExtractor {
	
	private static Pattern SUBJECT_PATTERN = Pattern.compile(
			Constants.subject
	      );
	
	private static Pattern SUBJECT_EXTRA = Pattern.compile(
			Constants.subject_extra
	      );
	
	private static Pattern SUBJECT_EXTRA_FROM = Pattern.compile(
			Constants.subject_from
	      );
	
	public static void extract (Document doc) {
		Assert.notNull(doc, "document null");
		Assert.notNull(doc.getMail(), "mail null");
		Assert.hasText(doc.getMail().getSubject(), "subject null or empty");
		
		Matcher matcher = SUBJECT_PATTERN.matcher(doc.getMail().getSubject());
		if (matcher.matches()) {
			if (matcher.groupCount() >= 17 && matcher.group(17) != null) {
				String[] splitted = matcher.group(17).split(":");
				doc.getDatapoints().put(Constants.LEGAL_ENTITY, splitted[0].trim());
				if (splitted.length >= 2) {
					doc.getDatapoints().put(Constants.SUBJECT_REASON, splitted[1].trim());
				}
			}
			if (matcher.groupCount() >= 11 && matcher.group(11) != null) {
				doc.getDatapoints().put(Constants.COUNTER_PARTY_NAME, matcher.group(11).trim());
			}
			if (matcher.groupCount() >= 7 && matcher.group(7) != null) {
				doc.getDatapoints().put(Constants.AGREEMENT_ID, matcher.group(7).trim());
			}
		}	else {
			matcher = SUBJECT_EXTRA.matcher(doc.getMail().getSubject());
			if (matcher.matches()) {
				if (matcher.groupCount() >= 19 && matcher.group(19) != null) {
					doc.getDatapoints().put(Constants.SUBJECT_TO, matcher.group(19).trim());
				}
				if (matcher.groupCount() >= 13 && matcher.group(13) != null) {
					doc.getDatapoints().put(Constants.SUBJECT_FROM, matcher.group(13).trim());
				}
				if (matcher.groupCount() >= 5 && matcher.group(5) != null) {
					doc.getDatapoints().put(Constants.SUBJECT_REASON, matcher.group(5).trim());
				}
			}	else {
				matcher = SUBJECT_EXTRA_FROM.matcher(doc.getMail().getSubject());
				if (matcher.matches()) {
					if (matcher.groupCount() >= 5 && matcher.group(5) != null) {
						doc.getDatapoints().put(Constants.SUBJECT_REASON, matcher.group(5).trim());
					}
					if (matcher.groupCount() >= 13 && matcher.group(13) != null) {
						doc.getDatapoints().put(Constants.SUBJECT_FROM, matcher.group(13).trim());
					}
				}	else {
					doc.getDatapoints().put(Constants.SUBJECT_REASON, doc.getMail().getSubject());
				}
			}
		}
	}
}
