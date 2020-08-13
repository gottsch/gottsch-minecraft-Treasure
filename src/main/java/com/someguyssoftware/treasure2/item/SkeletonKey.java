/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;

import com.someguyssoftware.treasure2.enums.Category;
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
	@SuppressWarnings("deprecation")
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		tooltip.add(
				new TranslationTextComponent("tooltip.label.specials", 
				TextFormatting.GOLD + new TranslationTextComponent("tooltip.skeleton_key.specials").getFormattedText())
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
