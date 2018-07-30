/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
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
	public TreasureToolItem(String modID, String name) {
		setItemName(modID, name);
		setCreativeTab(Treasure.TREASURE_TAB);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);		
		tooltip.add(I18n.translateToLocal("tooltip.label.treasure_tool"));
	}
	
	/**
	 * 
	 */
	@Override
	public boolean hasContainerItem() {
		return true;
	}
	
	@Override
	public Item getContainerItem() {
		return this;
	}
}
