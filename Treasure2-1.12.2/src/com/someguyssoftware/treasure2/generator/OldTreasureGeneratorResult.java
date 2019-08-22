/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

import com.someguyssoftware.gottschcore.generator.GeneratorResult;
import com.someguyssoftware.gottschcore.positional.ICoords;
/**
 * @author Mark Gottschling on Jul 27, 2019
 *
 */
@Deprecated
public class OldTreasureGeneratorResult extends GeneratorResult implements IOldTreasureGeneratorResult {
	private ICoords chestCoords;

	public OldTreasureGeneratorResult() {}
	
	public OldTreasureGeneratorResult(boolean success) {
		setSuccess(success);
	}

	public OldTreasureGeneratorResult(boolean success, ICoords coords) {
		setSuccess(success);
		setChestCoords(coords);
	}
	
	/* (non-Javadoc)
	 * @see com.someguyssoftware.treasure2.generator.IOldTreasureGeneratorResult#getChestCoords()
	 */
	@Override
	public ICoords getChestCoords() {
		return chestCoords;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.treasure2.generator.IOldTreasureGeneratorResult#setChestCoords(com.someguyssoftware.gottschcore.positional.ICoords)
	 */
	@Override
	public void setChestCoords(ICoords chestCoords) {
		this.chestCoords = chestCoords;
	}
	
	@Override
	public IOldTreasureGeneratorResult fail() {
		super.fail();
		return this;
	}
	
	@Override
	public IOldTreasureGeneratorResult success() {
		super.success();
		return this;
	}
}
