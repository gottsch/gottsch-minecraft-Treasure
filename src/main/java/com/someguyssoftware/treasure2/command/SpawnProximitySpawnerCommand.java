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
import com.someguyssoftware.gottschcore.measurement.Quantity;
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.gottschcore.tileentity.ProximitySpawnerTileEntity;
import com.someguyssoftware.treasure2.Treasure;
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
import net.minecraft.command.arguments.EntitySummonArgument;
import net.minecraft.command.arguments.SuggestionProviders;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.DungeonHooks;

/**
 * @author Mark Gottschling on Aug 28, 2020
 *
 */
public class SpawnProximitySpawnerCommand {
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
	
	private static final SuggestionProvider<CommandSource> SUGGEST_CHEST = (source, builder) -> {
		return ISuggestionProvider.suggest(TreasureData.CHESTS_BY_NAME.keySet().stream(), builder);
	};

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher
			.register(Commands.literal("t2-proximity-spawner")
					.requires(source -> {
						return source.hasPermission(2);
					})
					.then(Commands.argument("pos", BlockPosArgument.blockPos())
							.executes(source -> {
								return spawn(source.getSource(), BlockPosArgument.getOrLoadBlockPos(source, "pos"), null);
							})
//							.then(Commands.argument("mob", StringArgumentType.string())
//									/*.suggests(SUGGEST_CHEST)*/.executes(source -> {
//										return spawn(source.getSource(), BlockPosArgument.getOrLoadBlockPos(source, "pos"),
//												StringArgumentType.getString(source, "mob"));
//									})
//							)
							.then(Commands.argument("entity", EntitySummonArgument.id()).suggests(SuggestionProviders.SUMMONABLE_ENTITIES).executes((source) -> {
						         return spawn(source.getSource(), BlockPosArgument.getOrLoadBlockPos(source, "pos"), EntitySummonArgument.getSummonableEntity(source, "entity"));
						      })
							
					)
					// TODO add min/max mobs and proximity
			));
	}

	/**
	 * 
	 * @param source
	 * @param pos
	 * @param name
	 * @return
	 */
	private static int spawn(CommandSource source, BlockPos pos, ResourceLocation name) { //String name) {
		Treasure.LOGGER.info("executing spawn poximity spawner, pos -> {}, name -> {}", pos, name);
		try {
			ServerWorld world = source.getLevel();
			Random random = new Random();

			// TODO take into account null value for name
	    	world.setBlock(pos, TreasureBlocks.PROXIMITY_SPAWNER.defaultBlockState(), 3);
	    	ProximitySpawnerTileEntity te = (ProximitySpawnerTileEntity) world.getBlockEntity(pos);
	    	if (te != null) {
	 	    	ResourceLocation mobLoc =name;// new ResourceLocation(name);
		    	Treasure.LOGGER.debug("using mob -> {} for poximity spawner.", mobLoc.toString());
		    	te.setMobName(mobLoc);
		    	te.setMobNum(new Quantity(1, 5));
		    	te.setProximity(5D);
	    	}
	    	else {
	    		Treasure.LOGGER.debug("unable to generate proximity spawner at -> {}",pos);
	    	}
		}
		catch(Exception e) {
			Treasure.LOGGER.error("an error occurred: ", e);
		}
		return 1;
	}
}
