/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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
package com.someguyssoftware.treasure2.integration;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.eventhandler.CharmEventHandler;
import com.someguyssoftware.treasure2.eventhandler.HotbarEquipmentCharmHandler;
import com.someguyssoftware.treasure2.eventhandler.IEquipmentCharmHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;

/**
 * 
 * @author Mark Gottschling on Sep 16, 2022
 *
 */
public class TreasureIntegrations {

	public static void registerCuriosIntegration() {
		IEquipmentCharmHandler equipmentCharmHandler = null;
		if (TreasureConfig.INTEGRATION.enableCurios.get() && ModList.get().isLoaded("curios")) {
			Treasure.LOGGER.debug("curios IS loaded");
			try {
				equipmentCharmHandler = 
						(IEquipmentCharmHandler) Class.forName("com.someguyssoftware.treasure2.eventhandler.CuriosEquipmentCharmHandler").newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				Treasure.LOGGER.warn("Unable to load Curios compatiblity class.");
			}
		}
		if (equipmentCharmHandler == null) {
			Treasure.LOGGER.debug("equipmentHandler is null");
			equipmentCharmHandler = new HotbarEquipmentCharmHandler();
		}
		MinecraftForge.EVENT_BUS.register(new CharmEventHandler(equipmentCharmHandler));
	}
}
