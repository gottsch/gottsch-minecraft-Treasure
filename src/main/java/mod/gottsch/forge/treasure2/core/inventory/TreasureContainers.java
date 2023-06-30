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
import mod.gottsch.forge.treasure2.core.capability.PouchCapability;
import mod.gottsch.forge.treasure2.core.chest.SkullChestType;
import mod.gottsch.forge.treasure2.core.setup.Registration;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
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
	public static final RegistryObject<MenuType<SkullChestContainerMenu>> SKULL_CHEST_CONTAINER;
	public static final RegistryObject<MenuType<StrongboxContainerMenu>> STRONGBOX_CONTAINER;
	public static final RegistryObject<MenuType<CompressorChestContainerMenu>> COMPRESSOR_CHEST_CONTAINER;
	public static final RegistryObject<MenuType<VikingChestContainerMenu>> VIKING_CHEST_CONTAINER;
	public static final RegistryObject<MenuType<WitherChestContainerMenu>> WITHER_CHEST_CONTAINER;
		
	public static final RegistryObject<MenuType<KeyRingContainerMenu>> KEY_RING_CONTAINER;
	public static final RegistryObject<MenuType<PouchContainerMenu>> POUCH_CONTAINER;
		
	static {
		STANDARD_CHEST_CONTAINER = Registration.CONTAINERS.register("standard_chest_container",
	            () -> IForgeMenuType.create((windowId, inventory, data) -> new StandardChestContainerMenu(windowId, data.readBlockPos(), inventory, inventory.player)));			
		SKULL_CHEST_CONTAINER = Registration.CONTAINERS.register("skull_chest_container",
	            () -> IForgeMenuType.create((windowId, inventory, data) -> new SkullChestContainerMenu(windowId, data.readBlockPos(), inventory, inventory.player)));			
		STRONGBOX_CONTAINER = Registration.CONTAINERS.register("strongbox_container",
	            () -> IForgeMenuType.create((windowId, inventory, data) -> new StrongboxContainerMenu(windowId, data.readBlockPos(), inventory, inventory.player)));			
		COMPRESSOR_CHEST_CONTAINER = Registration.CONTAINERS.register("compressor_chest_container",
	            () -> IForgeMenuType.create((windowId, inventory, data) -> new CompressorChestContainerMenu(windowId, data.readBlockPos(), inventory, inventory.player)));			
		VIKING_CHEST_CONTAINER = Registration.CONTAINERS.register("viking_chest_container",
	            () -> IForgeMenuType.create((windowId, inventory, data) -> new VikingChestContainerMenu(windowId, data.readBlockPos(), inventory, inventory.player)));			
		WITHER_CHEST_CONTAINER = Registration.CONTAINERS.register("wither_chest_container",
	            () -> IForgeMenuType.create((windowId, inventory, data) -> new WitherChestContainerMenu(windowId, data.readBlockPos(), inventory, inventory.player)));			

		
		KEY_RING_CONTAINER = Registration.CONTAINERS.register("key_ring_container",
	            () -> IForgeMenuType.create((windowId, inventory, data) -> new KeyRingContainerMenu(windowId, inventory, new ItemStackHandler(KeyRingCapability.INVENTORY_SIZE))));			
		POUCH_CONTAINER = Registration.CONTAINERS.register("pouch_container",
	            () -> IForgeMenuType.create((windowId, inventory, data) -> new PouchContainerMenu(windowId, inventory, new ItemStackHandler(PouchCapability.INVENTORY_SIZE))));			

	}
	
	public static void register(IEventBus bus) {
		Registration.registerContainers(bus);
	}
}
