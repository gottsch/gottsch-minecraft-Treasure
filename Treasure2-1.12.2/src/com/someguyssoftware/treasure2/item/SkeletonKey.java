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
 * @author Mark Gottschling on Feb 2, 2018
 *
 */
public class SkeletonKey extends KeyItem {

	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public SkeletonKey(String modID, String name) {
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
				TextFormatting.GOLD) + I18n.translateToLocal("tooltip.skeleton_key.specials")
			);
	
	}
	
	/**
	 * This key can fits any lock from the with a Rarity of [COMMON, UNCOMMON, SCARCE, and RARE].
	 */
	@Override
	public boolean fitsLock(LockItem lockItem) {
		Rarity rarity = lockItem.getRarity();
		if (rarity == Rarity.COMMON ||
				rarity == Rarity.UNCOMMON ||
				(rarity == Rarity.SCARCE  && lockItem.getCategory() != Category.WITHER) ||
				rarity == Rarity.RARE) return true;

		return false;
	}	

}
