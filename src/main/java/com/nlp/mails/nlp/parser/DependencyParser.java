package com.nlp.mails.nlp.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import org.maltparser.MaltParserService;
import org.maltparser.core.exception.MaltChainedException;
import org.maltparser.core.syntaxgraph.DependencyStructure;
import org.maltparser.core.syntaxgraph.LabelSet;
import org.maltparser.core.syntaxgraph.edge.Edge;
import org.maltparser.core.syntaxgraph.node.DependencyNode;
import org.springframework.util.Assert;

import com.nlp.mails.model.Constants;
import com.nlp.mails.model.Sentence;
import com.nlp.mails.utils.StringTools;

import lombok.extern.log4j.Log4j2;
import opennlp.tools.parser.ParserModel;

@Log4j2
public class DependencyParser implements SyntaxParser {
	
	MaltParserService service;
	
	public DependencyParser () {
		initialize  ();
	}

	public DependencyParser initialize () {
		try {
			ParserModel model = new ParserModel (DependencyParser.class.getClassLoader().getResourceAsStream(Constants.PARSER_EN));
			
			this.service =  new MaltParserService();
			service.initializeParserModel(Constants.MALTPARSER_ARGS);
			
		} 	catch (IOException | MaltChainedException e) {
			log.error(e.getMessage());
		}
		
		return this;
	}
	
	@Override
	public Sentence annotate(Sentence sentence) {
		Assert.notNull(sentence, "sentence shouldn't be null");
		Assert.notNull(sentence.getWords(), "sentence content shouldn't be null");
		
		String[] tokens = StringTools.sentence2conllFormat(sentence);
		DependencyStructure graph = null;
		try {
			graph = service.parse(tokens);
		} 	catch (MaltChainedException e) {
			log.error(e.getMessage());
		}
		log.info(graph);
		try {
			List<Edge> edges = new ArrayList<>(graph.getEdges());
			for (int i = 1; i <= sentence.getWords().size(); i++) {
				DependencyNode node = graph.getDependencyNode(i);
				Edge edge = edges.get(i-1);
				int head = node.getHead().getIndex();
				sentence.getWords().get(i-1).setFather(head);
				if (edge.toString().contains(":")) {
					String label = edge.toString().split(" ")[1].split(":")[1];
					sentence.getWords().get(i-1).setDepTag(label);
				}
			}
		} catch (MaltChainedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sentence;
	}

}
