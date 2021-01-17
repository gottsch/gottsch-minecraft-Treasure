/**
 * 
 */
package com.someguyssoftware.treasure2.command;

import java.util.Map;
import java.util.Random;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.data.TreasureData;
import com.someguyssoftware.treasure2.enums.PitTypes;
import com.someguyssoftware.treasure2.enums.Pits;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.enums.WorldGenerators;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.chest.IChestGenerator;
import com.someguyssoftware.treasure2.generator.pit.IPitGenerator;
import com.someguyssoftware.treasure2.world.gen.feature.TreasureFeatures;

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
	private static final String PIT_ARG = "pit";

	
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher
			.register(Commands.literal("t2-pit")
					.requires(source -> {
						return source.hasPermissionLevel(2);
					})
					.then(Commands.argument("pos", BlockPosArgument.blockPos())
							.executes(source -> {
								return spawn(source.getSource(), BlockPosArgument.getBlockPos(source, "pos"), Rarity.COMMON.name(), "");
							})
							.then(Commands.argument(RARITY_ARG, StringArgumentType.string())
									.suggests(SUGGEST_RARITY).executes(source -> {
										return spawn(source.getSource(), BlockPosArgument.getBlockPos(source, "pos"),
												StringArgumentType.getString(source, RARITY_ARG), "");
									})
									// TODO what if you want both RARITY AND PIT - i think there is a subcommand for this
							)
							.then(Commands.argument(PIT_ARG, StringArgumentType.string())
									.suggests(SUGGEST_PIT).executes(source -> {
										return spawn(source.getSource(), BlockPosArgument.getBlockPos(source, "pos"),
												"", StringArgumentType.getString(source, PIT_ARG));
									})
							)
					)
			);
	}

	/**
	 * 
	 */
	private static final SuggestionProvider<CommandSource> SUGGEST_RARITY = (source, builder) -> {
		return ISuggestionProvider.suggest(Pits.getNames().stream(), builder);
	};
	
	private static final SuggestionProvider<CommandSource> SUGGEST_PIT = (source, builder) -> {
		return ISuggestionProvider.suggest(Rarity.getNames().stream(), builder);
	};
	/**
	 * 
	 * @param source
	 * @param pos
	 * @param name
	 * @return
	 */
	private static int spawn(CommandSource source, BlockPos pos, String rarityName, String pitName) {
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
						
			IPitGenerator<GeneratorResult<ChestGeneratorData>> pitGenerator = null;			
			Pits pit = null;
			if (pitName != null && !pitName.isEmpty()) {
				pit = Pits.valueOf(pitName.toUpperCase());
				// TODO if no pit type is selected, then randomly choose pit type (will need to check both if doesn't exist for one)
				// for now just get standard pits ie no structures
				Map<Pits, IPitGenerator<GeneratorResult<ChestGeneratorData>>> pitGenMap =TreasureData.PIT_GENS.row(PitTypes.STANDARD);
				pitGenerator = pitGenMap.get(pit);
			}
			
			// TODO want chest?
			// TODO want markers or structure?
			
			
			// get surface coords
			ICoords coords = new Coords(pos);
			ICoords surfaceCoords = WorldInfo.getDryLandSurfaceCoords(world, 
					new Coords(coords.getX(), WorldInfo.getHeightValue(world, coords), coords.getZ()));
			
			// TODO check if coords.Y >= surface.Y. if so, error out.
			
//			GeneratorResult<ChestGeneratorData> result = TreasureFeatures.SURFACE_CHEST_FEATURE.generatePit(world, random, rarity, new Coords(pos), TreasureConfig.CHESTS.surfaceChests.configMap.get(rarity));
			GeneratorResult<ChestGeneratorData> result = pitGenerator.generate(world, random, surfaceCoords, coords);
			if (result.isSuccess()) {
				IChestGenerator generator = TreasureData.CHEST_GENS.get(rarity, WorldGenerators.SURFACE_CHEST).next();
				ICoords chestCoords = result.getData().getChestContext().getCoords();
				if (chestCoords != null) {
					GeneratorResult<ChestGeneratorData> chestResult = generator.generate(world, random, chestCoords, rarity, result.getData().getChestContext().getState());
					Treasure.LOGGER.debug("pit chest result -> {}", chestResult);
				}
				Treasure.LOGGER.debug("pit result -> {}", result);
			}			
		}
		catch(Exception e) {
			Treasure.LOGGER.error("an error occurred: ", e);
		}
		return 1;
	}
}