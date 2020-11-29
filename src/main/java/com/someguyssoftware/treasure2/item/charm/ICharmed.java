/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.List;

import com.someguyssoftware.treasure2.capability.CharmCapabilityProvider;
import com.someguyssoftware.treasure2.capability.ICharmCapability;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Apr 25, 2020
 *
 */
@SuppressWarnings("deprecation")
public interface ICharmed {

	/**
	 * 
	 * @param stack
	 * @return
	 */
	default public List<ICharmState> getCharmStates(ItemStack stack) {
		List<ICharmState> charmStates = null;
		if (stack.hasCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null)) {
			ICharmCapability cap = stack.getCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null);
			if (cap != null) {
				charmStates = cap.getCharmStates();
			}
		}
		return charmStates;
	}
	
	/**
	 * 
	 * @param stack
	 * @param worldIn
	 * @param tooltip
	 * @param flagIn
	 */
	@SuppressWarnings("deprecation")
	default public void addCharmedInfo(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD.toString() + "" + TextFormatting.ITALIC.toString() + I18n.translateToLocal("tooltip.label.charmed"));
		tooltip.add(TextFormatting.YELLOW.toString() + "" + TextFormatting.BOLD+ I18n.translateToLocal("tooltip.label.charms"));
		// get the capabilities
		ICharmCapability cap = stack.getCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null);
		if (cap != null) {
			List<ICharmState> charmStates = cap.getCharmStates();
			for (ICharmState state : charmStates) {
				TextFormatting color = TextFormatting.WHITE;
				CharmType type = state.getCharm().getCharmType();
				String extra = "";
				switch(type) {
				case HEALING:
					color = TextFormatting.RED;
					extra = TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.healing_rate");
					break;
				case SHIELDING:
					color = TextFormatting.AQUA;
					extra = TextFormatting.GRAY + "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.shield_rate", state.getCharm().getMaxPercent()*100F);
					break;
				case FULLNESS:
					color = TextFormatting.DARK_GREEN;
					extra = TextFormatting.GRAY + "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.fullness_rate");
					break;
				case HARVESTING:
					color = TextFormatting.GREEN;
					extra = TextFormatting.GRAY + "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.harvest_rate", state.getCharm().getMaxPercent());
					break;
				case DECAY:
					color = TextFormatting.DARK_RED;
					extra = TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.decay_rate");
					break;
				case ILLUMINATION:
					color = TextFormatting.WHITE;
					extra = TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.illumination_rate");
					break;					
				}
				
				// TODO this could change depending on charm. going to have to have sub classes or calls Charm.addInformation(...)
				tooltip.add("  " + color + I18n.translateToLocalFormatted("tooltip.charm." + state.getCharm().getName(), 
						String.valueOf(Math.toIntExact(Math.round(state.getVitals().getValue()))), 
						String.valueOf(Math.toIntExact(Math.round(state.getCharm().getMaxValue())))));
				if (!"".equals(extra)) {
					tooltip.add(" " + extra);
				}
			}
		}
	}
}
