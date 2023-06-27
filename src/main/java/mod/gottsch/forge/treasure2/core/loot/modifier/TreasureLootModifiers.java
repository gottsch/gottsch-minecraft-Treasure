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
package mod.gottsch.forge.treasure2.core.loot.modifier;

import com.mojang.serialization.Codec;

import mod.gottsch.forge.treasure2.Treasure;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 
 * @author Mark Gottschling on Jun 27, 2023
 *
 */
public class TreasureLootModifiers {
	public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS = 
			DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Treasure.MODID);
	
	public static final RegistryObject<Codec<? extends IGlobalLootModifier>> TREASURE_LOOT_MODIFIER =
			LOOT_MODIFIER_SERIALIZERS.register("default", TreasureLootModifier.CODEC);
	
	public static void register(IEventBus bus) {
		LOOT_MODIFIER_SERIALIZERS.register(bus);
	}
}
