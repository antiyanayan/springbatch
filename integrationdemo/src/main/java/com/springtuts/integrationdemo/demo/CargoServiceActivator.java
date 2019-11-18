package com.springtuts.integrationdemo.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;

@MessageEndpoint
public class CargoServiceActivator {
	 private final Logger logger = LoggerFactory.getLogger(CargoServiceActivator.class);
	 
	 @ServiceActivator(inputChannel = "cargoTransformerOutputChannel")
	 public void getCargo(CargoMessage message, @Header("CARGO_BATCH_ID") long batchId) {
		 logger.info(">>>>>>>>>>>>>>>>>>>>>>>>Message in Batch[{}] is received with payload : {}",batchId, message);
	 }
}
