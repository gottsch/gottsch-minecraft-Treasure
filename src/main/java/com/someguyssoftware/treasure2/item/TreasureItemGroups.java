/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.function.Supplier;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

/**
 * 
 * @author Mark Gottschling on Aug 12, 2020
 *
 */
public class TreasureItemGroups {
	public static final ItemGroup TREASURE_ITEM_GROUP = new ModItemGroup(
			Treasure.MODID,	() -> new ItemStack(TreasureItems.TREASURE_TAB.get()));
	
	public static final ItemGroup ADORNMENTS_TAB = new ModItemGroup(
			Treasure.MODID,	() -> new ItemStack(TreasureItems.ADORNMENTS_TAB.get()));
    
	/**
	 *
	 */
	public static class ModItemGroup extends ItemGroup {
		
		private final Supplier<ItemStack> iconSupplier;

		public ModItemGroup(final String name, final Supplier<ItemStack> iconSupplier) {
			super(name);
			this.iconSupplier = iconSupplier;
		}

		@Override
		public ItemStack makeIcon() {
			return iconSupplier.get();
		}
	}
}
