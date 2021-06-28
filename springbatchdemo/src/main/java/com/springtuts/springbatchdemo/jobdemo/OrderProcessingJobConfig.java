package com.springtuts.springbatchdemo.jobdemo;

import java.io.FileNotFoundException;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;


@Configuration
public class OrderProcessingJobConfig {

	@Autowired
	JobBuilderFactory jobBuilderFactory;

	@Autowired
	StepBuilderFactory stepBuilderFactory;

	@Autowired
	DataSource dataSource;
	
	@Autowired
	JobRegistry jobRegistry;

	private static final String INPUT_DIR = "D:/Nayan/Logs/";
	private static final String FILE_NAME = "orders.csv";
	private static final String FILE_PATH = INPUT_DIR + FILE_NAME;

	@Bean(name = "orderProcessJob")
	public Job orderProcessJob() {
		return jobBuilderFactory.get("orderProcessJob")
				.start(step1()).build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<Order, Order>chunk(5)
				.reader(reader())
				.writer(writer())
				.faultTolerant()
				.retryPolicy(new OrderProcessRetryPolicy())
				.skipLimit(2)
				.skip(FlatFileParseException.class)
				.noSkip(FileNotFoundException.class)
				.build();
	}

	@Bean
	@StepScope
	public SkipPolicy skipPolicy() {
		return new OrderSkipPolicy();
	}

	@Bean
	@StepScope
	public FlatFileItemReader<Order> reader() {
		FlatFileItemReader<Order> reader = new FlatFileItemReader<>();
		reader.setResource(new FileSystemResource(FILE_PATH));

		DefaultLineMapper<Order> defaultLineMapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[] { "orderId", "item", "supplier", "address", "price" });
		tokenizer.setDelimiter(";");
		defaultLineMapper.setLineTokenizer(tokenizer);
		defaultLineMapper.setFieldSetMapper(new OrderFieldSetMapper());

		reader.setLinesToSkip(1);
		reader.setLineMapper(defaultLineMapper);
		reader.setStrict(false);

		return reader;

	}

	@Bean
	public JdbcBatchItemWriter<Order> writer() {
		JdbcBatchItemWriter<Order> writer = new JdbcBatchItemWriter<>();
		writer.setDataSource(dataSource);
		writer.setSql(
				"INSERT into ORDERS (orderId, item, address, supplier, price) VALUES (:orderId, :item, :address, :supplier, :price)");
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Order>());
		return writer;
	}
	
	@Bean
	public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {
		JobRegistryBeanPostProcessor beanPostProcessor = new JobRegistryBeanPostProcessor();
		beanPostProcessor.setJobRegistry(jobRegistry);
		return beanPostProcessor;
	}
	
}
