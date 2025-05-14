package com.synit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synit.common_dtos.TicketHistoryDto;
import com.synit.domain.TicketHistory;
import com.synit.repository.TicketHistoryRepository;

@Service
public class TicketHistoryService {

	@Autowired
	TicketHistoryRepository ticketHistoryRepository;
	
	public TicketHistory saveTicketHistory(TicketHistory ticketHistory) {
		return ticketHistoryRepository.save(ticketHistory);
	}
	
	public List<TicketHistory> getTicketHistory(){
		return ticketHistoryRepository.findAll();
	}
	
	public List<TicketHistoryDto> getTicketHistoryByTicketId(Long id){
		List<TicketHistory> history = ticketHistoryRepository.findByTicketId(id);
		List<TicketHistoryDto> dtos = new ArrayList<>();
		for(TicketHistory h : history) {
			TicketHistoryDto dto = new TicketHistoryDto();
			dto.setTicketId(h.getTicket().getId());
			dto.setAction(h.getAction());
			dto.setActionBy(h.getActionBy().getEmail());
			dto.setActionDate(h.getActionDate());
			dto.setComments(h.getComments());
			dtos.add(dto);
		}
		return dtos;
	}
	
	public void updateTicketHistory(long id, String comments) {
		TicketHistory ticketHistory = ticketHistoryRepository.findById(id).orElse(null);
		if(ticketHistory != null) {
			ticketHistory.setComments(comments);
			ticketHistoryRepository.save(ticketHistory);
		}
	}
	
	public void deleteTicketHistory(long id) {
		ticketHistoryRepository.deleteById(id);
	}
	
}
