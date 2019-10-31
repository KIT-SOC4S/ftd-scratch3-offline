package com.github.intrigus.ftd.ui;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnswerMessageWrapper extends MessageWrapper {
	@JsonProperty(value = "status")
	private Status status;

	@JsonProperty(value = "errorMessage")
	private String errorMessage;

	@JsonProperty(value = "result")
	private String result;

	public AnswerMessageWrapper(Status status, String errorMessage, String result) {
		this.status = status;
		this.errorMessage = errorMessage;
		this.result = result;
	}
}
