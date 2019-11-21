package com.springtuts.springbatchdemo.config;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

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
	
	/*
	 * @Bean public PlatformTransactionManager transactionManager() { return new
	 * ResourcelessTransactionManager(); }
	 * 
	 * @Bean public JobRepository jobRepository() throws Exception {
	 * JobRepositoryFactoryBean jobRepositoryFactoryBean = new
	 * JobRepositoryFactoryBean();
	 * jobRepositoryFactoryBean.setDataSource(dataSource());
	 * jobRepositoryFactoryBean.setTransactionManager(transactionManager());
	 * jobRepositoryFactoryBean.afterPropertiesSet(); return
	 * jobRepositoryFactoryBean.getObject(); }
	 */
}
