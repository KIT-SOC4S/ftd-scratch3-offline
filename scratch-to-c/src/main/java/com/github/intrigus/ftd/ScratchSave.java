package com.github.intrigus.ftd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//TODO Documentation
@JsonIgnoreProperties({ "monitors", "extensions", "meta" }) // TODO
public class ScratchSave {

	@JsonProperty("targets")
	private ScratchTarget[] targets;

	public ScratchBlocks getBlocks() {
		return targets[1].getBlocks();
	}

}
