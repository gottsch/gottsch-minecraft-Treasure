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
package mod.gottsch.forge.treasure2.core.inventory;

import mod.gottsch.forge.treasure2.core.capability.KeyRingCapability;
import mod.gottsch.forge.treasure2.core.capability.TreasureCapabilities;
import mod.gottsch.forge.treasure2.core.setup.Registration;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.RegistryObject;

/**
 * 
 * @author Mark Gottschling on Nov 22, 2022
 *
 */
public class TreasureContainers {
	// containers
	public static final RegistryObject<MenuType<StandardChestContainerMenu>> STANDARD_CHEST_CONTAINER;
	public static final RegistryObject<MenuType<KeyRingContainerMenu>> KEY_RING_CONTAINER;
		
	static {
		STANDARD_CHEST_CONTAINER = Registration.CONTAINERS.register("standard_chest_container",
	            () -> IForgeMenuType.create((windowId, inventory, data) -> new StandardChestContainerMenu(windowId, data.readBlockPos(), inventory, inventory.player)));			
		KEY_RING_CONTAINER = Registration.CONTAINERS.register("key_ring_container",
	            () -> IForgeMenuType.create((windowId, inventory, data) -> new KeyRingContainerMenu(windowId, inventory, new ItemStackHandler(KeyRingCapability.INVENTORY_SIZE))));			

	}
	
	public static void register() {
		Registration.registerContainers();
	}
}
