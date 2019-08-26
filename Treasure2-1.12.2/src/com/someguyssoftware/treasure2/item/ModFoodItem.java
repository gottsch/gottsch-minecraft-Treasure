/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;

/**
 * @author Mark Gottschling on Aug 25, 2019
 *
 */
public class ModFoodItem extends ItemFood {

	public ModFoodItem(String modID, String name, int amount, float saturation, boolean isWolfFood) {
		super(amount, saturation, isWolfFood);
		setItemName(modID, name);
		setCreativeTab(Treasure.TREASURE_TAB);		
	}

	/**
	 * Set the registry name of {@code this Item} to {@code name} and the unlocalised name to the full registry name.
	 * @param modID
	 * @param name
	 */
	public Item setItemName(String modID, String name) {
		setRegistryName(modID, name);
		setUnlocalizedName(getRegistryName().toString());
		return this;
	}
}
