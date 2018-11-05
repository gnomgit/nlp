package com.taiger.kp.citimails.nlp.ner;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.Assert;

import com.taiger.kp.citimails.model.Constants;
import com.taiger.kp.citimails.model.Sentence;
import com.taiger.kp.citimails.model.Word;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;



@Log4j2
@Getter
@Setter
public class MoneyFinderNER implements NER {

	private NameFinderME nameFinderEn;
	
	private Pattern AMOUNT_PATTERN = Pattern.compile(
			Constants.amount4
	      );
	
	private Pattern CURRENCY_PATTERN = Pattern.compile(
			Constants.currency
	      );
	
	public MoneyFinderNER () {
		initialize ();
	}

	public MoneyFinderNER initialize () {
		try {
			TokenNameFinderModel lnfModel = new TokenNameFinderModel(MoneyFinderNER.class.getClassLoader().getResourceAsStream(Constants.NER_EN_MONEY));
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
			sentence.getS().get(span.getStart()).setNerTag(Constants.B + Constants.MONEY);
			sentence.getS().get(span.getStart()).setNerProb(nameFinderEn.probs()[span.getStart()]);
			
			for (int i = span.getStart() + 1; i < span.getEnd(); i++) {
				sentence.getS().get(i).setNerTag(Constants.I + Constants.MONEY);
				sentence.getS().get(i).setNerProb(nameFinderEn.probs()[i]);
			}
			
		} //*/
		
		extractMoneyRegEx(sentence);
		
		return sentence;
	}
	
	private Sentence extractMoneyRegEx (Sentence sentence) {
		Assert.notNull(sentence, "sentence shouldn't be null");
		
		for (Word w : sentence.getS()) {
			Matcher matcher = AMOUNT_PATTERN.matcher(w.getW());
			if (matcher.matches()) {
				log.info(matcher.toString());
				w.setNerTag(Constants.B + Constants.AMOUNT);
			}
			if (CURRENCY_PATTERN.matcher(w.getW().toLowerCase()).matches()) {
				w.setNerTag(Constants.B + Constants.CURRENCY);
			}
		}
		
		for (int i = 0; i < sentence.getS().size() - 1; i++) {
			Word current = sentence.getS().get(i);
			Word next = sentence.getS().get(i+1);
			
			if ((current.getNerTag().equals(Constants.B + Constants.AMOUNT) && next.getNerTag().equals(Constants.B + Constants.AMOUNT))
					|| (current.getNerTag().equals(Constants.I + Constants.AMOUNT) && next.getNerTag().equals(Constants.B + Constants.AMOUNT))) {
				next.setNerTag(Constants.I + Constants.AMOUNT);
			}
			if ((current.getNerTag().equals(Constants.B + Constants.AMOUNT) && next.getNerTag().equals(Constants.B + Constants.CURRENCY))
					|| (current.getNerTag().equals(Constants.B + Constants.CURRENCY) && next.getNerTag().equals(Constants.B + Constants.AMOUNT))) {
				current.setNerTag(Constants.B + Constants.MONEY);
				next.setNerTag(Constants.I + Constants.MONEY);
			}	
			if ((current.getNerTag().equals(Constants.I + Constants.AMOUNT) && next.getNerTag().equals(Constants.B + Constants.CURRENCY))
					|| (current.getNerTag().contains(Constants.AMOUNT) && next.getNerTag().equals(Constants.B + Constants.MONEY))) {
				for (int j = i; j >= 0 && sentence.getS().get(j).getNerTag().contains(Constants.AMOUNT); j--) {
					sentence.getS().get(j).setNerTag(sentence.getS().get(j).getNerTag().replace(Constants.AMOUNT, Constants.MONEY));
				}
				next.setNerTag(Constants.I + Constants.MONEY);
			}	
			if ((current.getNerTag().equals(Constants.B + Constants.MONEY) && next.getNerTag().equals(Constants.B + Constants.AMOUNT))
					|| (current.getNerTag().equals(Constants.I + Constants.MONEY) && next.getNerTag().equals(Constants.B + Constants.AMOUNT))) {
				next.setNerTag(Constants.I + Constants.MONEY);
			}
		}
		
		/*for (int i = 0; i < sentence.getS().size() - 1; i++) {
			Word current = sentence.getS().get(i);
			Word next = sentence.getS().get(i+1);
			
				
			if ((current.getNerTag().contains(Constants.AMOUNT) && next.getNerTag().equals(Constants.B + Constants.MONEY))) {
				for (int j = i; j >= 0 && sentence.getS().get(j).getNerTag().contains(Constants.AMOUNT); j--) {
					sentence.getS().get(j).setNerTag(sentence.getS().get(j).getNerTag().replace(Constants.AMOUNT, Constants.MONEY));
				}
				next.setNerTag(Constants.I + Constants.MONEY);
			}
		}*/
		
		return sentence;
	}
	

}
