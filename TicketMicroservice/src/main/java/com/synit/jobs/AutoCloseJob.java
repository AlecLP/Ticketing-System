package com.synit.jobs;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.synit.common_enums.Status;
import com.synit.domain.Ticket;
import com.synit.service.TicketService;

public class AutoCloseJob implements Job{
	
	@Autowired
	TicketService ticketService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Date fiveDaysAgo = Date.from(
			    LocalDate.now().minusDays(5).atStartOfDay(ZoneId.systemDefault()).toInstant()
		);
        List<Ticket> ticketsToClose = ticketService.findByStatusAndDateBefore(Status.RESOLVED, fiveDaysAgo);
        for(Ticket t : ticketsToClose) {
        	ticketService.updateTicketStatus(t.getId(), Status.CLOSED.name(), t.getCreatedBy().getEmail(), "Auto closed.");
        }
	}

}
