package com.springtuts.integrationdemo.simplemessagetransfer;

public class CargoMessage {
	private final Cargo cargo;

	public CargoMessage(Cargo cargo) {
		this.cargo = cargo;
	}

	public Cargo getCargo() {
		return cargo;
	}

	@Override
	public String toString() {
		return cargo.toString();
	}
}
