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
import com.someguyssoftware.treasure2.enums.Wells;
import com.someguyssoftware.treasure2.enums.WorldGenerators;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.chest.IChestGenerator;
import com.someguyssoftware.treasure2.generator.well.WellGenerator;
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
public class SpawnWellCommand {

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher
		.register(Commands.literal("t2-well")
				.requires(source -> {
					return source.hasPermissionLevel(2);
				})
				.then(Commands.argument("pos", BlockPosArgument.blockPos())
						.executes(source -> {
							return spawn(source.getSource(), BlockPosArgument.getBlockPos(source, "pos"), Treasure.MODID, Wells.WISHING_WELL.getValue());
						})
						.then(Commands.argument("modid", StringArgumentType.string())
								.suggests(SUGGEST_WELL).executes(source -> {
									return spawn(source.getSource(), BlockPosArgument.getBlockPos(source, "pos"),
											Treasure.MODID,
											Wells.WISHING_WELL.getValue());
								})
								.then(Commands.argument("name", StringArgumentType.string())
										.suggests(SUGGEST_MODID).executes(source -> {
											return spawn(source.getSource(), BlockPosArgument.getBlockPos(source, "pos"),
													StringArgumentType.getString(source, "modid"),
													StringArgumentType.getString(source, "name"));
										})
										)
								)
						)
				);
	}

	/**
	 * 
	 */
	private static final SuggestionProvider<CommandSource> SUGGEST_WELL = (source, builder) -> {
		return ISuggestionProvider.suggest(Wells.getNames().stream(), builder);
	};
	
	/**
	 * 
	 */
	private static final SuggestionProvider<CommandSource> SUGGEST_MODID = (source, builder) -> {
		return ISuggestionProvider.suggest(new String[] {Treasure.MODID}, builder);
	};

	/**
	 * NOTE modID and name are for future expansion where you can actually pick the well based on template name (not well name);
	 * @param source
	 * @param pos
	 * @param name
	 * @return
	 */
	private static int spawn(CommandSource source, BlockPos pos, String modID, String name) {
		Treasure.LOGGER.info("executing spawn well, pos -> {}", pos);

		try {
			ServerWorld world = source.getWorld();
			Random random = new Random();
			
			WellGenerator gen = (WellGenerator) TreasureData.WELL_GEN;
			GeneratorResult<GeneratorData> result = null;
			result = gen.generate(world, random, new Coords(pos), TreasureConfig.WELLS);

			if (result.isSuccess()) {
				Treasure.LOGGER.debug("Well start coords at -> {}", result.getData().getSpawnCoords().toShortString());
			}
			else {
				Treasure.LOGGER.debug("Well failed.");
			}
		}
		catch(Exception e) {
			Treasure.LOGGER.error("Error generating Treasure2! well:", e);
		}
		return 1;
	}
}