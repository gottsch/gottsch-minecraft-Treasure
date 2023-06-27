/*
 * This file is part of  Treasure2.
 * Copyright (c) 2018 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.command;

import java.util.Optional;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.config.ChestFeaturesConfiguration;
import mod.gottsch.forge.treasure2.core.config.ChestFeaturesConfiguration.ChestRarity;
import mod.gottsch.forge.treasure2.core.config.ChestFeaturesConfiguration.Generator;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.registry.TreasureTemplateRegistry;
import mod.gottsch.forge.treasure2.core.structure.StructureType;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureGenContext;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import mod.gottsch.forge.treasure2.core.world.feature.gen.IFeatureGenerator;
import mod.gottsch.forge.treasure2.core.world.feature.gen.TreasureFeatureGenerators;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;


/**
 * 
 * @author Mark Gottschling on Jan 25, 2018
 *
 */
public class SpawnWitherTreeCommand {

	/**
	 * 
	 */
	private static final SuggestionProvider<CommandSourceStack> SUGGEST_WELL = (source, builder) -> {
		return SharedSuggestionProvider.suggest(
				TreasureTemplateRegistry.getTemplate(StructureType.WELL).stream()
				.map(w -> w.getLocation().toString()).toList(), builder);
	};

	/**
	 * 
	 * @param dispatcher
	 */
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher
		.register(Commands.literal("t2-witherTree")
				.requires(source -> {
					return source.hasPermission(2);
				})
				.then(Commands.argument("pos", BlockPosArgument.blockPos())
						// TODO add additional params for num of supporting trees etc.
								.executes(source -> {
									return spawn(source.getSource(), 
											BlockPosArgument.getLoadedBlockPos(source, "pos"));
								})	
						)
				);
	}

	/**
	 * NOTE modID and name are for future expansion where you can actually pick the well based on template name (not well name);
	 * @param source
	 * @param pos
	 * @param name
	 * @return
	 */
	private static int spawn(CommandSourceStack source, BlockPos pos) {
		Treasure.LOGGER.info("executing spawn wither tree, pos -> {}", pos);

		try {
			ServerLevel world = source.getLevel();
			RandomSource random = world.getRandom();
			IRarity rarity = Rarity.COMMON;
			
//			ResourceLocation dimension = WorldInfo.getDimension(world);
			// get the generator config
			ChestFeaturesConfiguration config = Config.chestConfig; // Config.chestConfigMap.get(dimension);
			if (config == null) {
				Treasure.LOGGER.debug("ChestConfiguration is null. This shouldn't be.");
				return -1;
			}

			Generator generatorConfig = config.getGenerator(FeatureType.TERRANEAN.getName());
			if (generatorConfig == null) {
				Treasure.LOGGER.warn("unable to locate a config for feature type -> {}.", FeatureType.TERRANEAN.getName());
				return -1;
			}
			
			Optional<ChestRarity> rarityConfig = generatorConfig.getRarity(rarity);
			if (!rarityConfig.isPresent()) {
				Treasure.LOGGER.warn("unable to locate rarity config for rarity - >{}", rarity);
				return -1;
			}
			
			IFeatureGenerator generator = TreasureFeatureGenerators.WITHER_FEATURE_GENERATOR_SELECTOR.select();
			Optional<GeneratorResult<ChestGeneratorData>> result = generator.generate(new FeatureGenContext(world, world.getChunkSource().getGenerator(), random, FeatureType.TERRANEAN), new Coords(pos), rarity, rarityConfig.get());

		}
		catch(Exception e) {
			Treasure.LOGGER.error("error generating Treasure2 Wither Tree:", e);
		}
		return 1;
	}
}