package com.taiger.kp.citimails.nlp.ner;

import org.springframework.util.Assert;

import com.taiger.kp.citimails.model.Constants;
import com.taiger.kp.citimails.model.Sentence;
import com.taiger.kp.citimails.model.Word;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;



@Log4j2
@Getter
@Setter
@NoArgsConstructor
public class CorrectorNER implements NER {

	@Override
	public Sentence annotate(Sentence sentence) {
		Assert.notNull(sentence, "sentence shouldn't be null");
		Assert.notNull(sentence.getWords(), "sentence content shouldn't be null");
		
		String prevTag = Constants.O;
		for (Word w : sentence.getWords()) {
			if ((prevTag.startsWith(Constants.O) && w.getNerTag().startsWith(Constants.I)) 
					|| (prevTag.startsWith(Constants.I) && w.getNerTag().startsWith(Constants.I) && !prevTag.equals(w.getNerTag()))) {
				w.setNerTag(w.getNerTag().replace(Constants.I, Constants.B));
			}
		}
		
		return sentence;
	}
	

}
