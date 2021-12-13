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
 * @author Mark Gottschling on Sep 11, 2020
 *
 */
public class LightningKey extends KeyItem {

	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public LightningKey(String modID, String name, Item.Properties properties) {
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
						ChatFormatting.GOLD + new TranslatableComponent("tooltip.lightning_key.specials").getString())
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