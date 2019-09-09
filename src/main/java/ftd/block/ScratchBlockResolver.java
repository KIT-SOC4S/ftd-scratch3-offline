package ftd.block;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ScratchBlockResolver extends TypeIdResolverBase {

	@Override
	public JavaType typeFromId(DatabindContext context, String id) throws IOException {
		// TODO Auto-generated method stub
		return TypeFactory.defaultInstance().constructFromCanonical("ftd.block." + id);
	}

	@Override
	public String idFromValue(Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String idFromValueAndType(Object value, Class<?> suggestedType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Id getMechanism() {
		return Id.NAME;
	}

}
