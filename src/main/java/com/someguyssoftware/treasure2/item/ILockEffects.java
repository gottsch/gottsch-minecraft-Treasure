
package com.someguyssoftware.treasure2.item;

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

		((ServerWorld) worldIn).sendParticles(ParticleTypes.SOMETHING_NO_SMOKE, (double)chestPos.getX() + lockState.getSlot().getXOffset(), (double)chestPos.getY() + lockState.getSlot().getYOffset(), (double)chestPos.getZ() + lockState.getSlot().getZOffset(), 12, 0.0D, 0.0D, 0.0D, 0.0D);
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