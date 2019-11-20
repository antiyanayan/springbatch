package com.springtuts.batchsession.config;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.Assert;

@Configuration
@EnableBatchProcessing
@PropertySource("classpath:application.properties")
public class BaseConfig {

	@Autowired
	Environment env;
	
	@Bean 
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("database.driver"));
		dataSource.setUrl(env.getProperty("database.url"));
		dataSource.setUsername(env.getProperty("database.user"));
		dataSource.setPassword(env.getProperty("database.password"));

		return dataSource;
	}
	
	@Bean
	public PlatformTransactionManager txManager() {
		return new ResourcelessTransactionManager();
	}
	
	@Bean
	public JobRepository jobRepository(DataSource dataSource, PlatformTransactionManager txManager) throws Exception {
		Assert.state(dataSource != null, "No DataSource found. Please configure a datasource!");
		Assert.state(txManager != null, " No TransactionManager found. Please configure a transantionManager!");
		JobRepositoryFactoryBean bean = new  JobRepositoryFactoryBean();
		bean.setDataSource(dataSource);
		bean.setTransactionManager(txManager);
		return bean.getObject();
	}
	
	@Bean 
	public JobLauncher jobLauncher(JobRepository jobRepository) {
		Assert.state(jobRepository != null, "JobRepository is necessary for configuring an job launcherS");
		SimpleJobLauncher launcher = new SimpleJobLauncher();
		launcher.setJobRepository(jobRepository);
		return launcher;
	}
}
