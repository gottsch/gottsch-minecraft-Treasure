/**
 * 
 */
package com.someguyssoftware.treasure2.eventhandler;

import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.entity.monster.MimicEntity;

import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Mark Gottschling on Sep 16, 2018
 *
 */
public class MimicEventHandler {
	// reference to the mod.
	private IMod mod;
	
	/**
	 * 
	 */
	public MimicEventHandler(IMod mod) {
		setMod(mod);
	}

	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void onKnockback(LivingKnockBackEvent event) {
        if (WorldInfo.isClientSide(event.getEntity().getEntityWorld())) {
        	return;
        }
        
		// check if Mimic
		if (event.getEntity() instanceof MimicEntity) {
			event.setCanceled(true);
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
