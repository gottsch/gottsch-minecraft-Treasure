/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;

import com.someguyssoftware.treasure2.enums.Category;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Sep 11, 2020
 *
 */
public class LightningKey extends KeyItem {

	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public LightningKey(String modID, String name) {
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
				TextFormatting.GOLD) + I18n.translateToLocal("tooltip.lightning_key.specials")
			);
	
	}
	
	/**
	 * This key can fits any lock from the with a Category of [ELEMENTAL].
	 */
	@Override
	public boolean fitsLock(LockItem lockItem) {
		Category category = lockItem.getCategory();
		if (category == Category.ELEMENTAL) return true;
		return false;
	}
}