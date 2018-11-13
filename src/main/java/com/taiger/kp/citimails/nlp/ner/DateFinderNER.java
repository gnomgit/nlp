package com.taiger.kp.citimails.nlp.ner;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
import org.springframework.util.Assert;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import com.taiger.kp.citimails.model.Constants;
import com.taiger.kp.citimails.model.Sentence;
import com.taiger.kp.citimails.model.Word;
import com.wanasit.chrono.Chrono;
import com.wanasit.chrono.ParsedResult;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

@Log4j2
@Getter
@Setter
public class DateFinderNER implements NER {
	
	private NameFinderME nameFinder;
	
	private Pattern DATE_PATTERN = Pattern.compile(
			// yyyy-MM-dd
			"^(" + Constants.yyyy_b + Constants.date_sep + Constants.MM_b + Constants.date_sep + Constants.dd_b + ")$"
			+ "|^(" + Constants.yyyy + Constants.date_sep + Constants.MM_b + Constants.date_sep + Constants.dd_nb + ")$"
			+ "|^(" + Constants.yyyy + Constants.date_sep + Constants.MM_31 + Constants.date_sep + Constants.dd_31 + ")$"
			+ "|^(" + Constants.yyyy + Constants.date_sep + Constants.MM_30 + Constants.date_sep + Constants.dd_30 + ")$"
	      
			+ "|^(" + Constants.dd_b + Constants.date_sep + Constants.MM_b + Constants.date_sep + Constants.yyyy_b + ")$"
			+ "|^(" + Constants.dd_nb + Constants.date_sep + Constants.MM_b + Constants.date_sep + Constants.yyyy + ")$"
			+ "|^(" + Constants.dd_31 + Constants.date_sep + Constants.MM_31 + Constants.date_sep + Constants.yyyy + ")$"
			+ "|^(" + Constants.dd_30 + Constants.date_sep + Constants.MM_30 + Constants.date_sep + Constants.yyyy + ")$"
			
			+ "|^(" + Constants.yy + Constants.date_sep + Constants.MM_b + Constants.date_sep + Constants.dd_b + ")$"
			+ "|^(" + Constants.yy + Constants.date_sep + Constants.MM_b + Constants.date_sep + Constants.dd_nb + ")$"
			+ "|^(" + Constants.yy + Constants.date_sep + Constants.MM_31 + Constants.date_sep + Constants.dd_31 + ")$"
			+ "|^(" + Constants.yy + Constants.date_sep + Constants.MM_30 + Constants.date_sep + Constants.dd_30 + ")$"
	      
			+ "|^(" + Constants.dd_b + Constants.date_sep + Constants.MM_b + Constants.date_sep + Constants.yyyy_b + ")$"
			+ "|^(" + Constants.dd_nb + Constants.date_sep + Constants.MM_b + Constants.date_sep + Constants.yyyy + ")$"
			+ "|^(" + Constants.dd_31 + Constants.date_sep + Constants.MM_31 + Constants.date_sep + Constants.yyyy + ")$"
			+ "|^(" + Constants.dd_30 + Constants.date_sep + Constants.MM_30 + Constants.date_sep + Constants.yyyy + ")$"
			
			+ "|^(" + Constants.dd_b + Constants.date_sep + Constants.MM_b + ")$"
			+ "|^(" + Constants.dd_nb + Constants.date_sep + Constants.MM_b + ")$"
			+ "|^(" + Constants.dd_31 + Constants.date_sep + Constants.MM_31 + ")$"
			+ "|^(" + Constants.dd_30 + Constants.date_sep + Constants.MM_30 + ")$"
	      );
	
	public DateFinderNER () {
		initialize  ();
	}

	public DateFinderNER initialize () {
		try {
			TokenNameFinderModel dnfModel = new TokenNameFinderModel(DateFinderNER.class.getClassLoader().getResourceAsStream(Constants.NER_EN_DATE));
			this.nameFinder = new NameFinderME (dnfModel);
		} 	catch (IOException e) {
			log.error(e.getMessage());
		}
		
		return this;
	}

	@Override
	public Sentence annotate (Sentence sentence) {
		Assert.notNull(sentence, "sentence shouldn't be null");
		Assert.notNull(sentence.getWords(), "sentence content shouldn't be null");
		
		/*String[] tokens = new String[sentence.getWords().size()];
		for (int i = 0; i < sentence.getWords().size(); i++) {
			tokens[i] = sentence.getWords().get(i).getW();
		}
 
		Span spans[] = nameFinder.find(tokens);
		for (Span span : spans) {
			sentence.getWords().get(span.getStart()).setNerTag(Constants.B + Constants.DATE);
			sentence.getWords().get(span.getStart()).getPrevTags().add(Constants.DATE);
			sentence.getWords().get(span.getStart()).setNerProb(nameFinder.probs()[span.getStart()]);
			
			for (int i = span.getStart() + 1; i < span.getEnd(); i++) {
				sentence.getWords().get(i).setNerTag(Constants.I + Constants.DATE);
				sentence.getWords().get(i).getPrevTags().add(Constants.DATE);
				sentence.getWords().get(i).setNerProb(nameFinder.probs()[i]);
			}
		}*/
		
		//extractDatesNatty (sentence);
		//extractDatesChrono (sentence);
		extractDatesPrettydate (sentence);
		extractDatesRegEx (sentence);
		
		return sentence;
	}
	
	private Sentence extractDatesNatty (Sentence sentence) {
		Assert.notNull(sentence, "sentence shouldn't be null");
		
		Parser parser = new Parser();
		List<DateGroup> groups = parser.parse(sentence.getOriginal());
		
		for(DateGroup group : groups) {
			
			int column = group.getPosition();
			String matchingValue = group.getText();
			
			int offset = column - 1;
			int end = offset + matchingValue.length();
			
			for (Word w : sentence.getWords()) {
				if (w.getOffset() <= offset && w.getOffset() + w.getW().length() >= offset) {
					w.setNerTag(Constants.B + Constants.DATE);
				}	else if (w.getOffset() > offset && w.getOffset() < end) {
					w.setNerTag(Constants.I + Constants.DATE);
				}
			}
		}
		
		return sentence;
	}
	
	private Sentence extractDatesChrono (Sentence sentence) {
		Assert.notNull(sentence, "sentence shouldn't be null");
		
		List<ParsedResult> result = Chrono.Parse(sentence.getOriginal());
		
		for (ParsedResult pr : result) {
			
			int offset = pr.index;
			int end = offset + pr.text.length();
			
			for (Word w : sentence.getWords()) {
				if (w.getOffset() <= offset && w.getOffset() + w.getW().length() >= offset) {
					w.setNerTag(Constants.B + Constants.DATE);
				}	else if (w.getOffset() > offset && w.getOffset() < end) {
					w.setNerTag(Constants.I + Constants.DATE);
				}
			}
		}
		
		return sentence;
	}
	
	private Sentence extractDatesPrettydate (Sentence sentence) {
		Assert.notNull(sentence, "sentence shouldn't be null");
		
		List<org.ocpsoft.prettytime.nlp.parse.DateGroup> parse = (new PrettyTimeParser()).parseSyntax(sentence.getOriginal());
		//log.info(sentence);

		for (org.ocpsoft.prettytime.nlp.parse.DateGroup dg : parse) {
			
			int offset = dg.getPosition();
			int end = offset + dg.getText().length();
			
			for (Word w : sentence.getWords()) {
				if (w.getOffset() <= offset && w.getOffset() + w.getW().length() >= offset) {
					w.setNerTag(Constants.B + Constants.DATE);
				}	else if (w.getOffset() > offset && w.getOffset() < end) {
					w.setNerTag(Constants.I + Constants.DATE);
				}
			}
		}
		
		return sentence;
	}
	
	private Sentence extractDatesRegEx (Sentence sentence) {
		Assert.notNull(sentence, "sentence shouldn't be null");
		
		for (Word w : sentence.getWords()) {
			if (DATE_PATTERN.matcher(w.getW()).matches() && w.getNerTag().contains(Constants.O)) {
				w.setNerTag(Constants.B + Constants.DATE);
			}
		}
		
		return sentence;
	}

}
