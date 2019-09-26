package ftd.field;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.block.ScratchBlock;

public class MenuMotorDir extends ScratchField {
	private List<String> DIR;
	private MotorDir motorDir;

	@JsonCreator()
	private MenuMotorDir(@JsonProperty(value = "DIR") List<String> dir) {
		if (dir.size() < 2) {
			throw new IllegalStateException("unexpected");
		}
		if (dir.get(1) != null) {
			throw new IllegalStateException("expected null");
		}
		this.DIR = dir;
		if (dir.get(0) != null) {
			motorDir = MotorDir.forValue(dir.get(0));
		}
	}

	public MotorDir getMotorDirection() {
		return motorDir;
	}

	@Override
	public String toString() {
		return "MenuMotorDir [DIR=" + DIR + ", motorDir=" + motorDir + "]";
	}

	@Override
	public void updateRelations(Map<String, ScratchBlock> blocks) {
	}

	public static enum MotorDir {

		LEFT, RIGHT;
		private static Map<String, MotorDir> namesMap = new HashMap<String, MotorDir>(2);
		static {
			namesMap.put("LEFT", LEFT);
			namesMap.put("RIGHT", RIGHT);
		}

		@JsonCreator
		public static MotorDir forValue(String value) {
			String searchValue = value.toUpperCase(Locale.ROOT);
			if (!namesMap.containsKey(searchValue)) {
				throw new IllegalStateException("unknown value:" + value);
			}
			return namesMap.get(searchValue);
		}

	}
}
