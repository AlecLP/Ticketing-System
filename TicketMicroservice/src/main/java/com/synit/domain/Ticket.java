package com.synit.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.synit.common_classes.TicketPdfMessage;
import com.synit.common_enums.Priority;
import com.synit.common_enums.Status;
import com.synit.dtos.TicketSendDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Ticket {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String title;
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "created_by_email")
	private Employee createdBy;
	
	@ManyToOne
	@JoinColumn(name = "assignee_email")
	private Employee assignee;
	
	@Enumerated(EnumType.STRING)
	private Priority priority;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@Column(name = "ticket_date")  // maps DB column to Java field
    @Temporal(TemporalType.TIMESTAMP)
    private Date ticketDate;
	private String category;
	@ElementCollection
	private List<String> fileAttachmentPaths = new ArrayList<>();
	
	@OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketHistory> history = new ArrayList<>();
	
	public Ticket() {}
	
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
		return ticketDate;
	}
	public void setDate(Date ticketDate) {
		this.ticketDate = ticketDate;
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
	public List<TicketHistory> getHistory() {
		return history;
	}
	public void setHistory(List<TicketHistory> history) {
		this.history = history;
	}
	public void addHistory(TicketHistory history) {
		this.history.add(history);
	}
	
	public TicketSendDto toSendDto() {
		TicketSendDto dto = new TicketSendDto();
		dto.setId(id);
		dto.setTitle(title);
		dto.setDescription(description);
		dto.setCreatedBy(createdBy.getEmail());
		if(assignee != null) {
			dto.setAssignee(assignee.getEmail());
		}
		else {
			dto.setAssignee("None");
		}
		dto.setPriority(priority);
		dto.setStatus(status);
		dto.setTicket_date(ticketDate);
		dto.setCategory(category);
		dto.setFileAttachmentPaths(fileAttachmentPaths);
		return dto;
	}
	
	public TicketPdfMessage toPdfMessage() {
		TicketPdfMessage pdfMessage = new TicketPdfMessage();
		pdfMessage.setId(id);
		pdfMessage.setTitle(title);
		pdfMessage.setDescription(description);
		pdfMessage.setCreatedBy(createdBy.getEmail());
		pdfMessage.setAssignee(assignee != null ? assignee.getEmail() : "None");
		pdfMessage.setPriority(priority.name());
		pdfMessage.setStatus(status.name());
		pdfMessage.setDate(ticketDate);
		pdfMessage.setCategory(category);
		for(TicketHistory h : history) {
			pdfMessage.addHistory(h.toPdfMessage());
		}
		return pdfMessage;
	}
}
