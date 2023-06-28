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
package mod.gottsch.forge.treasure2.core.item.effects;

import mod.gottsch.forge.treasure2.core.block.entity.ITreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.lock.LockState;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

/**
 * 
 * @author Mark Gottschling on Nov 9, 2022
 *
 */
public interface IKeyEffects {

	// TODO this is actually a lock effect
	/**
	 * 
	 * @param worldIn
	 * @param player
	 * @param chestPos
	 * @param chestTileEntity
	 */
	default public void doKeyUnlockEffects(Level worldIn, Player player, BlockPos chestPos, LockState lockState) {

		((ServerLevel) worldIn).sendParticles(ParticleTypes.SMOKE, (double)chestPos.getX() + lockState.getSlot().getXOffset(), (double)chestPos.getY() + lockState.getSlot().getYOffset(), (double)chestPos.getZ() + lockState.getSlot().getZOffset(), 12, 0.0D, 0.0D, 0.0D, 0.0D);
		// play noise
		worldIn.playSound(player, chestPos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, 0.6F);
	}

	/**
	 * 
	 * @param worldIn
	 * @param player
	 * @param chestPos
	 * @param chestTileEntity
	 */
	default public void doKeyBreakEffects(Level worldIn, Player player, BlockPos chestPos) {
		player.sendSystemMessage(Component.translatable(LangUtil.chat("key.key_break")));

		BlockEntity be = worldIn.getBlockEntity(chestPos);
		if (be instanceof ITreasureChestBlockEntity) {
			ITreasureChestBlockEntity chestBe = (ITreasureChestBlockEntity)be;
			for (LockState lockState : chestBe.getLockStates()) {
				if (lockState.getLock() != null) {((ServerLevel) worldIn).sendParticles(ParticleTypes.CRIT, chestPos.getX() + lockState.getSlot().getXOffset(), chestPos.getY() + lockState.getSlot().getYOffset(), chestPos.getZ() + lockState.getSlot().getZOffset(), 6, 0.0D, 0.0D, 0.0D, 0.1D);
				}
			}
		}
		Vec3 vec = player.getLookAngle();
		double lookX = player.position().x + (vec.x * 2);
		double lookY = player.position().y + player.getEyeHeight() - 0.5;
		double lookZ = player.position().z + (vec.z * 2);
		((ServerLevel) worldIn).sendParticles(ParticleTypes.SMOKE, lookX, lookY, lookZ, 20, 0.0D, 0.0D, 0.0D, 0.5D);
		worldIn.playSound(null, chestPos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 0.3F, 0.8F);
	}

	/**
	 * 
	 * @param worldIn
	 * @param player
	 * @param chestPos
	 * @param chestTileEntity
	 */
	default public void doKeyNotFitEffects(Level worldIn, Player player, BlockPos chestPos) {
		player.sendSystemMessage(Component.translatable(LangUtil.chat("key.key_not_fit")));
		
		BlockEntity be = worldIn.getBlockEntity(chestPos);
		if (be instanceof ITreasureChestBlockEntity) {
			ITreasureChestBlockEntity chestBe = (ITreasureChestBlockEntity)be;
			for (LockState lockState : chestBe.getLockStates()) {
				if (lockState.getLock() != null) {((ServerLevel) worldIn).sendParticles(ParticleTypes.CRIT, chestPos.getX() + lockState.getSlot().getXOffset(), chestPos.getY() + lockState.getSlot().getYOffset(), chestPos.getZ() + lockState.getSlot().getZOffset(), 6, 0.0D, 0.0D, 0.0D, 0.1D);
				}
			}
		}
		worldIn.playSound(null, chestPos, SoundEvents.SHIELD_BREAK, SoundSource.BLOCKS, 0.3F, 2.0F);
	}

	/**
	 * 
	 * @param worldIn
	 * @param player
	 * @param chestPos
	 * @param chestTileEntity
	 */
	default public void doKeyUnableToUnlockEffects(Level worldIn, Player player, BlockPos chestPos) {		
		player.sendSystemMessage(Component.translatable(LangUtil.chat("key.key_unable_unlock")));
	}
}
