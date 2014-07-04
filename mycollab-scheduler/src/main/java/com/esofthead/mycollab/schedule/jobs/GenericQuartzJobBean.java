package com.esofthead.mycollab.schedule.jobs;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.0
 * 
 */
public abstract class GenericQuartzJobBean extends QuartzJobBean {
	private static Logger log = LoggerFactory
			.getLogger(GenericQuartzJobBean.class);

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		try {
			JobDetail jobDetail = context.getJobDetail();
			executeJob(context);
		} catch (Exception e) {
			log.error("Exception in running schedule", e);
		}
	}

	abstract protected void executeJob(JobExecutionContext context)
			throws JobExecutionException;

}
