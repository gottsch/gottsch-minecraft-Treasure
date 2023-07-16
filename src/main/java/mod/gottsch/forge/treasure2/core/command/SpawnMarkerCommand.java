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

import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.world.WorldGenContext;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.ruin.IRuinGenerator;
import mod.gottsch.forge.treasure2.core.registry.RuinGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.registry.TreasureTemplateRegistry;
import mod.gottsch.forge.treasure2.core.structure.IStructureCategory;
import mod.gottsch.forge.treasure2.core.structure.StructureCategory;
import mod.gottsch.forge.treasure2.core.structure.StructureType;
import mod.gottsch.forge.treasure2.core.structure.TemplateHolder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;


/**
 * Usage: /t2-ruins <x> <y> <z> <category> <name>
 * Spawns ruins at location (x, y, z) using the type and  structure name
 * @author Mark Gottschling on Jan 25, 2018
 *
 */
public class SpawnMarkerCommand {
	private static final String CATEGORY_ARG = "category";
	private static final String NAME_ARG = "name";

	private static final SuggestionProvider<CommandSourceStack> SUGGEST_CATEGORY = (source, builder) -> {
		return SharedSuggestionProvider.suggest(TreasureApi.getStructureCategories().stream().map(c -> c.getValue()).toList(), builder);
	};
	
	private static final SuggestionProvider<CommandSourceStack> SUGGEST_MARKER = (source, builder) -> {
		String category = StringArgumentType.getString(source, CATEGORY_ARG);
		Optional<IStructureCategory> structureCategory = TreasureApi.getStructureCategory(category);
		if (structureCategory.isEmpty()) {
			structureCategory = Optional.of(StructureCategory.TERRANEAN);
		}
		return SharedSuggestionProvider.suggest(
				TreasureTemplateRegistry.getTemplate(structureCategory.get(), StructureType.MARKER).stream()
				.map(w -> w.getLocation().toString()).toList(), builder);
	};
	
	/**
	 * 
	 * @param dispatcher
	 */
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher
		.register(Commands.literal("t2-marker")
				.requires(source -> {
					return source.hasPermission(2);
				})
				.then(Commands.argument("pos", BlockPosArgument.blockPos())
						.then(Commands.argument(CATEGORY_ARG, StringArgumentType.string())
								.suggests(SUGGEST_CATEGORY)
								.then(Commands.argument(NAME_ARG, ResourceLocationArgument.id())
										.suggests(SUGGEST_MARKER)
										.executes(source -> {
											return spawn(source.getSource(),
													BlockPosArgument.getLoadedBlockPos(source, "pos"),
													StringArgumentType.getString(source, CATEGORY_ARG),
													ResourceLocationArgument.getId(source, NAME_ARG));
										})
										)
								)
						)
				);
	}


	/**
	 * 
	 * @param source
	 * @param pos
	 * @param modID
	 * @param category
	 * @param name
	 * @param decay
	 * @return
	 */
	public static int spawn(CommandSourceStack source, BlockPos pos, String category, ResourceLocation name) {
		ServerLevel level = source.getLevel();
		Random random = new Random();

		Optional<IStructureCategory> structureCategory = TreasureApi.getStructureCategory(category);
		if (structureCategory.isEmpty()) {
			structureCategory = Optional.of(StructureCategory.TERRANEAN);
		}
		try {
			
			// get the template
			Optional<TemplateHolder> template = TreasureTemplateRegistry.getTemplate(name);
			if (template.isPresent()) {
				List<IRuinGenerator<GeneratorResult<ChestGeneratorData>>> generators =  RuinGeneratorRegistry.get(structureCategory.get());
				IRuinGenerator<GeneratorResult<ChestGeneratorData>> generator = generators.get(random.nextInt(generators.size()));
				Optional<GeneratorResult<ChestGeneratorData>> result = generator.generate(new WorldGenContext(level, level.getChunkSource().getGenerator(), random), new Coords(pos), template.get());
			}
			else {
				Treasure.LOGGER.debug("unable to locate ruin template -> {}", name);
			}
		}
		catch(Exception e) {
			Treasure.LOGGER.error("an error generating Treasure2 ruin occurred: ", e);
		}
		return 0;
	}
}