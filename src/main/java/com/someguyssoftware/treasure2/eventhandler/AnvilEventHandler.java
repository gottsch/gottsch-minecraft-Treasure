/**
 * 
 */
package com.someguyssoftware.treasure2.eventhandler;

import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import static com.someguyssoftware.treasure2.Treasure.logger;

import com.someguyssoftware.treasure2.capability.CharmCapabilityProvider;
import com.someguyssoftware.treasure2.capability.CharmableCapabilityProvider;
import com.someguyssoftware.treasure2.capability.EffectiveMaxDamageCapability;
import com.someguyssoftware.treasure2.capability.EffectiveMaxDamageCapabilityProvider;
import com.someguyssoftware.treasure2.capability.ICharmCapability;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.item.GemItem;
import com.someguyssoftware.treasure2.item.KeyItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.item.charm.ICharmable;
import com.someguyssoftware.treasure2.item.charm.ICharmed;

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
        else if (leftItemStack.getItem() instanceof ICharmable && rightItemStack.getItem() instanceof GemItem) {
            if (addSlotsToCharmable(leftItemStack, rightItemStack)) {
            	event.setCost(1);
                event.setOutput(leftItemStack); // TODO create a new item
            }
            else {
                // TODO cancel event ?
            }
        }
        else if (leftItemStack.getItem() instanceof ICharmable && rightItemStack.getItem() instanceof ICharmed) {
            if (addCharmsToCharmable(leftItemStack, rightItemStack)) {
            	event.setCost(1);
                event.setOutput(leftItemStack);
            }
        }
    }

    /**
     * 
     * @param charmableStack
     * @param gemItem
     */
    public boolean addSlotsToCharmable(ItemStack charmableStack, ItemStack gemStack) {
        
        if (charmableStack.hasCapability(CharmableCapabilityProvider.CHARMABLE_CAPABILITY, null)) {
            ICharmableCapability charmableCap = charmableStack.getCapability(CharmableCapabilityProvider.CHARMABLE_CAPABILITY, null);
            // check is slots available
            ICharmable charmableItem = (ICharmable)charmableStack.getItem();
            if (charmableItem.getMaxSlots() > 0 && charmableCap.getSlots() < charmableItem.getMaxSlots()) {
                // add a slot to the charmable stack
                charmableCap.setSlots(charmableCap.getSlots() + 1);
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @param charmableStack
     * @param charmedStack
     */
    public boolean addCharmsToCharmable(ItemStack charmableStack, ItemStack charmedStack) {
                
        if (charmableStack.hasCapability(CharmableCapabilityProvider.CHARMABLE_CAPABILITY, null)) {
            ICharmableCapability charmableCap = charmableStack.getCapability(CharmableCapabilityProvider.CHARMABLE_CAPABILITY, null);
            ICharmCapability charmedCap = charmableStack.getCapability(CharmableCapabilityProvider.CHARM_CAPABILITY, null);
            ICharmCapability sourceCharmedCap = charmedStack.getCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null);
            // check is slots available
            ICharmable charmableItem = (ICharmable)charmableStack.getItem();
            if (charmableItem.getMaxSlots() > 0 && charmableCap.getSlots() > 0 && sourceCharmedCap.getCharmInstances().size() > 0) {
                // TODO add charm to charmable
                int freeSlots = charmableItem.getMaxSlots() - charmableCap.getSlots();
                for (int x = 0; x < Math.min(freeSlots, sourceCharmedCap.getCharmInstances().size()); x++) {
                    charmedCap.getCharmInstances().add(sourceCharmedCap.getCharmInstances().get(x));
                }
                return true;
            }
        }
        return false;
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
