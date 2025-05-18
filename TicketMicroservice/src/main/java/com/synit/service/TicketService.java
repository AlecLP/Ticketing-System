package com.synit.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.synit.common_classes.TicketMessage;
import com.synit.common_enums.Action;
import com.synit.common_enums.Priority;
import com.synit.common_enums.Status;
import com.synit.domain.Employee;
import com.synit.domain.Ticket;
import com.synit.domain.TicketHistory;
import com.synit.dtos.TicketDto;
import com.synit.repository.TicketRepository;

@Service
public class TicketService {
	
	@Autowired
	TicketRepository ticketRepository;
	
	@Autowired
	EmployeeService employeeService;
	
	private final JmsTemplate jmsTemplate;
	
	@Value("${ticket.upload.dir}")
    private String uploadDir;
	
	public TicketService(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	
	public void sendMessage(String to, String ticketTitle, String action, String comments) {
		TicketMessage message = new TicketMessage(to, ticketTitle, action, comments);
		jmsTemplate.convertAndSend("notification.queue", message);
	}
	
	public void sendPdfMessage(Ticket ticket) {
		jmsTemplate.convertAndSend("pdf.queue", ticket.toPdfMessage());
	}
	
	public void saveTicket(TicketDto ticketDto, MultipartFile[] files) {
		Date currentDate = new Date();
		Ticket ticket = new Ticket();
		ticket.setTitle(ticketDto.getTitle());
		ticket.setDescription(ticketDto.getDescription());
		ticket.setCategory(ticketDto.getCategory());
		ticket.setPriority(Priority.valueOf(ticketDto.getPriority()));
		
		Employee createdBy = employeeService.getEmployeeByEmail(ticketDto.getCreatedBy());
		ticket.setCreatedBy(createdBy);
		Employee assignee = employeeService.getEmployeeByEmail(createdBy.getManagerEmail());
		ticket.setAssignee(assignee);
		
		ticket.setStatus(Status.OPEN);
		ticket.setDate(currentDate);
		
		TicketHistory history = new TicketHistory();
		history.setTicket(ticket);
		history.setAction(Action.CREATED);
		history.setActionBy(createdBy);
		history.setActionDate(currentDate);
		history.setComments("Initial Creation.");
		
		ticket.addHistory(history);
		
		Ticket saved = ticketRepository.save(ticket);
		
		if (files != null) {
			List<String> fileAttachmentPaths = new ArrayList<>();
			
			String safeUserName = createdBy.getName().replaceAll("[^a-zA-Z0-9_-]", "_");
		    Path uploadPath = Paths.get(uploadDir, safeUserName, String.valueOf(saved.getId()));
		    try {
		        Files.createDirectories(uploadPath);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }

		    for (MultipartFile file : files) {
		        try {
		            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename()).replaceAll("\"", "");
		            Path filePath = uploadPath.resolve(originalFilename);
		            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
		            String storedPath = Paths.get(createdBy.getName(), String.valueOf(saved.getId()), originalFilename).toString();
		            fileAttachmentPaths.add(storedPath);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		    saved.setFileAttachmentPaths(fileAttachmentPaths);
		    ticketRepository.save(saved);
		}
	}
	
	public List<Ticket> findByCreatedByEmail(String email){
		return ticketRepository.findByCreatedByEmail(email);
	}
	
	public List<Ticket> findByAssigneeEmail(String email){
		return ticketRepository.findByAssigneeEmail(email);
	}
	
	public ByteArrayOutputStream zipFiles(Long id) {
		Ticket ticket = ticketRepository.findById(id).orElse(null);
	    List<String> filePaths = ticket.getFileAttachmentPaths();

	    ByteArrayOutputStream zipOut = new ByteArrayOutputStream();
	    try (ZipOutputStream zos = new ZipOutputStream(zipOut)) {
	        for (String path : filePaths) {
	            File file = new File(uploadDir, path);
	            if (file.exists()) {
	                try {
						zos.putNextEntry(new ZipEntry(file.getName()));
						Files.copy(file.toPath(), zos);
		                zos.closeEntry();
					} catch (IOException e) {
						e.printStackTrace();
					} 
	            }
	        }
	    } catch (IOException e1) {
			e1.printStackTrace();
		}
	    return zipOut;
	}
	
	public void updateTicketStatus(long id, String status, String email, String comments) {
		Status s = Status.valueOf(status);
		
		Ticket ticket = ticketRepository.findById(id).orElse(null);
		ticket.setStatus(s);
		
		TicketHistory history = new TicketHistory();
		history.setTicket(ticket);
		Action action = switch(s) {
			case APPROVED -> Action.APPROVED;
			case REJECTED -> Action.REJECTED;
			case RESOLVED -> Action.RESOLVED;
			case REOPENED -> Action.REOPENED;
			case CLOSED -> Action.CLOSED;
			default -> throw new IllegalArgumentException("Unexpected value: " + status);
		};
		history.setAction(action);
		
		Employee actionBy = employeeService.getEmployeeByEmail(email);
		if(action == Action.APPROVED) {
			Employee manager = employeeService.getEmployeeByEmail(actionBy.getManagerEmail());
			ticket.setAssignee(manager);
		}
		else if(action == Action.REJECTED) {
			ticket.setAssignee(null);
		}
		history.setActionBy(actionBy);
		history.setActionDate(new Date());
		history.setComments(comments);	
		
		ticket.addHistory(history);
		
		ticketRepository.save(ticket);
		
		if(action != Action.RESOLVED) {
			sendMessage(ticket.getCreatedBy().getEmail(), ticket.getTitle(), action.name(), comments);
		}
		else {
			sendPdfMessage(ticket);
		}
	}
}
