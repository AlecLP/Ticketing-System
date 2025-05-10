package com.synit.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.synit.domain.Action;
import com.synit.domain.Priority;
import com.synit.domain.Ticket;
import com.synit.domain.TicketCreateDto;
import com.synit.domain.TicketHistory;
import com.synit.domain.Status;
import com.synit.service.TicketHistoryService;
import com.synit.service.TicketService;

@RestController
public class TicketController {
	
	@Autowired
	TicketService ticketService;
	@Autowired
	TicketHistoryService ticketHistoryService;
	@Value("${ticket.upload.dir}")
    private String uploadDir;
	
	@PostMapping("/createTicket")
	public ResponseEntity<String> createTicket(
			@RequestPart("ticket") TicketCreateDto ticketDto,
			@RequestPart(value="file", required=false) MultipartFile file){
		String fileAttachmentPath = "";
		
	    if (file != null && !file.isEmpty()) {
	        File uploadPath = new File(uploadDir);
	        if (!uploadPath.exists()) {
	            uploadPath.mkdirs();
	        }

	        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename()).replaceAll("\"", "");
	        Path filePath = Paths.get(uploadDir, originalFilename);
	        try {
	            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        fileAttachmentPath = filePath.toAbsolutePath().toString();
	    }

	    Priority p = Priority.valueOf(ticketDto.getPriority().toUpperCase());
	    Ticket ticket = new Ticket(ticketDto.getTitle(), ticketDto.getDescription(),
	    		ticketDto.getCreatedBy(), ticketDto.getAssignee(), p, Status.OPEN, new Date(),
	    		ticketDto.getCategory(), fileAttachmentPath);
	    TicketHistory ticketHistory = new TicketHistory(ticket, Action.CREATED, ticketDto.getCreatedBy(), new Date(), "Initial creation.");
	    ticket.addHistory(ticketHistory);

	    ticketService.saveTicket(ticket);

	    return ResponseEntity.ok("Ticket created");
		
		
	}

}
