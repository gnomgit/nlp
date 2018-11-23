package com.taiger.kp.citimails;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taiger.kp.citimails.controller.annotators.MailAnnotator;
import com.taiger.kp.citimails.controller.extractors.MailExtractor;
import com.taiger.kp.citimails.controller.extractors.SimpleEmlParser;
import com.taiger.kp.citimails.controller.extractors.SimpleMsgParser;
import com.taiger.kp.citimails.controller.extractors.SimpleParser;
import com.taiger.kp.citimails.controller.generators.PDFGenerator;
import com.taiger.kp.citimails.model.Document;
import com.taiger.kp.citimails.model.Mail;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class App {

	public static void main(String[] args) {
		
		/* exit
		boolean exit = true;
		if (exit) return;
		//*/
		
		Mail mail = new Mail();
		SimpleParser msg = new SimpleMsgParser();
		SimpleParser eml = new SimpleEmlParser();
		
		List<String> list = new ArrayList<>();
		fillList (list);
		MailExtractor extractor = new MailExtractor(); 
		MailAnnotator annotator = new MailAnnotator();
		PDFGenerator pdf = new PDFGenerator();
		
		for (String fullpath : list) {
			
			System.out.println("=> " + fullpath);
			
			Document document = extractor.extract(fullpath);
			
			if (document != null) {
				
				annotator.annotate(document);
				/*
				document.getContent().forEach(s -> {
					log.info(s.getOriginal());
					s.getWords().forEach(log::info);
					System.out.println();
				});
				//*/
				//*
				ObjectMapper objectMapper = new ObjectMapper();
				try {
					objectMapper.writeValue(new File("target/doc.json"), document);
				} 	catch (IOException e) {
					e.printStackTrace();
				}//*/
				
				pdf.generate(document);
				
			}	else {
				log.error("null document");
			}
			
		}
	}

	private static void fillList(List<String> list) {
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/RE  MC from Dealer 4 to CITIBKLDN.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ [EXTERN] 19687-Bank 6 Vs. CBNA.eml");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ [External] 148705-Bank 3 Vs. CGML.eml");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ 14107- BANK 1  Vs. CGML.eml");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ 104009- BANK 10 Vs. CITIBKLDN.eml"); 
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
		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/ 156466-Dealer 10 Vs. CGML.eml");
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
		
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/corpus2/RE  987654- PLC B vs  CGML.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/corpus2/555999 - Company A  Vs  CGML.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/corpus2/BANK E vs  CGML   Portfolio Statement.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/corpus2/CITIGROUP MARGIN CALL - Client D.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/corpus2/CITIGROUP MARGIN CALL - Client E.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/corpus2/Fund B vs  CGML   Margin Notice.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/corpus2/Fund B-CBNA  Margin Call.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/corpus2/Margin Call Fund T vs Citi 12-11-2018.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/corpus2/Margin Call Notice Cash Collateral Client A.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/corpus2/Margin Call Response from PLC A to CITIBANK N A .msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/corpus2/MC from PLC K to CITIBKLDN.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/corpus2/PLC D margin notice to Citibank NA London.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/corpus2/RE  123456- Client B Vs  CBNA.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/corpus2/RE  147258 -  Type B  Bank C vs  CGML   Portfolio Statement  Margin Notice.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/corpus2/RE  222222-Bank D Vs  CGML.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/corpus2/RE  222333-Company A Vs  CGML.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/corpus2/RE  321654-Fund A Vs  CGML.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/corpus2/RE  444555 -  Type B  Company D vs  CGML   Margin Notice  Portfolio Statement.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/corpus2/RE  741852-Client C Vs  CGML.msg");
//		list.add ("/Users/jorge.rios/eclipse-workspace/citimails/docs/corpus2/RE  789456 -  Type A  Bank A vs  CBNA   Portfolio Statement  Margin Notice.msg");

	}

}
