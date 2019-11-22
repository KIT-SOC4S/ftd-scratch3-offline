package com.github.intrigus.ftd.block;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.intrigus.ftd.ScratchValue;
import com.github.intrigus.ftd.util.RelationShip;
import com.github.intrigus.ftd.util.StringUtil;

/**
 * Implements the scratch block that calls user defined functions.
 */
@JsonIgnoreProperties(value = { "fields" })
public class procedures_call extends ScratchBlock {

	@JsonProperty(value = "mutation")
	private Mutation mutation;

	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class Mutation {
		@JsonCreator()
		private Mutation(@JsonProperty(index = 1) Map<String, Object> values)
				throws JsonParseException, JsonMappingException, IOException {
			ObjectMapper mapper = new ObjectMapper();
			Objects.requireNonNull(values, "Values must not be null");

			Object proccode = values.get("proccode");
			Objects.requireNonNull(proccode, "proccode must not be null");
			if (proccode instanceof String) {
				this.proccode = (String) proccode;
			} else {
				throw new IllegalStateException("proccode must be instanceof of String!");
			}

			Object argumentIds = values.get("argumentids");
			Objects.requireNonNull(argumentIds, "argumentIds must not be null");
			if (argumentIds instanceof String) {
				this._argumentIds = mapper.readValue((String) argumentIds,
						TypeFactory.defaultInstance().constructCollectionType(List.class, String.class));
			} else {
				throw new IllegalStateException("argumentIds must be instanceof of String!");
			}
		}

		private String proccode;

		private List<String> _argumentIds;
	}

	@JsonProperty(value = "inputs")
	private Input inputs;

	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class Input implements RelationShip {
		private Map<String, ScratchValue> inputs = new HashMap<>();

		@JsonAnySetter
		void setBlock(String key, ScratchValue value) {
			inputs.put(key, value);
		}

		@Override
		public void updateRelations(Map<String, ScratchBlock> blocks) {
			inputs.forEach((a, b) -> b.updateRelations(blocks));
		}
	}

	public String gen() {
		String code = StringUtil.convertToCIdentifier(mutation.proccode) + "(" + gatherFunctionArguments() + ");\n";
		return code;
	}

	/*
	 * Provides a String that contains all used arguments.
	 */
	private String gatherFunctionArguments() {
		return mutation._argumentIds.stream().map((it) -> inputs.inputs.get(it))
				// boolean arguments don't get a default input in `inputs.inputs`
				// they use a default value defined in the corresponding
				// procedures_prototype.mutation.argumentdefaults
				// we don't use the provided default value, but instead default to `0` in all
				// cases we are missing arguments.
				// this is because scratch is still very fluid it doing this properly is
				// currently not worth it
				.map((it) -> "(" + (it != null ? it.generateCode() : "scratchNumber(0)") + ")")
				.collect(Collectors.joining(", "));
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		inputs.updateRelations(blocks);
	}

	@Override
	public BlockType getBlockType() {
		return BlockType.STACK;
	}
}
