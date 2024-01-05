/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.item;

import mod.gottsch.forge.treasure2.core.lock.LockState;
import mod.gottsch.forge.treasure2.core.tileentity.ITreasureChestTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * 
 * @author Mark Gottschling on Sep 21, 2022
 *
 */
public interface ILockEffects {

	/**
	 * 
	 * @param worldIn
	 * @param player
	 * @param chestPos
	 * @param chestTileEntity
	 */
	default public void doUnlockedEffects(World worldIn, PlayerEntity player, BlockPos chestPos,
			ITreasureChestTileEntity chestTileEntity, LockState lockState) {

		((ServerWorld) worldIn).sendParticles(ParticleTypes.SMOKE, (double)chestPos.getX() + lockState.getSlot().getXOffset(), (double)chestPos.getY() + lockState.getSlot().getYOffset(), (double)chestPos.getZ() + lockState.getSlot().getZOffset(), 12, 0.0D, 0.0D, 0.0D, 0.0D);
		// play noise
		worldIn.playSound(player, chestPos, SoundEvents.LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
	}

	/**
	 * 
	 * @param worldIn
	 * @param player
	 * @param chestPos
	 * @param chestTileEntity
	 */
	default public void doLockedEffects(World worldIn, PlayerEntity player, BlockPos chestPos,
			ITreasureChestTileEntity chestTileEntity, LockState lockState) {

		((ServerWorld) worldIn).sendParticles(ParticleTypes.EFFECT, (double)chestPos.getX() + lockState.getSlot().getXOffset(), (double)chestPos.getY() + lockState.getSlot().getYOffset(), (double)chestPos.getZ() + lockState.getSlot().getZOffset(), 3, 0.0D, 0.0D, 0.0D, 0.5D);
		// play noise
		worldIn.playSound(player, chestPos, SoundEvents.LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
	}

    /**
	 * 
	 * @param worldIn
	 * @param player
	 * @param chestPos
	 * @param chestTileEntity
	 */
	default public void doDestroyedEffects(World worldIn, PlayerEntity player, BlockPos chestPos,
			ITreasureChestTileEntity chestTileEntity, LockState lockState) {

		// ((ServerWorld) worldIn).sendParticles(ParticleTypes.SMOKE, (double)chestPos.getX() + lockState.getSlot().getXOffset(), (double)chestPos.getY() + lockState.getSlot().getYOffset(), (double)chestPos.getZ() + lockState.getSlot().getZOffset(), 12, 0.0D, 0.0D, 0.0D, 0.0D);
		// // play noise
		// worldIn.playSound(player, chestPos, SoundEvents.LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
	}

}