package com.synit.dtos;

import java.util.Date;

import com.synit.common_enums.Action;

public class TicketHistoryDto {
	
	private Action action;
	private String actionBy;
	private Date actionDate;
	private String comments;
	
	public TicketHistoryDto() {}

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
