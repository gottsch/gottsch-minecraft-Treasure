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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.registry.TreasureTemplateRegistry;
import mod.gottsch.forge.treasure2.core.structure.TemplateHolder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;


/**
 * Usage: /t2-ruins <x> <y> <z> [-modid <mod id> -archetype <archetype> -name <name>] [-decay <relative filepath>] [-rarity <rarity>]
 * Spawns ruins at location (x, y, z) using the optional structure name, and option decay ruleset.
 * @author Mark Gottschling on Jan 25, 2018
 *
 */
public class SpawnRuinsCommand {
//	private static final String MOD_ID_ARG = "modid";
//	private static final String ARCHETYPE_ARG = "archetype";
//	private static final String NAME_ARG = "name";
////	private static final String DECAY_ARG = "decay";
//	private static final String RARITY_ARG = "rarity";
//	
//	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
//		dispatcher
//			.register(Commands.literal("t2-ruins")
//					.requires(source -> {
//						return source.hasPermission(2);
//					})
//					.then(Commands.argument("pos", BlockPosArgument.blockPos())
//							.executes(source -> {
//								return spawn(source.getSource(), BlockPosArgument.getOrLoadBlockPos(source, "pos"), Treasure.MODID, StructureArchetype.SURFACE.getValue(), "", "");
//							})
//							.then(Commands.literal("-"+MOD_ID_ARG)
//								.then(Commands.argument(MOD_ID_ARG, StringArgumentType.string())
//										.suggests(SUGGEST_MODID).executes(source -> {
//											return spawn(source.getSource(),
//													BlockPosArgument.getOrLoadBlockPos(source, "pos"),
//													StringArgumentType.getString(source, MOD_ID_ARG),
//													StructureArchetype.SURFACE.getValue(),
//													"",
//													"");
//										})
//										.then(Commands.literal("-"+ARCHETYPE_ARG)
//												.then(Commands.argument(ARCHETYPE_ARG, StringArgumentType.string())
//														.suggests(SUGGEST_ARCHETYPE)
//														.executes(source -> {
//															return spawn(source.getSource(),
//																	BlockPosArgument.getOrLoadBlockPos(source, "pos"),
//																	StringArgumentType.getString(source, MOD_ID_ARG),
//																	StringArgumentType.getString(source, ARCHETYPE_ARG),
//			    													"",
//			    													"");														
//														})
//														.then(Commands.literal("-"+NAME_ARG)
//																.then(Commands.argument(NAME_ARG, StringArgumentType.string())
//																		.executes(source -> {
//																			return spawn(source.getSource(),
//																					BlockPosArgument.getLoadedBlockPos(source, "pos"),
//																					StringArgumentType.getString(source, MOD_ID_ARG),
//																					StringArgumentType.getString(source, ARCHETYPE_ARG),
//							    													StringArgumentType.getString(source, NAME_ARG),
//							    													"");
//																		})
//																		
//																)
//														)
//												)
//										)
//								)
//							)
//                             )
//			);
//	}
//    
//	// TODO read all the loaded mods OR all the registered mods in TreasureTemplateRegistry
//    private static final SuggestionProvider<CommandSourceStack> SUGGEST_MODID = (source, builder) -> {
//		return SharedSuggestionProvider.suggest(Arrays.asList("minecraft", "treasure2").stream(), builder);
//    };
//    
//    // TODO where does this go? In the arugment class
//    private static final SuggestionProvider<CommandSourceStack> SUGGEST_ARCHETYPE = (source, builder) -> {
//		return SharedSuggestionProvider.suggest(StructureArchetype.getNames().stream(), builder);
//    };
//    
//	/**
//	 * 
//	 * @param source
//	 * @param pos
//	 * @param modID
//	 * @param archetype
//	 * @param name
//	 * @param decay
//	 * @return
//	 */
//	public static int spawn(CommandSourceStack source, BlockPos pos, String modID, String archetype, String name, String decay) {
//		ServerLevel world = source.getLevel();
//		Random random = new Random();
//		
//        modID = (modID == null || modID.isEmpty()) ? Treasure.MODID : modID;
//        archetype = (archetype == null || archetype.isEmpty()) ? StructureArchetype.SURFACE.getValue() : archetype;
//        
//        if (name == null || name.isEmpty()) {
//        	return 0;
//        }
//
//        try {
//		// TODO for now just use the surface ruins generator
//		IRuinGenerator<GeneratorResult<ChestGeneratorData>> ruinGenerator = new SurfaceRuinGenerator();
//		
//       ResourceLocation templateKey = new ResourceLocation(modID, archetype.toLowerCase()	+ "/" + name);
//
//		TemplateHolder holder = TreasureTemplateRegistry.getManager().getTemplatesByResourceLocationMap().get(templateKey);
//		if (holder == null) {
//			Treasure.LOGGER.debug("Unable to locate template by key -> {}", templateKey.toString());
//		}
//        
//		// TODO check/add decay set
//		GeneratorResult<ChestGeneratorData> result = ruinGenerator.generate(world, null, random, new Coords(pos), holder);
//        }
//		catch(Exception e) {
//			Treasure.LOGGER.error("an error occurred: ", e);
//		}
//		return 0;
//	}
}