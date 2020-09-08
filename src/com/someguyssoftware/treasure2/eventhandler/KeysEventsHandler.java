/**
 * 
 */
package com.someguyssoftware.treasure2.eventhandler;

import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Mark Gottschling on Sep 6, 2020
 *
 */
public class KeysEventHandler {
	// reference to the mod.
	private IMod mod;
	
	/**
	 * 
	 */
	public ServerEventHandler(IMod mod) {
		setMod(mod);
	}
	
	@SubscribeEvent
	public void onAnvilUpdate(AnvilUpdateEvent event) {
		Treasure.logger.debug("Anvil update...");

        ItemStack leftItem = event.getLeft();
        ItemStack rightItem = event.getRight();

        // add all uses/damage remaining in the right item to the left item.
        if (leftItem == rightItem && (leftItem instanceof KeyItem)) {
            if (leftItem.hasCapability(EffectiveMaxDamageCapabilityProvider.EFFECTIVE_MAX_DAMAGE_CAPABILITY, null)
                && rightItem.hasCapability(EffectiveMaxDamageCapabilityProvider.EFFECTIVE_MAX_DAMAGE_CAPABILITY, null)) {

                EffectiveMaxDamageCapability leftItemCap = leftItem.getCapability(EffectiveMaxDamageCapabilityProvider.EFFECTIVE_MAX_DAMAGE_CAPABILITY, null);
                EffectiveMaxDamageCapability rightItemCap = rightItem.getCapability(EffectiveMaxDamageCapabilityProvider.EFFECTIVE_MAX_DAMAGE_CAPABILITY, null);
            
                if (leftItemCap != null && rightItemCap != null) {
                    int leftRemainingUses = leftItemCap.getEffectiveMaxDamage() - leftItem.getItemDamage();
                    int rightRemainingUses = rightItemCap.getEffectiveMaxDamage() - rightItem.getItemDamage();

                    if (leftRemainingUses + rightRemainingUses > leftItemCap.getEffectiveMaxDamage()) {
                        leftItemCap.setEffectiveMaxDamage(leftRemainingUses + rightRemainingUses);
                        leftItem.setItemDamage(0);
                    }
                    else {
                        leftItem.setItemDamage(leftItem.getItemDamage() - rightRemainingUses);
                    }

                    // cancel vanilla behaviour - OR should vanilla behaviour ALWAYS be cancelled, so other tools can't fix the key
                    event.setCanceled(true);
                }
                
            // TODO research the setter/getters for the damages -> maybe have to override
            // ru1 = emd1 - damage1
            // ru2 = emd2 - damage2
            // if (ru1 + ru2 > emd1) {
                // emd1 = ru1 + ru2
                // damage1 = 0
            //}
            // else {
                // damage1 = damage1 - ru2
            //}
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
