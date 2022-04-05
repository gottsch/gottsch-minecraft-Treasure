/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
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
	public void onServerStopping(FMLServerStoppingEvent event) {
		Treasure.LOGGER.debug("Closing out of world.");
		// clear all resource managers
//		Treasure.LOOT_TABLE_MASTER.clear();
//		Treasure.LOOT_TABLES.clear();
		// TODO implement similar registries like LOOT_TABLE_MASTER
//		Treasure.TEMPLATE_MANAGER.clear();
//		Treasure.META_MANAGER.clear();
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
