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
package mod.gottsch.forge.treasure2.core.block.effects;

import mod.gottsch.forge.gottschcore.world.WorldInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * 
 * @author Mark Gottschling on Nov 21, 2022
 *
 */
public interface IChestEffects {
	default public void doChestOpenEffects(Level level, Player player, BlockPos chestPos) {
		if (WorldInfo.isClientSide(level)) {
			// play noise
			playSound(level, chestPos, SoundEvents.CHEST_OPEN);
		}
		else {
//		((ServerLevel) level).sendParticles(ParticleTypes.SMOKE, (double)chestPos.getX() + lockState.getSlot().getXOffset(), (double)chestPos.getY() + lockState.getSlot().getYOffset(), (double)chestPos.getZ() + lockState.getSlot().getZOffset(), 12, 0.0D, 0.0D, 0.0D, 0.0D);
		// play noise
			level.playSound(player, chestPos, SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 0.3F, 0.6F);
		}
	}
	
	/**
	 * 
	 * @param level
	 * @param player
	 * @param pos
	 */
	default public void doChestCloseEffects(Level level, Player player, BlockPos pos) {
		if (WorldInfo.isClientSide(level)) {
			playSound(level, pos, SoundEvents.CHEST_CLOSE);
		}
		else {
			level.playSound(player, pos, SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS, 0.3F, 0.6F);
		}
	}
	
	default public void doClientTickParticleEffects(Level level, BlockPos pos) {
		
	}
	
	default public void doServerTickParticleEffects(ServerLevel level, BlockPos pos) {
		
	}
	
	/**
	 * 
	 * @param level
	 * @param pos
	 * @param sound
	 */
	default public void playSound(Level level, BlockPos pos, SoundEvent sound) {
		double d0 = pos.getX() + 0.5D;
		double d1 = pos.getY() + 0.5D;
		double d2 = pos.getZ() + 0.5D;
		level.playLocalSound(d0, d1, d2, sound, SoundSource.BLOCKS, 1F, 1F, false);
	}
}
