/**
 * 
 */
package com.someguyssoftware.treasure2.eventhandler;

import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import static com.someguyssoftware.treasure2.Treasure.logger;
import com.someguyssoftware.treasure2.capability.EffectiveMaxDamageCapability;
import com.someguyssoftware.treasure2.capability.EffectiveMaxDamageCapabilityProvider;
import com.someguyssoftware.treasure2.item.KeyItem;
import com.someguyssoftware.treasure2.item.TreasureItems;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Mark Gottschling on Sep 6, 2020
 *
 */
public class AnvilEventHandler {
	// reference to the mod.
	private IMod mod;

	/**
	 * 
	 */
	public AnvilEventHandler(IMod mod) {
		setMod(mod);
	}

	@SubscribeEvent
	public void onAnvilUpdate(AnvilUpdateEvent event) {
		ItemStack leftItemStack = event.getLeft();
		ItemStack rightItemStack = event.getRight();

		// add all uses/damage remaining in the right item to the left item.
		if (leftItemStack.getItem() == rightItemStack.getItem() && (leftItemStack.getItem() instanceof KeyItem)) {
			if (leftItemStack.hasCapability(EffectiveMaxDamageCapabilityProvider.EFFECTIVE_MAX_DAMAGE_CAPABILITY, null)
					&& rightItemStack.hasCapability(EffectiveMaxDamageCapabilityProvider.EFFECTIVE_MAX_DAMAGE_CAPABILITY, null)) {

				event.setCost(1);
				EffectiveMaxDamageCapability leftItemCap = (EffectiveMaxDamageCapability) leftItemStack.getCapability(EffectiveMaxDamageCapabilityProvider.EFFECTIVE_MAX_DAMAGE_CAPABILITY, null);
				EffectiveMaxDamageCapability rightItemCap = (EffectiveMaxDamageCapability) rightItemStack.getCapability(EffectiveMaxDamageCapabilityProvider.EFFECTIVE_MAX_DAMAGE_CAPABILITY, null);

				if (leftItemCap != null && rightItemCap != null) {
					int leftRemainingUses = leftItemCap.getEffectiveMaxDamage() - leftItemStack.getItemDamage();
					int rightRemainingUses = rightItemCap.getEffectiveMaxDamage() - rightItemStack.getItemDamage();
					ItemStack outputItem = new ItemStack(leftItemStack.getItem());

					EffectiveMaxDamageCapability outputItemCap = (EffectiveMaxDamageCapability) outputItem.getCapability(EffectiveMaxDamageCapabilityProvider.EFFECTIVE_MAX_DAMAGE_CAPABILITY, null);

					int remainingUses = leftRemainingUses + rightRemainingUses;
					if (remainingUses > Math.max(leftItemCap.getEffectiveMaxDamage(), rightItemCap.getEffectiveMaxDamage())) {
//						if (logger.isDebugEnabled()) {
//							logger.debug("output has greater uses -> {} than emd -> {} - update emd", remainingUses, Math.max(leftItemCap.getEffectiveMaxDamage(), rightItemCap.getEffectiveMaxDamage()));
//						}
						outputItemCap.setEffectiveMaxDamage(Math.max(leftItemCap.getEffectiveMaxDamage(), rightItemCap.getEffectiveMaxDamage()) + leftItemStack.getMaxDamage());
						outputItem.setItemDamage(leftItemStack.getItemDamage() + rightItemStack.getItemDamage());
					}
					else {
						if (remainingUses < Math.min(leftItemCap.getEffectiveMaxDamage(), rightItemCap.getEffectiveMaxDamage())) {
							outputItemCap.setEffectiveMaxDamage(Math.min(leftItemCap.getEffectiveMaxDamage(), rightItemCap.getEffectiveMaxDamage()));
						}
						else {
							outputItemCap.setEffectiveMaxDamage(Math.max(leftItemCap.getEffectiveMaxDamage(), rightItemCap.getEffectiveMaxDamage()));
						}
						outputItem.setItemDamage(outputItemCap.getEffectiveMaxDamage() - remainingUses);
					}
					event.setOutput(outputItem);
				}
			}
		}
	}

	/**
	 * @return the mod
	 */
	public IMod getMod() {
		return mod;
	}

	/**
	 * @param mod the mod to set
	 */
	public void setMod(IMod mod) {
		this.mod = mod;
	}
}
