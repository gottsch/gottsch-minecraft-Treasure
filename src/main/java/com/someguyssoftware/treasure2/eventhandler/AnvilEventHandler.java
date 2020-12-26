/**
 * 
 */
package com.someguyssoftware.treasure2.eventhandler;

import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import static com.someguyssoftware.treasure2.Treasure.logger;

import java.util.Optional;

import com.someguyssoftware.treasure2.Treasure;
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
		/*
		 * order of conditions matters! gems can be charmed, so treat them as charmed first
		 */
        else if (leftItemStack.getItem() instanceof ICharmable && rightItemStack.getItem() instanceof ICharmed) {
        	Optional<ItemStack> outputItemStack = addCharmsToCharmable(leftItemStack, rightItemStack);
            if (outputItemStack.isPresent()) {
            	event.setCost(1);
            	event.setMaterialCost(1);
                event.setOutput(outputItemStack.get());
            }
        }
        else if (leftItemStack.getItem() instanceof ICharmable && rightItemStack.getItem() instanceof GemItem) {
            Optional<ItemStack> outputItemStack = addSlotsToCharmable(leftItemStack, rightItemStack);
            if (outputItemStack.isPresent()) {
            	event.setCost(1);
            	event.setMaterialCost(1);
                event.setOutput(outputItemStack.get());
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
    	Treasure.logger.debug("add slots to charmable called...");
        if (leftStack.hasCapability(CharmableCapabilityProvider.CHARMABLE_CAPABILITY, null)) {
            ItemStack output = new ItemStack(leftStack.getItem());
            ICharmCapability outputCharmCap = output.getCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null);
            ICharmableCapability outputCharmableCap = output.getCapability(CharmableCapabilityProvider.CHARMABLE_CAPABILITY, null);
            
        	ICharmCapability leftCharmCap = leftStack.getCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null);
            ICharmableCapability leftCharmableCap = leftStack.getCapability(CharmableCapabilityProvider.CHARMABLE_CAPABILITY, null);

            int currentCharmsSize = leftCharmCap.getCharmInstances().size();
            
            // initialize output to that of the left
            outputCharmCap.getCharmInstances().addAll(leftCharmCap.getCharmInstances());
            outputCharmableCap.setSlots(leftCharmableCap.getSlots());
            
            // check is slots available
            ICharmable outputItem = (ICharmable)output.getItem();
            if (outputItem.getMaxSlots() > 0 && outputCharmableCap.getSlots() < outputItem.getMaxSlots() 
            		&& outputCharmableCap.getSlots() < (outputItem.getMaxSlots() - currentCharmsSize)) {
                // add a slot to the charmable stack
                outputCharmableCap.setSlots(outputCharmableCap.getSlots() + 1);
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
    	Treasure.logger.debug("add charms to charmable called...");
        if (leftStack.hasCapability(CharmableCapabilityProvider.CHARMABLE_CAPABILITY, null)) {
            ItemStack output = new ItemStack(leftStack.getItem());
            ICharmCapability outputCharmCap = output.getCapability(CharmableCapabilityProvider.CHARM_CAPABILITY, null);
            ICharmableCapability outputCharmableCap = output.getCapability(CharmableCapabilityProvider.CHARMABLE_CAPABILITY, null);
            Treasure.logger.debug("new output charm instances -> {}", outputCharmCap.getCharmInstances().size());
            
            ICharmableCapability leftCharmableCap = leftStack.getCapability(CharmableCapabilityProvider.CHARMABLE_CAPABILITY, null);
            ICharmCapability leftCharmCap = leftStack.getCapability(CharmableCapabilityProvider.CHARM_CAPABILITY, null);
            Treasure.logger.debug("left charm instances -> {}", leftCharmCap.getCharmInstances().size());
            
            ICharmCapability rightCharmCap = rightStack.getCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null);
            Treasure.logger.debug("right charm instances -> {}", rightCharmCap.getCharmInstances().size());
            
            // copy left's charms to output (initialize)
            outputCharmCap.getCharmInstances().addAll(leftCharmCap.getCharmInstances());
            outputCharmableCap.setSlots(leftCharmableCap.getSlots());
            
            ICharmable item = (ICharmable)output.getItem();
            // check is slots available
            if (item.getMaxSlots() > 0 && outputCharmableCap.getSlots() > 0 && rightCharmCap.getCharmInstances().size() > 0) {
                // copy charms from right to output
                int freeSlots = outputCharmableCap.getSlots();
                for (int x = 0; x < Math.min(freeSlots, rightCharmCap.getCharmInstances().size()); x++) {
                	// TODO check for duplicate charm types
                    outputCharmCap.getCharmInstances().add(rightCharmCap.getCharmInstances().get(x));
                    outputCharmableCap.setSlots(outputCharmableCap.getSlots()-1);
                    Treasure.logger.debug("add charm {} from right to output", rightCharmCap.getCharmInstances().get(x).getCharm().getName());
                }
                Treasure.logger.debug("output charm instances -> {}", outputCharmCap.getCharmInstances().size());
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
