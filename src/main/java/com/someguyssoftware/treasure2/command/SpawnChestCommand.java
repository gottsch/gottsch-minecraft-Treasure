/**
 * 
 */
package com.someguyssoftware.treasure2.command;

import org.apache.commons.lang3.StringUtils;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.data.TreasureData;

import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

/**
 * @author Mark Gottschling on Aug 28, 2020
 *
 */
public class SpawnChestCommand {

	private static final SuggestionProvider<CommandSource> SUGGEST_CHEST = (source, builder) -> {
		return ISuggestionProvider.suggest(TreasureData.CHESTS_BY_NAME.keySet().stream(), builder);
	};

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher
			.register(Commands.literal("t2-chest")
					.requires(source -> {
						return source.hasPermissionLevel(2);
					})
					.then(Commands.argument("pos", BlockPosArgument.blockPos())
							.executes(source -> {
								return spawn(source.getSource(), BlockPosArgument.getBlockPos(source, "pos"), "");
							})
							.then(Commands.argument("name", StringArgumentType.string())
									.suggests(SUGGEST_CHEST).executes(source -> {
										return spawn(source.getSource(), BlockPosArgument.getBlockPos(source, "pos"),
												StringArgumentType.getString(source, "name"));
									})
							)
					)
			);
	}

	private static int spawn(CommandSource source, BlockPos pos, String name) {
		try {
			ServerWorld world = source.getWorld();
			Block chestBlock = null;
			if (!StringUtils.isEmpty(name)) {
				chestBlock = TreasureData.CHESTS_BY_NAME.get(name);
			}
			else {
				chestBlock = TreasureBlocks.WOOD_CHEST;
			}
			if (chestBlock != null) {
				world.setBlockState(pos, chestBlock.getDefaultState());
			}
		}
		catch(Exception e) {
			Treasure.LOGGER.error(e);
		}
		return 1;
	}
}
