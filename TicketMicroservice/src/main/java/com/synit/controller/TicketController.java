package com.synit.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synit.common_dtos.TicketCreateDto;
import com.synit.common_dtos.TicketDecisionsDto;
import com.synit.common_dtos.TicketDto;
import com.synit.common_dtos.TicketHistoryDto;
import com.synit.common_enums.Action;
import com.synit.common_enums.Status;
import com.synit.domain.Employee;
import com.synit.domain.Ticket;
import com.synit.domain.TicketHistory;
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
	private final ObjectMapper objectMapper = new ObjectMapper();
	
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
	    
	    Employee createdBy = objectMapper.convertValue(ticketDto.getCreatedBy(), Employee.class);
	    Employee assignee = objectMapper.convertValue(ticketDto.getAssignee(), Employee.class);
	    Ticket ticket = new Ticket(ticketDto.getTitle(), ticketDto.getDescription(),
	    		createdBy, assignee, ticketDto.getPriority(), Status.OPEN, new Date(),
	    		ticketDto.getCategory(), fileAttachmentPath);
	    TicketHistory ticketHistory = new TicketHistory(ticket, Action.CREATED, createdBy, new Date(), "Initial creation.");
	    ticket.addHistory(ticketHistory);

	    ticketService.saveTicket(ticket);

	    return ResponseEntity.ok("Ticket created");
	}
	
	@PostMapping("/getByEmails")
	public List<TicketDto> getByEmails(@RequestBody List<String> emails){
		List<Ticket> tickets = ticketService.getTicketsByEmails(emails);
		List<TicketDto> dtos = new ArrayList<>();
		for(Ticket t : tickets) {
			dtos.add(t.toTicketDto());
		}
		return dtos;	
	}
	
	@GetMapping("/tickets/{id}/download")
	public ResponseEntity<Resource> downloadFile(@PathVariable Long id){
		Ticket ticket = ticketService.getTicketById(id);
		
		if(ticket == null) {
			return ResponseEntity.notFound().build();	
		}
		if(ticket.getfileAttachmentPath() == null) {
			return ResponseEntity.notFound().build();
		}
		
		Path path = Paths.get(ticket.getfileAttachmentPath());
		Resource resource = null;
		try {
			resource = new UrlResource(path.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +path.getFileName() +"\"")
				.body(resource);
	}
	
	@PostMapping("/processApprovalsAndRejections")
	public ResponseEntity<Void> process(@RequestBody TicketDecisionsDto dto){
		dto.getDecisions().forEach((ticketId, decision) -> {
			Status status = null;
			if(decision.equals("APPROVED")) {
				status = Status.APPROVED;
			}
			else if(decision.equals("REJECTED")) {
				status = Status.REJECTED;
			}
			Employee employee = objectMapper.convertValue(dto.getEmployee(), Employee.class);
			Employee assignee = objectMapper.convertValue(dto.getAssignee(), Employee.class);
			ticketService.updateTicket(ticketId, status, employee, assignee);
		});
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/getAdminTickets")
	public List<TicketDto> getAdminTickets(@RequestBody String email){
		List<Ticket> tickets = ticketService.getAdminTickets(email);
		List<TicketDto> dtos = new ArrayList<>();
		for(Ticket t : tickets) {
			dtos.add(t.toTicketDto());
		}
		return dtos;
	}
	
	@PostMapping("/processAdminDecisions")
	public ResponseEntity<Void> processAdminDecisions(@RequestBody TicketDecisionsDto dto){
		dto.getDecisions().forEach((ticketId, decision) -> {
			Employee employee = objectMapper.convertValue(dto.getEmployee(), Employee.class);
			ticketService.updateTicket(ticketId, Status.RESOLVED, employee, null);
		});
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/getTicketHistory")
	public List<TicketHistoryDto> getTicketHistory(@RequestBody Long id){
		return ticketHistoryService.getTicketHistoryByTicketId(id);
	}

}
