package com.springtuts.springbatchdemo.jobdemo;

import org.springframework.batch.item.ItemProcessor;

public class OrderItemProcessor implements ItemProcessor<Order, Order> {

	public Order process(Order item) {
		System.out.println("Item with order details : " + item + " has been processed");
		return item;
	}

}
