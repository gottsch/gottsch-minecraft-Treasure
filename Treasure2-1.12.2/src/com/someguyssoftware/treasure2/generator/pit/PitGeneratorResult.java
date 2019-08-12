/**
 * 
 */
package com.someguyssoftware.treasure2.generator.pit;

import com.someguyssoftware.gottschcore.generator.GeneratorResult;
import com.someguyssoftware.gottschcore.generator.IGeneratorResult;
import com.someguyssoftware.gottschcore.positional.ICoords;
/**
 * @author Mark Gottschling on Jul 27, 2019
 *
 */
public class PitGeneratorResult extends GeneratorResult {
	private boolean success;
	private ICoords chestCoords;

	public PitGeneratorResult() {}
	
	public PitGeneratorResult(boolean success) {
		setSuccess(success);
	}

	public PitGeneratorResult(boolean success, ICoords coords) {
		setSuccess(success);
		setChestCoords(coords);
	}
	
	@Override
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public ICoords getChestCoords() {
		return chestCoords;
	}

	public void setChestCoords(ICoords chestCoords) {
		this.chestCoords = chestCoords;
	}
}
