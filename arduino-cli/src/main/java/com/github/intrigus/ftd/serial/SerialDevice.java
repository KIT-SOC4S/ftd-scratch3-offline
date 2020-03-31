package com.github.intrigus.ftd.serial;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SerialDevice {
	@JsonProperty(value = "address")
	private String address;
	@JsonProperty(value = "prefs")
	private SerialDevice.Prefs prefs;

	private List<String> validVendorIds = List.of("0x1c40");
	private List<String> validProductIds = List.of("0x0537", "0x0538");

	public String getAddress() {
		return address;
	}

	public String getProductId() {
		return prefs.productId;
	}

	public String getVendorId() {
		return prefs.vendorId;
	}

	public boolean isFtduino() {
		boolean isFtduinoVendorId = validVendorIds.stream().anyMatch((it) -> it.equalsIgnoreCase(getVendorId()));
		boolean isFtduinoProductId = validProductIds.stream().anyMatch((it) -> it.equalsIgnoreCase(getProductId()));
		return isFtduinoVendorId && isFtduinoProductId;
	}

	@Override
	public String toString() {
		return "SerialDevice [address=" + address + ", prefs=" + prefs + "]";
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class Prefs {
		@JsonProperty(value = "productId")
		private String productId;
		@JsonProperty(value = "vendorId")
		private String vendorId;

		@Override
		public String toString() {
			return "Prefs [productId=" + productId + ", vendorId=" + vendorId + "]";
		}

	}
}