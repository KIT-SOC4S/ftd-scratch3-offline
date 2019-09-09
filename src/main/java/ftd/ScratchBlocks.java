package ftd;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.block.ScratchBlock;

public class ScratchBlocks {
	@JsonProperty(value = "blocks")
	private Map<String, ScratchBlock> blocks;

	public void init() {
		blocks.forEach((s, b) -> b.updateRelations(blocks));
	}

	@Override
	public String toString() {
		return "ScratchBlocks [blocks=" + blocks + "]";
	}

	public String generateSetupCode() {
		return "#include <Ftduino.h>\n" + "\n" + "void setup() {\n" + "  ftduino.init();\n"
				+ "  pinMode(LED_BUILTIN, OUTPUT);\n" + "}";
	}

	public List<ScratchBlock> getTopLevelBlocks() {
		return blocks.entrySet().stream().filter(e -> e.getValue().topLevel).map(m -> m.getValue())
				.collect(Collectors.toList());
	}
}
