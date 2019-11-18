package com.springtuts.integrationdemo;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.integration.support.MessageBuilder;

import com.springtuts.integrationdemo.demo.Cargo;
import com.springtuts.integrationdemo.demo.Cargo.ShippingType;
import com.springtuts.integrationdemo.demo.ICargoGeteway;

@SpringBootApplication
public class IntegrationdemoApplication {

	private static Logger logger = LoggerFactory.getLogger(IntegrationdemoApplication.class);

	public static void main(String[] args) {
		logger.info(">>>>>>>>>>>>>>>>> Starting spring integration application. Please have patience");
		ConfigurableApplicationContext ctx = SpringApplication.run(IntegrationdemoApplication.class, args);
		logger.info(">>>>>>>>>>>>>>>>> Application started");

		ICargoGeteway orderGateway = ctx.getBean(ICargoGeteway.class);

		getCargoBatchMap().forEach((batchId, cargoList) -> orderGateway.processCargoRequest(
				MessageBuilder.withPayload(cargoList).setHeader("CARGO_BATCH_ID", batchId).build()));
		
		Map<Integer, List<Cargo>> result = getCargoBatchMap();
	}

	private static Map<Integer, List<Cargo>> getCargoBatchMap() {
		Map<Integer, List<Cargo>> cargoBatchMap = new HashMap<>();

		cargoBatchMap.put(1, Arrays.asList(

				new Cargo.CargoBuilder(1, "Nayan Antiya", "Malad East, Mumbai", 0.5, ShippingType.DOMESTIC).setRegion(1)
						.setDescription("Radio").build(),
				new Cargo.CargoBuilder(2, "Raj shah", "Dallas, India", 2_000, ShippingType.INTERNATIONAL)
						.setDeliveryDayCommitment(3).setDescription("Furniture").build(),
				new Cargo.CargoBuilder(3, "Tom Jerry", "Atlanta, USA", 5, ShippingType.INTERNATIONAL)
						.setDeliveryDayCommitment(2).setDescription("TV").build(),
				new Cargo.CargoBuilder(4, "Holly Holly", "Texas, USA", 8, null).setDeliveryDayCommitment(2)
						.setDescription("Chair").build()));

		cargoBatchMap.put(2, Arrays.asList(

				new Cargo.CargoBuilder(5, "Ketan Shah", "Mira Road, Mumbai", 1_200, ShippingType.DOMESTIC).setRegion(2)
						.setDescription("Refrigerator").build(),
				new Cargo.CargoBuilder(6, "Dharak Patel", "Andheri, Mumbai", 20, ShippingType.DOMESTIC).setRegion(3)
						.setDescription("Table").build(),
				new Cargo.CargoBuilder(7, "Swapnit Antiya", "Palanpur, Gujarat", 5, null).setDeliveryDayCommitment(1)
						.setDescription("TV").build()));

		cargoBatchMap.put(3,
				Arrays.asList(
						new Cargo.CargoBuilder(8, "Receiver_Name8", "Address8", 200, ShippingType.DOMESTIC).setRegion(2)
								.setDescription("Washing Machine").build(),
						new Cargo.CargoBuilder(9, "Receiver_Name9", "Address9", 4.75, ShippingType.INTERNATIONAL)
								.setDeliveryDayCommitment(1).setDescription("Document").build()));

		return Collections.unmodifiableMap(cargoBatchMap);
	}

}
