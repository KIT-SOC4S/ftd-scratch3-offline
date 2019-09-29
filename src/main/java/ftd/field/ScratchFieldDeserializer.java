package ftd.field;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Deserializer used for scratch fields. Fields are effectively inputs to the
 * different scratch blocks. This is primarily used for deserializing ftduino
 * specific fields. It does this by mapping the field name to the matching
 * class.
 * 
 * @see <a href=
 *      "https://github.com/harbaum/scratch-vm/blob/9b63c1117a27b70dc8ef10c8a2ce80d412030104/src/extensions/scratch3_ftduino/index.js#L696-L736"
 *      target="_top">ftduino specific fields</a>
 */
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