package com.someguyssoftware.treasure2.generator;

import com.someguyssoftware.gottschcore.generator.IGeneratorResult;
import com.someguyssoftware.gottschcore.positional.ICoords;

public interface ITreasureGeneratorResult extends IGeneratorResult {

	ICoords getChestCoords();

	void setChestCoords(ICoords chestCoords);
	
	@Override
	public ITreasureGeneratorResult fail();
	
	@Override
	public ITreasureGeneratorResult success();

}