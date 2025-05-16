package com.synit.domain;

import java.util.Date;

import com.synit.common_enums.Action;
import com.synit.dtos.TicketHistoryDto;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class TicketHistory {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;
	
	@Enumerated(EnumType.STRING)
	private Action action;
	
	@Embedded
	@ManyToOne
	@JoinColumn(name = "actionBy")
	private Employee actionBy;
	
	private Date actionDate;
	private String comments;
	
	public TicketHistory() {}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Ticket getTicket() {
		return ticket;
	}
	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	public Employee getActionBy() {
		return actionBy;
	}
	public void setActionBy(Employee actionBy) {
		this.actionBy = actionBy;
	}
	public Date getActionDate() {
		return actionDate;
	}
	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public TicketHistoryDto toDto() {
		TicketHistoryDto dto = new TicketHistoryDto();
		dto.setAction(action);
		dto.setActionBy(actionBy.getEmail());
		dto.setActionDate(actionDate);
		dto.setComments(comments);
		return dto;
	}
}
