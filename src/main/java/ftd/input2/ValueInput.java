package ftd.input2;

import java.util.Arrays;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.block.ScratchBlock;

public class ValueInput extends ScratchInput {

	@JsonProperty(value = "VALUE")
	private String[] VALUE;
	public ScratchBlock value;

	@Override
	public void updateRelations(Map<String, ScratchBlock> blocks) {
		if (VALUE.length < 2) {
			throw new IllegalStateException("unexpected");
		}
		if (!VALUE[0].equals("1")) {
			throw new IllegalStateException("expected 1");
		}
		this.value = blocks.get(VALUE[1]);
	}

	@Override
	public String toString() {
		return "ValueInput [VALUE=" + Arrays.toString(VALUE) + ", value=" + value + "]";
	}

}
