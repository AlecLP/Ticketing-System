package com.synit.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.synit.common_dtos.TicketDto;
import com.synit.common_dtos.TicketHistoryDto;
import com.synit.component.TicketMicroserviceClient;
import com.synit.domain.Employee;
import com.synit.domain.Role;
import com.synit.service.EmployeeService;
import com.synit.service.RoleService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping
public class EmployeeController {
	
	@Autowired
	EmployeeService employeeService;
	@Autowired
	RoleService roleService;
	@Autowired
	TicketMicroserviceClient ticketMicroserviceClient;
	
	@GetMapping("/employeeDashboard")
	public String employeeDashboard(Principal principal, Model model) {
		model.addAttribute("name", principal.getName());
		return "employeeDashboard";
	}
	
	@GetMapping("/login")
	public String login(@RequestParam(required=false) String logout, @RequestParam(required=false) String error,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Model model) {
		String message = "";
		if(error != null) {
			message = "Invalid Credentials";
		}
		if(logout != null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth != null) {
				new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, auth);
			}
			message = "Logout";
			return "login";
		}
		model.addAttribute("Message", message);
		return "login";
	}
	
	@GetMapping(value = "/accessDeniedPage")
	public String accessDenied(Principal principal, Model model) {
		String message = principal.getName() + ", Unauthorized access";
		model.addAttribute("Message", message);
		return "accessDeniedPage";
	}
	
	@PostMapping(value = "/signup")
	public String signup(@RequestParam String email, @RequestParam String password, @RequestParam String name,
			@RequestParam String department, @RequestParam String project, @RequestParam String managerId) {
		Employee employee = new Employee();	
		employee.setEmail(email);
		employee.setPassword(password);
		employee.setName(name);
		employee.setDepartment(department);
		employee.setProject(project);
		employee.setManagerId(managerId);
		Set<Role> role = roleService.getDefaultRole();
		employee.setRoles(role);
		employeeService.saveEmployee(employee);
		return "login";
	}
	
	@GetMapping("/register")
	public String register() {
		return "signup";
	}
	
	@GetMapping("/viewTickets")
	public String showTickets(Principal principal, Model model) {
		Employee user = employeeService.findByEmail(principal.getName());
		List<String> email = new ArrayList<>();
		email.add(user.getEmail());
		List<TicketDto> tickets = ticketMicroserviceClient.sendGetByEmailsRequest(email);
		
		model.addAttribute("tickets", tickets);
		
		return "viewTickets";
	}
	
	@GetMapping("/viewHistory/{id}")
	public String viewHistory(Principal principal, Model model, @PathVariable Long id) {
		List<TicketHistoryDto> history = ticketMicroserviceClient.sendGetTicketHistoryRequest(id);
		
		model.addAttribute("histories", history);
		
		return "viewHistory";
	}

}
