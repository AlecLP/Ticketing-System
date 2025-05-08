package com.synit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.synit.domain.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>{

}
