/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.CharmCapabilityProvider;
import com.someguyssoftware.treasure2.capability.ICharmCapability;
import com.someguyssoftware.treasure2.enums.AdornmentType;
import com.someguyssoftware.treasure2.item.charm.ICharmable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * @author Mark Gottschling on Dec 20, 2020
 *
 */
public class Adornment extends ModItem implements IAdornment, ICharmable, IPouchable {
	private AdornmentType type;
	private int maxCharms = 2;
	
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param type
	 */
	public Adornment(String modID, String name, AdornmentType type) {
		super();
		setItemName(modID, name);
		setMaxStackSize(1);
		setCreativeTab(Treasure.TREASURE_TAB);
		setType(type);
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
	public boolean hasEffect(ItemStack stack) {
		boolean charmed =  false;
		if (stack.hasCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null)) {
			ICharmCapability cap = stack.getCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null);
			if (cap.getCharmInstances() != null && cap.getCharmInstances().size() > 0) {
				charmed = true;
			}
		}
		return charmed;
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);		
		// TODO needs to take into account if has charms or not
//		tooltip.add(TextFormatting.GOLD + "" + TextFormatting.ITALIC + I18n.translateToLocal("tooltip.label.coin"));
	}
	
	// TODO add subtypes
	public AdornmentType getType() {
		return type;
	}

	public void setType(AdornmentType type) {
		this.type = type;
	}

	public int getMaxCharms() {
		return maxCharms;
	}

	public void setMaxCharms(int maxCharms) {
		this.maxCharms = maxCharms;
	}
}
