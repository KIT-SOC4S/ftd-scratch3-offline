package ftd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.block.ScratchBlock;
import ftd.util.NumberUtil;
import ftd.util.RelationShip;

public class ScratchValue implements RelationShip {

	public final ShadowType shadowType;

	private boolean directValue;

	private String id;
	private ScratchBlock block;

	private List<Object> array;

	@JsonCreator()
	private ScratchValue(@JsonProperty(index = 1) List<Object> values) {
		if (values == null)
			throw new IllegalStateException("unexpected");
		if (values.size() < 2) {
			throw new IllegalStateException("unexpected");
		}
		if (!(values.get(0) instanceof Integer)) {
			throw new IllegalStateException("unexpected");
		}
		this.shadowType = ShadowType.forValue((Integer) values.get(0));
		if (values.get(1) instanceof List<?>) {
			directValue = true;
			this.array = (List<Object>) values.get(1);
		} else if (values.get(1) instanceof String) {
			directValue = false;
			this.id = (String) values.get(1);
		} else {
			throw new IllegalStateException("unexpected");
		}
	}

	public static enum ShadowType {
		Shadow, NoShadow, ObscuredShadow;

		private static Map<Integer, ShadowType> namesMap = new HashMap<Integer, ShadowType>(3);

		static {
			namesMap.put(1, Shadow);
			namesMap.put(2, NoShadow);
			namesMap.put(3, NoShadow);
		}

		@JsonCreator
		public static ShadowType forValue(Integer value) {
			if (!namesMap.containsKey(value)) {
				throw new IllegalStateException("unknown value:" + value);
			}
			return namesMap.get(value);
		}
	}

	public enum ValueType {
		Number, PosNumber, PosInteger, Integer, Angle, Color, String, Broadcast, Variable, List;

		private static Map<Integer, ValueType> namesMap = new HashMap<Integer, ValueType>(10);

		static {
			namesMap.put(4, Number);
			namesMap.put(5, PosNumber);
			namesMap.put(6, PosInteger);
			namesMap.put(7, Integer);
			namesMap.put(8, Angle);
			namesMap.put(9, Color);
			namesMap.put(10, String);
			namesMap.put(11, Broadcast);
			namesMap.put(12, Variable);
			namesMap.put(13, List);
		}

		@JsonCreator
		public static ValueType forValue(Integer value) {
			if (!namesMap.containsKey(value)) {
				throw new IllegalStateException("unknown value:" + value);
			}
			return namesMap.get(value);
		}
	}

	public ScratchBlock getBlock() {
		if (directValue == false) {
			return this.block;
		} else {
			return null;
		}
	}

	public String generateCode() {
		if (directValue) {
			ValueType type = ValueType.forValue((Integer) array.get(0));
			switch (type) {
			case Number:
				return "scratchNumber(" + NumberUtil.asFloat(array.get(1)) + ")";
			// break;
			case Angle:
				throw new IllegalStateException("unimplemented");
				// break;
			case Broadcast:
				throw new IllegalStateException("unimplemented");
				// break;
			case Color:
				throw new IllegalStateException("unimplemented");
				// break;
			case Integer:
				return "scratchNumber(" + NumberUtil.asInt(array.get(1)) + ")";
			// break;
			case List:
				throw new IllegalStateException("unimplemented");
				// break;
			case PosInteger:
				return "scratchNumber(" + NumberUtil.asPosInt(array.get(1)) + ")";
			// break;
			case PosNumber:
				return "scratchNumber(" + NumberUtil.asPosFloat(array.get(1)) + ")";
			// break;
			case String:
				return "scratchString(\"" + array.get(1) + "\")";
			// break;
			case Variable:
				throw new IllegalStateException("unimplemented");
				// break;
			default:
				throw new IllegalStateException("should not happen");
			}
		} else {
			return this.block.generateCode();
		}
	}

	@Override
	public void updateRelations(Map<String, ScratchBlock> blocks) {
		if (directValue == false) {
			this.block = blocks.get(id);
		}
	}
}