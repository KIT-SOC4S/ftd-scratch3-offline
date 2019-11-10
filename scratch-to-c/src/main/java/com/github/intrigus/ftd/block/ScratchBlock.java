package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.github.intrigus.ftd.util.RelationShip;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "opcode")
@JsonTypeIdResolver(ScratchBlockResolver.class)
public abstract class ScratchBlock implements RelationShip {
	@JsonProperty(value = "opcode")
	public String opcode;
	@JsonProperty(value = "next")
	private String next_;
	public ScratchBlock next;
	@JsonProperty(value = "parent")
	private String parent_;
	public ScratchBlock parent;
	public boolean shadow;
	public boolean topLevel;

	@JsonProperty(value = "x")
	private double x;

	@JsonProperty(value = "y")
	private double y;

	public void updateRelations(Map<String, ScratchBlock> blocks) {
		this.next = blocks.get(next_);
		this.parent = blocks.get(parent_);
		// if (fields != null) {
		// fields.updateRelations(blocks);
		// }
		updateOtherRelations(blocks);
	}

	protected abstract void updateOtherRelations(Map<String, ScratchBlock> blocks);

	public abstract BlockType getBlockType();

	protected String beginGen() {
		return "";
	}

	protected String afterGen() {
		String code = "";
		if (this.next != null) {
			code += next.generateCode();
		}
		return code;
	}

	public String generateCode() {
		return beginGen() + gen() + afterGen();
	}

	protected abstract String gen();

	public static enum BlockType {
		HAT, REPORTER, BOOLEAN, C, STACK, CUSTOM_DEF, CUSTOM_HAT
	}
}
