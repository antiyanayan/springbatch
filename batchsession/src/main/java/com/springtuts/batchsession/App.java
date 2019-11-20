package com.springtuts.batchsession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.springtuts.batchsession.config.BaseConfig;
import com.springtuts.batchsession.demojob.OrderProcessingJobConfig;

public class App {

	private static Logger logger = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) {
	
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		
		try {

			ctx.register(BaseConfig.class, OrderProcessingJobConfig.class);
			ctx.registerShutdownHook();
			ctx.refresh();
			
			JobLauncher launcher = ctx.getBean(JobLauncher.class);
			Job processOrder = (Job)ctx.getBean("orderProcessingJob");
			
			JobParametersBuilder builder = new JobParametersBuilder();
			builder.addString("JobId", Long.toString(System.currentTimeMillis()));
			builder.addString("name", "executed by nayan");
			
			JobExecution execution = launcher.run(processOrder, builder.toJobParameters());
			 logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Job Execution Status : {}", execution.getExitStatus());
			
		} catch (Exception e) {
			logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Caught Exception {} with message {}", e.getClass(), e.getMessage());
		}
		finally {
			ctx.close();
		}
	}
}
