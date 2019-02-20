package com.nlp.mails.nlp.splitter;

import java.util.List;

public interface SentenceSplitter {
	
	List<String> detect (String text);
	
}
