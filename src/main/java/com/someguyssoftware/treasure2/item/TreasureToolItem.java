/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;

import com.someguyssoftware.gottschcore.item.ModItem;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

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
		super(modID, name, properties.tab(TreasureItemGroups.MOD_ITEM_GROUP));
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);	
		tooltip.add(new TranslatableComponent("tooltip.label.treasure_tool"));
	}	
	
	/**
	 * Required to prevent item consumpution in recipe
	 */
	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return true;
	}

	/**
	 * Required to prevent item consumpution in recipe
	 */
	@Override
	public ItemStack getContainerItem(ItemStack itemStack) {
        if (!hasContainerItem(itemStack)) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(this);
	}
}
