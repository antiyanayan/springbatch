package com.springtuts.integrationdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.MessageChannel;

@Configuration
@ComponentScan("com.springtuts.integrationdemo")
@EnableIntegration
@IntegrationComponentScan("com.springtuts.integrationdemo.demo")
public class IntegrationConfiguration {

	@Bean
	public MessageChannel cargoDefaultChannel() {
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
