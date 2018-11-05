package com.taiger.kp.citimails.controller.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.mail.util.MimeMessageParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.util.Assert;

import com.taiger.kp.citimails.model.Mail;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class SimpleEmlParser implements SimpleParser {
	
	public void parse (String path, String name, Mail mail) throws Exception {
		Assert.hasText(path, "path shouldn't be empty");
		Assert.hasText(name, "name shouldn't be empty");
		File file = new File(path + name);
		Assert.isTrue(file.exists(), "File does not exist");
		Assert.notNull(mail, "mail shouldn't be null");
		
        Properties props = System.getProperties();
        props.put("mail.host", "smtp.dummydomain.com");
        props.put("mail.transport.protocol", "smtp");

        InputStream is = new FileInputStream(file);
        MimeMessage mime = new MimeMessage(null, is);
        
        MimeMessageParser parser = new MimeMessageParser(mime);
        //String from = parser.getFrom();
        for (Address from : mime.getFrom()) {
        	mail.setFrom(mail.getFrom() + from.toString() + ";");
        }
        for (Address to : mime.getAllRecipients()) {
        	mail.setFrom(mail.getFrom() + to.toString() + ";");
        }
        mail.setSubject(parser.getSubject());
        String htmlContent = parser.parse().getHtmlContent();
        mail.setContent(parseHtml(htmlContent));
        
        //List<Address> to = parser.getTo();
        List<Address> cc = parser.getCc();
        List<Address> bcc = parser.getBcc();
        
        /*MimeMessage msg = mime;//(MimeMessage)iter.next();
        if(msg.getContentType().equalsIgnoreCase("text/plain")){
             String content = (String)msg.getContent();
             System.out.println("content: " + content);
             //And am writing some logic to put it as CLOB field in DB.
        }else{
             MimeMultipart multipart = (MimeMultipart) msg.getContent();
             for (int i = 0; i < multipart.getCount(); i++) {
                      BodyPart bodyPart = multipart.getBodyPart(i);
                      System.out.println("bodypart: " + bodyPart);
                      InputStream stream = bodyPart.getInputStream();     
                      System.out.println("stream: " + parseHtml(convert(stream, Charset.forName("UTF-8"))));
                      //And am writing some logic to put the stream as BLOB in DB
             }
        }*/
        
        try {
        	MimeMultipart multipart = (MimeMultipart) mime.getContent();
        	for (int i = 0; i < multipart.getCount(); i++) {
        		BodyPart bodyPart = multipart.getBodyPart(i);
        		if (bodyPart.getFileName() != null)
        			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------");
        	}
        } catch (Exception ex) {
        }
    }
	
	public String parseHtml (String html) {

        Document doc = Jsoup.parse(html);
        String title = doc.title();
        String body = doc.body().text();

//        System.out.println("Title: " + title);
//        System.out.println("Body: " + body);
        
        return title + "\n" + body;
    }
	
	/*
	public String convert (InputStream inputStream, Charset charset) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset))) {	
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}
		}
	 
		return stringBuilder.toString();
	}*/
	
}
