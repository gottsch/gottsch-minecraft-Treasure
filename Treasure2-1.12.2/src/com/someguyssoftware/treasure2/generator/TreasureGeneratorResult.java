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
public class TreasureGeneratorResult extends GeneratorResult implements ITreasureGeneratorResult {
	private ICoords chestCoords;

	public TreasureGeneratorResult() {}
	
	public TreasureGeneratorResult(boolean success) {
		setSuccess(success);
	}

	public TreasureGeneratorResult(boolean success, ICoords coords) {
		setSuccess(success);
		setChestCoords(coords);
	}
	
	/* (non-Javadoc)
	 * @see com.someguyssoftware.treasure2.generator.ITreasureGeneratorResult#getChestCoords()
	 */
	@Override
	public ICoords getChestCoords() {
		return chestCoords;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.treasure2.generator.ITreasureGeneratorResult#setChestCoords(com.someguyssoftware.gottschcore.positional.ICoords)
	 */
	@Override
	public void setChestCoords(ICoords chestCoords) {
		this.chestCoords = chestCoords;
	}
	
	@Override
	public ITreasureGeneratorResult fail() {
		super.fail();
		return this;
	}
	
	@Override
	public ITreasureGeneratorResult success() {
		super.success();
		return this;
	}
}
