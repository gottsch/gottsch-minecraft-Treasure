/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;
import java.util.stream.Collectors;

import com.someguyssoftware.treasure2.enums.Category;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Jan 17, 2018
 *
 */
public class MetallurgistsKey extends KeyItem {

	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public MetallurgistsKey(String modID, String name) {
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
				TextFormatting.GOLD) + I18n.translateToLocal("tooltip.metallurgists_key.specials")
			);
	
	}
	
	/**
	 * This key can fits any lock from the METALS category.
	 */
	@Override
	public boolean fitsLock(LockItem lockItem) {
		if (lockItem.getCategory() == Category.METALS) return true;
		return false;
	}	

}
