package com.foya;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@ComponentScan
@SpringBootApplication
@EnableBatchProcessing
@EnableScheduling
public class BatchApplication {
    @Autowired
    JobLauncher jobLauncher;
	@Value("${batch.runOnce}")
    boolean runOnce;
    @Autowired
    Job processJob;
	public static void main(String[] args) {
		SpringApplication.run(BatchApplication.class, args);
	}
 // @Scheduled(cron = "${cron.expression}")  
    @Scheduled(fixedDelayString = "${fixedDelayString.expression}")
	public void perform() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
        		.addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(processJob, jobParameters);
		if(runOnce) {
			System.exit(0);
		}
    }
}
