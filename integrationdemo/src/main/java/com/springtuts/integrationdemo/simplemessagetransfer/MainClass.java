package com.springtuts.integrationdemo.simplemessagetransfer;

import com.springtuts.integrationdemo.simplemessagetransfer.Cargo.CargoBuilder;
import com.springtuts.integrationdemo.simplemessagetransfer.Cargo.ShippingType;

public class MainClass {
	public static void main(String[] args) {
		CargoBuilder cargoBuuilder = new CargoBuilder(1, "nayan", "malad east", 1000.0, ShippingType.DOMESTIC);
		cargoBuuilder.setDescription("handle with care");
		cargoBuuilder.setRegion(0);
		cargoBuuilder.setDeliveryDayCommitment(0);
		
		Cargo cargo = cargoBuuilder.build();
		
		System.out.println(cargo.getDeliveryDayCommitment());
	}
}
