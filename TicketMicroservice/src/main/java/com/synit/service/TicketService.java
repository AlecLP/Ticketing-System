package com.synit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synit.common_enums.Priority;
import com.synit.common_enums.Status;
import com.synit.domain.Ticket;
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
	
	public void updateTicket(long id, Priority priority, Status status) {
		Ticket ticket = ticketRepository.findById(id).orElse(null);
		if(ticket != null) {
			ticket.setPriority(priority);
			ticket.setStatus(status);
			ticketRepository.save(ticket);
		}
	}
	
	public void deleteTicket(long id) {
		ticketRepository.deleteById(id);
	}

}
