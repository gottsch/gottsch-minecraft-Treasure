package com.someguyssoftware.treasure2.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Sep 5, 2018
 *
 */
public class EmberLock extends LockItem {

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param keys
	 */
    public EmberLock(String modID, String name, KeyItem[] keys) {
		super(modID, name, keys);
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
				TextFormatting.GOLD) + I18n.translateToLocal("tooltip.ember_lock.specials")
			);
    }
    
    @Override
    public boolean breaksKey(KeyItem key) {
        if (key == TreasureItems.WOOD_KEY) {
            return true;
        }
        return false;
    }
}