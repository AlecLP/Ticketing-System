package com.synit.common_classes;

import java.io.Serializable;

public class TicketMessage implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String to;
	private String ticketTitle;
	private String action;
	private String comments;
	
	public TicketMessage(String to, String ticketTitle, String action, String comments) {
		this.to = to;
		this.ticketTitle = ticketTitle;
		this.action = action;
		this.comments = comments;
	}

	public String getTo() {
		return to;
	}
	
	public void setTo(String to) {
		this.to = to;
	}
	
	public String getTicketTitle() {
		return ticketTitle;
	}

	public void setTicketTitle(String ticketTitle) {
		this.ticketTitle = ticketTitle;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
