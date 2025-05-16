package com.synit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.synit.common_enums.Priority;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ViewsController {
	
	@GetMapping("/ticketForm")
	public String ticketForm(HttpServletRequest request) {
		request.setAttribute("priorities", Priority.values());
		return "ticketForm";
	}
	
	@GetMapping("/viewTickets")
	public String viewTickets() {
		return "viewTickets";
	}
	
	@GetMapping("/managerDashboard")
	public String managerDashboard() {
		return "managerDashboard";
	}
	
	@GetMapping("/adminDashboard")
	public String adminDashboard() {
		return "adminDashboard";
	}
	
}
