package com.taiger.kp.citimails.controller.extractors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.Assert;

import com.taiger.kp.citimails.model.Constants;
import com.taiger.kp.citimails.model.Document;
import com.taiger.kp.citimails.model.Sentence;
import com.taiger.kp.citimails.model.Word;

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
		
		String subject = doc.getSubject().get(0).getOriginal();
		Sentence s = doc.getSubject().get(0);
		
		//for (Sentence s : doc.getSubject()) {
		/*if (doc.getSubject().size() >= 1) {
			int last = s.getS().size() - 1;
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < s.getS().get(last).getOffset() + s.getS().get(last).getW().length(); i++) {
				sb.append(" ");
			}
			for (Word w : s.getS()) {
				sb.insert(w.getOffset(), w.getW());
			}
			subject = sb.toString().trim();
		}*/
		
		
		log.info(subject);
		
		Matcher matcher = SUBJECT_PATTERN.matcher(subject);
		if (matcher.matches()) {
			if (matcher.groupCount() >= 17 && matcher.group(17) != null) {
				String[] splitted = matcher.group(17).split(":");
				log.info(Constants.DP_LEGAL_ENTITY + " {}", splitted[0].trim());
				insertDataPoint (s, subject, splitted[0].trim(), Constants.DP_LEGAL_ENTITY);
				if (splitted.length >= 2) {
					log.info(Constants.DP_SUBJECT_REASON + " {}", splitted[1].trim());
					insertDataPoint (s, subject, splitted[1].trim(), Constants.DP_SUBJECT_REASON);
				}
			}
			if (matcher.groupCount() >= 11 && matcher.group(11) != null) {
				log.info(Constants.DP_COUNTER_PARTY_NAME + " {}", matcher.group(11).trim());
				insertDataPoint (s, subject, matcher.group(11).trim(), Constants.DP_COUNTER_PARTY_NAME);
			}
			if (matcher.groupCount() >= 7 && matcher.group(7) != null) {
				log.info(Constants.DP_AGREEMENT_ID + " {}", matcher.group(7).trim());
				insertDataPoint (s, subject, matcher.group(7).trim(), Constants.DP_AGREEMENT_ID);
			}
		}	else {
			matcher = SUBJECT_EXTRA.matcher(subject);
			if (matcher.matches()) {
				if (matcher.groupCount() >= 19 && matcher.group(19) != null) {
					log.info(Constants.DP_SUBJECT_TO + " {}", matcher.group(19).trim());
					insertDataPoint (s, subject, matcher.group(19).trim(), Constants.DP_SUBJECT_TO);
				}
				if (matcher.groupCount() >= 13 && matcher.group(13) != null) {
					log.info(Constants.DP_SUBJECT_FROM + " {}", matcher.group(13).trim());
					insertDataPoint (s, subject, matcher.group(13).trim(), Constants.DP_SUBJECT_FROM);
				}
				if (matcher.groupCount() >= 5 && matcher.group(5) != null) {
					log.info(Constants.DP_SUBJECT_REASON + " {}", matcher.group(5).trim());
					insertDataPoint (s, subject, matcher.group(5).trim(), Constants.DP_SUBJECT_REASON);
				}
			}	else {
				matcher = SUBJECT_EXTRA_FROM.matcher(subject);
				if (matcher.matches()) {
					if (matcher.groupCount() >= 5 && matcher.group(5) != null) {
						log.info(Constants.DP_SUBJECT_REASON + " {}", matcher.group(5).trim());
						insertDataPoint (s, subject, matcher.group(5).trim(), Constants.DP_SUBJECT_REASON);
					}
					if (matcher.groupCount() >= 13 && matcher.group(13) != null) {
						log.info(Constants.DP_SUBJECT_FROM + " {}", matcher.group(13).trim());
						insertDataPoint (s, subject, matcher.group(13).trim(), Constants.DP_SUBJECT_FROM);
					}
				}	else {
					log.info(Constants.DP_SUBJECT_REASON + " {}", doc.getMail().getSubject());
					insertDataPoint (s, subject, doc.getMail().getSubject().trim(), Constants.DP_SUBJECT_REASON);
				}
			}
		}
		

		for (Word w : doc.getSubject().get(0).getWords()) {
			log.info(w);
		}
	}

	private static void insertDataPoint(Sentence sentence, String fullString, String subString, String dataPointLabel) {
		int start = fullString.indexOf(subString);
		int end = start + subString.length();
		
		for (Word w : sentence.getWords()) {
			if (w.getOffset() >= start && w.getOffset() < end) {
				w.getDatapoints().add(dataPointLabel);
			}
		}
	}
}
