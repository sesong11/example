package com.cbot.quartzjdbc.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cbot.quartzjdbc.job.MyJob;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class JobSchedulerController {

	@Autowired
    private Scheduler scheduler;
	
	@GetMapping("/schedule")
	public ResponseEntity<Object> schedule() {
		try {
			var cal = Calendar.getInstance();
			cal.set(Calendar.SECOND, cal.get(Calendar.SECOND)+10);
			JobDetail jobDetail = buildJobDetail();
			 Trigger trigger = buildCronJobTrigger(jobDetail, cal.getTime(), "0/10 * * * * ?");
	           scheduler.scheduleJob(jobDetail, trigger);
	           log.info("Job : "+jobDetail.getKey());
	           log.info("Job start in 10 second and repeat every 10 second.");
			return ResponseEntity.ok("OK");
		} catch (Exception ex) {
			log.error("Error scheduling", ex);
			return ResponseEntity.badRequest().body("Oops");
		}
	}
	
	@GetMapping(path = "/reloadschedule")
	public ResponseEntity<Object> reloadSchedule(@RequestParam(value = "id") String id, @RequestParam(value = "cron") String cron) {
		try {
			TriggerKey triggerKey = new TriggerKey(UUID.fromString(id).toString(), "my-triggers");
			var trigger = scheduler.getTrigger(triggerKey);
			CronTriggerImpl t = (CronTriggerImpl) trigger;
			t.setCronExpression(cron);
			scheduler.rescheduleJob(triggerKey, t);
	        log.info("Job reloaded {}", trigger);
			return ResponseEntity.ok("OK");
		} catch (Exception ex) {
			log.error("Error scheduling", ex);
			return ResponseEntity.badRequest().body("Oops");
		}
	}
	private JobDetail buildJobDetail() {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("name", "Test Job");

        return JobBuilder.newJob(MyJob.class)
                .withIdentity(UUID.randomUUID().toString(), "My jobs")
                .withDescription("My Jobs")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }
		
	private Trigger buildCronJobTrigger(JobDetail jobDetail, Date startAt, String cron) {
		return TriggerBuilder.newTrigger()
				.forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "my-triggers")
                .withDescription("My Trigger")
                .startAt(startAt)
			    .withSchedule(CronScheduleBuilder.cronSchedule(cron))
			    .build();
		
    }
}
