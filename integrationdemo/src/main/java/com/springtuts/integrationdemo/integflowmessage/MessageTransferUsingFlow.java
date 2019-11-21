package com.springtuts.integrationdemo.integflowmessage;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.annotation.SplitterAnnotationPostProcessor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

@Configuration
public class MessageTransferUsingFlow {

	@Bean
	public MessageChannel requestChannel() {
		return new DirectChannel();
	}
	
	@Bean
	public MessageChannel replyChannel() {
		return new DirectChannel();
	}
	
	@MessagingGateway
	public interface MyGateway {
		
		@Gateway(requestChannel = "requestChannel", replyChannel = "replyChannel")
		String send(Message<List<Orders>> message);
	}
	
}
