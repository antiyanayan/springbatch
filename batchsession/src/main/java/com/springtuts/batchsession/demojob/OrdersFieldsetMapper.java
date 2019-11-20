package com.springtuts.batchsession.demojob;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class OrdersFieldsetMapper implements FieldSetMapper<OrdersDto> {

	@Override
	public OrdersDto mapFieldSet(FieldSet fieldSet) throws BindException {
		OrdersDto dto = new OrdersDto();
		dto.setOrderid(fieldSet.readString("orderId"));
		dto.setProductName(fieldSet.readString("productName"));
		dto.setShippingAdd(fieldSet.readString("shippingAdd"));
		dto.setModeOfTransport(fieldSet.readString("modeOfTransport"));
		dto.setIsFragile(fieldSet.readString("isFragile"));
		dto.setSupplier(fieldSet.readString("supplier"));
		
		return dto;
	}

}
