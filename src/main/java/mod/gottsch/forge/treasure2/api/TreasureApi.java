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
package mod.gottsch.forge.treasure2.api;

import mod.gottsch.forge.gottschcore.enums.IEnum;
import mod.gottsch.forge.treasure2.core.rarity.IRarity;
import mod.gottsch.forge.treasure2.core.rarity.TreasureRarities;
import mod.gottsch.forge.treasure2.core.registry.EnumRegistry;
import mod.gottsch.forge.treasure2.core.registry.MimicRegistry;
import mod.gottsch.forge.treasure2.core.structure.templatesystem.chest.ChestGeneration;
import mod.gottsch.forge.treasure2.core.wishable.IWishableHandler;
import mod.gottsch.forge.treasure2.core.wishable.TreasureWishables;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;
import java.util.Optional;

/**
 * 
 * @author Mark Gottschling on Nov 10, 2022
 *
 */
public class TreasureApi {
	public static final String FEATURE_TYPE = "featureType";

	public static List<IRarity> getRarities() {
		return TreasureRarities.getRarities();
	}

	/*
	 * core functionality. may need so modders can add their own feature types.
	 */
	public static void registerFeatureType(IFeatureType type) {
		EnumRegistry.register(FEATURE_TYPE, type);
	}
	
	public static Optional<IFeatureType> getFeatureType(String key) {
		IEnum ienum = EnumRegistry.get(FEATURE_TYPE, key);
		if (ienum == null) {
			return Optional.empty();
		}
		else {
			return Optional.of((IFeatureType) ienum);
		}
	}

	// TODO candidate to move to data setup
	public static void registerWishableHandler(Item item, IWishableHandler handler) {
		TreasureWishables.registerHandler(item, handler);
	}

	/*
	 * core functionality. don't move to data setup. ie don't want players to be able to change
	 * a mimic from its respective chest.
	 */
	public static void registerMimic(ResourceLocation chestName, ResourceLocation mimicName) {
		MimicRegistry.register(chestName, mimicName);
	}

	/**
	 * Generate a real Treasure2 chest for another mod's structure, drawing the rarity from the
	 * feature type's weights.
	 *
	 * @see #generateChest(LevelReader, BlockPos, RandomSource, Direction, IFeatureType, Optional, Optional)
	 */
	public static StructureTemplate.StructureBlockInfo generateChest(LevelReader level, BlockPos pos,
			RandomSource random, Direction facing, IFeatureType featureType) {

		return ChestGeneration.generate(level, pos, random, facing, featureType);
	}

	/**
	 * Generate a real Treasure2 chest for another mod's structure.
	 *
	 * <p>This is the same pipeline {@code TreasureChestProcessor} runs, and there is only one copy
	 * of it: a rarity is drawn, the (feature type, rarity) pair resolves a chest subprocessor, and
	 * that picks the chest block, loot table, seal, locks and any mimic, then records the chest in
	 * Treasure2's cache. <strong>Writing a chest block and NBT by hand instead produces a chest
	 * Treasure2 does not know about</strong> &mdash; no rarity, no locks, empty, and absent from
	 * discovery tracking &mdash; which is why this exists.</p>
	 *
	 * <h2>Calling it from a structure processor</h2>
	 * <p>Call it from {@code processBlock} with the marker's position and return the result in place
	 * of your marker. {@code facing} is the direction the finished chest should face, so apply your
	 * own {@code placeSettings.getRotation()} before calling.</p>
	 *
	 * <p>Then call {@link #finalizeChest(ServerLevelAccessor, BlockPos)} from your
	 * {@code finalizeProcessing} for each chest you placed. {@code processBlock} only has a
	 * {@link LevelReader}, which cannot name its dimension, so a chest cached there has none until
	 * that second call supplies it.</p>
	 *
	 * @param facing      the direction the finished chest faces. {@code ITreasureChestBlock.FACING}
	 *                    accepts all six, so a vertical value is placed rather than rejected, but
	 *                    only the horizontals render as a chest should
	 * @param featureType what generated this chest; anything but {@code AQUATIC} is treated as
	 *                    {@code TERRANEAN}. Register your own with {@link #registerFeatureType}
	 * @param rarity      forces the rarity rather than drawing one &mdash; a boss chest, say. Must
	 *                    be one of {@link #getRarities()}; a forced rarity deliberately does not
	 *                    feed back into the adaptive weights
	 * @param lootTable   pins the loot table, overriding the subprocessor's choice. Everything else
	 *                    about the chest still comes from Treasure2
	 * @return the block info to place; never null
	 */
	public static StructureTemplate.StructureBlockInfo generateChest(LevelReader level, BlockPos pos,
			RandomSource random, Direction facing, IFeatureType featureType,
			Optional<IRarity> rarity, Optional<ResourceLocation> lootTable) {

		return ChestGeneration.generate(level, pos, random, facing, featureType, rarity, lootTable);
	}

	/**
	 * Complete the cache entry for a chest placed by {@link #generateChest}, once a level that knows
	 * its own dimension is available. Call it from a structure processor's
	 * {@code finalizeProcessing}.
	 */
	public static void finalizeChest(ServerLevelAccessor levelAccessor, BlockPos pos) {
		ChestGeneration.finalizeChest(levelAccessor, pos);
	}

	/**
	 * Whether a block state is one of Treasure2's chests. Lets a caller check without importing
	 * {@code ITreasureChestBlock}.
	 */
	public static boolean isTreasureChest(BlockState state) {
		return ChestGeneration.isTreasureChest(state);
	}

	/**
	 * this method no longer does anything. it is here for legacy purposes
	 * so existing mods (loot packs) don't break.
	 * @param modID
	 */
	@Deprecated(forRemoval = true)
	public static void registerLootTables(String modID) {
		// do nothing
	}
}
