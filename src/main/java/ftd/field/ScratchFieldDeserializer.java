package ftd.field;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ftd.input2.ScratchInput;

@SuppressWarnings("serial")
public class ScratchFieldDeserializer extends StdDeserializer<ScratchField> {

	public ScratchFieldDeserializer() {
		super(ScratchInput.class);
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
			inputClass = MenuInputD.class;
		} else if (root.get("INPUT") != null) {
			inputClass = MenuInput.class;
		} else if (root.get("MODE") != null) {
			inputClass = InputMode.class;
		} else if (root.get("MOTOR") != null) {
			inputClass = MenuMotor.class;
		} else if (root.get("DIR") != null) {
			inputClass = MenuMotorDir.class;
		} else if (root.get("COUNTER") != null) {
			inputClass = MenuCounter.class;
		} else if (root.get("STOPMODE") != null) {
			inputClass = MotorStopModeField.class;
		} else if (root.get("OUTPUT") != null) {
			inputClass = MenuOutput.class;
		}
		if (root.isEmpty(null)) {
			return null;
		}
		if (inputClass == null)
			throw new RuntimeException("Unknown:" + root);
		return mapper.readValue(root.toString(), inputClass);
	}
}