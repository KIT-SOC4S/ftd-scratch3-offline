package com.github.intrigus.ftd.field;

import java.util.Map;

import com.github.intrigus.ftd.block.ScratchBlock;

public abstract class ScratchField {
	public abstract void updateRelations(Map<String, ScratchBlock> blocks);

	public abstract String toString();
}
