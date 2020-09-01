/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Aug 25, 2019
 *
 */
public class ModSoupItem extends ModFoodItem {

	public ModSoupItem(String modID, String name, int healAmount, float saturation) {
		super(modID, name, healAmount, saturation, false);		
		this.setMaxStackSize(1);
	}
	
    /**
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
     * the Item before the action is complete.
     */
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        super.onItemUseFinish(stack, worldIn, entityLiving);
        return new ItemStack(Items.BOWL);
    }
}
