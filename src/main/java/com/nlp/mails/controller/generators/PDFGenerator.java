package com.nlp.mails.controller.generators;

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

import com.nlp.mails.model.Constants;
import com.nlp.mails.model.Document;
import com.nlp.mails.model.Word;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class PDFGenerator {
	
	private PDFont font;
	private int index;
	
	public PDFGenerator () {
		font = PDType1Font.COURIER;
		index = 0;
	}

	public void generate (Document mail) {
		Assert.notNull(mail, "null doc");
	 
		String filename = mail.getPath() + "_.pdf";
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
	        			w.updateLocation(pos, line, npage+1, (int)page.getMediaBox().getWidth(), (int)page.getMediaBox().getHeight());
	        			
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
	
	public void generate2 (Document mail) {
		Assert.notNull(mail, "null doc");
	 
		String filename = mail.getPath() + ".pdf";
		List<Word> msgLine = null;
	    index = 0;
	    int npage = 0;
	    int line = Constants.YTOP;
	    List<Word> words = bagOfWords(mail);
	    String current = "";
	    
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
	        			
	        			w.updateLocation(pos, line, npage+1, (int)page.getMediaBox().getWidth(), (int)page.getMediaBox().getHeight());
	        			
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
	    	log.error("{} caused:\n{}", current, e.getMessage());
		}
	}

	private List<Word> getLine (List<Word> words) {
		List<Word> result = new ArrayList<>();
		
		int cumul = 0;
		for (; index < words.size() && cumul + words.get(index).getW().length() + 1 < Constants.NCHARS; index++) {
			result.add(words.get(index));
			cumul += (words.get(index).getW().length() + 1);
		}
		
		return result;
	}
	
	private List<Word> bagOfWords (Document mail) {
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
	
	private Color getColor (Word word) {
		
		if (!word.getDatapoints().isEmpty()) {
			for (String dp : word.getDatapoints()) {
				if (dp.startsWith(Constants.AT)) return Color.RED;
			}
			return Color.BLUE;
		}
		
		return Color.BLACK;
	}
	
	private boolean bann (String word) {
		//log.info("{} - ({})", word, word.length());
		for (int i = 0; i < word.length(); i++) {
		    if (Character.UnicodeBlock.of(word.charAt(i)).equals(Character.UnicodeBlock.CYRILLIC)) {
		        return true;
		    }
		    if (Character.UnicodeBlock.of(word.charAt(i)).equals(Character.UnicodeBlock.BOX_DRAWING)) {
		        return true;
		    }
		    if (Character.UnicodeBlock.of(word.charAt(i)).equals(Character.UnicodeBlock.GENERAL_PUNCTUATION)
		    		&& (int) word.charAt(i) == 8203) {
		    	return true;
		    }
		}
		return false;
	}
	
}
