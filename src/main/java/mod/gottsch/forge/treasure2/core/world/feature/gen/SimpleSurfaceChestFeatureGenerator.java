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
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.config.ChestConfiguration.ChestRarity;
import mod.gottsch.forge.treasure2.core.enums.RegionPlacement;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.chest.IChestGenerator;
import mod.gottsch.forge.treasure2.core.registry.RarityLevelWeightedChestGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;

/**
 * 
 * @author Mark Gottschling May 12, 2023
 *
 */
public class SimpleSurfaceChestFeatureGenerator implements IFeatureGenerator {

	@Override
	public Optional<GeneratorResult<ChestGeneratorData>> generate(IWorldGenContext context, ICoords spawnCoords, IRarity rarity, ChestRarity rarityConfig) {

		Treasure.LOGGER.debug("surface coords -> {}", spawnCoords.toShortString());
		if (!WorldInfo.isHeightValid(spawnCoords)) {
			Treasure.LOGGER.debug("surface coords are invalid -> {}", spawnCoords.toShortString());
			return Optional.empty();
		}

		ICoords chestCoords = new Coords(spawnCoords);
		Treasure.LOGGER.debug("surface chest coords -> {}", chestCoords);

		IChestGenerator chestGenerator = RarityLevelWeightedChestGeneratorRegistry.getNextGenerator(rarity, FeatureType.TERRESTRIAL);
		GeneratorResult<ChestGeneratorData> chestResult = chestGenerator.generate(context, chestCoords, rarity, null);
		if (!chestResult.isSuccess()) {
			return Optional.empty();
		}

		GeneratorResult<ChestGeneratorData> generationResult = new GeneratorResult<>(ChestGeneratorData.class);
		generationResult.getData().setPlacement(RegionPlacement.SURFACE);
		generationResult.getData().setCoords(chestCoords);
		generationResult.getData().setSpawnCoords(spawnCoords);
		generationResult.getData().setRegistryName(chestResult.getData().getRegistryName());
		generationResult.getData().setRarity(rarity);
		
		Treasure.LOGGER.info("CHEATER! {} chest at coords: {}", rarity, spawnCoords.toShortString());
		return Optional.ofNullable(generationResult);
	}

}
