package com.synit.common_dtos;

import java.util.Date;

import com.synit.common_enums.Action;

public class TicketHistoryDto {
	private Long ticketId;
	private Action action;
	private String actionBy;
	private Date actionDate;
	private String comments;
	public Long getTicketId() {
		return ticketId;
	}
	public TicketHistoryDto() {}
	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	public String getActionBy() {
		return actionBy;
	}
	public void setActionBy(String actionBy) {
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
	
}
