/**
 * 
 */
package com.someguyssoftware.treasure2.eventhandler;

import com.someguyssoftware.gottschcore.mod.IMod;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * @author Mark Gottschling on Jan 22, 2018
 *
 */
public class LogoutEventHandler {
	// reference to the mod.
	private IMod mod;

	/**
	 * 
	 * @param mod
	 */
	public LogoutEventHandler(IMod mod) {
		setMod(mod);
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
