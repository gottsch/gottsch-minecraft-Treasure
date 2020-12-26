/**
 * 
 */
package com.someguyssoftware.treasure2.eventhandler;

import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import static com.someguyssoftware.treasure2.Treasure.logger;

import java.util.Optional;

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
            Optional<ItemStack> outputItemStack = addSlotsToCharmable(leftItemStack, rightItemStack);
            if (outputItem.isPresent()) {
            	event.setCost(1);
                event.setOutput(outputItemStack.get());
            }
        }
        else if (leftItemStack.getItem() instanceof ICharmable && rightItemStack.getItem() instanceof ICharmed) {
            if (addCharmsToCharmable(leftItemStack, rightItemStack)) {
            	event.setCost(1);
                event.setOutput(leftItemStack);
            }
        }
    }

    public boolean doKeyMerge() {

        return true;
    }

    /**
     * 
     * @param charmableStack
     * @param gemItem
     */
    public Optional<ItemStack> addSlotsToCharmable(ItemStack leftStack, ItemStack gemStack) {
        ItemStack output = new ItemStack(leftStack.getItem());
        if (leftStack.hasCapability(CharmableCapabilityProvider.CHARMABLE_CAPABILITY, null)) {
            ICharmableCapability leftCap = leftStack.getCapability(CharmableCapabilityProvider.CHARMABLE_CAPABILITY, null);
            ICharmableCapability outputCap = charmableStack.getCapability(CharmableCapabilityProvider.CHARMABLE_CAPABILITY, null);
            outputCap.setSlots(leftCap.getSlots());
            // check is slots available
            ICharmable outputItem = (ICharmable)output.getItem();
            if (outputItem.getMaxSlots() > 0 && outputCap.getSlots() < outputItem.getMaxSlots()) {
                // add a slot to the charmable stack
                outputCap.setSlots(outputCap.getSlots() + 1);
                return Optional.of(output);
            }
        }
        return Optional.empty();
    }

    /**
     * 
     * @param charmableStack
     * @param charmedStack
     */
    public Optional<ItemStack> addCharmsToCharmable(ItemStack leftStack, ItemStack rightStack) {
                
        if (leftStack.hasCapability(CharmableCapabilityProvider.CHARMABLE_CAPABILITY, null)) {
            ItemStack output = new ItemStack(leftStack.getItem());
            ICharmCapability outputCharmedCap = output.getCapability(CharmableCapabilityProvider.CHARM_CAPABILITY, null);
            
            ICharmableCapability leftCharmableCap = leftStack.getCapability(CharmableCapabilityProvider.CHARMABLE_CAPABILITY, null);
            ICharmCapability leftCharmedCap = leftStack.getCapability(CharmableCapabilityProvider.CHARM_CAPABILITY, null);
            ICharmCapability rightCharmedCap = rightStack.getCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null);
            
            // copy left's charms to output
            outputCharmedCap.getCharmInstances().addAll(leftCharmedCap.getCharmInstances());

            ICharmable leftItem = (ICharmable)leftStack.getItem();
            // check is slots available
            if (leftItem.getMaxSlots() > 0 && leftCharmableCap.getSlots() > 0 && rightCharmedCap.getCharmInstances().size() > 0) {
                // copy charms from right to output
                int freeSlots = leftItem.getMaxSlots() - leftCharmableCap.getSlots();
                for (int x = 0; x < Math.min(freeSlots, rightCharmedCap.getCharmInstances().size()); x++) {
                    outputCharmedCap.getCharmInstances().add(rightCharmedCap.getCharmInstances().get(x));
                }
                return Optional.of(output);
            }
        }
        return Optional.empty();
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
