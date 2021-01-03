package com.springtuts.integrationdemo.simplemessagetransfer;

import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.MessageEndpoint;

@MessageEndpoint
public class CargoFilter {

	
	private static final long CARGO_WEIGHT_LIMIT = 1_000;
	
	@Filter(inputChannel = "cargoSplitterOutputChannel", outputChannel = "cargoFilterOutputChannel",  discardChannel = "cargoFilterDiscardChannel")
	public boolean filterIfCargoWeightExceeds(Cargo cargo) {
		return cargo.getWeight() <= CARGO_WEIGHT_LIMIT;
	}
	
}

