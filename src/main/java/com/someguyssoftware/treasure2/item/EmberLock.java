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
public class EmberLock extends LockItem {

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param keys
	 */
	public EmberLock(String modID, String name, Item.Properties properties, KeyItem[] keys) {
		super(modID, name, properties, keys);
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

		tooltip.add(new TranslatableComponent("tooltip.label.specials",
				ChatFormatting.GOLD + new TranslatableComponent("tooltip.ember_lock.specials").getString())
				);		
	}

	@Override
	public boolean breaksKey(KeyItem key) {
		if (key != TreasureItems.EMBER_KEY && key != TreasureItems.LIGHTNING_KEY) {
			return true;
		}
		return false;
	}
}