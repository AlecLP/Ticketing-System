package com.synit.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.synit.common_enums.Priority;
import com.synit.common_enums.Status;

public class TicketSendDto {
	
	private long id;
	private String title;
	private String description;
	private String createdBy;
	private String assignee;
	private Priority priority;
	private Status status;
	private Date ticket_date;
	private String category;
	private List<String> fileAttachmentPaths = new ArrayList<>();
	
	public TicketSendDto() {}

	public Long getId() {
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

	public List<String> getFileAttachmentPaths() {
		return fileAttachmentPaths;
	}

	public void setFileAttachmentPaths(List<String> fileAttachmentPaths) {
		this.fileAttachmentPaths = fileAttachmentPaths;
	}
	
	
}
