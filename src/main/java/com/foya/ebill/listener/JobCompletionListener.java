package com.foya.ebill.listener;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;


public class JobCompletionListener extends JobExecutionListenerSupport {
	private Log log = LogFactory.getLog(JobCompletionListener.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public void beforeJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.STARTED) {
			log.info("BATCH JOB STARTED SUCCESSFULLY");
		}
	}
	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED || jobExecution.getStatus() == BatchStatus.FAILED) {
			log.info("BATCH JOB " + jobExecution.getStatus());
		}
	}
	
}