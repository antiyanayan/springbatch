package com.springtuts.integrationdemo.demo;

import java.util.List;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

@MessagingGateway(name = "cargoGateway", defaultRequestChannel = "cargoDefaultChannel")
public interface ICargoGeteway {

	@Gateway
	void processCargoRequest(Message<List<Cargo>> message);
}
