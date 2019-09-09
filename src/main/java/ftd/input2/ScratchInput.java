package ftd.input2;

import java.util.Map;

import ftd.block.ScratchBlock;

//@JsonDeserialize(using = ScratchInputDeserializer.class)
public abstract class ScratchInput {

	public abstract void updateRelations(Map<String, ScratchBlock> blocks);

	public abstract String toString();
}
