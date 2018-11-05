package com.taiger.kp.citimails.nlp.ner;

import org.springframework.util.Assert;

import com.taiger.kp.citimails.model.Constants;
import com.taiger.kp.citimails.model.Sentence;
import com.taiger.kp.citimails.model.Word;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;



@Log4j2
@Getter
@Setter
public class CUSIPFinderNER implements NER {
	
	public CUSIPFinderNER () {
		initialize ();
	}

	public CUSIPFinderNER initialize () {
		return this;
	}

	@Override
	public Sentence annotate(Sentence sentence) {
		Assert.notNull(sentence, "sentence shouldn't be null");
		Assert.notNull(sentence.getS(), "sentence content shouldn't be null");
		
		//* 
		for (Word w : sentence.getS()) {
			if (isCusip(w.getW())) {
				w.setNerTag(Constants.B + Constants.CUSIP);
				w.setNerProb(1.0);
			}
		} //*/
		
		return sentence;
	}
	
	private static Boolean isCusip(String s) {
        if (s.length() != 9) return false;
        int sum = 0;
        for (int i = 0; i < 7; i++) {
            char c = s.charAt(i);
 
            int v;
            if (c >= '0' && c <= '9') {
                v = c - 48;
            } else if (c >= 'A' && c <= 'Z') {
                v = c - 64;  // lower case letters apparently invalid
            } else if (c == '*') {
                v = 36;
            } else if (c == '@') {
                v = 37;
            } else if (c == '#') {
                v = 38;
            } else {
                return false;
            }
            if (i % 2 == 1) v *= 2;  // check if odd as using 0-based indexing
            sum += v / 10 + v % 10;
        }
        return s.charAt(8) - 48 == (10 - (sum % 10)) % 10;
    }

}
