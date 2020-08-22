/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;

import com.someguyssoftware.gottschcore.item.ModItem;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

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
		super(modID, name, properties.group(TreasureItemGroups.MOD_ITEM_GROUP));
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);	
		tooltip.add(new TranslationTextComponent("tooltip.label.treasure_tool"));
	}	
	
	/**
	 * 
	 */
	@Override
	public boolean hasContainerItem() {
		return true;
	}
}
