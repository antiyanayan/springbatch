package com.springtuts.integrationdemo.simplemessagetransfer;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Router;

import com.springtuts.integrationdemo.simplemessagetransfer.Cargo.ShippingType;

@MessageEndpoint
public class CargoRouter {

	@Router(inputChannel = "cargoFilterOutputChannel")
	public String routrCargo(Cargo cargo) {
		if(cargo.getShippingType() == ShippingType.INTERNATIONAL)
			return "cargoRouterInternationalOutputChannel";
		else if(cargo.getShippingType() == ShippingType.DOMESTIC)
			return "cargoRouterDomesticOutputChannel";	
		return "nullChannel";
	}
	
}
