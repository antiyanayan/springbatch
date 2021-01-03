package com.springtuts.springbatchdemo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);

	private static Job job;
	private static JobLauncher launcher;
	private static JobRepository jobRepository;
	private static JobExplorer explorer;
	private static JobOperator operator;
	private static ClassPathXmlApplicationContext ctx;
    private static String[] springConfig  = {"spring/batch/jobs/import-job-beans.xml" };
	
    
    
	public static void main(String[] args) {
		

		try {
			ctx = new ClassPathXmlApplicationContext(springConfig);

			job = (Job) ctx.getBean("orderProcessJob");
	        launcher = (JobLauncher) ctx.getBean("jobLauncher");    
	        jobRepository = (JobRepository) ctx.getBean("jobRepository");
	        explorer = ctx.getBean(JobExplorer.class);
	        operator = ctx.getBean(JobOperator.class);
	        
	        
	        
			JobParametersBuilder builder = new JobParametersBuilder();
			builder.addString("JobId", Long.toString(System.currentTimeMillis()));
			
			
			JobInstance instance = explorer.getLastJobInstance("orderProcessJob");
			
			
			
		} catch (Exception e) {
			logger.error(">>> An Exception {} occured with message : {}", e.getClass(), e.getMessage());
		} finally {
			ctx.close();
		}
	}
	
	public static BatchStatus checkPreviousJobInstanceExitStatus(String jobName) {
		return null;
	}
}
