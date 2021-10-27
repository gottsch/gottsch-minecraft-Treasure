/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.List;

import com.someguyssoftware.treasure2.capability.ICharmInventoryCapability;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.ICharmEntity;

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
//	default public List<ICharmInstance> getCharmInstances(ItemStack stack) {
//		List<ICharmInstance> charmInstances = null;
//		if (stack.hasCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null)) {
//			ICharmCapability cap = stack.getCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null);
//			if (cap != null) {
//				charmInstances = cap.getCharmInstances();
//			}
//		}
//		return charmInstances;
//	}
    
    /**
     * 
     * @param stack
     * @param world
     * @param tooltip
     * @param flag
     */
    default public void addCharmedInfo2(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
	    tooltip.add(TextFormatting.GOLD.toString() + "" + TextFormatting.ITALIC.toString() + I18n.translateToLocal("tooltip.label.charmed"));
		tooltip.add(TextFormatting.YELLOW.toString() + "" + TextFormatting.BOLD+ I18n.translateToLocal("tooltip.label.charms"));
		// get the capabilities
		ICharmInventoryCapability cap = stack.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);
		if (cap != null) {
			List<ICharmEntity> charmInstances = cap.getCharmEntities();
			for (ICharmEntity instance : charmInstances) {
                instance.getCharm().addInformation(stack, world, tooltip, flag, instance);
            }
        }
    }
//    default public void addCharmedInfo2(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
//	    tooltip.add(TextFormatting.GOLD.toString() + "" + TextFormatting.ITALIC.toString() + I18n.translateToLocal("tooltip.label.charmed"));
//		tooltip.add(TextFormatting.YELLOW.toString() + "" + TextFormatting.BOLD+ I18n.translateToLocal("tooltip.label.charms"));
//		// get the capabilities
//		ICharmCapability cap = stack.getCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null);
//		if (cap != null) {
//			List<ICharmInstance> charmInstances = cap.getCharmInstances();
//			for (ICharmInstance instance : charmInstances) {
//                instance.getCharm().addInformation(stack, world, tooltip, flag, instance.getData());
//            }
//        }
//    }

	/**
	 * 
	 * @param stack
	 * @param worldIn
	 * @param tooltip
	 * @param flagIn
	 */
    @Deprecated
	@SuppressWarnings("deprecation")
	default public void addCharmedInfo(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD.toString() + "" + TextFormatting.ITALIC.toString() + I18n.translateToLocal("tooltip.label.charmed"));
		tooltip.add(TextFormatting.YELLOW.toString() + "" + TextFormatting.BOLD+ I18n.translateToLocal("tooltip.label.charms"));
		// get the capabilities
		ICharmInventoryCapability cap = stack.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);
		if (cap != null) {
			List<ICharmEntity> charmInstances = cap.getCharmEntities();
			for (ICharmEntity state : charmInstances) {
				TextFormatting color = TextFormatting.WHITE;
//				CharmType type = state.getCharm().getCharmType();
//				String extra = "";
//				switch(type) {
//				case HEALING:
//					color = TextFormatting.RED;
//					extra = TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.healing_rate");
//					break;
//				case SHIELDING:
//					color = TextFormatting.AQUA;
//					extra = TextFormatting.GRAY + "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.shield_rate", state.getCharm().getMaxPercent()*100F);
//					break;
//				case FULLNESS:
//					color = TextFormatting.DARK_GREEN;
//					extra = TextFormatting.GRAY + "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.fullness_rate");
//					break;
//				case HARVESTING:
//					color = TextFormatting.GREEN;
//					extra = TextFormatting.GRAY + "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.harvest_rate", state.getCharm().getMaxPercent());
//					break;
//				case DECAY:
//					color = TextFormatting.DARK_RED;
//					extra = TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.decay_rate");
//					break;
//				case ILLUMINATION:
//					color = TextFormatting.WHITE;
//					extra = TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.illumination_rate");
//					break;					
//				}
//				
//				// TODO this could change depending on charm. going to have to have sub classes or calls Charm.addInformation(...)
//				tooltip.add("  " + color + I18n.translateToLocalFormatted("tooltip.charm." + state.getCharm().getName(), 
//						String.valueOf(Math.toIntExact(Math.round(state.getVitals().getValue()))), 
//						String.valueOf(Math.toIntExact(Math.round(state.getCharm().getMaxValue())))));
//				if (!"".equals(extra)) {
//					tooltip.add(" " + extra);
//				}
			}
		}
	}
}
