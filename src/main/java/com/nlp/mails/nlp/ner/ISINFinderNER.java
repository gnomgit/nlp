package com.nlp.mails.nlp.ner;

import org.springframework.util.Assert;

import com.nlp.mails.model.Constants;
import com.nlp.mails.model.Sentence;
import com.nlp.mails.model.Word;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;



@Log4j2
@Getter
@Setter
public class ISINFinderNER implements NER {
	
	public ISINFinderNER () {
		initialize ();
	}

	public ISINFinderNER initialize () {
		return this;
	}

	@Override
	public Sentence annotate(Sentence sentence) {
		Assert.notNull(sentence, "sentence shouldn't be null");
		Assert.notNull(sentence.getWords(), "sentence content shouldn't be null");
		
		//* 
		for (Word w : sentence.getWords()) {
			if (ISINtest(w.getW())) {
				w.setNerTag(Constants.B + Constants.ISIN);
				w.setNerProb(1.0);
			}
		} //*/
		
		return sentence;
	}
	
	private boolean ISINtest(String isin) {
        isin = isin.trim().toUpperCase();
 
        if (!isin.matches(Constants.isin_regexp))
            return false;
 
        StringBuilder sb = new StringBuilder();
        for (char c : isin.substring(0, 12).toCharArray())
            sb.append(Character.digit(c, 36));
 
        return luhnTest(sb.toString());
    }
 
    private boolean luhnTest(String number) {
        int s1 = 0, s2 = 0;
        String reverse = new StringBuffer(number).reverse().toString();
        for (int i = 0; i < reverse.length(); i++){
            int digit = Character.digit(reverse.charAt(i), 10);
            //This is for odd digits, they are 1-indexed in the algorithm.
            if (i % 2 == 0){
                s1 += digit;
            } else { // Add 2 * digit for 0-4, add 2 * digit - 9 for 5-9.
                s2 += 2 * digit;
                if(digit >= 5){
                    s2 -= 9;
                }
            }
        }
        return (s1 + s2) % 10 == 0;
    }

}
