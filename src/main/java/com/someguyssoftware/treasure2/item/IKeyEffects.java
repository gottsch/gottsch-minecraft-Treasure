package com.someguyssoftware.treasure2.item;

import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.ITreasureChestTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public interface IKeyEffects {

	/**
	 * 
	 * @param worldIn
	 * @param player
	 * @param chestPos
	 * @param chestTileEntity
	 */
	default public void doKeyUnlockEffects(World worldIn, EntityPlayer player, BlockPos chestPos,
			ITreasureChestTileEntity chestTileEntity, LockState lockState) {

		((WorldServer) worldIn).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, chestPos.getX() + lockState.getSlot().getXOffset(), chestPos.getY() + lockState.getSlot().getYOffset(), chestPos.getZ() + lockState.getSlot().getZOffset(), 12, 0.0D, 0.0D, 0.0D, 0.0D);
		// play noise
		worldIn.playSound(player, chestPos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
		
	}
	
	/**
	 * 
	 * @param worldIn
	 * @param player
	 * @param chestPos
	 * @param chestTileEntity
	 */
	default public void doKeyBreakEffects(World worldIn, EntityPlayer player, BlockPos chestPos, ITreasureChestTileEntity chestTileEntity) {

        player.sendMessage(new TextComponentString( I18n.translateToLocal("chat.key_break")));
		for (LockState lockState : chestTileEntity.getLockStates()) {
			if (lockState.getLock() != null) {((WorldServer) worldIn).spawnParticle(EnumParticleTypes.CRIT, chestPos.getX() + lockState.getSlot().getXOffset(), chestPos.getY() + lockState.getSlot().getYOffset(), chestPos.getZ() + lockState.getSlot().getZOffset(), 6, 0.0D, 0.0D, 0.0D, 0.1D);
			}
		}
		Vec3d vec = player.getLookVec();
		double lookX = player.posX + (vec.x * 2);
		double lookY = player.posY + player.getEyeHeight() - 0.5;
		double lookZ = player.posZ + (vec.z * 2);
		((WorldServer) worldIn).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, lookX, lookY, lookZ, 20, 0.0D, 0.0D, 0.0D, 0.5D);
		worldIn.playSound(null, chestPos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 0.3F, 0.8F);
	}
	
	/**
	 * 
	 * @param worldIn
	 * @param player
	 * @param chestPos
	 * @param chestTileEntity
	 */
	default public void doKeyNotFitEffects(World worldIn, EntityPlayer player, BlockPos chestPos, ITreasureChestTileEntity chestTileEntity) {
		
        player.sendMessage(new TextComponentString( I18n.translateToLocal("chat.key_not_fit")));
		for (LockState lockState : chestTileEntity.getLockStates()) {
			if (lockState.getLock() != null) {((WorldServer) worldIn).spawnParticle(EnumParticleTypes.CRIT, chestPos.getX() + lockState.getSlot().getXOffset(), chestPos.getY() + lockState.getSlot().getYOffset(), chestPos.getZ() + lockState.getSlot().getZOffset(), 6, 0.0D, 0.0D, 0.0D, 0.1D);
			}
		}
		worldIn.playSound(null, chestPos, SoundEvents.ITEM_SHIELD_BREAK, SoundCategory.BLOCKS, 0.3F, 2.0F);
	}
	
	/**
	 * 
	 * @param worldIn
	 * @param player
	 * @param chestPos
	 * @param chestTileEntity
	 */
	default public void doKeyUnableToUnlockEffects(World worldIn, EntityPlayer player, BlockPos chestPos,
			ITreasureChestTileEntity chestTileEntity) {
		
        player.sendMessage(new TextComponentString( I18n.translateToLocal("chat.key_unable_unlock")));

	}
}
