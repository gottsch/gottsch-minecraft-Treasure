package com.someguyssoftware.treasure2.item;

import com.someguyssoftware.gottschcore.item.ModItem;

import net.minecraft.world.item.ItemStack;

/**
 * @author Mark Gottschling on May 14, 2020
 *
 */
public class GemItem extends ModItem /*implements IPouchable*/ {
	
	/**
	 * 
	 */
	public GemItem (String modID, String name, Item.Properties properties)	 {
		super(modID, name, properties.tab(TreasureItemGroups.MOD_ITEM_GROUP));
	}

}