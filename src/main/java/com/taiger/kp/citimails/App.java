package com.taiger.kp.citimails;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.taiger.kp.citimails.controller.annotators.ContentAnnotator;
import com.taiger.kp.citimails.controller.annotators.NERAnnotator;
import com.taiger.kp.citimails.controller.annotators.SubjectAnnotator;
import com.taiger.kp.citimails.controller.extractors.SimpleEmlParser;
import com.taiger.kp.citimails.controller.extractors.SimpleMsgParser;
import com.taiger.kp.citimails.controller.extractors.SimpleParser;
import com.taiger.kp.citimails.controller.generators.PDFGenerator;
import com.taiger.kp.citimails.model.Attachment;
import com.taiger.kp.citimails.model.Document;
import com.taiger.kp.citimails.model.Mail;
import com.taiger.kp.citimails.model.Sentence;
import com.taiger.kp.citimails.nlp.ner.CUSIPFinderNER;
import com.taiger.kp.citimails.nlp.ner.CorrectorNER;
import com.taiger.kp.citimails.nlp.ner.DateFinderNER;
import com.taiger.kp.citimails.nlp.ner.ISINFinderNER;
import com.taiger.kp.citimails.nlp.ner.LocationFinderNER;
import com.taiger.kp.citimails.nlp.ner.MoneyFinderNER;
import com.taiger.kp.citimails.nlp.ner.NER;
import com.taiger.kp.citimails.nlp.ner.OrganizationFinderNER;
import com.taiger.kp.citimails.nlp.ner.PercentageFinderNER;
import com.taiger.kp.citimails.nlp.ner.PersonFinderNER;
import com.taiger.kp.citimails.nlp.ner.TimeFinderNER;
import com.taiger.kp.citimails.nlp.oer.CitiWordsFinderOER;
import com.taiger.kp.citimails.nlp.oer.OER;
import com.taiger.kp.citimails.nlp.parser.DeepParser;
import com.taiger.kp.citimails.nlp.parser.SyntaxParser;
import com.taiger.kp.citimails.nlp.splitter.SentenceSplitter;
import com.taiger.kp.citimails.nlp.splitter.SmileSentenceSplitter;
import com.taiger.kp.citimails.nlp.tagger.METagger;
import com.taiger.kp.citimails.nlp.tagger.POSTagger;
import com.taiger.kp.citimails.nlp.tokenizer.SimpleSpaceTokenizer;
import com.taiger.kp.citimails.nlp.tokenizer.Tokenizer;
import com.taiger.kp.citimails.utils.FileTools;

import lombok.extern.log4j.Log4j2;

/**
 * Hello world!
 *
 */
@Log4j2
public class App {

	public static void main(String[] args) {
		
		//NER ner = new LocationFinderNER();
		//SentenceSplitter splitter = new MESplitter();
		SentenceSplitter splitter = new SmileSentenceSplitter();
		//Tokenizer tokenizer = new METokenizer();
		//Tokenizer tokenizer = new SmileSimpleTokenizer();
		Tokenizer tokenizer = new SimpleSpaceTokenizer();
		//POSTagger tagger = new PerceptronTagger();
		POSTagger tagger = new METagger();
		SyntaxParser parser = new DeepParser();
		//OER doer = new DateOER();
		OER oer = new CitiWordsFinderOER();
		
		
		/* exit
		boolean exit = true;
		if (exit) return;
		//*/
		
		String path = "/Users/jorge.rios/eclipse-workspace/citimails/docs/";
		String i_name = "Notification of Margin Call 2.msg";
		String o_name = "Notification of Margin Call 2.json";
		
		Mail mail = new Mail();
		SimpleParser msg = new SimpleMsgParser();
		SimpleParser eml = new SimpleEmlParser();
		
		List<String> list = new ArrayList<>();
		fillList (list);
		
		for (String fullpath : list) {
			
			System.out.println("=> " + fullpath);
			
			path = FileTools.filePath(fullpath);
			String name = FileTools.fileNameNoPath(fullpath);
			String ext = FileTools.fileExtension(fullpath);
			i_name = name + "." + ext;
			o_name = name + ".json";
			FileTools.createDir(path + ext + File.separator);
			
			mail = new Mail();
			if (ext.equals("msg")) {
				try {
					msg.parse(path, i_name, mail);
				} 	catch (IOException e) {
					log.error(e.getMessage());
				} 	catch (Exception e) {
					log.error(e.getMessage());
				}
			}	else if (ext.equals("eml")) {
				try {
					eml.parse(path, i_name, mail);
				} 	catch (IOException e) {
					log.error(e.getMessage());
				} 	catch (Exception e) {
					log.error(e.getMessage());
				}	
			}	else {
				log.info("unknown extension");
				continue;
			}
			
			//log.info(mail.toString());
			mail.save(new File(path + ext + File.separator + o_name));
			
			
			Document doc = new Document();
			doc.setMail(mail);
			doc.setPath(fullpath);
			List<String> text = null;
			
			
			/* tokenizer
			List<Sentence> sentences = new ArrayList<>();
			sentences.add(tokenizer.tokenize(mail.getSubject()));
			doc.setSubject(sentences); //*/
			
			
			//SubjectAnnotator.extract(doc);
			
			/* exit
			boolean exit = true;
			if (exit) return;
			//*/
			
			//log.info("{} - {} ", doc.getDatapoints().keySet(), doc.getDatapoints().values());
			//log.info("doc {}", doc);
			
			//mail.setContent("Jorge Rios and Stephen Hawkings from IBM owe me 120.00 euros at 3:00pm the 30% and also 30.00 dollars at 2:59 every monday on 2000/september/28 or maybe 10/09/18 or maybe 05 September 2018 and can be today or even can try on september 30th.");
			//mail.setContent("Verzonden: woensdag 5 september 2018 11:32 Aan: xxxxxxx.xxxxxxx@xxxx.xxx;");
			//mail.setAttachments(new ArrayList<>());
			
			System.out.println(mail.getContent());
			
			/* splitter
			text = splitter.detect(mail.getContent());
			for (Attachment a : mail.getAttachments()) {
				for (String s : splitter.detect(a.getTextcontent())) {
					text.add(s);
				}
			} //*/
			
			
			/* tokenizer
			sentences = new ArrayList<>();
			for (String s : text) {
				sentences.add(tokenizer.tokenize(s));
			}
			doc.setContent(sentences); //*/
			
			
			//* pos tagger
			sentences = new ArrayList<>();
			for (Sentence s : doc.getContent()) {
				sentences.add(tagger.annotate(s));
			}
			doc.setContent(sentences); //*/
			
			
			NERAnnotator ner = new NERAnnotator();
			ner.annotate(doc);
			
			/* ner org
			sentences = new ArrayList<>();
			ner = new OrganizationFinderNER();
			for (Sentence s : doc.getContent()) {
				sentences.add(ner.annotate(s));
			}
			doc.setContent(sentences); //*/
			
			
			/* ner per
			sentences = new ArrayList<>();
			ner = new PersonFinderNER();
			for (Sentence s : doc.getContent()) {
				sentences.add(ner.annotate(s));
			}
			doc.setContent(sentences); //*/
			
			
			/* ner date
			sentences = new ArrayList<>();
			ner = new DateFinderNER();
			for (Sentence s : doc.getContent()) {
				sentences.add(ner.annotate(s));
			}
			doc.setContent(sentences); //*/
			
			
			/* ner time
			sentences = new ArrayList<>();
			ner = new TimeFinderNER();
			for (Sentence s : doc.getContent()) {
				sentences.add(ner.annotate(s));
			}
			doc.setContent(sentences); //*/
			
			
			/* ner perc
			sentences = new ArrayList<>();
			ner = new PercentageFinderNER();
			for (Sentence s : doc.getContent()) {
				sentences.add(ner.annotate(s));
			}
			doc.setContent(sentences); //*/
			
			
			/* ner money
			sentences = new ArrayList<>();
			ner = new MoneyFinderNER();
			for (Sentence s : doc.getContent()) {
				sentences.add(ner.annotate(s));
			}
			doc.setContent(sentences); //*/
			
			
			/* ner isin
			sentences = new ArrayList<>();
			ner = new ISINFinderNER();
			for (Sentence s : doc.getContent()) {
				sentences.add(ner.annotate(s));
			}
			doc.setContent(sentences); //*/
			
			
			/* ner cusip
			sentences = new ArrayList<>();
			ner = new CUSIPFinderNER();
			for (Sentence s : doc.getContent()) {
				sentences.add(ner.annotate(s));
			}
			doc.setContent(sentences); //*/
			
			
			/* ner corrector
			sentences = new ArrayList<>();
			ner = new CorrectorNER();
			for (Sentence s : doc.getContent()) {
				sentences.add(ner.annotate(s));
			}
			doc.setContent(sentences); //*/
			
			/* oer
			sentences = new ArrayList<>();
			for (Sentence s : doc.getContent()) {
				sentences.add(oer.annotate(s));
			}
			doc.setContent(sentences); //*/
			
			
			/* parser
			sentences = new ArrayList<>();
			parser = new DeepParser();
			for (Sentence s : doc.getText().getSentences()) {
				sentences.add(parser.annotate(s));
			}
			doc.getText().setSentences(sentences); //*/
			
			
			/* parser
			sentences = new ArrayList<>();
			parser = new DependencyParser();
			for (Sentence s : doc.getText().getSentences()) {
				sentences.add(parser.annotate(s));
			}
			doc.getText().setSentences(sentences); //*/
			
			//ContentAnnotator.extract(doc);
			
			PDFGenerator.generate(doc);
			
			//*
			doc.getContent().forEach(s -> {
				log.info(s.getOriginal());
				s.getWords().forEach(log::info);
				System.out.println();
			});
			//*/
			
		}
		
		
		/*
		try {
			msg.parse(path, i_name, mail);
		} 	catch (IOException e) {
			log.severe(e.getMessage());
		} 	catch (Exception e) {
			log.severe(e.getMessage());
		}
		//*/
 
		
		/*
		try {
			eml.parse("/Users/jorge.rios/eclipse-workspace/citimails/docs/", " 14107- BANK 1  Vs. CGML.eml", mail);
		} 	catch (IOException e) {
			log.severe(e.getMessage());
		} 	catch (Exception e) {
			log.severe(e.getMessage());
		}
		//*/
		
		
	}

	private static void fillList(List<String> list) {
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  MC from Dealer 4 to CITIBKLDN.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ [EXTERN] 19687-Bank 6 Vs. CBNA.eml");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ [External] 148705-Bank 3 Vs. CGML.eml");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ 14107- BANK 1  Vs. CGML.eml");
		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ 104009- BANK 10 Vs. CITIBKLDN.eml"); 
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ 104606-Broker 8 Vs. CBNA.eml");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ 104607-Broker 9 Vs. CBNA.eml");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ 118684-Dealer 5 Vs. CGML.eml");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ 132447-Fund 7. Vs. CBNA.eml");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ 134878-Dealer 6 Vs. CGML.eml");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ 135682- Fund 4 Vs. CIP.eml");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ 145023-Bank 2 Vs. CGML.eml");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ 146746- HF1 Vs. CGML.eml");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ 146889-Bank 5 Vs. CGML.eml");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ 148882-Dealer 7 Vs. CGML.eml");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ 152407-Fund 6Vs. CGML .eml");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ 152810- HF 4 Vs. CGML.eml");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ 156463-Dealer 8 Vs. CGML.eml");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ 156463-Insurance 3 Vs. CGML.eml");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ 156464-Dealer 9 Vs. CGML.eml");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ 156464-Insurance 4 Vs. CGML.eml");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ 156466-Dealer 10 Vs. CGML.eml");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ 172638-Bank 4 Vs. CGML.eml");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ 181449- HF 6 Vs. CGML.eml");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/2018.eml");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/Client JHK - Margin Call Notice Citigroup xlsx.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/file.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/Margin Call Notice from (Client GEF - CITIGROUP GLOBAL MARKETS EUROPE ) COB date  14-Sep-2018.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/Margin Notice - Variation Demand Client DEF Citigroup Global Markets Limited COB 2018-Sep-14.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/Notification of Margin Call 2.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/OTC Margin Call Notice from Client ABS to CGGM Ltd .msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE   203612-Fund 3 Vs  CBNA.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  13966-Bank 7 Vs  CGML.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  102444 -  Type 4  Pension Fund 2 SA vs  CBNA   Margin Notice  Portfolio Statement.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  114229 -  Type 1  Fund 1 vs  CEP   Portfolio Statement  Margin Notice.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  118768-HF 3 Vs  CGML.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  120288 -  Type 2  Pension Fund 4 vs  CBNA   Portfolio Statement  Margin Notice.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  135366- Broker 1 Vs  CGML.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  135366- Broker 2 Vs  CGML.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  143796 -  Type 2  Insurance 1 vs  CGML   Portfolio Statement  Margin Notice.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  143802 -  Type 1  Insurance 2 vs  CGML   Margin Notice  Portfolio Statement.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  148974 -  Type 2  Broker 3 vs  CGML   Margin Notice  Portfolio Statement.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  148975 -  Type 3  Broker 4 vs  CGML   Margin Notice  Portfolio Statement.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  150435 -  Type 2  Insurance 7 vs  CGML   Portfolio Statement  Margin Notice.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  150436 -  Type 2  Insurance 8 vs  CGML   Portfolio Statement  Margin Notice.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  150772 -   TYPE 2   Fund 8 vs  CGML   Margin Notice  Portfolio Statement.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  152204-Pension Fund 6 Vs  CGML.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  152205-Pension Fund 7 Vs  CGML.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  162166-Dealer 1 Vs  CBNA  External .msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  162179-Dealer 2 Vs  CBNA  External .msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  171674 -  Type 3  Fund 6 vs  CGML   Portfolio Statement  Margin Notice.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  172580 -  Type 4  (Secure) Broker 6 vs  CBNA   Portfolio Statement  Margin Notice.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  172970 -  Type 4  Broker 5 vs  CBNA   Portfolio Statement  Margin Notice.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  178470-HF 9 Vs  CGML.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  179299 -  Type 3  Insurance 5 vs  CBNA   Portfolio Statement  Margin Notice.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  188735-HF 8 Vs  CGML.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  MC from Dealer 3 to CITIBKLDN.msg");
	}

}
