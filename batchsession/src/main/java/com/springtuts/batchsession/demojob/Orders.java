package com.springtuts.batchsession.demojob;

public class Orders {
	private int orderId;
	private String productName;
	private String supplier;
	private String shippingAdd;
	private String modeOfTransport;
	private boolean isFragile;

	public int getOrderId() {
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

	public boolean getIsFragile() {
		return isFragile;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
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

	public void setIsFragile(boolean isFragile) {
		this.isFragile = isFragile;
	}

	@Override
	public String toString() {
		return "Orders [orderId=" + orderId + ", productName=" + productName + ", supplier=" + supplier
				+ ", shippingAdd=" + shippingAdd + ", modeOfTransport=" + modeOfTransport + ", isFragile=" + isFragile
				+ "]";
	}

}
