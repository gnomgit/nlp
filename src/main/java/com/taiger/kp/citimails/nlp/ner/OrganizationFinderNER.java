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
public class OrganizationFinderNER implements NER {

	private NameFinderME nameFinderEn;
	
	public OrganizationFinderNER () {
		initialize ();
	}

	public OrganizationFinderNER initialize () {
		try {
			TokenNameFinderModel lnfModel = new TokenNameFinderModel(OrganizationFinderNER.class.getClassLoader().getResourceAsStream(Constants.NER_EN_ORGANIZATION));
			this.nameFinderEn = new NameFinderME (lnfModel);
		} 	catch (IOException e) {
			log.error(e.getMessage());
		}
		
		return this;
	}

	@Override
	public Sentence annotate(Sentence sentence) {
		Assert.notNull(sentence, "sentence shouldn't be null");
		Assert.notNull(sentence.getS(), "sentence content shouldn't be null");
		
		//* searching in english
		String[] tokens = new String[sentence.getS().size()];
		for (int i = 0; i < sentence.getS().size(); i++) {
			tokens[i] = sentence.getS().get(i).getW();
		}

		Span[] spansEn = nameFinderEn.find(tokens);
		
		for (Span span : spansEn) {
			sentence.getS().get(span.getStart()).setNerTag(Constants.B + Constants.ORGANIZATION);
			sentence.getS().get(span.getStart()).setNerProb(nameFinderEn.probs()[span.getStart()]);
			
			for (int i = span.getStart() + 1; i < span.getEnd(); i++) {
				sentence.getS().get(i).setNerTag(Constants.I + Constants.ORGANIZATION);
				sentence.getS().get(i).setNerProb(nameFinderEn.probs()[i]);
			}
			
		} //*/
		
		return sentence;
	}
	

}
