package com.springtuts.batchsession.demojob;

import java.util.List;

import org.springframework.batch.item.ItemWriter;


public class OrderWriter implements ItemWriter<Orders> {

	@Override
	public void write(List<? extends Orders> items) throws Exception {
		
		items.forEach(order -> System.out.println(order));
	}

}
