package com.springtuts.batchsession.demojob;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.scheduling.config.TaskExecutorFactoryBean;

@Configuration
public class OrderProcessingJobConfig {

	@Autowired
	JobBuilderFactory jobs;

	@Autowired
	StepBuilderFactory steps;

	@Autowired
	DataSource dataSource;
	
	private static final String INPUT_DIR = "D:/Nayan/Logs/";
	private static final String INPUT_FILE = "orders.csv";
	private static final String FILE = INPUT_DIR + INPUT_FILE;

	@Bean
	public Job orderProcessingJob() {
		return jobs.get("orderProcessingJob")
				.start(step1())
				.build();
	}

	@Bean
	public Step step1() {
		return steps.get("step1")
				.<OrdersDto, Orders>chunk(10)
				.reader(reader())
				.faultTolerant()
				.skipPolicy(new OrdersFilterSkipPolicy())
				.processor(processor())
				.writer(writer())
				.build();	
	}

	@Bean
	@StepScope
	public FlatFileItemReader<OrdersDto> reader() {
		FlatFileItemReader<OrdersDto> fileReader = new FlatFileItemReader<>();
		
		DefaultLineMapper<OrdersDto> lineMapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(
				new String[] { "orderId", "productName", "supplier", "shippingAdd", "modeOfTransport", "isFragile" });
		tokenizer.setDelimiter(",");
		
		lineMapper.setLineTokenizer(tokenizer);
		lineMapper.setFieldSetMapper(new OrdersFieldsetMapper());
		
		fileReader.setResource(new FileSystemResource(FILE));
		fileReader.setLinesToSkip(1);
		fileReader.setStrict(false);
		fileReader.setLineMapper(lineMapper);
		return fileReader;
	}
	
	@Bean 
	@StepScope
	public ItemProcessor<OrdersDto, Orders> processor(){
		return new OrdersProcessing();
	}
	
	@Bean
	@StepScope
	public ItemWriter<Orders> writer(){
		JdbcBatchItemWriter<Orders> writer = new JdbcBatchItemWriter<>();
		writer.setDataSource(dataSource);
		writer.setSql("INSERT into N_ORDERS (orderId, productName, supplier, shippingAdd, modeOfTransport, isFragile) VALUES (:orderId, :productName, :supplier, :shippingAdd, :modeOfTransport, :isFragile)");
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Orders>());
		return writer;
	}
	
	@Bean
	@StepScope
	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
		executor.setConcurrencyLimit(5);
		return executor;
	}
}
