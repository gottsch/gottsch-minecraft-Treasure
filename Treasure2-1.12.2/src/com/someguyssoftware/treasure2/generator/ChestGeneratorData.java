/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.world.gen.structure.BlockContext;

import net.minecraft.block.state.IBlockState;

/**
 * 
 * @author Mark Gottschling on Feb 8, 2020
 *
 */
public class ChestGeneratorData extends GeneratorData {
	private BlockContext chestContext;
	
	/**
	 * 
	 */
	public ChestGeneratorData() {
		this.chestContext = new BlockContext();
	}
	
	/**
	 * 
	 * @param chestCoords
	 * @param chestState
	 */
	public ChestGeneratorData(ICoords chestCoords, IBlockState chestState) {
		super();
		this.chestContext = new BlockContext(chestCoords, chestState);
	}

	public BlockContext getChestContext() {
		if (chestContext == null) {
			chestContext = new BlockContext();
		}
		return chestContext;
	}

	public void setChestContext(BlockContext chestContext) {
		this.chestContext = chestContext;
	}

	@Override
	public String toString() {
		return "ChestGeneratorData2 [chestContext=" + chestContext + "]";
	}
}
