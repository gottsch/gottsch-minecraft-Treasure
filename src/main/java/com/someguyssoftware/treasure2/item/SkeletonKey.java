/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;

import com.someguyssoftware.treasure2.enums.Category;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

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
	public SkeletonKey(String modID, String name, Item.Properties properties) {
		super(modID, name, properties);
	}
	
	/**
	 * Format: (Additions)
	 * 
	 * Specials: [text] [color=gold]
	 */
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		
		tooltip.add(
				new TranslatableComponent("tooltip.label.specials", 
				ChatFormatting.GOLD + new TranslatableComponent("tooltip.skeleton_key.specials").getString())
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
