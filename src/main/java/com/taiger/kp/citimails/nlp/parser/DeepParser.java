package com.taiger.kp.citimails.nlp.parser;

import java.io.IOException;

import org.springframework.util.Assert;

import com.taiger.kp.citimails.model.Constants;
import com.taiger.kp.citimails.model.Sentence;
import com.taiger.kp.citimails.nlp.tokenizer.METokenizer;

import lombok.extern.log4j.Log4j2;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.tokenize.Tokenizer;

@Log4j2
public class DeepParser implements SyntaxParser {

	private Parser parser;
	
	public DeepParser () {
		initialize  ();
	}

	public DeepParser initialize () {
		try {
			ParserModel model = new ParserModel (DeepParser.class.getClassLoader().getResourceAsStream(Constants.PARSER_EN));
			this.parser = ParserFactory.create(model);
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
		StringBuilder original = new StringBuilder();
		
		for (int i = 0; i < sentence.getWords().size(); i++) {
			tokens[i] = sentence.getWords().get(i).getW();
			original.append(sentence.getWords().get(i).getW() + " ");
		}

		Parse[] tags = ParserTool.parseLine(sentence.getOriginal(), this.parser, (Tokenizer)(new METokenizer()).getTokenizer(), 1);
        for (int i = 0; i < tokens.length; i++) {
        	
        }
        for (Parse p : tags) {
        	p.show();
        	log.info(p.getLabel());
        	log.info(p.getType());
        }
        System.out.println();
		
		return sentence;
	}

}
