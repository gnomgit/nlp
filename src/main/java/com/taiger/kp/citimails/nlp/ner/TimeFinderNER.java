package com.taiger.kp.citimails.nlp.ner;

import java.io.IOException;

import org.springframework.util.Assert;

import com.taiger.kp.citimails.model.Constants;
import com.taiger.kp.citimails.model.Sentence;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;



@Log4j2
@Getter
@Setter
public class TimeFinderNER implements NER {

	private NameFinderME nameFinderEn;
	
	public TimeFinderNER () {
		initialize ();
	}

	public TimeFinderNER initialize () {
		try {
			TokenNameFinderModel lnfModel = new TokenNameFinderModel(TimeFinderNER.class.getClassLoader().getResourceAsStream(Constants.NER_EN_TIME));
			this.nameFinderEn = new NameFinderME (lnfModel);
		} 	catch (IOException e) {
			log.error(e.getMessage());
		}
		
		return this;
	}

	@Override
	public Sentence annotate(Sentence sentence) {
		Assert.notNull(sentence, "sentence shouldn't be null");
		Assert.notNull(sentence.getWords(), "sentence content shouldn't be null");
		
		//* searching in english
		String[] tokens = new String[sentence.getWords().size()];
		for (int i = 0; i < sentence.getWords().size(); i++) {
			tokens[i] = sentence.getWords().get(i).getW();
		}

		Span[] spansEn = nameFinderEn.find(tokens);
		
		for (Span span : spansEn) {
			sentence.getWords().get(span.getStart()).setNerTag(Constants.B + Constants.TIME);
			sentence.getWords().get(span.getStart()).setNerProb(nameFinderEn.probs()[span.getStart()]);
			
			for (int i = span.getStart() + 1; i < span.getEnd(); i++) {
				sentence.getWords().get(i).setNerTag(Constants.I + Constants.TIME);
				sentence.getWords().get(i).setNerProb(nameFinderEn.probs()[i]);
			}
			
		} //*/
		
		return sentence;
	}
	

}
