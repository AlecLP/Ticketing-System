package com.synit.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.synit.common_enums.Status;
import com.synit.domain.Ticket;
import com.synit.domain.TicketHistory;
import com.synit.dtos.TicketDto;
import com.synit.dtos.TicketHistoryDto;
import com.synit.dtos.TicketSendDto;
import com.synit.dtos.TicketStatusDto;
import com.synit.service.EmployeeService;
import com.synit.service.TicketHistoryService;
import com.synit.service.TicketService;

@RestController
@CrossOrigin(origins="http://localhost:8181")
public class TicketController {
	
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	TicketService ticketService;
	
	@Autowired
	TicketHistoryService ticketHistoryService;
	
	@PostMapping("/createTicket")
	public ResponseEntity<String> createTicket(
			@RequestPart("ticketData") TicketDto ticketDto,
			@RequestPart(value="files", required=false) MultipartFile[] files)
	{
		ticketService.saveTicket(ticketDto, files);
		return ResponseEntity.ok("Ticket created!");
	}
	
	@PostMapping("/getUserTickets")
	public ResponseEntity<List<TicketSendDto>> getUserTickets(@RequestBody Map<String, String> body) {
	    String email = body.get("email");
	    List<Ticket> tickets = ticketService.findByCreatedByEmail(email);
	    List<TicketSendDto> dtos = new ArrayList<>();
	    for(Ticket t : tickets) {
	    	TicketSendDto dto = t.toSendDto();
	    	dtos.add(dto);
	    }
	    Collections.sort(dtos, Comparator.comparing(TicketSendDto::getTicket_date).reversed());
	    return ResponseEntity.ok(dtos);
	}
	
	@PostMapping("/getManagerTickets")
	public ResponseEntity<List<TicketSendDto>> getManagerTickets(@RequestBody Map<String, String> body) {
	    String email = body.get("email");
	    List<Ticket> tickets = ticketService.findByAssigneeEmail(email);
	    List<TicketSendDto> dtos = new ArrayList<>();
	    for(Ticket t : tickets) {
	    	TicketSendDto dto = t.toSendDto();
	    	dtos.add(dto);
	    }
	    Collections.sort(dtos, Comparator.comparing(TicketSendDto::getTicket_date).reversed());
	    return ResponseEntity.ok(dtos);
	}
	
	@PostMapping("/getAdminTickets")
	public ResponseEntity<List<TicketSendDto>> getAdminTickets(@RequestBody Map<String, String> body) {
	    String email = body.get("email");
	    List<Ticket> tickets = ticketService.findByAssigneeEmail(email);
	    List<TicketSendDto> dtos = new ArrayList<>();
	    for(Ticket t : tickets) {
	    	if(t.getStatus() == Status.APPROVED || t.getStatus() == Status.REOPENED) {
	    		TicketSendDto dto = t.toSendDto();
		    	dtos.add(dto);
	    	}
	    }
	    Collections.sort(dtos, Comparator.comparing(TicketSendDto::getTicket_date).reversed());
	    return ResponseEntity.ok(dtos);
	}
	
	@GetMapping("/download/{id}")
	public ResponseEntity<Resource> downloadFiles(@PathVariable Long id){
	    ByteArrayResource resource = new ByteArrayResource(ticketService.zipFiles(id).toByteArray());
	    
	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Ticket_Files.zip\"")
	        .contentType(MediaType.APPLICATION_OCTET_STREAM)
	        .body(resource);
	}
	
	@GetMapping("/ticketHistory/{ticketId}")
	public ResponseEntity<List<TicketHistoryDto>> getTicketHistory(@PathVariable Long ticketId) {
	    List<TicketHistory> history = ticketHistoryService.getTicketHistoryByTicketId(ticketId);
	    List<TicketHistoryDto> dtos = new ArrayList<>();
		for(TicketHistory h : history) {
			TicketHistoryDto dto = h.toDto();
			dtos.add(dto);
		}
		Collections.sort(dtos, Comparator.comparing(TicketHistoryDto::getActionDate));
	    return ResponseEntity.ok(dtos);
	}
	
	@PostMapping("/updateTicketStatuses")
	public ResponseEntity<Void> updateTicketStatuses(@RequestBody List<TicketStatusDto> updates){
		updates.forEach(update -> {
	        ticketService.updateTicketStatus(update.getTicketId(), update.getStatus(), update.getEmail(), update.getComments());
	    });
	    return ResponseEntity.ok().build();
	}
}
