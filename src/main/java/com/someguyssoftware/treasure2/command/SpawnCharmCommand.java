/**
 * 
 */
package com.someguyssoftware.treasure2.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.someguyssoftware.gottschcore.loot.LootTableShell;
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.data.TreasureData;
import com.someguyssoftware.treasure2.enums.ChestGeneratorType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.enums.WorldGenerators;
import com.someguyssoftware.treasure2.generator.chest.IChestGenerator;
import com.someguyssoftware.treasure2.item.CharmItem;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ItemArgument;
import net.minecraft.command.arguments.ItemInput;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.server.ServerWorld;

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
						.then(Commands.argument("charm",ResourceLocationArgument.id())
								.suggests(SUGGEST_CHARM)
								.executes((source) -> {
									return giveCharm(source.getSource(), ResourceLocationArgument.getId(source, "charm"), EntityArgument.getPlayers(source, "targets"), 1);
								})

								//					.then(Commands.argument("pos", BlockPosArgument.blockPos())
								//							.executes(source -> {
								//								return spawn(source.getSource(), BlockPosArgument.getOrLoadBlockPos(source, "pos"), "");
								//							})
								//							.then(Commands.argument("name", StringArgumentType.string())
								//									.suggests(SUGGEST_CHARM).executes(source -> {
								//										return spawn(source.getSource(), BlockPosArgument.getOrLoadBlockPos(source, "pos"),
								//												StringArgumentType.getString(source, "name"));
								//									})
								//							)
								//					)
								)
						)
				);
	}

	private static final SuggestionProvider<CommandSource> SUGGEST_CHARM = (source, builder) -> {
		return ISuggestionProvider.suggest(Arrays.asList("treasure2:copper_charm", "treasure2:silver_charm", "treasure2:gold_charm").stream(), builder);
	};

	private static int giveCharm(CommandSource source, ResourceLocation charmLocation, Collection<ServerPlayerEntity> players, int p_198497_3_) throws CommandSyntaxException {
		Treasure.LOGGER.debug("spawn charm resource location -> {}", charmLocation);
		//		ResourceLocation charmLocation = new ResourceLocation(charmItemName);
		Item item = Registry.ITEM.getOptional(charmLocation).orElseThrow(IllegalStateException::new);
		if (item instanceof CharmItem) {
			for(ServerPlayerEntity player : players) {
				ItemStack stack = new ItemStack(item, 1);
				// TODO get capability
				// TODO add random charms


				boolean flag = player.inventory.add(stack);
//				 TODO look at give for rest of it.
			}
		}
		return 1;
	}
}
