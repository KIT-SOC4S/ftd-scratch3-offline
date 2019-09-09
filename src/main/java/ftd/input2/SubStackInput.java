package ftd.input2;

import java.util.Arrays;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.block.ScratchBlock;

public class SubStackInput extends ScratchInput {

	private String[] SUBSTACK;
	public ScratchBlock subStack;

	@JsonCreator()
	private SubStackInput(@JsonProperty(index = 1) String[] subStack) {
		SUBSTACK = subStack;
		if (SUBSTACK == null)
			throw new IllegalStateException("unexpected");
		if (SUBSTACK.length < 2) {
			throw new IllegalStateException("unexpected");
		}
		if (!SUBSTACK[0].equals("2")) {
			throw new IllegalStateException("expected 2");
		}
	}

	public void updateRelations(Map<String, ScratchBlock> blocks) {
		this.subStack = blocks.get(SUBSTACK[1]);
	}

	@Override
	public String toString() {
		return "SubStackInput [SUBSTACK=" + Arrays.toString(SUBSTACK) + ", subStack=" + subStack + "]";
	}
}
