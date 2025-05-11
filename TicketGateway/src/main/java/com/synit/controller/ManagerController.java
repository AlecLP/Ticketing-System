package com.synit.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.synit.common_dtos.TicketDecisionsDto;
import com.synit.common_dtos.TicketDto;
import com.synit.component.TicketMicroserviceClient;
import com.synit.domain.Employee;
import com.synit.service.EmployeeService;

@Controller
public class ManagerController {
	
	@Autowired
	EmployeeService employeeService;
	@Autowired
	TicketMicroserviceClient ticketMicroserviceClient;
	
	@GetMapping("/managerDashboard")
	public String showManagerTickets(Principal principal, Model model) {
		Employee user = employeeService.findByEmail(principal.getName());
		List<String> emails = employeeService.findByManagerId(user.getId());
		List<TicketDto> tickets = ticketMicroserviceClient.sendGetByEmailsRequest(emails);
		
		model.addAttribute("tickets", tickets);
		
		return "managerDashboard";
	}
	
	@PostMapping("/processTickets")
	public String processTickets(@RequestParam Map<String, String> actions, Principal principal) {
		Map<Long, String> decisions = new HashMap<>();
		
		actions.forEach((key, value) -> {
			if(key.startsWith("actions[")) {
				Long ticketId = Long.valueOf(key.substring(8, key.length() - 1));
				if(value.equals("APPROVED") || value.equals("REJECTED")) {
					decisions.put(ticketId, value);
				}
			}
		});
		
		TicketDecisionsDto dto = new TicketDecisionsDto();
		dto.setDecisions(decisions);
		
		Employee employee = employeeService.findByEmail(principal.getName());
		dto.setEmployee(employee.toEmployeeDto());
		dto.setAssignee(employeeService.findByEmployeeId(Long.valueOf(employee.getManagerId())).toEmployeeDto());
		
		ticketMicroserviceClient.sendProcessTicketsRequest(dto);
		
		return "employeeDashboard";
	}

}
