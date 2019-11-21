package com.springtuts.integrationdemo.simplemessagetransfer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.MessageChannel;

@Configuration
public class ChannelConfiguration {

	@Bean
	public MessageChannel cargoDefaultRequestChannel() {
		return new DirectChannel();
	}
	

	@Bean
	public MessageChannel cargoDefaultReplyChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel cargoSplitterOutputChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel cargoFilterOutputChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel cargoFilterDiscardChannel() {
		return new DirectChannel();
	}
	
	@Bean
	public MessageChannel cargoRouterDomesticOutputChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel cargoRouterInternationalOutputChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel cargoTransformerOutputChannel() {
		return new DirectChannel();
	}
	
	
}
