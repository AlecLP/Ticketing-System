package com.synit.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synit.common_enums.Action;
import com.synit.common_enums.Status;
import com.synit.domain.Employee;
import com.synit.domain.Ticket;
import com.synit.domain.TicketHistory;
import com.synit.repository.TicketRepository;

@Service
public class TicketService {
	
	@Autowired
	TicketRepository ticketRepository;
	
	public Ticket saveTicket(Ticket ticket) {
		return ticketRepository.save(ticket);
	}
	
	public List<Ticket> getTickets(){
		return ticketRepository.findAll();
	}
	
	public Ticket getTicketById(long id) {
		return ticketRepository.findById(id).orElse(null);
	}
	
	public void updateTicket(long id, Status status, Employee employee, Employee assignee) {
		Ticket ticket = ticketRepository.findById(id).orElse(null);
		if(ticket != null) {
			List<TicketHistory> history = ticket.getHistory();
			Action action = switch(status) {
				case APPROVED -> Action.APPROVED;
				case REJECTED -> Action.REJECTED;
				case RESOLVED -> Action.RESOLVED;
				default -> throw new IllegalArgumentException("Unexpected value: " + status);
			};
			TicketHistory newHistory = new TicketHistory(ticket, action, employee, new Date(), "");
			history.add(newHistory);
			
			ticket.setStatus(status);
			if(action == Action.APPROVED) {
				ticket.setAssignee(assignee);
			}
			else if(action == Action.REJECTED){
				ticket.setAssignee(null);
			}
			ticketRepository.save(ticket);
		}
	}
	
	public void deleteTicket(long id) {
		ticketRepository.deleteById(id);
	}
	
	public List<Ticket> getTicketsByEmails(List<String> emails){
		List<Ticket> tickets = new ArrayList<>();
		for(String email : emails) {
			tickets.addAll(ticketRepository.findByCreatedByEmailAndStatus(email, Status.OPEN));
		}
		return tickets;
	}
	
	public List<Ticket> getAdminTickets(String email){
		return ticketRepository.findByAssigneeEmail(email);
	}

}
