/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Jul 5, 2020
 *
 */
public class EmberKey extends KeyItem {

	/**
	 * 
	 * @param properties
	 */
	public EmberKey(Item.Properties properties) {
		super(properties);
	}
	
	/**
	 * 
	 * @param modID
	 * @param name
	 */
	@Deprecated
	public EmberKey(String modID, String name, Item.Properties properties) {
		super(modID, name, properties);
	}
	
	/**
	 * Format: (Additions)
	 * 
	 * Specials: [text] [color=gold]
	 */
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	
		tooltip.add(
				new TranslationTextComponent("tooltip.label.specials", 
						TextFormatting.GOLD + new TranslationTextComponent("tooltip.ember_key.specials").getString())
				);			
	}
	
	/**
	 * This key can fits ember locks and wood locks.
	 */
	@Override
	public boolean fitsLock(LockItem lockItem) {
        if (lockItem == TreasureItems.EMBER_LOCK.get() || lockItem == TreasureItems.WOOD_LOCK.get()) {
            return true;
        }
		return false;
	}

    @Override
    public boolean breaksLock(LockItem lockItem) {
        if (lockItem == TreasureItems.WOOD_LOCK.get()) { 
            return true;
        }
        return false;
    }
}