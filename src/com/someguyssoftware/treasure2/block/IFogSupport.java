/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * @author Mark Gottschling on Mar 1, 2018
 *
 */
public interface IFogSupport {

	/**
	 * 
	 * @param state
	 * @param world
	 * @param pos
	 * @return
	 */
	default public boolean canSustainFog(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}

}
