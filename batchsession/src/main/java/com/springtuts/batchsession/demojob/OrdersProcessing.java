package com.springtuts.batchsession.demojob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.Assert;

public class OrdersProcessing implements ItemProcessor<OrdersDto, Orders> {

	private Logger logger = LoggerFactory.getLogger(OrdersProcessing.class);

	@Override
	public Orders process(OrdersDto item) throws Exception {
		Orders orders = new Orders();
		orders.setOrderId(Integer.parseInt(item.getOrderid()));
		orders.setIsFragile(Boolean.parseBoolean(item.getIsFragile()));
		orders.setModeOfTransport(item.getModeOfTransport());
		orders.setProductName(item.getProductName());
		orders.setShippingAdd(item.getShippingAdd());
		orders.setSupplier(item.getSupplier());
		return orders;
	}
}
