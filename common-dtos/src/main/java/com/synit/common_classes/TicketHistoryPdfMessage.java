package com.synit.common_classes;

import java.io.Serializable;
import java.util.Date;

public class TicketHistoryPdfMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String action;
	private String actionBy;
	private Date actionDate;
	private String comments;
	
	public TicketHistoryPdfMessage() {}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
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
