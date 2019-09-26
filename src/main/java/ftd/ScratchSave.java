package ftd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({ "monitors", "extensions", "meta" }) // TODO
public class ScratchSave {

	@JsonProperty("targets")
	private ScratchTarget[] targets;

	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class ScratchTarget {
		@JsonProperty("blocks")
		private ScratchBlocks blocks;
	}

	public ScratchBlocks getBlocks() {
		return targets[1].blocks;
	}

}
