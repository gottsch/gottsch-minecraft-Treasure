/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.capability;

import mod.gottsch.forge.treasure2.Treasure;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.items.IItemHandler;

/**
 * @author Mark Gottschling on Aug 2, 2021
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = Bus.MOD)
public class TreasureCapabilities {

	public static Capability<IDurabilityHandler> DURABILITY = CapabilityManager.get(new CapabilityToken<>() {	});
	public static Capability<IKeyRingHandler> KEY_RING = CapabilityManager.get(new CapabilityToken<>() {	});
	public static Capability<IItemHandler> KEY_RING_INV = CapabilityManager.get(new CapabilityToken<>() {	});
	public static Capability<IItemHandler> POUCH = CapabilityManager.get(new CapabilityToken<>() {	});
	
	/**
	 * 
	 */
	@SubscribeEvent
	public static void register(final RegisterCapabilitiesEvent event) {
		DurabilityCapability.register(event);
		KeyRingCapability.register(event);
	}
}
