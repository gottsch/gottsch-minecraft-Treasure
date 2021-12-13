/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * @author Mark Gottschling on Feb 22, 2020
 *
 */
public class FallingGrassBlock extends AbstractTreasureFallingBlock implements ITreasureBlock {

	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public FallingGrassBlock(String modID, String name, Block.Properties properties) {
		super(modID, name, properties);
		registerDefaultState(getStateDefinition().any().setValue(ACTIVATED, Boolean.valueOf(false)));
	}

	@Override
	public void onEndFalling(Level worldIn, BlockPos pos, BlockState p_176502_3_, BlockState p_176502_4_) {
		worldIn.setBlockAndUpdate(pos, Blocks.GRASS_BLOCK.defaultBlockState());
	}
}