package com.github.intrigus.ftd.serial;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.intrigus.ftd.serial.SerialDisoveryMessage.AddMessage;
import com.github.intrigus.ftd.serial.SerialDisoveryMessage.ErrorMessage;
import com.github.intrigus.ftd.serial.SerialDisoveryMessage.ListMessage;
import com.github.intrigus.ftd.serial.SerialDisoveryMessage.QuitMessage;
import com.github.intrigus.ftd.serial.SerialDisoveryMessage.RemoveMessage;

/**
 * The serial-discovery process returns json responses. These are mapped to
 * specific classes here.
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "eventType")
@JsonSubTypes({ @Type(value = QuitMessage.class, name = "quit"), @Type(value = AddMessage.class, name = "add"),
		@Type(value = ListMessage.class, name = "list"), @Type(value = RemoveMessage.class, name = "remove"),
		@Type(value = ErrorMessage.class, name = "error") })
public abstract class SerialDisoveryMessage {

	protected static class AddMessage extends SerialDisoveryMessage {
		@JsonProperty(value = "port")
		protected SerialDevice port;
	}

	protected static class QuitMessage extends SerialDisoveryMessage {
		@JsonProperty(value = "message")
		private String message;
	}

	protected static class RemoveMessage extends SerialDisoveryMessage {
		@JsonProperty(value = "port")
		protected SerialDevice port;
	}

	protected static class ErrorMessage extends SerialDisoveryMessage {
		@JsonProperty(value = "message")
		protected String message;
	}

	protected static class ListMessage extends SerialDisoveryMessage {
		@JsonProperty(value = "ports")
		protected List<SerialDevice> ports;
	}
}
