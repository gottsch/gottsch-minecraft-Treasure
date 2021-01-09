/**
 * 
 */
package com.someguyssoftware.treasure2.command;

import java.util.Random;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.data.TreasureData;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.enums.WorldGenerators;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.chest.IChestGenerator;
import com.someguyssoftware.treasure2.worldgen.feature.TreasureFeatures;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

/**
 * 
 * @author Mark Gottschling on Jan 25, 2018
 *
 */
public class SpawnPitCommand {
	private static final String RARITY_ARG = "rarity";
	
//	@Override
//	public String getName() {
//		return "t2-pit";
//	}
//
//	@Override
//	public String getUsage(ICommandSender var1) {
//		return "/t2-pit <x> <y> <z> [-rarity <rarity>]: spawns a Treasure! pit at location (x,y,z)";
//	}
	
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher
			.register(Commands.literal("t2-pit")
					.requires(source -> {
						return source.hasPermissionLevel(2);
					})
					.then(Commands.argument("pos", BlockPosArgument.blockPos())
							.executes(source -> {
								return spawn(source.getSource(), BlockPosArgument.getBlockPos(source, "pos"), Rarity.COMMON.name());
							})
							.then(Commands.argument("rarity", StringArgumentType.string())
									.suggests(SUGGEST_CHEST).executes(source -> {
										return spawn(source.getSource(), BlockPosArgument.getBlockPos(source, "pos"),
												StringArgumentType.getString(source, "rarity"));
									})
							)
					)
			);
	}

	/**
	 * 
	 */
	private static final SuggestionProvider<CommandSource> SUGGEST_CHEST = (source, builder) -> {
		return ISuggestionProvider.suggest(Rarity.getNames().stream(), builder);
	};
	
	/**
	 * 
	 * @param source
	 * @param pos
	 * @param name
	 * @return
	 */
	private static int spawn(CommandSource source, BlockPos pos, String rarityName) {
		Treasure.LOGGER.info("executing spawn pit, pos -> {}, name -> {}", pos, rarityName);
		
		try {
			ServerWorld world = source.getWorld();
			Random random = new Random();
			
			Rarity rarity = null;
			if (rarityName != null && !rarityName.isEmpty()) {
				rarity = Rarity.getByValue(rarityName.toLowerCase());
			}
			if (rarity == null) {
				rarity = Rarity.COMMON;
			}			
			Treasure.LOGGER.debug("Rarity:" + rarity);
			
			GeneratorResult<ChestGeneratorData> result = TreasureFeatures.SURFACE_CHEST_FEATURE.generatePit(world, random, rarity, new Coords(pos), TreasureConfig.CHESTS.surfaceChests.configMap.get(rarity));
			if (result.isSuccess()) {
				IChestGenerator generator = TreasureData.CHEST_GENS.get(rarity, WorldGenerators.SURFACE_CHEST).next();
//				SurfaceChestWorldGenerator chestGens = (SurfaceChestWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGeneratorType.SURFACE_CHEST);
//				IChestGenerator gen = chestGens.getChestGenMap().get(rarity).next();
				ICoords chestCoords = result.getData().getChestContext().getCoords();
				if (chestCoords != null) {
					GeneratorResult<ChestGeneratorData> chestResult = generator.generate(world, random, chestCoords, rarity, result.getData().getChestContext().getState());
					Treasure.LOGGER.debug("pit chest result -> {}", chestResult);
				}
			}			
		}
		catch(Exception e) {
			Treasure.LOGGER.error("an error occurred: ", e);
		}
		return 1;
	}
	
//	@Override
//	public void execute(MinecraftServer server, ICommandSender commandSender, String[] args) {
//		Treasure.LOGGER.debug("Starting to build Treasure! pit ...");
//		
//		try {
//			int x, y, z = 0;
//			x = Integer.parseInt(args[0]);
//			y = Integer.parseInt(args[1]);
//			z = Integer.parseInt(args[2]);
//			
//			String[] parserArgs = (String[]) Arrays.copyOfRange(args, 3, args.length);
//			
//			// create the parser
//			CommandLineParser parser = new DefaultParser();
//
//			// create Options object
//			Options options = new Options();
//			options.addOption(RARITY_ARG, true, "");
//
//			// parse the command line arguments
//			CommandLine line = parser.parse(options, parserArgs);
//			
//			Rarity rarity = Rarity.COMMON;
//			if (line.hasOption(RARITY_ARG)) {
//				String rarityArg = line.getOptionValue(RARITY_ARG);
//				rarity = Rarity.valueOf(rarityArg.toUpperCase());
//			}
//			
//			Treasure.LOGGER.debug("Rarity:" + rarity + "; " + rarity.ordinal());
//			World world = commandSender.getEntityWorld();
//
//			Random random = new Random();
//			GeneratorResult<ChestGeneratorData> result = SurfaceChestWorldGenerator.generatePit(world, random, rarity, new Coords(x, y, z), TreasureConfig.CHESTS.surfaceChests.configMap.get(rarity));
//			if (result.isSuccess()) {
//				SurfaceChestWorldGenerator chestGens = (SurfaceChestWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGeneratorType.SURFACE_CHEST);
//				IChestGenerator gen = chestGens.getChestGenMap().get(rarity).next();
//				ICoords chestCoords = result.getData().getChestContext().getCoords();
//				if (chestCoords != null) {
//					GeneratorResult<ChestGeneratorData> chestResult = gen.generate(world, random, chestCoords, rarity, result.getData().getChestContext().getState());
//				}
//			}			
//		}
//		catch(Exception e) {
//			Treasure.LOGGER.error("Error generating Treasure! chest:", e);
//		}
//	}

}