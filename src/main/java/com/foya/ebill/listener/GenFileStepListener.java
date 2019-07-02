package com.foya.ebill.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;


public class GenFileStepListener extends StepExecutionListenerSupport{
	private Log log = LogFactory.getLog(GenFileStepListener.class);
	
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
