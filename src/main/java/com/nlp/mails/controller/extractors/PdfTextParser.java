package com.nlp.mails.controller.extractors;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.util.Assert;

import com.nlp.mails.App;
import com.nlp.mails.model.Attachment;
import com.nlp.mails.model.Mail;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class PdfTextParser implements SimpleParser {

	public void parse (String path, String name, Mail mail) throws IOException {
		Assert.hasText(path, "path shouldn't be empty");
		Assert.hasText(name, "name shouldn't be empty");
		File file = new File(path + name);
		Assert.isTrue(file.exists(), "File does not exist");
		Assert.notNull(mail, "mail shouldn't be null");

		try (PDDocument document = PDDocument.load(file)) {
			document.getClass();

			if (!document.isEncrypted()) {
				PDFTextStripperByArea stripper = new PDFTextStripperByArea();
				stripper.setSortByPosition(true);

				PDFTextStripper tStripper = new PDFTextStripper();

				String pdfFileInText = tStripper.getText(document);
				
				String lines[] = pdfFileInText.split("\\r?\\n");
				StringBuilder sb = new StringBuilder();
				for (String line : lines) {
					//System.out.println("--------->>>>>> " + line);
					sb.append(line + "\n");
				}
				
				mail.getAttachments().add(new Attachment(path + name, sb.toString()));
			}
		}
	}

}
