/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Feb 22, 2020
 *
 */
public class FallingSandBlock extends AbstractTreasureFallingBlock implements ITreasureBlock {
	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public FallingSandBlock(String modID, String name, Block.Properties properties) {
		super(modID, name, properties);
		registerDefaultState(getStateDefinition().any().setValue(ACTIVATED, Boolean.valueOf(false)));
	}
	
	@Override
	public void onEndFalling(World worldIn, BlockPos pos, BlockState state, BlockState state2) {
		worldIn.setBlockAndUpdate(pos, Blocks.SAND.defaultBlockState());
	}
}