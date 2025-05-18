package com.synit.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Comparator;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.synit.common_classes.TicketHistoryPdfMessage;
import com.synit.common_classes.TicketPdfMessage;

@Service
public class PdfService {
	
	public String generateAndSavePdf(TicketPdfMessage message, String filePath) {	
		Document document = new Document();
		try{
			File file = new File(filePath);
	        File parentDir = file.getParentFile();
	        if (parentDir != null && !parentDir.exists()) {
	            parentDir.mkdirs();
	        }
			
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			PdfWriter.getInstance(document, fileOutputStream);
			document.open();
			
			document.add(new Paragraph("Ticket Title: " +message.getTitle()));
			document.add(new Paragraph("Ticket Category: " +message.getCategory()));
			document.add(new Paragraph("Ticket Created By: " +message.getCreatedBy()));
			document.add(new Paragraph("Ticket Assignee: " +message.getAssignee()));
			document.add(new Paragraph("Ticket Description: " +message.getDescription()));
			document.add(new Paragraph("Ticket Priority: " +message.getPriority()));
			document.add(new Paragraph("Ticket Status: " +message.getStatus()));
			document.add(new Paragraph("Ticket Date: " +message.getDate()));
			
			PdfPTable table = new PdfPTable(4);
	        table.addCell("Action");
	        table.addCell("Action By");
	        table.addCell("Action Date");
	        table.addCell("Comments");
	        Collections.sort(message.getHistory(), Comparator.comparing(TicketHistoryPdfMessage::getActionDate));
	        for (TicketHistoryPdfMessage history : message.getHistory()) {
	            table.addCell(history.getAction());
	            table.addCell(history.getActionBy());
	            table.addCell(history.getActionDate().toString());
	            table.addCell(history.getComments());     
	        }
	        document.add(table);
	        
	        document.close();
		}
		catch(Exception e) {
			System.out.println("Error: " +e);
		}
		return filePath;
	}

}
