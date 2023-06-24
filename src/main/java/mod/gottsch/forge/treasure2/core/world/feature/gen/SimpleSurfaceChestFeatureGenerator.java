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

import java.util.List;
import java.util.Optional;
import java.util.Random;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.config.ChestFeaturesConfiguration.ChestRarity;
import mod.gottsch.forge.treasure2.core.enums.IMarkerType;
import mod.gottsch.forge.treasure2.core.enums.MarkerType;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.chest.IChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.marker.IMarkerGenerator;
import mod.gottsch.forge.treasure2.core.registry.MarkerGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.registry.RarityLevelWeightedChestGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureGenContext;
import net.minecraft.resources.ResourceLocation;

/**
 * 
 * @author Mark Gottschling May 12, 2023
 *
 */
public class SimpleSurfaceChestFeatureGenerator implements IFeatureGenerator {
	private ResourceLocation name = new ResourceLocation(Treasure.MODID, "simple_surface");
	
	@Override
	public ResourceLocation getName() {
		return name;
	}
	
	@Override
	public Optional<GeneratorResult<ChestGeneratorData>> generate(IFeatureGenContext context, ICoords spawnCoords, IRarity rarity, ChestRarity rarityConfig) {

		Treasure.LOGGER.debug("surface coords -> {}", spawnCoords.toShortString());
		if (!WorldInfo.isHeightValid(spawnCoords)) {
			Treasure.LOGGER.debug("surface coords are invalid -> {}", spawnCoords.toShortString());
			return Optional.empty();
		}

		ICoords chestCoords = new Coords(spawnCoords);
		Treasure.LOGGER.debug("surface chest coords -> {}", chestCoords);

		IChestGenerator chestGenerator = RarityLevelWeightedChestGeneratorRegistry.getNextGenerator(rarity, FeatureType.TERRANEAN);
		GeneratorResult<ChestGeneratorData> chestResult = chestGenerator.generate(context, chestCoords, rarity, null);
		if (!chestResult.isSuccess()) {
			return Optional.empty();
		}

		// add markers 
		// select a marker generator
		IMarkerGenerator<GeneratorResult<GeneratorData>> markerGenerator = selectMarkerGenerator(context.random());
		// generate marker
		markerGenerator.generate(context, spawnCoords);
		
		GeneratorResult<ChestGeneratorData> generationResult = new GeneratorResult<>(ChestGeneratorData.class);
//		generationResult.getData().setPlacement(RegionPlacement.SURFACE);
		generationResult.getData().setCoords(chestCoords);
		generationResult.getData().setSpawnCoords(spawnCoords);
		generationResult.getData().setRegistryName(chestResult.getData().getRegistryName());
		generationResult.getData().setRarity(rarity);
		
		Treasure.LOGGER.info("CHEATER! {} chest at coords: {}", rarity, spawnCoords.toShortString());
		return Optional.ofNullable(generationResult);
	}

	/**
	 * TODO probably should make this method abstract out for all FeatureGenerators
	 * @param random
	 * @return
	 */
	public IMarkerGenerator<GeneratorResult<GeneratorData>> selectMarkerGenerator(Random random) {
		IMarkerType markerType = MarkerType.STANDARD;
		
		List<IMarkerGenerator<GeneratorResult<GeneratorData>>> markerGenerators = MarkerGeneratorRegistry.get(markerType);
		IMarkerGenerator<GeneratorResult<GeneratorData>> markerGenerator = markerGenerators.get(random.nextInt(markerGenerators.size()));
		Treasure.LOGGER.debug("using MarkerType -> {}, gen -> {}", markerType, markerGenerator.getClass().getSimpleName());

		return markerGenerator;
	}
}
