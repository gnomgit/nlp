package com.nlp.mails.nlp.ner;

import java.io.IOException;

import org.springframework.util.Assert;

import com.nlp.mails.model.Constants;
import com.nlp.mails.model.Sentence;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;



@Log4j2
@Getter
@Setter
public class LocationFinderNER implements NER {

	private NameFinderME nameFinderEn;
	
	//private NameFinderME nameFinderEs;
	
	public LocationFinderNER () {
		initialize ();
	}

	public LocationFinderNER initialize () {
		try {
			TokenNameFinderModel lnfModel = new TokenNameFinderModel(LocationFinderNER.class.getClassLoader().getResourceAsStream(Constants.NER_EN_LOCATION));
			this.nameFinderEn = new NameFinderME (lnfModel);
			//lnfModel = new TokenNameFinderModel(LocationFinderNER.class.getClassLoader().getResourceAsStream(Constants.NER_ES_LOCATION));
			//this.nameFinderEs = new NameFinderME (lnfModel);
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
		//analyze (spansEn, tokens);
		for (Span span : spansEn) {
			sentence.getWords().get(span.getStart()).setNerTag(Constants.B + Constants.LOCATION);
			sentence.getWords().get(span.getStart()).setNerProb(nameFinderEn.probs()[span.getStart()]);
			
			for (int i = span.getStart() + 1; i < span.getEnd(); i++) {
				sentence.getWords().get(i).setNerTag(Constants.I + Constants.LOCATION);
				sentence.getWords().get(i).setNerProb(nameFinderEn.probs()[i]);
			}
			
			/*String chunk = Constants.formChunk(tokens, span);
			
			DBPediaLocation dbpl = RestConsumer.dbpediaCall(chunk, "DBpedia:Place,DBpedia:Location");
			Assert.notNull(dbpl, "RestConsumer.dbpediaCall(chunk, \"DBpedia:Place,DBpedia:Location\") NULL");
			if (dbpl.getResources() != null) {
				Pair<Double, Double> coordinates = RestConsumer.dbpediaSparql(dbpl.getResources().get(0).getUri());
			
				LocationNER loc = findLocation(chunk, coordinates);
				if (loc != null) {
					loc.setGeoLink(findGeoLink(chunk));
					sentence.addLocation(loc);
					sentence.getS().get(span.getStart()).setLocation(loc);
					
					for (int i = span.getStart() + 1; i < span.getEnd(); i++) {
						sentence.getS().get(i).setNerTag(Constants.I + Constants.LOCATION);
						sentence.getS().get(i).setNerProb(nameFinderEn.probs()[i]);
					}
				}
			}*/
			
			
		} //*/
		
		/* Searching in spanish
		int i = 0;
		for (WordNER w : sentence.getS()) {
			boolean condition = w.getNerTag().contains(Constants.B) || w.getNerTag().contains(Constants.I);
			tokens[i] = condition ? "" : w.getW();
			i++;
		}

		Span[] spansEs = nameFinderEs.find(tokens);
		for (Span span : spansEs) {
			sentence.getS().get(span.getStart()).setNerTag(Constants.B + Constants.LOCATION);
			sentence.getS().get(span.getStart()).setNerProb(nameFinderEs.probs()[span.getStart()]);
			
			String chunk = Constants.formChunk(tokens, span);
			
			DBPediaLocation dbpl = RestConsumer.dbpediaCall(chunk, "DBpedia:Place,DBpedia:Location");
			Assert.notNull(dbpl, "RestConsumer.dbpediaCall(chunk, \"DBpedia:Place,DBpedia:Location\") NULL");
			if (dbpl.getResources() != null) {
				Pair<Double, Double> coordinates = RestConsumer.dbpediaSparql(dbpl.getResources().get(0).getUri());
				
				LocationNER loc = findLocation(chunk, coordinates);
				if (loc != null) {
					loc.setGeoLink(findGeoLink(chunk));
					sentence.addLocation(loc);
					sentence.getS().get(span.getStart()).setLocation(loc);
					
					for (i = span.getStart() + 1; i < span.getEnd(); i++) {
						sentence.getS().get(i).setNerTag(Constants.I + Constants.LOCATION);
						sentence.getS().get(i).setNerProb(nameFinderEs.probs()[i]);
					}
				}
			}
		} //*/
		
		return sentence;
	}
	

}
