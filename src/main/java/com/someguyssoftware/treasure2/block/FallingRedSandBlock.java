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
public class FallingRedSandBlock extends AbstractTreasureFallingBlock implements ITreasureBlock {
	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public FallingRedSandBlock(String modID, String name, Block.Properties properties) {
		super(modID, name, properties);
		setDefaultState(this.stateContainer.getBaseState().with(ACTIVATED, Boolean.valueOf(false)));
	}
	
	@Override
	public void onEndFalling(World worldIn, BlockPos pos, BlockState p_176502_3_, BlockState p_176502_4_) {
		worldIn.setBlockState(pos, Blocks.RED_SAND.getDefaultState());
	}
}