package com.springtuts.springbatchdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.springtuts.springbatchdemo.config.BaseConfiguration;
import com.springtuts.springbatchdemo.jobdemo.OrderProcessingJobConfig;

public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);

	private static JobExplorer explorer;
	private static JobOperator operator;
	private static final String ORDER_PROCESS_JOB_NAME = "orderProcessJob";

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(BaseConfiguration.class,
				OrderProcessingJobConfig.class);

		try {
			ctx.registerShutdownHook();

			explorer = ctx.getBean(JobExplorer.class);
			operator = ctx.getBean(JobOperator.class);
			
			checkAndRunJobInstance(ORDER_PROCESS_JOB_NAME);

		} catch (Exception e) {
			logger.error("An Exception occurred with description : ", e);
		} finally {
			ctx.close();
		}
	}

	public static void checkAndRunJobInstance(String jobName)
			throws NoSuchJobException, JobInstanceAlreadyExistsException, JobParametersInvalidException,
			JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
			NoSuchJobExecutionException {
		
		logger.info("Getting last job instance for \"{}\" job.", jobName);
		JobInstance instance = explorer.getLastJobInstance(jobName);

		if (instance == null) {
			createAndRunNewJobInstance(jobName);
		} else {
			logger.info("Found an instance for \"{}\" job with parameters = {}", jobName,
					instance.toString());
			JobExecution exec = explorer.getLastJobExecution(instance);
			if (exec.getStatus().equals(BatchStatus.FAILED)) {
				long result = operator.restart(exec.getId());
				logger.info("Exit status  -> {}", explorer.getJobExecution(result).getExitStatus());
			} else {
				createAndRunNewJobInstance(jobName);
			}
		}
	}

	public static void createAndRunNewJobInstance(String jobName)
			throws NoSuchJobException, JobInstanceAlreadyExistsException, JobParametersInvalidException,
			JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

		JobParametersBuilder builder = new JobParametersBuilder();
		builder.addString("JobId", Long.toString(System.currentTimeMillis()));
		String parameters = builder.toJobParameters().toString();
		long result = operator.start(ORDER_PROCESS_JOB_NAME, parameters);
		logger.info("Exit status  -> {}", explorer.getJobExecution(result).getExitStatus());
	}
}
