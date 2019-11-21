package com.springtuts.springbatchdemo.jobdemo;

public class Order {
	private int orderId;
	private String item;
	private String address;
	private String supplier;
	private double price;

	public int getOrderId() {
		return orderId;
	}

	public String getItem() {
		return item;
	}

	public String getAddress() {
		return address;
	}

	public String getSupplier() {
		return supplier;
	}

	public double getPrice() {
		return price;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", item=" + item + ", address=" + address + ", supplier=" + supplier
				+ ", price=" + price + "]";
	}
}
