package com.foya.ebill.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;


public class MailSendStepListener extends StepExecutionListenerSupport{
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		//TODO
		return null;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		//TODO
	}
}