package ftd.input2;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.block.ScratchBlock;

public class DurationInput extends ScratchInput {

	private List<Object> DURATION;
	public Duration duration;

	@SuppressWarnings("unchecked")
	@JsonCreator()
	private DurationInput(@JsonProperty(value = "DURATION") List<Object> duration) {
		DURATION = duration;
		this.duration = Duration.of(Integer.valueOf((String) ((List<Object>) DURATION.get(1)).get(1)),
				ChronoUnit.SECONDS);
	}

	@Override
	public void updateRelations(Map<String, ScratchBlock> blocks) {
	}

	@Override
	public String toString() {
		return "DurationInput [DURATION=" + DURATION + ", duration=" + duration + "]";
	}

}
