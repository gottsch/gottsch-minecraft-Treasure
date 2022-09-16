/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;

import com.someguyssoftware.treasure2.enums.Category;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Jan 17, 2018
 *
 */
public class MetallurgistsKey extends KeyItem {

	public MetallurgistsKey(Item.Properties properties) {
		super(properties);
	}
	
	/**
	 * 
	 * @param modID
	 * @param name
	 */
	@Deprecated
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
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		
		tooltip.add(
				new TranslationTextComponent("tooltip.label.specials", 
				TextFormatting.GOLD + new TranslationTextComponent("tooltip.metallurgists_key.specials").getString())
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
