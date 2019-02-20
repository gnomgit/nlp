package com.nlp.mails.controller.extractors;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.util.Assert;

import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.MsgParser;
import com.auxilii.msgparser.attachment.Attachment;
import com.auxilii.msgparser.attachment.FileAttachment;
import com.nlp.mails.App;
import com.nlp.mails.model.Mail;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoArgsConstructor
public class SimpleMsgParser implements SimpleParser {

	public void parse(String path, String name, Mail mail) throws IOException {
		Assert.hasText(path, "path shouldn't be empty");
		Assert.hasText(name, "name shouldn't be empty");
		Assert.isTrue((new File(path + name)).exists(), "File does not exist");
		Assert.notNull(mail, "mail shouldn't be null");
		
		try {
			MsgParser msgp = new MsgParser();
			Message msg = msgp.parseMsg(path + name);
			String fromEmail = msg.getFromEmail();
			String fromName = msg.getFromName();
			String subject = msg.getSubject();
			String body = msg.getBodyText();
			List<Attachment> list = msg.getAttachments();
			//System.out.println("Attachments (" + list.size() + ")");
			
			mail.setAttachments(new LinkedList<>());
			
			list.forEach(a -> {
				if (FileAttachment.class.isAssignableFrom(a.getClass())) {
					//System.out.println(((FileAttachment) a).getLongFilename());
					try (FileOutputStream fos = new FileOutputStream(new File (path + ((FileAttachment) a).getLongFilename()))) {
						fos.write(((FileAttachment) a).getData(), 0, (int)((FileAttachment) a).getSize());
						fos.flush();
					}	catch (Exception e) {
						log.error(e.getMessage());
					}
					try {
						(new PdfTextParser()).parse(path, ((FileAttachment) a).getLongFilename(), mail);
					} 	catch (IOException e) {
						log.error(e.getMessage());
					}
				}	
			});
			
//			System.out.println("-----");
//			System.out.println("from_email: " + from_email);
//			System.out.println("from_name: " + from_name);
//			System.out.println("to_list: " + to_list);
//			System.out.println("cc_list: " + cc_list);
//			System.out.println("bcc_list: " + bcc_list);
//			System.out.println("subject: " + subject);
//			System.out.println("body: " + body);
			
			mail.setContent(body);
			mail.setSubject(subject);
			mail.setFrom(fromEmail + " " + fromName);
			mail.setTo(msg.getToRecipient().toString());
			
		} 	catch (Exception e) {
			log.error(e.getMessage());
		}

	}

	

}
