package com.synit.controller;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.synit.common_dtos.TicketCreateDto;
import com.synit.common_enums.Priority;
import com.synit.component.TicketMicroserviceClient;
import com.synit.domain.Employee;
import com.synit.service.EmployeeService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CreateTicketController {
	
	@Autowired
	EmployeeService employeeService;
	@Autowired
	TicketMicroserviceClient ticketMicroserviceClient;
	
	@PostMapping("/createTicket")
	public String createTicket(@RequestParam("title") String title,
			@RequestParam("description") String description,
	        @RequestParam("category") String category,
	        @RequestParam("priority") Priority priority, 
	        @RequestParam(value = "file", required = false) MultipartFile file,
	        Principal principal) {
	    Employee employee = employeeService.findByEmail(principal.getName());
	    Employee assignee = employeeService.findByEmployeeId(Long.parseLong(employee.getManagerId()));
	    
	    TicketCreateDto ticketDto = new TicketCreateDto();
	    ticketDto.setTitle(title);
	    ticketDto.setDescription(description);
	    ticketDto.setCategory(category);
	    ticketDto.setPriority(priority);
	    ticketDto.setCreatedBy(employee.toEmployeeDto());
	    ticketDto.setAssignee(assignee.toEmployeeDto());
	    
	    try {
			ticketMicroserviceClient.sendCreateTicketRequest(ticketDto, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    return "redirect:/employeeDashboard";
	}
	
	@GetMapping("/ticketForm")
	public String ticketForm(HttpServletRequest request) {
		request.setAttribute("priorities", Priority.values());
		return "ticketForm";
	}

}
