package com.springtuts.springbatchdemo.config;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@EnableBatchProcessing
@PropertySource("classpath:application.properties")
@ComponentScan("com.springtuts.springbatchdemo")
public class BaseConfiguration {

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
	public JobExplorer jobExplorer() throws Exception {
		JobExplorerFactoryBean bean = new JobExplorerFactoryBean();
		bean.setDataSource(dataSource());
		bean.setTablePrefix("BATCH_");
		bean.afterPropertiesSet();
		return bean.getObject();
	}
	
	@Bean
	public JobOperator jobOperator(JobRegistry jobRegistry, JobLauncher jobLauncher, JobRepository jobRepository) throws Exception {
		SimpleJobOperator simpleJobOperator = new SimpleJobOperator();
		simpleJobOperator.setJobExplorer(jobExplorer());
		simpleJobOperator.setJobLauncher(jobLauncher);
		simpleJobOperator.setJobRepository(jobRepository);
		simpleJobOperator.setJobRegistry(jobRegistry);
		simpleJobOperator.afterPropertiesSet();
		return simpleJobOperator;
	}
}
