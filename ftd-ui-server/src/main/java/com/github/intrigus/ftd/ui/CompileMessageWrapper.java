package com.github.intrigus.ftd.ui;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompileMessageWrapper extends MessageWrapper {

	@JsonProperty(value = "code")
	private String code;

	@JsonProperty(value = "serialPort")
	private String serialPort;

	public String getCode() {
		return code;
	}

	public String getSerialPort() {
		return serialPort;
	}

}
