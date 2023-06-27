/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.event;

import javax.annotation.Nonnull;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.loot.modifier.TreasureLootModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

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
