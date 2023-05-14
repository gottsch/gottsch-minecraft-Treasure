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
package mod.gottsch.forge.treasure2.core.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.random.WeightedCollection;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.config.ChestConfiguration;
import mod.gottsch.forge.treasure2.core.config.ChestConfiguration.ChestRarity;
import mod.gottsch.forge.treasure2.core.config.ChestConfiguration.Generator;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.generator.IGeneratorType;
import mod.gottsch.forge.treasure2.core.generator.chest.IChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.IChestGeneratorType;
import mod.gottsch.forge.treasure2.core.random.RarityLevelWeightedCollection;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureType;
import mod.gottsch.forge.treasure2.core.world.feature.gen.IFeatureGenerator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

/**
 * 
 * @author Mark Gottschling May 12, 2023
 *
 */
public class FeatureGeneratorRegistry {

	private static final Map<IFeatureType, IFeatureGenerator> MAP = new HashMap<>();

	/**
	 * 
	 */
	private FeatureGeneratorRegistry() {	}

	
	public static void registerGenerator(IFeatureType type, IFeatureGenerator featureGenerator) {
		// first check if featureType is registered
		if (!EnumRegistry.isRegistered("featureType", type)) {
			Treasure.LOGGER.warn("featureType {} is not registered. unable to complete feature generator registration.", type);
			return;
		}
		MAP.put(type, featureGenerator);
	}	

	public IFeatureGenerator get(IFeatureType type) {
		// TODO make more robust
		return MAP.get(type);
	}
}
