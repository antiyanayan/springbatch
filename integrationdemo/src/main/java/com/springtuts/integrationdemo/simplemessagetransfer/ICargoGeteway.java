package com.springtuts.integrationdemo.simplemessagetransfer;

import java.util.List;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

@MessagingGateway(name = "cargoGateway", defaultRequestChannel = "cargoDefaultRequestChannel", defaultReplyChannel = "cargoDefaultReplyChannel")
public interface ICargoGeteway {

	@Gateway
	String processCargoRequest(Message<List<Cargo>> message);
}
