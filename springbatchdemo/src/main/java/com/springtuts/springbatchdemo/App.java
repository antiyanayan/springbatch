package com.springtuts.springbatchdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.springtuts.springbatchdemo.config.BaseConfiguration;
import com.springtuts.springbatchdemo.jobdemo.OrderProcessingJobConfig;

public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(BaseConfiguration.class,
				OrderProcessingJobConfig.class);

		try {
			ctx.registerShutdownHook();

			JobLauncher launcher = ctx.getBean(JobLauncher.class);
			Job job = ctx.getBean(Job.class, "orderProcessJob");

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
