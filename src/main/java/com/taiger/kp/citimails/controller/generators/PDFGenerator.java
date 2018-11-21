package com.taiger.kp.citimails.controller.generators;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.util.Assert;

import com.taiger.kp.citimails.model.Constants;
import com.taiger.kp.citimails.model.Document;
import com.taiger.kp.citimails.model.Word;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class PDFGenerator {
	
	private static PDFont font = PDType1Font.COURIER;
	private static int index;
	
	private PDFGenerator () {}

	public static void generate (Document mail) {
		Assert.notNull(mail, "null doc");
	 
		String filename = mail.getPath() + ".pdf";
		List<Word> msgLine = null;
	    index = 0;
	    int npage = 0;
	    int line = Constants.YTOP;
	    List<Word> words = bagOfWords(mail);
	    
	    try (PDDocument doc = new PDDocument()){
	        PDPage page = new PDPage();
	        doc.addPage(page);
	        PDPageContentStream contents = new PDPageContentStream(doc, page);
	        
	        msgLine = getLine (words);
	        while (!msgLine.isEmpty()) {
	        	if (line <= Constants.Y0) {
	        		line = Constants.YTOP;
	        		contents.close();
	        		page = new PDPage();
	    	        doc.addPage(page);
	        		contents = new PDPageContentStream(doc, page);
	        		npage++;
	        	}
	        	
	        	int pos = Constants.X0;
	        	for (Word w : msgLine) {
	        		
	        		if (w.getW().equals("\n")) {
	        			line -= Constants.NVINC;
	        			if (line <= Constants.Y0) {
	    	        		line = Constants.YTOP;
	    	        		contents.close();
	    	        		page = new PDPage();
	    	    	        doc.addPage(page);
	    	        		contents = new PDPageContentStream(doc, page);
	    	        		npage++;
	    	        	}
	        			pos = Constants.X0;
	        			
	        		}	else {
	        			Color color = getColor(w);
	        			w.updateLocation(pos, line, npage);
	        			
	        			if (color != Color.BLACK) {
	        				contents.setNonStrokingColor(Color.LIGHT_GRAY);
		        			contents.addRect(w.getLocation().getX(), w.getLocation().getY(), w.getLocation().getWidth(), w.getLocation().getHeight());
		        			contents.fill();
	        			}
	        			
		        		contents.beginText();
		        		contents.setFont(font, Constants.FONTSIZE);
		        		
		                contents.setNonStrokingColor(color);
		                
		                contents.newLineAtOffset(pos, line);
		                pos += ((w.getW().length() + 1) * Constants.NHINC);
		                
		               
		                if (!bann(w.getW())) {
		                	contents.showText(w.getW());
		                }
		                contents.endText();
	        		}
	        	}
	        	
	        	line -= Constants.NVINC;
	        	msgLine = getLine (words);
	        }
	        
	        contents.close();
            doc.save(filename);
	
	    } 	catch (IOException e) {
	    	log.error(e.getMessage());
		}
	}
	
	public static void generate2 (Document mail) {
		Assert.notNull(mail, "null doc");
	 
		String filename = mail.getPath() + ".pdf";
		List<Word> msgLine = null;
	    index = 0;
	    int npage = 0;
	    int line = Constants.YTOP;
	    List<Word> words = bagOfWords(mail);
	    
	    try (PDDocument doc = new PDDocument()){
	        PDPage page = new PDPage();
	        doc.addPage(page);
	        PDPageContentStream contents = new PDPageContentStream(doc, page);
	        
	        msgLine = getLine (words);
	        while (!msgLine.isEmpty()) {
	        	if (line <= Constants.Y0) {
	        		line = Constants.YTOP;
	        		contents.close();
	        		page = new PDPage();
	    	        doc.addPage(page);
	        		contents = new PDPageContentStream(doc, page);
	        		npage++;
	        	}
	        	
	        	int pos = Constants.X0;
	        	for (Word w : msgLine) {
	        		
	        		if (w.getW().equals("\n")) {
	        			line -= Constants.NVINC;
	        			if (line <= Constants.Y0) {
	    	        		line = Constants.YTOP;
	    	        		contents.close();
	    	        		page = new PDPage();
	    	    	        doc.addPage(page);
	    	        		contents = new PDPageContentStream(doc, page);
	    	        		npage++;
	    	        	}
	        			pos = Constants.X0;
	        			
	        		}	else {
	        			w.updateLocation(pos, line, npage);
	        			
		        		contents.beginText();
		        		contents.setFont(font, Constants.FONTSIZE);
		                
		                contents.newLineAtOffset(pos, line);
		                pos += ((w.getW().length() + 1) * Constants.NHINC);
		                
		               
		                if (!bann(w.getW())) {
		                	contents.showText(w.getW());
		                }
		                contents.endText();
	        		}
	        	}
	        	
	        	line -= Constants.NVINC;
	        	msgLine = getLine (words);
	        }
	        
	        contents.close();
            doc.save(filename);
	
	    } 	catch (IOException e) {
	    	log.error(e.getMessage());
		}
	}

	private static List<Word> getLine (List<Word> words) {
		List<Word> result = new ArrayList<>();
		
		int cumul = 0;
		for (; index < words.size() && cumul + words.get(index).getW().length() + 1 < Constants.NCHARS; index++) {
			result.add(words.get(index));
			cumul += (words.get(index).getW().length() + 1);
		}
		
		return result;
	}
	
	private static List<Word> bagOfWords (Document mail) {
		Assert.notNull(mail.getSubject(), "null subject");
		Assert.notNull(mail.getContent(), "null content");
		List<Word> result = new ArrayList<>();
		
		mail.getSubject().getWords().forEach(result::add);
		result.add(new Word("\n", "", 0.0, 0, 0));
		result.add(new Word("\n", "", 0.0, 0, 0));
		
		mail.getContent().forEach(s -> {
			s.getWords().forEach(result::add);
			result.add(new Word("\n", "", 0.0, 0, 0));
			result.add(new Word("\n", "", 0.0, 0, 0));
		});
		
		return result;
	}
	
	private static Color getColor (Word word) {
		
		if (!word.getDatapoints().isEmpty()) {
			for (String dp : word.getDatapoints()) {
				if (dp.startsWith(Constants.AT)) return Color.RED;
			}
			return Color.BLUE;
		}
		
		return Color.BLACK;
	}
	
	private static boolean bann (String word) {
		for(int i = 0; i < word.length(); i++) {
		    if(Character.UnicodeBlock.of(word.charAt(i)).equals(Character.UnicodeBlock.CYRILLIC)) {
		        return true;
		    }
		}
		return false;
	}
	
}
