/*
 * This file is part of  Treasure2.
 * Copyright (c) 2020 Mark Gottschling (gottsch)
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
import java.util.Random;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import mod.gottsch.forge.gottschcore.loot.LootTableShell;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.Heading;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.AbstractTreasureChestBlock;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity.GenerationContext;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.generator.GeneratorType;
import mod.gottsch.forge.treasure2.core.generator.chest.IChestGenerator;
import mod.gottsch.forge.treasure2.core.registry.ChestRegistry;
import mod.gottsch.forge.treasure2.core.registry.RarityLevelWeightedChestGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author Mark Gottschling on Aug 28, 2020
 *
 */
public class SpawnChestCommand {
	
	private static final String NAME = "name";

	private static final SuggestionProvider<CommandSourceStack> SUGGEST_RARITY = (source, builder) -> {
		return SharedSuggestionProvider.suggest(Rarity.getNames().stream(), builder);
	};

	private static final SuggestionProvider<CommandSourceStack> SUGGEST_CHEST = (source, builder) -> {
		return SharedSuggestionProvider.suggest(ChestRegistry.getNames().stream().map(c -> c.toString()), builder);
	};

	private static final SuggestionProvider<CommandSourceStack> SUGGEST_DIRECTION = (source, builder) -> {    	
		return SharedSuggestionProvider.suggest(Heading.getNames().stream().filter(x -> !x.equalsIgnoreCase("UP") && !x.equalsIgnoreCase("DOWN")), builder);
	};

	/**
	 * 
	 * @param dispatcher
	 */
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher
		.register(Commands.literal("t2-chest")
				.requires(source -> {
					return source.hasPermission(2);
				})
				.then(Commands.argument("pos", BlockPosArgument.blockPos())
						.executes(source -> {
							return spawn(source.getSource(), BlockPosArgument.getLoadedBlockPos(source, "pos"), "", Rarity.COMMON.name());
						})
						.then(Commands.argument(NAME, ResourceLocationArgument.id())
								.suggests(SUGGEST_CHEST)
								.executes(source -> {
									return spawn(source.getSource(), BlockPosArgument.getLoadedBlockPos(source, "pos"),
											ResourceLocationArgument.getId(source, NAME).toString(), Rarity.COMMON.name());
								})
								.then(Commands.argument("rarity", StringArgumentType.string())
										.suggests(SUGGEST_RARITY)
										.executes(source -> {
											return spawn(source.getSource(), BlockPosArgument.getLoadedBlockPos(source, "pos"),
													ResourceLocationArgument.getId(source, NAME).toString(), StringArgumentType.getString(source, "rarity"));
										})
										.then(Commands.argument("direction", StringArgumentType.string())
												.suggests(SUGGEST_DIRECTION).executes(source -> {
													return spawn(source.getSource(), BlockPosArgument.getLoadedBlockPos(source, "pos"), 
															ResourceLocationArgument.getId(source, NAME).toString(), StringArgumentType.getString(source, "rarity"), StringArgumentType.getString(source, "direction"), false, false);							
												})
												.then(Commands.literal("locked")
														.executes(source -> {
															return spawn(source.getSource(), BlockPosArgument.getLoadedBlockPos(source, "pos"),
																	ResourceLocationArgument.getId(source, NAME).toString(), StringArgumentType.getString(source, "rarity"), StringArgumentType.getString(source, "direction"), true, false);
														})
														.then(Commands.literal("sealed")
																.executes(source -> {
																	return spawn(source.getSource(), BlockPosArgument.getLoadedBlockPos(source, "pos"),
																			ResourceLocationArgument.getId(source, NAME).toString(), StringArgumentType.getString(source, "rarity"), StringArgumentType.getString(source, "direction"), true, true);
																})
																)
														)
												.then(Commands.literal("sealed")
														.executes(source -> {
															return spawn(source.getSource(), BlockPosArgument.getLoadedBlockPos(source, "pos"),
																	ResourceLocationArgument.getId(source, NAME).toString(), StringArgumentType.getString(source, "rarity"), StringArgumentType.getString(source, "direction"), false, true);
														})
														.then(Commands.literal("locked")
																.executes(source -> {
																	return spawn(source.getSource(), BlockPosArgument.getLoadedBlockPos(source, "pos"),
																			ResourceLocationArgument.getId(source, NAME).toString(), StringArgumentType.getString(source, "rarity"), StringArgumentType.getString(source, "direction"), true, true);
																})
																)
														)
												)
										)
								)
						)
				);
	}


	private static int spawn(CommandSourceStack source, BlockPos pos, String chestName, String rarityName) {
		return spawn(source, pos, chestName, rarityName, Heading.SOUTH.name(), false, false);
	}

	/**
	 * 
	 * @param source
	 * @param pos
	 * @param name
	 * @return
	 */
	private static int spawn(CommandSourceStack source, BlockPos pos, String chestName, String rarityName, String directionName, boolean locked, boolean sealed) {
		Treasure.LOGGER.debug("executing spawn chest, pos -> {}, name -> {}, rarity -> {}", pos, chestName, rarityName);
		try {
			ServerLevel world = source.getLevel();
			Random random = new Random();
			// TODO fetch from the registry
			Rarity rarity = rarityName.isEmpty() ? Rarity.COMMON : Rarity.valueOf(rarityName.toUpperCase());
			Heading heading = directionName.isEmpty() ? Heading.SOUTH : Heading.valueOf(directionName);
			ResourceLocation chestLocation = ModUtil.asLocation(chestName);
			Optional<RegistryObject<Block>> optionalChest = ChestRegistry.getChest(chestLocation);
			AbstractTreasureChestBlock chest;
			
			// TODO provide Genertor by name in command (no weighted option)
			// TODO provide GeneratorType option in command
			IChestGenerator generator = RarityLevelWeightedChestGeneratorRegistry.getNextGenerator(rarity, FeatureType.TERRESTRIAL);
			if (optionalChest.isEmpty()) {
				chest = generator.selectChest(random, rarity);
			} else {
				chest = (AbstractTreasureChestBlock) optionalChest.get().get();
			}
			
			// select a  chest if it hasn't been provided
			
			Treasure.LOGGER.debug("generator -> {}", generator);

			// select loot table
			Optional<LootTableShell> lootTableShell = generator.selectLootTable(random, rarity);
			Treasure.LOGGER.debug("loot table shell -> {}", lootTableShell);

			ResourceLocation lootTableResourceLocation = null;
			if (lootTableShell.isPresent()) {
				lootTableResourceLocation = lootTableShell.get().getResourceLocation();
			}
			if (lootTableResourceLocation == null) {
				Treasure.LOGGER.debug("Unable to select a LootTable for rarity -> {}", rarity);
				return 0;
			}

			AbstractTreasureChestBlock chestBlock = chest;
			if (chestBlock != null) {
				world.setBlockAndUpdate(pos, chestBlock
						.defaultBlockState()
						.setValue(AbstractTreasureChestBlock.FACING, heading.getDirection())
						.setValue(AbstractTreasureChestBlock.DISCOVERED, false)	// TODO add command argument	
						);
			}

			AbstractTreasureChestBlockEntity blockEntity = (AbstractTreasureChestBlockEntity) world.getBlockEntity(pos);
			if (blockEntity != null) {				
				if (locked || sealed) {
					generator.addLootTable(blockEntity, lootTableResourceLocation);
					generator.addSeal(blockEntity);
				}
				generator.addGenerationContext(blockEntity, rarity);
				blockEntity.getGenerationContext().setFeatureType(FeatureType.TERRESTRIAL);

				if (locked) {
					generator.addLocks(random, chestBlock, (AbstractTreasureChestBlockEntity) blockEntity, rarity);
					(chestBlock).rotateLockStates(world, new Coords(pos), Heading.NORTH.getRotation(heading));
					(blockEntity).sendUpdates();
				}
			}
		}
		catch(Exception e) {
			Treasure.LOGGER.error("an error occurred: ", e);
		}
		return 1;
	}
}
