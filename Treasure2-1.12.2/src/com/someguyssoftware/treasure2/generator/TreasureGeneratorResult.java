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
public class TreasureGeneratorResult extends GeneratorResult {
	private ICoords chestCoords;

	public TreasureGeneratorResult() {}
	
	public TreasureGeneratorResult(boolean success) {
		setSuccess(success);
	}

	public TreasureGeneratorResult(boolean success, ICoords coords) {
		setSuccess(success);
		setChestCoords(coords);
	}
	
	public ICoords getChestCoords() {
		return chestCoords;
	}

	public void setChestCoords(ICoords chestCoords) {
		this.chestCoords = chestCoords;
	}
}
