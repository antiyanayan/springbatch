package com.springtuts.integrationdemo.simplemessagetransfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;

@MessageEndpoint
public class DiscardCargoMessageListener {


	Logger logger = LoggerFactory.getLogger(DiscardCargoMessageListener.class);
	
	
	@ServiceActivator(inputChannel = "cargoFilterDiscardChannel")
	public void handleDiscardedCargo(Cargo cargo,  @Header("CARGO_BATCH_ID") long batchId) {

		logger.info(">>>>>>>>>>>>>>>>>>>>>>Message in Batch [{}] is received with Discarded payload : {}", batchId, cargo);
	}
}
