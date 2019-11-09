package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Implements the scratch when flag clicked operator i.e. the when green flag
 * clicked hat. Since there are no graphics on the arduino the program will
 * automatically start after setup and not wait for any user action.
 */
@JsonIgnoreProperties(value = { "fields", "inputs" })
public class event_whenflagclicked extends ScratchBlock {

	@Override
	protected String beginGen() {
		return "void loop() {\n";
	}

	@Override
	protected String afterGen() {
		return super.afterGen() + "}\n";
	}

	public String gen() {
		return "";
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
	}
}
