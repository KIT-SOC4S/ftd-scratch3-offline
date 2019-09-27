package ftd.field;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

@SuppressWarnings("serial")
public class ScratchFieldDeserializer extends StdDeserializer<ScratchField> {

	public ScratchFieldDeserializer() {
		super(ScratchField.class);
	}

	@Override
	public ScratchField deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		ObjectMapper mapper = (ObjectMapper) jp.getCodec();
		ObjectNode root = (ObjectNode) mapper.readTree(jp);
		Class<? extends ScratchField> inputClass = null;
		if (root.get("ONOFFSTATE") != null) {
			inputClass = OnOffStateField.class;
		} else if (root.get("INPUT_D") != null) {
			inputClass = DigitalInputSpecifierField.class;
		} else if (root.get("INPUT") != null) {
			inputClass = AnalogInputSpecifierField.class;
		} else if (root.get("MODE") != null) {
			inputClass = InputModeField.class;
		} else if (root.get("MOTOR") != null) {
			inputClass = MotorSpecifierField.class;
		} else if (root.get("DIR") != null) {
			inputClass = MotorDirectionField.class;
		} else if (root.get("COUNTER") != null) {
			inputClass = CounterSpecifierField.class;
		} else if (root.get("STOPMODE") != null) {
			inputClass = MotorStopModeField.class;
		} else if (root.get("OUTPUT") != null) {
			inputClass = OutputSpecifierField.class;
		}
		if (root.isEmpty(null)) {
			return null;
		}
		if (inputClass == null)
			throw new RuntimeException("Unknown:" + root);
		return mapper.readValue(root.toString(), inputClass);
	}
}