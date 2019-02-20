package com.nlp.mails.nlp.ner;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.Assert;

import com.nlp.mails.model.Constants;
import com.nlp.mails.model.Sentence;
import com.nlp.mails.model.Word;

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
	
	private Pattern MONEY_PATTERN = Pattern.compile(
			Constants.amount5
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
		Assert.notNull(sentence.getWords(), "sentence content shouldn't be null");
		
		//* searching 
		String[] tokens = new String[sentence.getWords().size()];
		for (int i = 0; i < sentence.getWords().size(); i++) {
			tokens[i] = sentence.getWords().get(i).getW();
		}

		Span[] spansEn = nameFinderEn.find(tokens);
		
		for (Span span : spansEn) {
			sentence.getWords().get(span.getStart()).setNerTag(Constants.B + Constants.MONEY);
			sentence.getWords().get(span.getStart()).getPrevTags().add(Constants.MONEY);
			sentence.getWords().get(span.getStart()).setNerProb(nameFinderEn.probs()[span.getStart()]);
			
			for (int i = span.getStart() + 1; i < span.getEnd(); i++) {
				sentence.getWords().get(i).setNerTag(Constants.I + Constants.MONEY);
				sentence.getWords().get(i).getPrevTags().add(Constants.MONEY);
				sentence.getWords().get(i).setNerProb(nameFinderEn.probs()[i]);
			}
			
		} //*/
		
		extractMoneyRegEx(sentence);
		
		return sentence;
	}
	
	private Sentence extractMoneyRegEx (Sentence sentence) {
		Assert.notNull(sentence, "sentence shouldn't be null");
		
		for (Word w : sentence.getWords()) {
			Matcher matcher = AMOUNT_PATTERN.matcher(w.getW());
			if (matcher.matches()) {
				//log.info(matcher.toString());
				w.setNerTag(Constants.B + Constants.AMOUNT);
				w.getPrevTags().add(Constants.AMOUNT);
			}
			if (CURRENCY_PATTERN.matcher(w.getW().toLowerCase()).matches()) {
				w.setNerTag(Constants.B + Constants.CURRENCY);
				w.getPrevTags().add(Constants.CURRENCY);
			}
			if (MONEY_PATTERN.matcher(w.getW().toLowerCase()).matches()) {
				w.setNerTag(Constants.B + Constants.MONEY);
				w.getPrevTags().add(Constants.MONEY);
			}
		}
		
		for (int i = 0; i < sentence.getWords().size() - 1; i++) {
			Word current = sentence.getWords().get(i);
			Word next = sentence.getWords().get(i+1);
			
			if ((current.getNerTag().equals(Constants.B + Constants.AMOUNT) && next.getNerTag().equals(Constants.B + Constants.AMOUNT))
					|| (current.getNerTag().equals(Constants.I + Constants.AMOUNT) && next.getNerTag().equals(Constants.B + Constants.AMOUNT))) {
				next.setNerTag(Constants.I + Constants.AMOUNT);
			}
			if ((current.getNerTag().equals(Constants.B + Constants.AMOUNT) && next.getNerTag().equals(Constants.B + Constants.CURRENCY))
					|| (current.getNerTag().equals(Constants.B + Constants.CURRENCY) && next.getNerTag().equals(Constants.B + Constants.AMOUNT))
					|| (current.getNerTag().equals(Constants.B + Constants.CURRENCY) && next.getNerTag().equals(Constants.B + Constants.MONEY))) {
				current.setNerTag(Constants.B + Constants.MONEY);
				next.setNerTag(Constants.I + Constants.MONEY);
				current.getPrevTags().add(Constants.MONEY);
				next.getPrevTags().add(Constants.MONEY);
			}	
			if ((current.getNerTag().equals(Constants.I + Constants.AMOUNT) && next.getNerTag().equals(Constants.B + Constants.CURRENCY))
					|| (current.getNerTag().contains(Constants.AMOUNT) && next.getNerTag().equals(Constants.B + Constants.MONEY))) {
				for (int j = i; j >= 0 && sentence.getWords().get(j).getNerTag().contains(Constants.AMOUNT); j--) {
					sentence.getWords().get(j).setNerTag(sentence.getWords().get(j).getNerTag().replace(Constants.AMOUNT, Constants.MONEY));
				}
				next.setNerTag(Constants.I + Constants.MONEY);
				next.getPrevTags().add(Constants.MONEY);
			}	
			if ((current.getNerTag().equals(Constants.B + Constants.MONEY) && next.getNerTag().equals(Constants.B + Constants.AMOUNT))
					|| (current.getNerTag().equals(Constants.I + Constants.MONEY) && next.getNerTag().equals(Constants.B + Constants.AMOUNT))) {
				next.setNerTag(Constants.I + Constants.MONEY);
				next.getPrevTags().add(Constants.MONEY);
			}
		}
		
		return sentence;
	}
	

}
