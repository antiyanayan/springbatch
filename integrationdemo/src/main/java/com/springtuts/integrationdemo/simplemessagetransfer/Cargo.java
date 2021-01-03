package com.springtuts.integrationdemo.simplemessagetransfer;

public class Cargo {

	public enum ShippingType {
		DOMESTIC, INTERNATIONAL
	}

	private long trackingId;
	private String receiverName;
	private String deliveryAddress;
	private double weight;
	private String description;
	private ShippingType shippingType;
	private int deliveryDayCommitment;
	private int region;

	private Cargo(CargoBuilder cargoBuilder) {
		super();
		this.trackingId = cargoBuilder.trackingId;
		this.receiverName = cargoBuilder.receiverName;
		this.deliveryAddress = cargoBuilder.deliveryAddress;
		this.weight = cargoBuilder.weight;
		this.description = cargoBuilder.description;
		this.shippingType = cargoBuilder.shippingType;
		this.deliveryDayCommitment = cargoBuilder.deliveryDayCommitment;
		this.region = cargoBuilder.region;
	}

	public long getTrackingId() {
		return trackingId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public double getWeight() {
		return weight;
	}

	public String getDescription() {
		return description;
	}

	public ShippingType getShippingType() {
		return shippingType;
	}

	public int getDeliveryDayCommitment() {
		return deliveryDayCommitment;
	}

	public int getRegion() {
		return region;
	}

	public void setTrackingId(long trackingId) {
		this.trackingId = trackingId;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setShippingType(ShippingType shippingType) {
		this.shippingType = shippingType;
	}

	public void setDeliveryDayCommitment(int deliveryDayCommitment) {
		this.deliveryDayCommitment = deliveryDayCommitment;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	@Override
	public String toString() {
		return "Cargo [trackingId=" + trackingId + ", receiverName=" + receiverName + ", deliveryAddress="
				+ deliveryAddress + ", weight=" + weight + ", description=" + description + ", shippingType="
				+ shippingType + ", deliveryDayCommitment=" + deliveryDayCommitment + ", region=" + region + "]";
	}

	public static class CargoBuilder {
		private final long trackingId;
		private final String receiverName;
		private final String deliveryAddress;
		private final double weight;
		private final ShippingType shippingType;
		private int deliveryDayCommitment;
		private int region;
		private String description;

		public CargoBuilder(long trackingId, String receiverName, String deliveryAddress, double weight,
				ShippingType shippingType) {
			super();
			this.trackingId = trackingId;
			this.receiverName = receiverName;
			this.deliveryAddress = deliveryAddress;
			this.weight = weight;
			this.shippingType = shippingType;
		}

		public CargoBuilder setDeliveryDayCommitment(int deliveryDayCommitment) {
			this.deliveryDayCommitment = deliveryDayCommitment;
			return this;
		}

		public CargoBuilder setDescription(String description) {
			this.description = description;
			return this;
		}

		public CargoBuilder setRegion(int region) {
			this.region = region;
			return this;
		}

		public Cargo build() {
			Cargo cargo = new Cargo(this);
			if ((ShippingType.DOMESTIC == cargo.getShippingType())
					&& (cargo.getRegion() <= 0 || cargo.getRegion() > 4)) {
				throw new IllegalStateException("Region is invalid! Cargo Tracking Id : " + cargo.getTrackingId());
			}

			return cargo;
		}
	}

}
