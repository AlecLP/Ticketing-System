package com.synit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.synit.domain.TicketHistory;

@Repository
public interface TicketHistoryRepository extends JpaRepository<TicketHistory, Long>{

}
