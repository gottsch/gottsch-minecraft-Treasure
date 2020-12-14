/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.CharmCapabilityProvider;
import com.someguyssoftware.treasure2.item.charm.ICharmState;
import com.someguyssoftware.treasure2.item.charm.ICharmed;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * 
 * @author Mark Gottschling on May 19, 2020
 *
 */
public class CharmedGemItem extends GemItem implements ICharmed {
	
	/**
	 * 
	 */
	public CharmedGemItem (String modID, String name)	 {
		super(modID, name);
		this.setMaxStackSize(1);
		// prevent from showing in any creative tab
		this.setCreativeTab(null);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		CharmCapabilityProvider provider =  new CharmCapabilityProvider();
		return provider;
	}

	/**
	 * 
	 */
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		addCharmedInfo(stack, worldIn, tooltip, flagIn);
	}
	
	/**
	 * 
	 */
	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}
}
