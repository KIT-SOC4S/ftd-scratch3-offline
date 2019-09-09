package ftd.input2;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@SuppressWarnings("serial")
public class ScratchInputDeserializer extends StdDeserializer<ScratchInput> {

	public ScratchInputDeserializer() {
		super(ScratchInput.class);
	}

	@Override
	public ScratchInput deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		System.out.println(jp.getCurrentName());
		ObjectMapper mapper = (ObjectMapper) jp.getCodec();
		TreeNode root = mapper.readTree(jp);
		Class<? extends ScratchInput> inputClass = null;
		if ("SUBSTACK".equals(jp.getCurrentName())) {
			inputClass = SubStackInput.class;
		} else if ("VALUE".equals(jp.getCurrentName())) {
			inputClass = ValueInput.class;
		} else if ("DURATION".equals(jp.getCurrentName())) {
			inputClass = DurationInput.class;
		} else if ("TIMES".equals(jp.getCurrentName())) {
			inputClass = TimesInput.class;
		}
		if (inputClass == null)
			throw new RuntimeException("Unknown:" + root);
		return mapper.readValue(root.toString(), inputClass);
	}
}