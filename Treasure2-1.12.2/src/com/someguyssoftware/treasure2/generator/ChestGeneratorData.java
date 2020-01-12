/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

import com.someguyssoftware.gottschcore.positional.ICoords;

import net.minecraft.block.state.IBlockState;

/**
 * @author Mark Gottschling on Aug 21, 2019
 *
 */
public class ChestGeneratorData extends GeneratorData {
	private ICoords chestCoords;
	private IBlockState chestState;
	
	public ChestGeneratorData() {}
	
	public ChestGeneratorData(ICoords chestCoords, IBlockState chestState) {
		super();
		this.chestCoords = chestCoords;
		this.chestState = chestState;
	}

	@Override
	public String toString() {
		return "ChestGeneratorData [chestCoords=" + chestCoords + ", chestState=" + chestState + "]";
	}

	public ICoords getChestCoords() {
		return chestCoords;
	}

	public void setChestCoords(ICoords chestCoords) {
		this.chestCoords = chestCoords;
	}

	public IBlockState getChestState() {
		return chestState;
	}

	public void setChestState(IBlockState chestState) {
		this.chestState = chestState;
	}
}
