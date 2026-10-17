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
	/** The success probabilities on UNCOMMON and SCARCE locks. The COMMON probability is the key's success probability. */
	private double uncommonSuccessProbability;
	private double scarceSuccessProbability;

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
				getUncommonSuccessProbability(),
				getScarceSuccessProbability());
			
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
	 * COMMON locks use the success probability, UNCOMMON and SCARCE locks their own success probabilities (all configurable)
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
				if (RandomHelper.checkProbability(new Random(), this.getUncommonSuccessProbability())) {
					Treasure.LOGGER.debug("Unlock attempt met probability");
					return true;
				}				
			}
			else if (lockItem.getRarity() == Rarity.SCARCE) {
				if (RandomHelper.checkProbability(new Random(), this.getScarceSuccessProbability())) {
					Treasure.LOGGER.debug("Unlock attempt met probability");
					return true;
				}				
			}		
		}
		return false;
	}

	public double getUncommonSuccessProbability() {
		return uncommonSuccessProbability;
	}

	public ThiefsLockPick setUncommonSuccessProbability(double uncommonSuccessProbability) {
		this.uncommonSuccessProbability = uncommonSuccessProbability;
		return this;
	}

	public double getScarceSuccessProbability() {
		return scarceSuccessProbability;
	}

	public ThiefsLockPick setScarceSuccessProbability(double scarceSuccessProbability) {
		this.scarceSuccessProbability = scarceSuccessProbability;
		return this;
	}
}
