package com.springtuts.batchsession.demojob;

public class OrdersDto {
	private String orderId;
	private String productName;
	private String supplier;
	private String shippingAdd;
	private String modeOfTransport;
	private String isFragile;

	public String getOrderid() {
		return orderId;
	}

	public String getProductName() {
		return productName;
	}

	public String getSupplier() {
		return supplier;
	}

	public String getShippingAdd() {
		return shippingAdd;
	}

	public String getModeOfTransport() {
		return modeOfTransport;
	}

	public String getIsFragile() {
		return isFragile;
	}

	public void setOrderid(String orderid) {
		this.orderId = orderid;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public void setShippingAdd(String shippingAdd) {
		this.shippingAdd = shippingAdd;
	}

	public void setModeOfTransport(String modeOfTransport) {
		this.modeOfTransport = modeOfTransport;
	}

	public void setIsFragile(String isFragile) {
		this.isFragile = isFragile;
	}

	@Override
	public String toString() {
		return "Orders [orderid=" + orderId + ", productName=" + productName + ", supplier=" + supplier
				+ ", shippingAdd=" + shippingAdd + ", modeOfTransport=" + modeOfTransport + ", isFragile=" + isFragile
				+ "]";
	}

}
