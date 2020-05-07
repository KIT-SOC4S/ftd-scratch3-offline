package com.github.intrigus.ftd.block;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.intrigus.ftd.util.StringUtil;

/**
 * Implements the scratch block that users can use to define their own
 * functions.
 */
@JsonIgnoreProperties(value = { "fields", "inputs" })
public class procedures_prototype extends ScratchBlock {

	@JsonProperty(value = "mutation")
	protected Mutation mutation;

	@JsonIgnoreProperties(ignoreUnknown = true)
	protected static class Mutation {
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

			Object argumentnames = values.get("argumentnames");
			Objects.requireNonNull(argumentnames, "argumentnames must not be null");
			if (argumentnames instanceof String) {
				this.argumentnames = mapper.readValue((String) argumentnames,
						TypeFactory.defaultInstance().constructCollectionType(List.class, String.class));
			} else {
				throw new IllegalStateException("argumentnames must be instanceof of String!");
			}
		}

		private String proccode;

		protected List<String> argumentnames;
	}

	@Override
	public String gen() {
		return StringUtil.convertToCIdentifier(mutation.proccode);
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
	}

	@Override
	public BlockType getBlockType() {
		return BlockType.CUSTOM_DEF;
	}
}
