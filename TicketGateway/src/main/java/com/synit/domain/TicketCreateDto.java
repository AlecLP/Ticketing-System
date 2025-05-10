package com.synit.domain;

public class TicketCreateDto {
    private String title;
    private String description;
    private String category;
    private String priority;
    private EmployeeDto createdBy;
    private EmployeeDto assignee;
    
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
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
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
    
}

