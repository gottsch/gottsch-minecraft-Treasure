package mod.gottsch.forge.treasure2.core.item;

import mod.gottsch.forge.treasure2.core.lock.LockState;
import mod.gottsch.forge.treasure2.core.tileentity.ITreasureChestTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public interface IKeyEffects {

	// TODO this is actually a lock effect
	/**
	 * 
	 * @param worldIn
	 * @param player
	 * @param chestPos
	 * @param chestTileEntity
	 */
	default public void doKeyUnlockEffects(World worldIn, PlayerEntity player, BlockPos chestPos,
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
	default public void doKeyBreakEffects(World worldIn, PlayerEntity player, BlockPos chestPos, ITreasureChestTileEntity chestTileEntity) {

        player.sendMessage(new TranslationTextComponent("chat.key_break"), Util.NIL_UUID);
		for (LockState lockState : chestTileEntity.getLockStates()) {
			if (lockState.getLock() != null) {((ServerWorld) worldIn).sendParticles(ParticleTypes.CRIT, chestPos.getX() + lockState.getSlot().getXOffset(), chestPos.getY() + lockState.getSlot().getYOffset(), chestPos.getZ() + lockState.getSlot().getZOffset(), 6, 0.0D, 0.0D, 0.0D, 0.1D);
			}
		}
		BlockPos pos;
		Vector3d vec = player.getLookAngle();
		double lookX = player.position().x + (vec.x * 2);
		double lookY = player.position().y + player.getEyeHeight() - 0.5;
		double lookZ = player.position().z + (vec.z * 2);
		((ServerWorld) worldIn).sendParticles(ParticleTypes.SMOKE, lookX, lookY, lookZ, 20, 0.0D, 0.0D, 0.0D, 0.5D);
		worldIn.playSound(null, chestPos, SoundEvents.ITEM_BREAK, SoundCategory.BLOCKS, 0.3F, 0.8F);
	}
	
	/**
	 * 
	 * @param worldIn
	 * @param player
	 * @param chestPos
	 * @param chestTileEntity
	 */
	default public void doKeyNotFitEffects(World worldIn, PlayerEntity player, BlockPos chestPos, ITreasureChestTileEntity chestTileEntity) {
		
        player.sendMessage(new TranslationTextComponent("chat.key_not_fit"), Util.NIL_UUID);
		for (LockState lockState : chestTileEntity.getLockStates()) {
			if (lockState.getLock() != null) {((ServerWorld) worldIn).sendParticles(ParticleTypes.CRIT, chestPos.getX() + lockState.getSlot().getXOffset(), chestPos.getY() + lockState.getSlot().getYOffset(), chestPos.getZ() + lockState.getSlot().getZOffset(), 6, 0.0D, 0.0D, 0.0D, 0.1D);
			}
		}
		worldIn.playSound(null, chestPos, SoundEvents.SHIELD_BREAK, SoundCategory.BLOCKS, 0.3F, 2.0F);
	}
	
	/**
	 * 
	 * @param worldIn
	 * @param player
	 * @param chestPos
	 * @param chestTileEntity
	 */
	default public void doKeyUnableToUnlockEffects(World worldIn, PlayerEntity player, BlockPos chestPos,
			ITreasureChestTileEntity chestTileEntity) {		
        player.sendMessage(new TranslationTextComponent("chat.key_unable_unlock"), Util.NIL_UUID);
	}
}
