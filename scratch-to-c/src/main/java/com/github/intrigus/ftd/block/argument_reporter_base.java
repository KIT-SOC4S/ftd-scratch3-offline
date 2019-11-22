package com.github.intrigus.ftd.block;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.util.StringUtil;

/**
 * Scratch allows the user to use arguments in their self-defined functions.
 * Consider this example:
 * 
 * <pre>
 * {@code
 * foo(ScratchValue first, ScratchValue second) {
 *     wait(first);
 *     wait(second);
 *     wait(first);
 * }
 * </pre>
 * 
 * Each wait invocation will then have an argument_reporter_base block as input.
 * Such a block will either return the name of its argument, so in our case
 * either "first" or "second" or return a value that represents `0` iff the
 * function definition doesn't contain a matching argument name.
 */
@JsonIgnoreProperties(value = { "inputs" })
public class argument_reporter_base extends ScratchBlock {
	@JsonProperty(value = "fields")
	private Field fields;

	private static class Field {
		@JsonProperty(value = "VALUE")
		private Value value;

		private static class Value {
			private String argumentName;

			@JsonCreator()
			private Value(@JsonProperty(index = 1) List<Object> values) {
				if (values == null)
					throw new IllegalStateException("unexpected");
				if (values.size() < 2) {
					throw new IllegalStateException("unexpected");
				}
				if (!(values.get(0) instanceof String)) {
					throw new IllegalStateException("unexpected");
				}
				argumentName = (String) values.get(0);
			}
		}
	}

	private procedures_prototype getPrototype() {
		// find the parents of this block until there is no parent.
		// the reduce call will get us the last such block
		// this will be a procedures_definition block
		// use it to get the prototype block
		procedures_definition blockDef = (procedures_definition) Stream
				.iterate(parent, Objects::nonNull, (block) -> block.parent).reduce((a, b) -> b).get();
		return blockDef.getPrototypeBlock();
	}

	public String gen() {
		procedures_prototype prototype = getPrototype();
		/*
		 * in Scratch it is possible to use arguments that are not actually defined as
		 * argument of the function. I.e. foo(ScratchValue bar) { do(not_defined_arg); }
		 * Scratch in these cases uses `0` as default value.
		 */
		if (prototype.mutation.argumentnames.contains(fields.value.argumentName)) {
			return StringUtil.convertToCIdentifier(fields.value.argumentName);
		} else {
			return "scratchNumber(0)";
		}
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
	}

	@Override
	public BlockType getBlockType() {
		return BlockType.INTERNAL;
	}
}
