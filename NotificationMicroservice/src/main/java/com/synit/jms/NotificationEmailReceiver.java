package com.synit.jms;

import java.io.File;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.synit.common_classes.TicketMessage;
import com.synit.common_classes.TicketPdfMessage;
import com.synit.service.PdfService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class NotificationEmailReceiver {
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Autowired
	PdfService pdfService;
	
	@Value("${pdf.save.dir}")
    private String savePdfDir;
	
	@JmsListener(destination="notification.queue", concurrency="5-10")
	public void receiveMessage(TicketMessage message) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setFrom("alecphegley@gmail.com");
		email.setTo(message.getTo());
		email.setSubject("Ticket Status Update");
		
		String body = "Your Ticket: " +message.getTicketTitle() +" has had its status updated. Information below:\n"
				+"Action: " +message.getAction() +"\nComments: " +message.getComments();
		email.setText(body);

        mailSender.send(email);
	}
	
	@JmsListener(destination="pdf.queue", concurrency="5-10")
	public void receivePdfMessage(TicketPdfMessage message) {
		String filePath = Paths.get(savePdfDir, message.getId() + ".pdf").toString();
		pdfService.generateAndSavePdf(message, filePath);
		try {
			sendEmailWithAttachment(message.getCreatedBy(), "Ticket PDF", "Please find the attached PDF containing information on your ticket.", filePath);
		} catch (MessagingException e) {
			System.out.println("Error sending PDF: " +e);
		}	
	}
	
	public void sendEmailWithAttachment(String to, String subject, String text, String filePath) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

	    helper.setTo(to);
	    helper.setSubject(subject);
	    helper.setText(text);

	    File pdfFile = new File(filePath);
	    helper.addAttachment(pdfFile.getName(), pdfFile);

	    mailSender.send(message);
	}

}
