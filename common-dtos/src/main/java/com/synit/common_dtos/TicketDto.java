package com.synit.common_dtos;

import java.util.Date;

import com.synit.common_enums.Priority;
import com.synit.common_enums.Status;

public class TicketDto {
	
	private long id;
	private String title;
	private String description;
	private EmployeeDto createdBy;
	private EmployeeDto assignee;
	private Priority priority;
	private Status status;
	private Date ticket_date;
	private String category;
	private boolean hasFile;
	
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
	public EmployeeDto getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(EmployeeDto createdBy) {
		this.createdBy = createdBy;
	}
	public EmployeeDto getAssignee() {
		return assignee;
	}
	public void setAssignee(EmployeeDto assignee) {
		this.assignee = assignee;
	}
	public Priority getPriority() {
		return priority;
	}
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Date getTicket_date() {
		return ticket_date;
	}
	public void setTicket_date(Date ticket_date) {
		this.ticket_date = ticket_date;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public boolean getHasFile() {
		return hasFile;
	}
	public void setHasFile(boolean hasFile) {
		this.hasFile = hasFile;
	}
	
	public TicketDto() {}

}
