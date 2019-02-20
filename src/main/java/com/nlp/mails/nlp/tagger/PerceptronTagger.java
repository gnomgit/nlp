package com.nlp.mails.nlp.tagger;

import java.io.IOException;

import org.springframework.util.Assert;

import com.nlp.mails.model.Constants;
import com.nlp.mails.model.Sentence;

import lombok.extern.log4j.Log4j2;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

@Log4j2
public class PerceptronTagger implements POSTagger {

	private POSTaggerME tagger;
	
	public PerceptronTagger () {
		initialize  ();
	}

	public PerceptronTagger initialize () {
		try {
			POSModel model = new POSModel (PerceptronTagger.class.getClassLoader().getResourceAsStream(Constants.TAGGER_PER_EN));
			this.tagger = new POSTaggerME (model);
		} 	catch (IOException e) {
			log.error(e.getMessage());
		}
		
		return this;
	}
	
	@Override
	public Sentence annotate(Sentence sentence) {
		Assert.notNull(sentence, "sentence shouldn't be null");
		Assert.notNull(sentence.getWords(), "sentence content shouldn't be null");
		String[] tokens = new String[sentence.getWords().size()];
		for (int i = 0; i < sentence.getWords().size(); i++) {
			tokens[i] = sentence.getWords().get(i).getW();
		}
		
		String[] tags = tagger.tag(tokens);
        for (int i = 0; i < tokens.length; i++) {
        	sentence.getWords().get(i).setPosTag(tags[i]);
        }
		
		return sentence;
	}

}
