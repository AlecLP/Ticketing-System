package com.synit.jobs;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {
	
	@Bean
    public JobDetail pendingTicketsJobDetail() {
        return JobBuilder.newJob(PendingTicketsJob.class)
                .withIdentity("pendingTicketsJob")
                .storeDurably()
                .build();
    }
	
	@Bean
    public JobDetail autoCloseJobDetail() {
        return JobBuilder.newJob(AutoCloseJob.class)
                .withIdentity("autoCloseJob")
                .storeDurably()
                .build();
    }
	
	@Bean
    public Trigger pendingTicketsJobTrigger() {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                .cronSchedule("0 0 0 * * ?");

        return TriggerBuilder.newTrigger()
                .forJob(pendingTicketsJobDetail())
                .withIdentity("pendingTicketsTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }
	
	@Bean
    public Trigger autoCloseJobTrigger() {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                .cronSchedule("0 0 0 * * ?");

        return TriggerBuilder.newTrigger()
                .forJob(autoCloseJobDetail())
                .withIdentity("autoCloseTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }

}
