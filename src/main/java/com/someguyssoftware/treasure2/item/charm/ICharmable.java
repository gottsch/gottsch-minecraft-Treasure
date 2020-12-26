package com.someguyssoftware.treasure2.item.charm;

import java.util.List;

import com.someguyssoftware.treasure2.capability.CharmableCapabilityProvider;
import com.someguyssoftware.treasure2.capability.CharmCapabilityProvider;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.ICharmCapability;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

/**
 * NOTE: if I go this route, then this means that an Item has the CHARM CAPABILITY but the CharmInstances may be empty.
 * ie. an item has the ABILITY to be charmed but isn't necessarily charmed.
 * All checks against hasCapability() then would also have to check against the capabilities CharmInstances state.
 * ie. !getCapability().getCharmInstances().isEmpty()
 * NOTE: this also throws a wrench into items like Coins, where they are stackable, but if ICharmed, or ICharmable, they can not be stacked.
 * So in this case then you still would need multiple Item classes to represent each type of charm state.
 * Things like Rings, Amulets, Bracelets, Broches and other Adornments makes sense to be non-stackable, and thus would use ICharmable.
 */

/**
 * 
 * @author Mark Gottschling on Dec 19, 2020
 *
 */
public interface ICharmable {
	
	/**
	 * 
	 * @return
	 */
	int getMaxSlots();
	
	/**
	 * 
	 * @param stack
	 * @return
	 */
	default public boolean isCharmed(ItemStack stack) {
		if (stack.hasCapability(CharmableCapabilityProvider.CHARM_CAPABILITY, null)) {
			ICharmCapability cap = stack.getCapability(CharmableCapabilityProvider.CHARM_CAPABILITY, null);
			if (cap.getCharmInstances().size() > 0) {
				return true;
			}
		}
		return false;
	}
	
    /**
     * 
     * @param stack
     * @param world
     * @param tooltip
     * @param flag
     */
    default public void addCharmedInfo(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
	    tooltip.add(TextFormatting.GOLD.toString() + "" + TextFormatting.ITALIC.toString() + I18n.translateToLocal("tooltip.label.charmed.adornment"));
		tooltip.add(TextFormatting.YELLOW.toString() + "" + TextFormatting.BOLD+ I18n.translateToLocal("tooltip.label.charms"));
		// get the capabilities
		ICharmCapability cap = stack.getCapability(CharmableCapabilityProvider.CHARM_CAPABILITY, null);
		if (cap != null) {
			List<ICharmInstance> charmInstances = cap.getCharmInstances();
			for (ICharmInstance instance : charmInstances) {
                instance.getCharm().addInformation(stack, world, tooltip, flag, instance.getData());
            }
        }
    }
    
    /**
     * 
     * @param stack
     * @param world
     * @param tooltip
     * @param flag
     */
    @SuppressWarnings("deprecation")
	default public void addSlotsInfo(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
     	ICharmableCapability cap = stack.getCapability(CharmableCapabilityProvider.CHARMABLE_CAPABILITY, null);    	
		tooltip.add(TextFormatting.WHITE.toString() + I18n.translateToLocalFormatted("tooltip.label.charmable.slots", 
				String.valueOf(Math.toIntExact(Math.round(cap.getSlots()))), 
				String.valueOf(Math.toIntExact(Math.round(((ICharmable)stack.getItem()).getMaxSlots())))));
    }
}