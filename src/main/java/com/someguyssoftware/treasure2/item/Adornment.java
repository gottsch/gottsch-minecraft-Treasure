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
    // TODO add customName to capability

    // TODO this is max slots which exists in Item class
    // TODO need slots in a Capability that is saved to NBT
    // ex Rings can have a max of 2 charms, but Ring of Healing or low-level rings are made with only 1 slot
    /*
     * default max of slot.
     * maxSlots are the max number of charms an Adornment can hold.
     * // capability slots below
     * once a charm is added, a slot is filled and the count is decremented.
     * when a charm runs out of uses, it is removed from teh Adornment, but the maxSlots are not incremented.
     * once all maxSlots are used, the Adornment can not add any new Charms
     */
    private int maxSlots = 2;
	
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

	public int getMaxSlots() {
        return maxSlots;
	}

    public Adornment setMaxSlots(int maxSlots) {
        this.maxSlots = maxSlots;
        return this;
	}
}
