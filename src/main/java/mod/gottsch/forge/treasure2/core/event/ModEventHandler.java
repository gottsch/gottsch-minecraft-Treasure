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
package mod.gottsch.forge.treasure2.core.event;

import mod.gottsch.forge.treasure2.Treasure;
import net.minecraftforge.fml.common.Mod;

/**
 * 
 * @author Mark Gottschling Jun 12, 2023
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandler {
	
//	@SubscribeEvent
//	public static void registerModifierSerializers(@Nonnull final RegisterEvent event) {
//		event.getForgeRegistry().registerAll(
//				new TreasureLootModifier.Serializer().setRegistryName(
//						new ResourceLocation(Treasure.MODID, "default"))		
//				);
//	}
}
