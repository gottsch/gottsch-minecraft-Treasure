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

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.world.WorldGenContext;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.generator.GeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.well.IWellGenerator;
import mod.gottsch.forge.treasure2.core.registry.TreasureTemplateRegistry;
import mod.gottsch.forge.treasure2.core.registry.WellGeneratorRegistry;
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
import net.minecraft.util.RandomSource;


/**
 * 
 * @author Mark Gottschling on Jan 25, 2018
 *
 */
public class SpawnWellCommand {

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
		.register(Commands.literal("t2-well")
				.requires(source -> {
					return source.hasPermission(2);
				})
				.then(Commands.argument("pos", BlockPosArgument.blockPos())
						.then(Commands.argument("name", ResourceLocationArgument.id())
								.suggests(SUGGEST_WELL)
								.executes(source -> {
									return spawn(source.getSource(), 
											BlockPosArgument.getLoadedBlockPos(source, "pos"),
											ResourceLocationArgument.getId(source, "name"));
								})
								)
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
	private static int spawn(CommandSourceStack source, BlockPos pos, ResourceLocation name) {
		Treasure.LOGGER.info("executing spawn well, pos -> {}", pos);

		try {
			ServerLevel world = source.getLevel();
			RandomSource random = world.getRandom();

			// get the template
			Optional<TemplateHolder> template = TreasureTemplateRegistry.getTemplate(name);
			if (template.isPresent()) {
				List<IWellGenerator<GeneratorResult<GeneratorData>>> generators =  WellGeneratorRegistry.get(StructureCategory.TERRANEAN);
				IWellGenerator<GeneratorResult<GeneratorData>> generator = generators.get(random.nextInt(generators.size()));
				Optional<GeneratorResult<GeneratorData>> result = generator.generate(new WorldGenContext(world, world.getChunkSource().getGenerator(), random), new Coords(pos), template.get());
			}
			else {
				Treasure.LOGGER.debug("unable to locate well template -> {}", name);
			}
		}
		catch(Exception e) {
			Treasure.LOGGER.error("error generating Treasure2 well:", e);
		}
		return 1;
	}
}