/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

/**
 * 
 * @author Mark Gottschling on Jul 5, 2020
 *
 */
public class EmberKey extends KeyItem {

	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public EmberKey(String modID, String name, Item.Properties properties) {
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
						ChatFormatting.GOLD + new TranslatableComponent("tooltip.ember_key.specials").getString())
				);			
	}
	
	/**
	 * This key can fits ember locks and wood locks.
	 */
	@Override
	public boolean fitsLock(LockItem lockItem) {
        if (lockItem == TreasureItems.EMBER_LOCK || lockItem == TreasureItems.WOOD_LOCK) {
            return true;
        }
		return false;
	}

    @Override
    public boolean breaksLock(LockItem lockItem) {
        if (lockItem == TreasureItems.WOOD_LOCK) { 
            return true;
        }
        return false;
    }
}