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
            // TODO research the setter/getters for the damages -> maybe have to override
            leftItem.setDamage(leftItem.getDamage() + rightItem.getDamager());
        }

        // cancel vanilla behaviour
        event.setCanceled(true);
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
