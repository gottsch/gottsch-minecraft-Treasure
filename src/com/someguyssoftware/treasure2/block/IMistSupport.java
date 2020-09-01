/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import com.someguyssoftware.gottschcore.random.RandomHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Mar 1, 2020
 *
 */
public interface IMistSupport {

	/**
	 * 
	 * @param world
	 * @param random
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	default public boolean checkTorchPrevention(World world, Random random, int x, int y, int z) {
		int numberOfTorches = 0;
		// if all the blocks in the immediate area are loaded
		if (world.isAreaLoaded(new BlockPos(x - 3, y - 3, z - 3), new BlockPos(x + 3, y + 3, z + 3))) {
			// use a MutatableBlockPos instead of Cube\Coords or BlockPos to say the
			// recreation of many objects
			BlockPos.MutableBlockPos mbp = new BlockPos.MutableBlockPos();

			// change the randomness of particle creation
			// o torches = 100%
			// 1 torch = 50%
			// 2 torches = 25%
			// 3 torches = 0%
			for (int x1 = -3; x1 <= 3; ++x1) {
				for (int y1 = -3; y1 <= 3; ++y1) {
					for (int z1 = -3; z1 <= 3; ++z1) {
						// that just checks a value.
						IBlockState inspectBlockState = world.getBlockState(mbp.setPos(x + x1, y + y1, z + z1));
						Block inspectBlock = inspectBlockState.getBlock();

						// if the block is a torch, then destroy the fog and return
						if (inspectBlock instanceof BlockTorch) {
							numberOfTorches++;
						}
						if (numberOfTorches >= 3) {
							x1 = 99;
							y1 = 99;
							z1 = 99;
							break;
						}
					}
				}
			}
		}

		boolean isCreateParticle = true;
		if (numberOfTorches == 1) {
			isCreateParticle = RandomHelper.checkProbability(random, 50);
		} else if (numberOfTorches == 2) {
			isCreateParticle = RandomHelper.checkProbability(random, 25);
		} else if (numberOfTorches > 2) {
			isCreateParticle = false;
		}
		return isCreateParticle;
	}
}
