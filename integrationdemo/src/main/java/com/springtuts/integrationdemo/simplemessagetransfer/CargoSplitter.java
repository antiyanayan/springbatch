package com.springtuts.integrationdemo.simplemessagetransfer; 

import java.util.List;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Splitter;
import org.springframework.messaging.Message;

@MessageEndpoint
public class CargoSplitter {

	
	@Splitter(inputChannel = "cargoDefaultRequestChannel", outputChannel = "cargoSplitterOutputChannel")
	public List<Cargo> splitCargoLit(Message<List<Cargo>> message){
		return message.getPayload();
	}
	
}
