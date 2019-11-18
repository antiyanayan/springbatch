package com.springtuts.integrationdemo.demo;

import com.springtuts.integrationdemo.demo.Cargo.CargoBuilder;
import com.springtuts.integrationdemo.demo.Cargo.ShippingType;

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
