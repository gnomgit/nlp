package com.taiger.kp.citimails.nlp.tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.util.StringUtils;

import com.taiger.kp.citimails.model.Constants;
import com.taiger.kp.citimails.model.Sentence;
import com.taiger.kp.citimails.model.Word;
import com.taiger.kp.citimails.utils.StringTools;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import opennlp.tools.stemmer.PorterStemmer;

@Log4j2
@Getter
@Setter
public class SimpleSpaceTokenizer implements Tokenizer {
	
	private PorterStemmer porterStemmer;
	
	public SimpleSpaceTokenizer () {
		initialize ();
	}

	public SimpleSpaceTokenizer initialize () {
		this.porterStemmer = new PorterStemmer();
		
		return this;
	}

	@Override
	public Sentence tokenize(String sentence) {
		if (sentence == null || sentence.isEmpty()) return new Sentence();
		
		List<Word> tmp = new ArrayList<>();
		Sentence result = new Sentence();
		result.setOriginal(sentence);
		
		StringTokenizer st = new StringTokenizer(sentence);

		int min = 0;
		int max = sentence.length();
		for (int i = 0; st.hasMoreElements(); i++) {
			String token = (String) st.nextElement();
			int offset = sentence.substring(min, max).indexOf(token) + min;
			min = token.length() + offset;
			if (!StringTools.isStopWord(token)) {
				Word word = new Word (token, Constants.O, 0.1, i+1, offset);
				word.setStem(porterStemmer.stem(token).toLowerCase());
				tmp.add (word);
			}
		}
		
		Word head = null;
		StringBuilder before = new StringBuilder();
		int pos = 0;
		for (Word word : tmp) {
			if (StringTools.isMyNumber(word.getW())) {
				before.append(" " + word.getW());
				if (head == null) head = word;
			}	else {
				pos++;
				if (before.length() > 0 && head != null) {
					Word w = new Word (before.toString().trim(), Constants.O, 0.1, pos, head.getOffset());
					w.setStem(porterStemmer.stem(before.toString().trim()).toLowerCase());
					result.addWord(w);
					before = new StringBuilder();
					head = null;
					pos++;
				}
				Word w = new Word (word.getW(), Constants.O, 0.1, pos, word.getOffset());
				w.setStem(porterStemmer.stem(word.getW()).toLowerCase());
				result.addWord(w);
			}
		}
		
		if (before.length() > 0 && head != null) {
			Word w = new Word (before.toString().trim(), Constants.O, 0.1, pos, head.getOffset());
			w.setStem(porterStemmer.stem(before.toString().trim()).toLowerCase());
			result.addWord(w);
		}
		
		return result;
	}

}
