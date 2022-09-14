/**
 * 
 */
package com.someguyssoftware.treasure2.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.item.TreasureItems;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * 
 * @author Mark Gottschling on Aug 20, 2021
 *
 */
public class SpawnCharmCommand {

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher
		.register(Commands.literal("t2-charm")
				.requires(source -> {
					return source.hasPermission(2);
				})
				.then(Commands.argument("targets", EntityArgument.players())
						.then(Commands.argument("charm", StringArgumentType.string())
								.suggests(SUGGEST_CHARM)
								.executes((source) -> {
									return giveCharm(source.getSource(), StringArgumentType.getString(source, "charm"), EntityArgument.getPlayers(source, "targets"), 1, false);
								})
								.then(Commands.argument("level", IntegerArgumentType.integer())
										.executes(source -> {
											return giveCharm(source.getSource(), StringArgumentType.getString(source, "charm"), EntityArgument.getPlayers(source, "targets"), 
													IntegerArgumentType.getInteger(source, "level"), false);
										})
										.then(Commands.literal("book")
												.executes(source -> {
													return giveCharm(source.getSource(), StringArgumentType.getString(source, "charm"), EntityArgument.getPlayers(source, "targets"), 
															IntegerArgumentType.getInteger(source, "level"), true);
												})
												)
										)

								)
						)
				);
	}

	private static final SuggestionProvider<CommandSource> SUGGEST_CHARM = (source, builder) -> {
		// TODO make some sort of registry list of all charm types
		List<String> charms = Arrays.asList("aegis", "cheat_death", "decay", "decrepit", "dirt_fill", "dirt_walk", "drain", 
				"fire_immunity", "fire_resistence", "greater_healing", "healing", "illumination", "life_strike", "reflection",
				"ruin", "satiety", "shielding");
		return ISuggestionProvider.suggest(charms.stream(), builder);
	};

	/**
	 * 
	 * @param source
	 * @param charmTypeName
	 * @param players
	 * @param level
	 * @param isBook
	 * @return
	 * @throws CommandSyntaxException
	 */
	private static int giveCharm(CommandSource source, String charmTypeName, Collection<ServerPlayerEntity> players, int level, boolean isBook) throws CommandSyntaxException {
		Treasure.LOGGER.debug("spawn charm type -> {}", charmTypeName);

		Optional<ItemStack> charmStack = TreasureItems.getCharm(charmTypeName, level, isBook ? 1 : 0);
		if (!charmStack.isPresent()) {			
			return 1;
		}

		ItemStack stack = charmStack.get();
		ServerPlayerEntity player = source.getPlayerOrException();
		if (!player.inventory.add(stack)) {
			player.drop(stack, false);
		}
		return 1;
	}
}
