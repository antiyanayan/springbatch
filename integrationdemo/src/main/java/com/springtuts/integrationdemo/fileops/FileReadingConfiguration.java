package com.springtuts.integrationdemo.fileops;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

// No need to call any methods

@Configuration
public class FileReadingConfiguration {

	private static final String INPUT_DIR = "D:/Nayan/Logs";
	private static final String OUTPUT_DIR = "D:/Nayan/Logs/temp";
	private static final String FILE_PATTERN = "output_*.csv";

	@Bean
	public MessageChannel defaultRequestChannel() {
		return new DirectChannel();
	}
	
	@Bean
	@InboundChannelAdapter(value = "defaultRequestChannel", poller = @Poller(fixedDelay = "1000"))
	public MessageSource<File> fileMessageSource() {
		FileReadingMessageSource fileSource = new FileReadingMessageSource();
		fileSource.setDirectory(new File(INPUT_DIR));
		fileSource.setFilter(new SimplePatternFileListFilter(FILE_PATTERN));
		return fileSource;
	}

	@Bean
	@ServiceActivator(inputChannel = "defaultRequestChannel")
	public MessageHandler fileWriteMessageHandler() {
		FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(OUTPUT_DIR));
		handler.setFileExistsMode(FileExistsMode.REPLACE);
		handler.setExpectReply(false);
		handler.setDeleteSourceFiles(true);
		return handler;
	}
}
