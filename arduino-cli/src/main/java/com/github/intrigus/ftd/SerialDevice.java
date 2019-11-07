package com.github.intrigus.ftd;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SerialDevice {
	@JsonProperty(value = "address")
	private String address;

	@JsonProperty(value = "boards")
	private List<FtduinoBoard> boards;

	/**
	 * The address the board is connected to. E.g. /dev/tty* on Linux and macOS,
	 * COM3 on Windows.
	 * 
	 * @return the address the board is connected to
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * The possible boards this device represents. Boards are identified via USB's
	 * VID and PID, but the same VID & PID can be used for different boards. E.g.
	 * The "normal" ftduino has the same ids as the ftduino with WebUSB support.
	 * 
	 * @return the possible boards this device represents
	 */
	public List<FtduinoBoard> getBoards() {
		return boards;
	}

	public static class FtduinoBoard {
		@JsonProperty(value = "name")
		private String name;
		@JsonProperty(value = "FQBN")
		private String FQBN;

		/**
		 * 
		 * @return the name of the board
		 */
		public String getName() {
			return name;
		}

		/**
		 * Returns the FQBN. The FQBN identifies a board. E.g. ftduino:avr:ftduino for
		 * the "normal" ftduino and ftduino:avr:ftduinowebusb for the WebUSB enabled
		 * ftduino.
		 * 
		 * @return the FQBN
		 */
		public String getFQBN() {
			return FQBN;
		}
	}

}
