package com.github.intrigus.ftd.field;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.intrigus.ftd.block.ScratchBlock;

@JsonIgnoreProperties(value = { "x", "y" })
public abstract class ScratchField {
	public abstract void updateRelations(Map<String, ScratchBlock> blocks);

	public abstract String toString();

	public abstract String generateCode();
}
