package com.springtuts.springbatchdemo.jobdemo;

import org.springframework.batch.item.ItemProcessor;

public class OrderItemProcessor implements ItemProcessor<Order, Order> {

	@Override
	public Order process(Order item) throws Exception {
		System.out.println("Item with order details : " + item + " has been processed");
		return item;
	}

}
