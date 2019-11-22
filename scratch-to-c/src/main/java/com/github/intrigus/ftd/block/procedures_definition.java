package com.github.intrigus.ftd.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.ScratchValue;
import com.github.intrigus.ftd.util.StringUtil;

/**
 * Implements the scratch block that users can use to define their own
 * functions.
 */
@JsonIgnoreProperties(value = "fields")
public class procedures_definition extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "custom_block")
		private ScratchValue subStack;
	}

	@Override
	protected String beginGen() {
		procedures_prototype procBlock = (procedures_prototype) inputs.subStack.getBlock();
		List<String> argumentNames = procBlock.mutation.argumentnames;
		String code = "void " + inputs.subStack.generateCode() + " (" + argumentsToCFunctionParameters(argumentNames)
				+ ") {\n";
		return code;
	}

	/**
	 * Converts the given argument names to a String that is compilable by a C
	 * compiler. Duplicate argument names are silently ignored just like Scratch
	 * does.
	 */
	private static String argumentsToCFunctionParameters(List<String> args) {
		// Scratch allows self-defined functions to have the same argument names.
		// I.e. foo(ScratchValue b, ScratchValue b, ScratchValue b)
		// This is obviously not allowed in C.
		// That's why we only use the first occurrence of a duplicate argument name.
		// As usual we also have to convert the parameter names to a valid C identifier
		List<String> resolvedArgs = new ArrayList<>();
		List<String> resultArgs = new ArrayList<>();
		for (String arg : args) {
			if (!resolvedArgs.contains(arg)) {
				resultArgs.add("ScratchValue " + StringUtil.convertToCIdentifier(arg));
			} else {
				resultArgs.add("ScratchValue ");
			}
			resolvedArgs.add(arg);
		}
		return resultArgs.stream().collect(Collectors.joining(", "));
	}

	@Override
	public String gen() {
		return "";
	}

	@Override
	protected String afterGen() {
		return super.afterGen() + "}\n";
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.inputs.subStack.updateRelations(blocks);
	}

	@Override
	public BlockType getBlockType() {
		return BlockType.CUSTOM_HAT;
	}

	/**
	 * Returns the block that contains the prototype of this definition.
	 * 
	 * @return the block that contains the prototype of this definition
	 */
	protected procedures_prototype getPrototypeBlock() {
		return (procedures_prototype) this.inputs.subStack.getBlock();
	}
}
