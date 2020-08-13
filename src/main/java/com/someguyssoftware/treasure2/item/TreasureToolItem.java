/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import com.someguyssoftware.gottschcore.item.ModItem;

import net.minecraft.item.Item;

/**
 * @author Mark Gottschling on Jul 29, 2018
 *
 */
public class TreasureToolItem extends ModItem {

	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public TreasureToolItem(String modID, String name, Item.Properties properties) {
		super(modID, name, properties.group(ModItemGroups.MOD_ITEM_GROUP));
	}

	
	/**
	 * 
	 */
	@Override
	public boolean hasContainerItem() {
		return true;
	}

}
