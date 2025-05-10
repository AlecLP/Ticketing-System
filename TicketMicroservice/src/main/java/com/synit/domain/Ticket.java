package com.synit.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.synit.common_enums.Priority;
import com.synit.common_enums.Status;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Ticket {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String title;
	private String description;
	@Embedded
	@AttributeOverrides({
        @AttributeOverride(name = "email", column = @Column(name = "createdBy_email")),
        @AttributeOverride(name = "name", column = @Column(name = "createdBy_name")),
        @AttributeOverride(name = "managerId", column = @Column(name = "createdBy_managerid"))
    })
	private Employee createdBy;
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "email", column = @Column(name = "assignee_email")),
        @AttributeOverride(name = "name", column = @Column(name = "assignee_name")),
        @AttributeOverride(name = "managerId", column = @Column(name = "assignee_managerid"))
    })
	private Employee assignee;
	
	@Enumerated(EnumType.STRING)
	private Priority priority;
	@Enumerated(EnumType.STRING)
	private Status status;
	private Date ticket_date;
	private String category;
	private String fileAttachmentPath;
	@OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketHistory> history;
	
	public Ticket(String title, String description, Employee createdBy, Employee assignee, 
			Priority priority, Status status, Date ticket_date, String category, String fileAttachmentPath) {
		this.title = title;
		this.description = description;
		this.createdBy = createdBy;
		this.assignee = assignee;
		this.priority = priority;
		this.status = status;
		this.ticket_date = ticket_date;
		this.category = category;
		this.fileAttachmentPath = fileAttachmentPath;
		this.history = new ArrayList<>();
	}
	
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
	public Employee getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Employee createdBy) {
		this.createdBy = createdBy;
	}
	public Employee getAssignee() {
		return assignee;
	}
	public void setAssignee(Employee assignee) {
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
	public String getfileAttachmentPath() {
		return fileAttachmentPath;
	}
	public void setfileAttachmentPath(String fileAttachmentPath) {
		this.fileAttachmentPath = fileAttachmentPath;
	}
	public List<TicketHistory> getHistory() {
		return history;
	}
	public void setHistory(List<TicketHistory> history) {
		this.history = history;
	}
	public void addHistory(TicketHistory history) {
		this.history.add(history);
	}
}
