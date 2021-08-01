package com.techvirus.springbatchadmin.jobdemo;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class OrderFieldSetMapper implements FieldSetMapper<Order> {

	public Order mapFieldSet(FieldSet fieldSet) throws BindException {
	
		Order order = new Order();
		order.setOrderId(fieldSet.readInt("orderId"));
		order.setItem(fieldSet.readString("item"));
		order.setSupplier(fieldSet.readString("supplier"));
		order.setAddress(fieldSet.readString("address"));
		order.setPrice(fieldSet.readDouble("price"));
		return order;
	}

}
