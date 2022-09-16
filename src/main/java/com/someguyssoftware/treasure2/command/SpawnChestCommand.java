/**
 * 
 */
package com.someguyssoftware.treasure2.command;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.someguyssoftware.gottschcore.loot.LootTableShell;
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.gottschcore.spatial.Heading;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.data.TreasureData;
import com.someguyssoftware.treasure2.enums.ChestGeneratorType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.enums.WorldGenerators;
import com.someguyssoftware.treasure2.generator.chest.IChestGenerator;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

/**
 * @author Mark Gottschling on Aug 28, 2020
 *
 */
public class SpawnChestCommand {
	private enum Chests {
		WOOD(TreasureBlocks.WOOD_CHEST),
		CRATE(TreasureBlocks.CRATE_CHEST),
		MOLDY_CRATE(TreasureBlocks.MOLDY_CRATE_CHEST),
		IRON_BOUND(TreasureBlocks.IRONBOUND_CHEST),
		PIRATE(TreasureBlocks.PIRATE_CHEST),
		IRON_STRONGBOX(TreasureBlocks.IRON_STRONGBOX),
		GOLD_STRONGBOX(TreasureBlocks.GOLD_STRONGBOX),
		SAFE(TreasureBlocks.SAFE),
		DREAD_PIRATE(TreasureBlocks.DREAD_PIRATE_CHEST),
		COMPRESSOR(TreasureBlocks.COMPRESSOR_CHEST),
		SPIDER(TreasureBlocks.SPIDER_CHEST), 
		VIKING(TreasureBlocks.VIKING_CHEST),
		CARDBOARD_BOX(TreasureBlocks.CARDBOARD_BOX),
		MILK_CRATE(TreasureBlocks.MILK_CRATE),
		WITHER(TreasureBlocks.WITHER_CHEST),
		SKULL(TreasureBlocks.SKULL_CHEST),
		GOLD_SKULL(TreasureBlocks.GOLD_SKULL_CHEST),
		CRYSTAL_SKULL_CHEST(TreasureBlocks.CRYSTAL_SKULL_CHEST),
		CAULDRON(TreasureBlocks.CAULDRON_CHEST);

		Block chest;

		Chests(Block chestBlock) {
			this.chest = chestBlock;
		}

		public static List<String> getNames() {
			List<String> names = EnumSet.allOf(Chests.class).stream().map(x -> x.name()).collect(Collectors.toList());
			return names;
		}
	}

	private static final SuggestionProvider<CommandSource> SUGGEST_RARITY = (source, builder) -> {
		return ISuggestionProvider.suggest(Rarity.getNames().stream(), builder);
	};

	private static final SuggestionProvider<CommandSource> SUGGEST_CHEST = (source, builder) -> {
		return ISuggestionProvider.suggest(TreasureData.CHESTS_BY_NAME.keySet().stream(), builder);
	};

	private static final SuggestionProvider<CommandSource> SUGGEST_DIRECTION = (source, builder) -> {    	
		return ISuggestionProvider.suggest(Heading.getNames().stream().filter(x -> !x.equalsIgnoreCase("UP") && !x.equalsIgnoreCase("DOWN")), builder);
	};

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher
		.register(Commands.literal("t2-chest")
				.requires(source -> {
					return source.hasPermission(2);
				})
				.then(Commands.argument("pos", BlockPosArgument.blockPos())
						.executes(source -> {
							return spawn(source.getSource(), BlockPosArgument.getOrLoadBlockPos(source, "pos"), "", Rarity.COMMON.name());
						})
						.then(Commands.argument("name", StringArgumentType.string())
								.suggests(SUGGEST_CHEST).executes(source -> {
									return spawn(source.getSource(), BlockPosArgument.getOrLoadBlockPos(source, "pos"),
											StringArgumentType.getString(source, "name"), Rarity.COMMON.name());
								})
								.then(Commands.argument("rarity", StringArgumentType.string())
										.suggests(SUGGEST_RARITY).executes(source -> {
											return spawn(source.getSource(), BlockPosArgument.getOrLoadBlockPos(source, "pos"),
													StringArgumentType.getString(source, "name"), StringArgumentType.getString(source, "rarity"));
										})
										.then(Commands.argument("direction", StringArgumentType.string())
												.suggests(SUGGEST_DIRECTION).executes(source -> {
													return spawn(source.getSource(), BlockPosArgument.getOrLoadBlockPos(source, "pos"), 
															StringArgumentType.getString(source, "name"), StringArgumentType.getString(source, "rarity"), StringArgumentType.getString(source, "direction"), false, false);							
												})
												.then(Commands.literal("locked")
														.executes(source -> {
															return spawn(source.getSource(), BlockPosArgument.getOrLoadBlockPos(source, "pos"),
																	StringArgumentType.getString(source, "name"), StringArgumentType.getString(source, "rarity"), StringArgumentType.getString(source, "direction"), true, false);
														})
														.then(Commands.literal("sealed")
																.executes(source -> {
																	return spawn(source.getSource(), BlockPosArgument.getOrLoadBlockPos(source, "pos"),
																			StringArgumentType.getString(source, "name"), StringArgumentType.getString(source, "rarity"), StringArgumentType.getString(source, "direction"), true, true);
																})
																)
														)
												.then(Commands.literal("sealed")
														.executes(source -> {
															return spawn(source.getSource(), BlockPosArgument.getOrLoadBlockPos(source, "pos"),
																	StringArgumentType.getString(source, "name"), StringArgumentType.getString(source, "rarity"), StringArgumentType.getString(source, "direction"), false, true);
														})
														.then(Commands.literal("locked")
																.executes(source -> {
																	return spawn(source.getSource(), BlockPosArgument.getOrLoadBlockPos(source, "pos"),
																			StringArgumentType.getString(source, "name"), StringArgumentType.getString(source, "rarity"), StringArgumentType.getString(source, "direction"), true, true);
																})
																)
														)
												)
										)
								)
						)
				);
	}


	private static int spawn(CommandSource source, BlockPos pos, String name, String rarityName) {
		return spawn(source, pos, name, rarityName, Heading.SOUTH.name(), false, false);
	}

	/**
	 * 
	 * @param source
	 * @param pos
	 * @param name
	 * @return
	 */
	private static int spawn(CommandSource source, BlockPos pos, String name, String rarityName, String directionName, boolean locked, boolean sealed) {
		Treasure.LOGGER.debug("executing spawn chest, pos -> {}, name -> {}, rarity -> {}", pos, name, rarityName);
		try {
			ServerWorld world = source.getLevel();
			Random random = new Random();
			Rarity rarity = rarityName.isEmpty() ? Rarity.COMMON : Rarity.valueOf(rarityName.toUpperCase());
			Heading heading = directionName.isEmpty() ? Heading.SOUTH : Heading.valueOf(directionName);
			Optional<Chests> chests = Optional.empty();

			// get the chest world generator
			IChestGenerator gen = null; // TODO
			if (chests.isPresent() && ChestGeneratorType.getNames().contains(chests.get().name())) {
				gen = ChestGeneratorType.valueOf(chests.get().name()).getChestGenerator();
			}
			else {
				RandomWeightedCollection<IChestGenerator> w = TreasureData.CHEST_GENS.get(rarity, WorldGenerators.SURFACE_CHEST);
				if (w == null) {
					Treasure.LOGGER.debug("weight collection is null");
				}
				gen = w.next();
			}
			Treasure.LOGGER.debug("gen -> {}", gen);

			// select loot table
			Optional<LootTableShell> lootTableShell = gen.selectLootTable2(random, rarity);
			Treasure.LOGGER.debug("loot table shell -> {}", lootTableShell);

			ResourceLocation lootTableResourceLocation = null;
			if (lootTableShell.isPresent()) {
				lootTableResourceLocation = lootTableShell.get().getResourceLocation();
			}
			if (lootTableResourceLocation == null) {
				Treasure.LOGGER.debug("Unable to select a LootTable for rarity -> {}", rarity);
				return 0;
			}

			AbstractChestBlock chestBlock = null;
			if (!StringUtils.isEmpty(name)) {
				chestBlock = (AbstractChestBlock) TreasureData.CHESTS_BY_NAME.get(name);
			}
			else {
				chestBlock = (AbstractChestBlock) TreasureBlocks.WOOD_CHEST;
			}
			if (chestBlock != null) {
				world.setBlockAndUpdate(pos, chestBlock.defaultBlockState().setValue(AbstractChestBlock.FACING, heading.getDirection()));
			}

			AbstractTreasureChestTileEntity tileEntity = (AbstractTreasureChestTileEntity) world.getBlockEntity(pos);
			if (tileEntity != null) {
				if (locked || sealed) {
					gen.addLootTable(tileEntity, lootTableResourceLocation);
					gen.addSeal(tileEntity);
				}

				gen.addGenerationContext(tileEntity, rarity);

				if (locked) {
					gen.addLocks(random, chestBlock, (AbstractTreasureChestTileEntity) tileEntity, rarity);
					(chestBlock).rotateLockStates(world, pos, Heading.NORTH.getRotation(heading));
					(tileEntity).sendUpdates();
				}
			}
		}
		catch(Exception e) {
			Treasure.LOGGER.error("an error occurred: ", e);
		}
		return 1;
	}
}
