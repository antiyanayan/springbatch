package com.springtuts.springbatchdemo.jobdemo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import com.springtuts.springbatchdemo.App;

public class WriteToConsole implements ItemWriter<Order> {

	private static final Logger logger = LoggerFactory.getLogger(WriteToConsole.class);
	
	@Override
	public void write(List<? extends Order> items) throws Exception {
		for(Order o : items) {
			logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> An order has been processed : {}", o.toString());
		}
		
	}

}
