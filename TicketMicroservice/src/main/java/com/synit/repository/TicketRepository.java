package com.synit.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.synit.common_enums.Status;
import com.synit.domain.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>{
	
	public List<Ticket> findByCreatedByEmail(String email);
	
	public List<Ticket> findByAssigneeEmail(String email);
	
	public List<Ticket> findByStatusAndTicketDateBefore(Status status, Date cutoffDate);
	
}
