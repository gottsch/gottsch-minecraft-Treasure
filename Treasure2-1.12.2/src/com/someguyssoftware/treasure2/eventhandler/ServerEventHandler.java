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
 * @author Mark Gottschling on Aug 3, 2019
 *
 */
public class ServerEventHandler {
	// reference to the mod.
	private IMod mod;
	
	/**
	 * 
	 */
	public ServerEventHandler(IMod mod) {
		setMod(mod);
	}
	
	@SubscribeEvent
	public void checkFogInteraction(FMLServerStoppingEvent event) {
		// clear all resource managers
		Treasure.LOOT_TABLES.clear();
		Treasure.TEMPLATE_MANAGER.clear();
		Treasure.META_MANAGER.clear();
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
