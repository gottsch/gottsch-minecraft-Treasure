/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;
import java.util.stream.Collectors;

import com.someguyssoftware.treasure2.enums.Category;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
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
	public PilferersLockPick(String modID, String name) {
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
				TextFormatting.GOLD) + I18n.translateToLocal("tooltip.pilferers_lock_pick.specials")
			);
	
	}
	
	/**
	 * This key can fits any lock from the COMMON
	 */
	@Override
	public boolean fitsLock(LockItem lockItem) {
		if (lockItem.getRarity() == Rarity.COMMON) return true;
		return false;
	}	

	// TODO implement unlock using different success probabilities for COMMON and UNCOMMON
}
