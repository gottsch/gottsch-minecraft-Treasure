/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Feb 7, 2018
 *
 */
public class PilferersLockPick extends KeyItem {

	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public PilferersLockPick(String modID, String name, Item.Properties properties) {
		super(modID, name, properties);
	}
	
	/**
	 * Format: (Additions)
	 * 
	 * Specials: [text] [color=gold]
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
	
		TranslationTextComponent s1 = new TranslationTextComponent("tooltip.pilferers_lock_pick.specials", 
				getSuccessProbability(),
				(getSuccessProbability()/2));
			
		TranslationTextComponent s2 = new TranslationTextComponent("tooltip.label.specials", 
				TextFormatting.GOLD + s1.getFormattedText());
		
		tooltip.add(s2);			
	}
	
	/**
	 * This key can fits any lock from the COMMON
	 */
	@Override
	public boolean fitsLock(LockItem lockItem) {
		if (lockItem.getRarity() == Rarity.COMMON || lockItem.getRarity() == Rarity.UNCOMMON) return true;
		return false;
	}	

	/*
	 * If UNCOMMON lock, then this key has 50% less chance (ie x/2) of succeeding
	 * @see com.someguyssoftware.treasure2.item.KeyItem#unlock(com.someguyssoftware.treasure2.item.LockItem)
	 */
	@Override
	public boolean unlock(LockItem lockItem) {
		if (lockItem.acceptsKey(this) || fitsLock(lockItem)) {
			Treasure.LOGGER.debug("Lock accepts key");
			if (lockItem.getRarity() == Rarity.COMMON) {
				if (RandomHelper.checkProbability(new Random(), this.getSuccessProbability())) {
					Treasure.LOGGER.debug("Unlock attempt met probability");
					return true;
				}
			}
			else if (lockItem.getRarity() == Rarity.UNCOMMON) {
				if (RandomHelper.checkProbability(new Random(), this.getSuccessProbability()/2)) {
					Treasure.LOGGER.debug("Unlock attempt met probability");
					return true;
				}				
			}
			
		}
		return false;
	}
}
