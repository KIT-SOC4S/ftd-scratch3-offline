package com.github.intrigus.ftd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.github.intrigus.ftd.block.ScratchBlock;
import com.github.intrigus.ftd.exception.ScratchNoTopLevelBlockException;
import com.github.intrigus.ftd.exception.ScratchTooManyTopLevelBlocksException;

//TODO Documentation
public class ScratchBlocks {
	private Map<String, ScratchBlock> blocks = new HashMap<>();

	@JsonAnySetter
	void setBlock(String key, ScratchBlock value) {
		blocks.put(key, value);
	}

	public void init() {
		blocks.forEach((s, b) -> b.updateRelations(blocks));
	}

	@Override
	public String toString() {
		return "ScratchBlocks [blocks=" + blocks + "]";
	}

	private String generateSetupCode() {
		return "#define FTDUINO_NO_SHORTHANDS\n" + "#include <Ftduino.h>\n" + "#undef FTDUINO_NO_SHORTHANDS\n"
				+ "#include <Scratch_Ftduino_All.h>\n" + "void setup() {\n" + "  ftduino.init();\n"
				+ "  pinMode(LED_BUILTIN, OUTPUT);\n" + "}";
	}

	private List<ScratchBlock> getTopLevelBlocks() {
		return blocks.entrySet().stream().filter(e -> e.getValue().topLevel).map(m -> m.getValue())
				.collect(Collectors.toList());
	}

	private String generateLoopCode() {
		List<ScratchBlock> topLevelBlocks = getTopLevelBlocks();
		if (topLevelBlocks.size() <= 0) {
			throw new ScratchNoTopLevelBlockException();
		} else if (topLevelBlocks.size() > 1) {
			throw new ScratchTooManyTopLevelBlocksException();
		}
		return topLevelBlocks.get(0).generateCode();
	}

	public String generateCCode() {
		return generateSetupCode() + "\n" + generateLoopCode();
	}

	public class ScratchBlocksDeserializer {

	}
}
