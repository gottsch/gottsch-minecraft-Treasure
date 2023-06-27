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
package mod.gottsch.forge.treasure2.core.world.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import mod.gottsch.forge.treasure2.Treasure;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 
 * @author Mark Gottschling on Jun 27, 2023
 *
 */
public class TreasureBiomeModifiers {
//	public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIERS = 
//			DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Treasure.MODID);
//	
//	public static RegistryObject<Codec<? extends BiomeModifier>> ORE_MODIFIER= BIOME_MODIFIERS.register("ores", () ->
//		RecordCodecBuilder.create(builder -> builder.group(
//				Biome.LIST_CODEC.fieldOf("biomes").forGetter(::biomes),
//				PlacedFeature.CODEC.fieldOf("feature").forGetter(::feature)
//				).apply(builder, ::new)));
//	
//	);
//	public static void register() {
//		BIOME_MODIFIERS.register(FMLJavaModLoadingContext.get().getModEventBus());
//	}
}
