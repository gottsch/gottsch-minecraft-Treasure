/*
 * This file is part of  Treasure2.
 * Copyright (c) 2020 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.forge.treasure2.core.block;

import java.util.Random;

import mod.gottsch.forge.gottschcore.random.RandomHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;

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
	// TODO change xyz to ICoords
	default public boolean isMistAllowed(Level world, RandomSource random, int x, int y, int z) {
		int numberOfTorches = 0;
		// use a MutatableBlockPos instead of Cube\Coords or BlockPos to save the
		// recreation of many objects
		BlockPos.MutableBlockPos mbp = new BlockPos.MutableBlockPos();

		/*
		 *  change the randomness of particle creation:
		 *  o torches = 100%
		 * 1 torch = 50%
		 * 2 torches = 25%
		 * 3 torches = 0%
		 */
		for (int x1 = -3; x1 <= 3; ++x1) {
			for (int y1 = -3; y1 <= 3; ++y1) {
				for (int z1 = -3; z1 <= 3; ++z1) {
					// that just checks a value.
					BlockState inspectBlockState = world.getBlockState(mbp.set(x + x1, y + y1, z + z1));
					Block inspectBlock = inspectBlockState.getBlock();

					// if the block is a torch, then destroy the fog and return
					if (inspectBlock instanceof TorchBlock) {
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