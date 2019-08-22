package com.someguyssoftware.treasure2.generator;

import com.someguyssoftware.gottschcore.generator.IGeneratorResult;
import com.someguyssoftware.gottschcore.positional.ICoords;

@Deprecated
public interface IOldTreasureGeneratorResult extends IGeneratorResult {

	ICoords getChestCoords();

	void setChestCoords(ICoords chestCoords);
	
	@Override
	public IOldTreasureGeneratorResult fail();
	
	@Override
	public IOldTreasureGeneratorResult success();

}