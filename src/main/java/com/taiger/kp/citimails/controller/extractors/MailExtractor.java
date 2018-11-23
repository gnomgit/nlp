package com.taiger.kp.citimails.controller.extractors;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import com.taiger.kp.citimails.controller.generators.PDFGenerator;
import com.taiger.kp.citimails.model.Attachment;
import com.taiger.kp.citimails.model.Document;
import com.taiger.kp.citimails.model.Mail;
import com.taiger.kp.citimails.model.Sentence;
import com.taiger.kp.citimails.nlp.spellchecker.LuceneSpellChecker;
import com.taiger.kp.citimails.nlp.spellchecker.SpellChecker;
import com.taiger.kp.citimails.nlp.splitter.SentenceSplitter;
import com.taiger.kp.citimails.nlp.splitter.SmileSentenceSplitter;
import com.taiger.kp.citimails.nlp.tokenizer.METokenizer;
import com.taiger.kp.citimails.nlp.tokenizer.SimpleSpaceTokenizer;
import com.taiger.kp.citimails.nlp.tokenizer.SmileSimpleTokenizer;
import com.taiger.kp.citimails.nlp.tokenizer.Tokenizer;
import com.taiger.kp.citimails.utils.FileTools;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class MailExtractor {
		
	SentenceSplitter splitter;
	Tokenizer tokenizer;
	Tokenizer subjectTokenizer;
	SimpleParser msg;
	SimpleParser eml;
	Mail mail;
	PDFGenerator pdf;
	//SpellChecker spell;
	
	public MailExtractor () {
		
		splitter = new SmileSentenceSplitter();
		// tokenizer = new METokenizer();
		// tokenizer = new SmileSimpleTokenizer();
		tokenizer = new SimpleSpaceTokenizer();
		subjectTokenizer = new METokenizer();
		msg = new SimpleMsgParser();
		eml = new SimpleEmlParser();
		pdf = new PDFGenerator();
		//spell = new LuceneSpellChecker();
	}
	
	public Document extract (String fullpath) {
		Assert.hasText(fullpath, "null or empty");
		
		String path = FileTools.filePath(fullpath);
		String name = FileTools.fileNameNoPath(fullpath);
		String ext = FileTools.fileExtension(fullpath);
		String iname = name + "." + ext;
		String oname = name + ".json";
		FileTools.createDir(path + ext + File.separator);
		
		mail = new Mail();
		if (ext.equals("msg")) {
			try {
				msg.parse(path, iname, mail);
			} 	catch (IOException e) {
				log.error(e.getMessage());
			} 	catch (Exception e) {
				log.error(e.getMessage());
			}
		}	else if (ext.equals("eml")) {
			try {
				eml.parse(path, iname, mail);
			} 	catch (IOException e) {
				log.error(e.getMessage());
			} 	catch (Exception e) {
				log.error(e.getMessage());
			}	
		}	else {
			log.info("unknown extension");
			return null;
		}
		
		mail.save(new File(path + ext + File.separator + oname));
		
		Document doc = new Document();
		doc.setMail(mail);
		doc.setPath(fullpath);
		
		//mail.setSubject("RE: 104607-Broker 9 Vs. CBNA".replace("-", " - "));
		
		//* tokenizer
		mail.setSubject(mail.getSubject().replace("-", " - "));
		Sentence subject = new Sentence();
		subject = subjectTokenizer.tokenize(mail.getSubject());
		doc.setSubject(subject); //*/
		
		//mail.setContent("Please advise on attached margin call 04-Sep-18 Regards ");
		//mail.setContent("Make payment VD 12-02-2018 .");
		
		//* splitter
		List<String> text = null;
		text = splitter.detect(mail.getContent());
		for (Attachment a : mail.getAttachments()) {
			for (String s : splitter.detect(a.getTextcontent())) {
				text.add(s);
			}
		} //*/
		
		//* tokenizer
		List<Sentence> sentences = new ArrayList<>();
		for (String s : text) {
			sentences.add(tokenizer.tokenize(s));
		}
		doc.setContent(sentences); //*/
		
		//sentences.forEach(spell::checkSentence);
		
		pdf.generate2(doc);
		
		return doc;
	}
	
}
