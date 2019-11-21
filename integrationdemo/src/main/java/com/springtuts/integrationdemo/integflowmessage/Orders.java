package com.springtuts.integrationdemo.integflowmessage;

public class Orders {
	private int orderid;
	private String productName;
	private String supplier;
	private String shippingAdd;
	private String modeOfTransport;
	private boolean isFragile;

	public int getOrderid() {
		return orderid;
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

	public boolean isFragile() {
		return isFragile;
	}

	public void setOrderid(int orderid) {
		this.orderid = orderid;
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

	public void setFragile(boolean isFragile) {
		this.isFragile = isFragile;
	}

	@Override
	public String toString() {
		return "Orders [orderid=" + orderid + ", productName=" + productName + ", supplier=" + supplier
				+ ", shippingAdd=" + shippingAdd + ", modeOfTransport=" + modeOfTransport + ", isFragile=" + isFragile
				+ "]";
	}

}
