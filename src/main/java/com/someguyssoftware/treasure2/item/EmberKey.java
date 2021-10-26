/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;

import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/**
 * 
 * @author Mark Gottschling on Sep 5, 2018
 *
 */
public class EmberKey extends KeyItem {

	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public EmberKey(String modID, String name) {
		super(modID, name);
	}
	
	/**
	 * Format: (Additions)
	 * 
	 * Specials: [text] [color=gold]
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		tooltip.add(
				I18n.translateToLocalFormatted("tooltip.label.specials", 
				TextFormatting.GOLD) + I18n.translateToLocal("tooltip.ember_key.specials")
			);
	}
	
	/**
	 * This key can fits ember locks and wood locks.
	 */
	@Override
	public boolean fitsLock(LockItem lockItem) {
        if (lockItem == TreasureItems.EMBER_LOCK || lockItem == TreasureItems.WOOD_LOCK) {
            return true;
        }
		return false;
	}

    @Override
    public boolean breaksLock(LockItem lockItem) {
        if (lockItem == TreasureItems.WOOD_LOCK) { 
            return true;
        }
        return false;
    }
    
    /**
     * 
     */
	public void doKeyUnlockEffects(World worldIn, EntityPlayer player, BlockPos chestPos,
			AbstractTreasureChestTileEntity chestTileEntity, LockState lockState) {

		if (lockState.getLock() == TreasureItems.WOOD_LOCK) {
			((WorldServer) worldIn).spawnParticle(EnumParticleTypes.FLAME, chestPos.getX() + lockState.getSlot().getXOffset(), chestPos.getY() + lockState.getSlot().getYOffset(), chestPos.getZ() + lockState.getSlot().getZOffset(), 24, 0.0D, 0.1D, 0.0D, 0.1D);
			worldIn.playSound(null, chestPos, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.3F, 0.6F);
		}
		else {
			super.doKeyUnlockEffects(worldIn, player, chestPos, chestTileEntity, lockState);
		}
	}
}
