package ftd.field;

import java.util.Map;

import ftd.block.ScratchBlock;

public abstract class ScratchField {
	public abstract void updateRelations(Map<String, ScratchBlock> blocks);

	public abstract String toString();
}
