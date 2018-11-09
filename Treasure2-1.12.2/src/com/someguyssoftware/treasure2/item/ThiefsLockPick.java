/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Category;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Feb 7, 2018
 *
 */
public class ThiefsLockPick extends KeyItem {

	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public ThiefsLockPick(String modID, String name) {
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

		String s1 = I18n.translateToLocalFormatted("tooltip.thiefs_lock_pick.specials", 
				getSuccessProbability(), 
				(this.getSuccessProbability() - (this.getSuccessProbability()/4)),
				(getSuccessProbability()/2));
			
		String s2 = I18n.translateToLocalFormatted("tooltip.label.specials", 
				TextFormatting.GOLD + s1);
		tooltip.add(s2);
	}
	
	/**
	 * This key can fits any lock from the COMMON, UNCOMMON, and SCARCE rarities.
	 */
	@Override
	public boolean fitsLock(LockItem lockItem) {
		if (lockItem.getRarity() == Rarity.COMMON || lockItem.getRarity() == Rarity.UNCOMMON ||
				lockItem.getRarity() == Rarity.SCARCE) return true;
		return false;
	}	

	/*
	 * If UNCOMMON lock, then this key has 25% less chance (ie x - x/4) of succeeding,
	 * If SCARCE lock, then this key has 50% less chance (ie x/2) of succeeding,
	 * @see com.someguyssoftware.treasure2.item.KeyItem#unlock(com.someguyssoftware.treasure2.item.LockItem)
	 */
	@Override
	public boolean unlock(LockItem lockItem) {
		if (lockItem.acceptsKey(this) || fitsLock(lockItem)) {
			Treasure.logger.debug("Lock accepts key");
			if (lockItem.getRarity() == Rarity.COMMON) {
				if (RandomHelper.checkProbability(new Random(), this.getSuccessProbability())) {
					Treasure.logger.debug("Unlock attempt met probability");
					return true;
				}
			}
			else if (lockItem.getRarity() == Rarity.UNCOMMON) {
				if (RandomHelper.checkProbability(new Random(), this.getSuccessProbability() - (this.getSuccessProbability()/4))) {
					Treasure.logger.debug("Unlock attempt met probability");
					return true;
				}				
			}
			else if (lockItem.getRarity() == Rarity.SCARCE) {
				if (RandomHelper.checkProbability(new Random(), this.getSuccessProbability()/2)) {
					Treasure.logger.debug("Unlock attempt met probability");
					return true;
				}				
			}		
		}
		return false;
	}
}
