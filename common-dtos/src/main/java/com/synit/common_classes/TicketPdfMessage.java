package com.synit.common_classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TicketPdfMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String title;
	private String description;
	private String createdBy;
	private String assignee;
	private String priority;
	private String status;
	private Date ticket_date;
	private String category;
    private List<TicketHistoryPdfMessage> history = new ArrayList<>();
	
	public TicketPdfMessage() {}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getDate() {
		return ticket_date;
	}
	public void setDate(Date ticket_date) {
		this.ticket_date = ticket_date;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public List<TicketHistoryPdfMessage> getHistory() {
		return history;
	}
	public void setHistory(List<TicketHistoryPdfMessage> history) {
		this.history = history;
	}
	public void addHistory(TicketHistoryPdfMessage history) {
		this.history.add(history);
	}

}
