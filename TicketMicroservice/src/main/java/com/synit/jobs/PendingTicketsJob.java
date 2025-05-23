package com.synit.jobs;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.synit.common_enums.Action;
import com.synit.common_enums.Status;
import com.synit.domain.Ticket;
import com.synit.service.TicketService;

public class PendingTicketsJob implements Job{
	
	@Autowired
	TicketService ticketService;
	
	@Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
		Date sevenDaysAgo = Date.from(
			    LocalDate.now().minusDays(7).atStartOfDay(ZoneId.systemDefault()).toInstant()
		);
        List<Ticket> openTickets = ticketService.findByStatusAndDateBefore(Status.OPEN, sevenDaysAgo);
        for(Ticket t : openTickets) {
        	ticketService.sendMessage(t.getAssignee().getEmail(), t.getTitle(), Action.CREATED.name(), "Ticket has been open for >7 days.");
        }
        
        List<Ticket> approvedTickets = ticketService.findByStatusAndDateBefore(Status.APPROVED, sevenDaysAgo);
        for(Ticket t : approvedTickets) {
        	ticketService.sendMessage(t.getAssignee().getEmail(), t.getTitle(), Action.APPROVED.name(), "Ticket has been approved for >7 days.");
        }
    }

}
