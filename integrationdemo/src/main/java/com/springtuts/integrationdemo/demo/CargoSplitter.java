package com.springtuts.integrationdemo.demo; 

import java.util.List;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Splitter;
import org.springframework.messaging.Message;

@MessageEndpoint
public class CargoSplitter {

	
	@Splitter(inputChannel = "cargoDefaultChannel", outputChannel = "cargoSplitterOutputChannel")
	public List<Cargo> splitCargoLit(Message<List<Cargo>> message){
		return message.getPayload();
	}
	
}
