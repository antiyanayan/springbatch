package com.springtuts.springbatchdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);

	static Job job;
    static JobLauncher launcher;
    static ClassPathXmlApplicationContext ctx;
    private static String[] springConfig  = {"spring/batch/jobs/import-job-beans.xml" };
	
	public static void main(String[] args) {
		

		try {
			ctx = new ClassPathXmlApplicationContext(springConfig);

			job = (Job) ctx.getBean("orderProcessJob");
	        launcher = (JobLauncher) ctx.getBean("jobLauncher");    

			JobParametersBuilder builder = new JobParametersBuilder();
			builder.addString("JobId", Long.toString(System.currentTimeMillis()));

			launcher.run(job, builder.toJobParameters());

		} catch (Exception e) {
			logger.error(">>> An Exception {} occured with message : {}", e.getClass(), e.getMessage());
		} finally {
			ctx.close();
		}
	}
}
