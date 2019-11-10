package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Implements the scratch block that calls user defined functions.
 */
@JsonIgnoreProperties(value = { "fields", "inputs" })
public class procedures_call extends ScratchBlock {

	@JsonProperty(value = "mutation")
	private Mutation mutation;

	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class Mutation {
		@JsonProperty(value = "proccode")
		public String proccode;
	}

	public String gen() {
		String code = mutation.proccode + "();\n";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
	}

	@Override
	public BlockType getBlockType() {
		return BlockType.STACK;
	}
}
