/**
 * 
 */
package mod.gottsch.forge.treasure2.core.command;

import java.util.Optional;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import mod.gottsch.forge.gottschcore.loot.LootTableShell;

import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.Heading;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.AbstractTreasureChestBlock;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.generator.GeneratorType;
import mod.gottsch.forge.treasure2.core.generator.chest.IChestGenerator;
import mod.gottsch.forge.treasure2.core.registry.ChestRegistry;
import mod.gottsch.forge.treasure2.core.registry.WeightedChestGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author Mark Gottschling on Aug 28, 2020
 *
 */
public class SpawnChestCommand {
//	private enum Chests {
//		WOOD(TreasureBlocks.WOOD_CHEST.get()),
//		CRATE(TreasureBlocks.CRATE_CHEST.get()),
//		MOLDY_CRATE(TreasureBlocks.MOLDY_CRATE_CHEST.get()),
//		IRON_BOUND(TreasureBlocks.IRONBOUND_CHEST.get()),
//		PIRATE(TreasureBlocks.PIRATE_CHEST.get()),
//		IRON_STRONGBOX(TreasureBlocks.IRON_STRONGBOX.get()),
//		GOLD_STRONGBOX(TreasureBlocks.GOLD_STRONGBOX.get()),
//		SAFE(TreasureBlocks.SAFE.get()),
//		DREAD_PIRATE(TreasureBlocks.DREAD_PIRATE_CHEST.get()),
//		COMPRESSOR(TreasureBlocks.COMPRESSOR_CHEST.get()),
//		SPIDER(TreasureBlocks.SPIDER_CHEST.get()), 
//		VIKING(TreasureBlocks.VIKING_CHEST.get()),
//		CARDBOARD_BOX(TreasureBlocks.CARDBOARD_BOX.get()),
//		MILK_CRATE(TreasureBlocks.MILK_CRATE.get()),
//		WITHER(TreasureBlocks.WITHER_CHEST.get()),
//		SKULL(TreasureBlocks.SKULL_CHEST.get()),
//		GOLD_SKULL(TreasureBlocks.GOLD_SKULL_CHEST.get()),
//		CRYSTAL_SKULL_CHEST(TreasureBlocks.CRYSTAL_SKULL_CHEST.get()),
//		CAULDRON(TreasureBlocks.CAULDRON_CHEST.get());
//
//		Block chest;
//
//		Chests(Block chestBlock) {
//			this.chest = chestBlock;
//		}
//
//		public static List<String> getNames() {
//			List<String> names = EnumSet.allOf(Chests.class).stream().map(x -> x.name()).collect(Collectors.toList());
//			return names;
//		}
//	}

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
						.then(Commands.argument("name", StringArgumentType.string())
								.suggests(SUGGEST_CHEST).executes(source -> {
									return spawn(source.getSource(), BlockPosArgument.getLoadedBlockPos(source, "pos"),
											StringArgumentType.getString(source, "name"), Rarity.COMMON.name());
								})
								.then(Commands.argument("rarity", StringArgumentType.string())
										.suggests(SUGGEST_RARITY).executes(source -> {
											return spawn(source.getSource(), BlockPosArgument.getLoadedBlockPos(source, "pos"),
													StringArgumentType.getString(source, "name"), StringArgumentType.getString(source, "rarity"));
										})
										.then(Commands.argument("direction", StringArgumentType.string())
												.suggests(SUGGEST_DIRECTION).executes(source -> {
													return spawn(source.getSource(), BlockPosArgument.getLoadedBlockPos(source, "pos"), 
															StringArgumentType.getString(source, "name"), StringArgumentType.getString(source, "rarity"), StringArgumentType.getString(source, "direction"), false, false);							
												})
												.then(Commands.literal("locked")
														.executes(source -> {
															return spawn(source.getSource(), BlockPosArgument.getLoadedBlockPos(source, "pos"),
																	StringArgumentType.getString(source, "name"), StringArgumentType.getString(source, "rarity"), StringArgumentType.getString(source, "direction"), true, false);
														})
														.then(Commands.literal("sealed")
																.executes(source -> {
																	return spawn(source.getSource(), BlockPosArgument.getLoadedBlockPos(source, "pos"),
																			StringArgumentType.getString(source, "name"), StringArgumentType.getString(source, "rarity"), StringArgumentType.getString(source, "direction"), true, true);
																})
																)
														)
												.then(Commands.literal("sealed")
														.executes(source -> {
															return spawn(source.getSource(), BlockPosArgument.getLoadedBlockPos(source, "pos"),
																	StringArgumentType.getString(source, "name"), StringArgumentType.getString(source, "rarity"), StringArgumentType.getString(source, "direction"), false, true);
														})
														.then(Commands.literal("locked")
																.executes(source -> {
																	return spawn(source.getSource(), BlockPosArgument.getLoadedBlockPos(source, "pos"),
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
			Rarity rarity = rarityName.isEmpty() ? Rarity.COMMON : Rarity.valueOf(rarityName.toUpperCase());
			Heading heading = directionName.isEmpty() ? Heading.SOUTH : Heading.valueOf(directionName);
			ResourceLocation chestLocation = ModUtil.asLocation(chestName);
			Optional<RegistryObject<Block>> optionalChest = ChestRegistry.getChest(chestLocation);
			AbstractTreasureChestBlock chest;
			
			IChestGenerator generator = WeightedChestGeneratorRegistry.getNextGenerator(rarity, GeneratorType.TERRESTRIAL);
			if (optionalChest.isEmpty()) {
				chest = generator.selectChest(random, rarity);
			} else {
				chest = (AbstractTreasureChestBlock) optionalChest.get().get();
			}
			
			// select a  chest if it hasn't been provided
			
			Treasure.LOGGER.debug("gen -> {}", generator);

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

			AbstractTreasureChestBlock chestBlock = null;
			if (!StringUtils.isEmpty(name)) {
				chestBlock = (AbstractChestBlock) TreasureData.CHESTS_BY_NAME.get(name);
			}
			else {
				chestBlock = (AbstractTreasureChestBlock) TreasureBlocks.WOOD_CHEST.get();
			}
			if (chestBlock != null) {
				world.setBlockAndUpdate(pos, chestBlock.defaultBlockState().setValue(AbstractChestBlock.FACING, heading.getDirection()));
			}

			AbstractTreasureChestBlockEntity tileEntity = (AbstractTreasureChestBlockEntity) world.getBlockEntity(pos);
			if (tileEntity != null) {
				if (locked || sealed) {
					gen.addLootTable(tileEntity, lootTableResourceLocation);
					gen.addSeal(tileEntity);
				}

				gen.addGenerationContext(tileEntity, rarity);

				if (locked) {
					gen.addLocks(random, chestBlock, (AbstractTreasureChestBlockEntity) tileEntity, rarity);
					(chestBlock).rotateLockStates(world, new Coords(pos), Heading.NORTH.getRotation(heading));
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
