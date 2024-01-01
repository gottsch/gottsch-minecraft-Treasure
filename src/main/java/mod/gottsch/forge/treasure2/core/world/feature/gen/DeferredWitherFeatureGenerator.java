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
package mod.gottsch.forge.treasure2.core.world.feature.gen;

import java.util.Optional;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.block.entity.DeferredWitherTreeGeneratorBlockEntity;
import mod.gottsch.forge.treasure2.core.config.ChestFeaturesConfiguration.ChestRarity;
import mod.gottsch.forge.treasure2.core.enums.SpecialRarity;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureGenContext;
import net.minecraft.resources.ResourceLocation;

/**
 * 
 * @author Mark Gottschling Oct 12, 2023
 *
 */
public class DeferredWitherFeatureGenerator implements IFeatureGenerator {

	private ResourceLocation name = new ResourceLocation(Treasure.MODID, "deferred_wither");

	@Override
	public ResourceLocation getName() {
		return name;
	}

	/*
	 * During generation a 3x3 (x-z axis) chunk area is available to alter ( = 48 blocks).
	 * From center, there is a 23/24 block radius (since even number).
	 * To be safe, the max gen radius is set to 20.
	 */
	private static final int MAX_GEN_RADIUS = 20;

	@Override
	public Optional<GeneratorResult<ChestGeneratorData>> generate(IFeatureGenContext context, ICoords spawnCoords,
			IRarity rarity, ChestRarity config) {

		Treasure.LOGGER.debug("surface coords -> {}", spawnCoords.toShortString());
		if (!WorldInfo.isHeightValid(spawnCoords)) {
			Treasure.LOGGER.debug("surface coords are invalid -> {}", spawnCoords.toShortString());
			return Optional.empty();
		}

		context.level().setBlock(spawnCoords.toPos(), TreasureBlocks.DEFERRED_WITHER_TREE_GENERATOR.get().defaultBlockState(), 3);
		DeferredWitherTreeGeneratorBlockEntity be = (DeferredWitherTreeGeneratorBlockEntity) context.level().getBlockEntity(spawnCoords.toPos());
		if (be == null) {
			return Optional.empty();
		}
		be.setRarity(rarity);

		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);
		result.getData().setCoords(spawnCoords);
		result.getData().setRarity(SpecialRarity.WITHER);
		
		return Optional.of(result);
	}

}
