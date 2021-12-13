/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;

import com.someguyssoftware.treasure2.enums.Category;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

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
	public MetallurgistsKey(String modID, String name, Item.Properties properties) {
		super(modID, name, properties);
	}
	
	/**
	 * Format: (Additions)
	 * 
	 * Specials: [text] [color=gold]
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		
		tooltip.add(
				new TranslatableComponent("tooltip.label.specials", 
				ChatFormatting.GOLD + new TranslatableComponent("tooltip.metallurgists_key.specials").getString())
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
