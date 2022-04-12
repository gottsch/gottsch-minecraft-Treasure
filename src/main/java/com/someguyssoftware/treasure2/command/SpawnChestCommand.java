/**
 * 
 */
package com.someguyssoftware.treasure2.command;

import static com.someguyssoftware.treasure2.Treasure.LOGGER;
import com.someguyssoftware.treasure2.block.TreasureBlocks;


import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import com.someguyssoftware.gottschcore.enums.Direction;
import com.someguyssoftware.gottschcore.loot.LootTableShell;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.block.TreasureChestBlock;
import com.someguyssoftware.treasure2.enums.ChestGeneratorType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.enums.WorldGeneratorType;
import com.someguyssoftware.treasure2.generator.chest.IChestGenerator;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;
import com.someguyssoftware.treasure2.worldgen.SurfaceChestWorldGenerator;

import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Feb 10, 2017
 *
 */
public class SpawnChestCommand extends CommandBase {
	private static final String RARITY_ARG = "rarity";
	private static final String FACING_ARG = "facing";
	private static final String CHEST_ARG = "chest";
	private static final String LOCKED_ARG = "locked";
	private static final String SEALED_ARG = "sealed";
//	private static final String LOCKS_ARG = "locks";
//	private static final String LOOTTABLE_ARG = "loottable";
	
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
		CRYSTAL_SKULL(TreasureBlocks.CRYSTAL_SKULL_CHEST),
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
	
	@Override
	public String getName() {
		return "t2-chest";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/t2-chest <x> <y> <z> [-rarity <rarity>] [-facing <facing>] [-chest <chest>] [-locked] [-sealed]: generates a Treasure! chest at location (x,y,z)";
		// [-locks <lock1> <lock2> <lock3>] 
		//  [-loottable <loottable>]
	}

	/**
	 * Return the required permission level for this command.
	 */
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender commandSender, String[] args) {
		Treasure.LOGGER.debug("Starting to spawn Treasure! chest ...");
		try {

			int x, y, z = 0;
			x = getPositionValue(args[0], commandSender, 'x');
			y = getPositionValue(args[1], commandSender, 'y');
			z = getPositionValue(args[2], commandSender, 'z');

			String[] parserArgs = (String[]) Arrays.copyOfRange(args, 3, args.length);

			// create the parser
			CommandLineParser parser = new DefaultParser();

			// create Options object
			Options options = new Options();
			options.addOption(RARITY_ARG, true, "");
			options.addOption(FACING_ARG, true, "");
			options.addOption(CHEST_ARG, true, "");
			options.addOption(LOCKED_ARG, false, "");
			options.addOption(SEALED_ARG, false, "");			
			
			// parse the command line arguments
			CommandLine line = parser.parse(options, parserArgs);

			// get the rarity enum
			Rarity rarity = Rarity.COMMON;
			if (line.hasOption(RARITY_ARG)) {
				String rarityArg = line.getOptionValue(RARITY_ARG);
				try {
					rarity = Rarity.valueOf(rarityArg.toUpperCase());
				}
				catch(Exception e) {
					LOGGER.warn("unable to set rarity to -> {}, defaulting to {}", rarityArg, rarity);
				}
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("rarity -> {}", rarity);
			}
			
			// get the direction enum
			Direction direction = Direction.NORTH;
            if (line.hasOption(FACING_ARG)) {
                String directionArg = line.getOptionValue(FACING_ARG);
                try {
                	direction = Direction.valueOf(directionArg.toUpperCase());
                }
                catch(Exception e) {
                	LOGGER.warn("unable to set direction to -> {}, defaulting to NORTH", directionArg);
                }
            }
            if (LOGGER.isDebugEnabled()) {
            	LOGGER.debug("facing -> {}", direction);
            }
            
			// get the direction enum
            Optional<Chests> chests = Optional.empty();
            Block chest = null;
			if (line.hasOption(CHEST_ARG)) {
                String chestArg = line.getOptionValue(CHEST_ARG);
                try {
                	chests = Optional.of(Chests.valueOf(chestArg.toUpperCase()));
                }
                catch(Exception e) {
                	LOGGER.warn("unable to set chest to -> {}, defaulting based on rarity", chestArg);
                }
            }
            if (LOGGER.isDebugEnabled() && chests.isPresent()) {
            	LOGGER.debug("chest -> {}", chests.get());
            }

            // TODO get the loottable property
            // TODO get the locks property
            
			World world = commandSender.getEntityWorld();
			Random random = new Random();

			// get the chest world generator
			IChestGenerator gen = null;
			if (chests.isPresent() && ChestGeneratorType.getNames().contains(chests.get().name())) {
				gen = ChestGeneratorType.valueOf(chests.get().name()).getChestGenerator();
			}
			else {
			SurfaceChestWorldGenerator chestGens = (SurfaceChestWorldGenerator) Treasure.WORLD_GENERATORS
					.get(WorldGeneratorType.SURFACE_CHEST);
				// get the rarity chest generator
				gen = chestGens.getChestGenMap().get(rarity).next();
			}
			
			// select the chest
			if (chests.isPresent()) {
				chest = chests.get().chest;
			}
			else {
				chest = gen.selectChest(random, rarity);
			}
			
			// place the chest
			BlockPos pos = new BlockPos(x, y, z);
			world.setBlockState(pos, chest.getDefaultState().withProperty(TreasureChestBlock.FACING, direction.toFacing()));
			
			// select loot table
			// TODO determine if a loottable override has been specified
			Optional<LootTableShell> lootTableShell = gen.selectLootTable2(random, rarity);
			ResourceLocation lootTableResourceLocation = null;
			if (lootTableShell.isPresent()) {
				lootTableResourceLocation = lootTableShell.get().getResourceLocation();
			}
			if (lootTableResourceLocation == null) {
				LOGGER.debug("Unable to select a LootTable for rarity -> {}", rarity);
				return;
			}
			
			AbstractTreasureChestTileEntity tileEntity = (AbstractTreasureChestTileEntity) world.getTileEntity(pos);
			if (tileEntity != null) {
				// add the loot table
				if (line.hasOption(LOCKED_ARG) || line.hasOption(SEALED_ARG)) {
					gen.addLootTable((AbstractTreasureChestTileEntity) tileEntity, lootTableResourceLocation);
				
					// seal the chest
					gen.addSeal((AbstractTreasureChestTileEntity) tileEntity);
				}
				
				// update the backing tile entity's generation contxt
				gen.addGenerationContext((AbstractTreasureChestTileEntity) tileEntity, rarity);
				
				if (line.hasOption(LOCKED_ARG)) {
					// add the locks
					gen.addLocks(random, (AbstractChestBlock)chest, (AbstractTreasureChestTileEntity) tileEntity, rarity);
					// rotate the locks
					((AbstractChestBlock)chest).rotateLockStates(world, pos, Direction.NORTH.getRotation(direction));
					// update the client
					((AbstractTreasureChestTileEntity) tileEntity).sendUpdates();
				}
			}
			
		} catch (Exception e) {
			Treasure.LOGGER.error("Error generating Treasure! chest: ", e);
		}
	}

	/**
	 * Get a list of options for when the user presses the TAB key
	 */
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos targetPos) {
		if (args.length > 3) {
			if (args[args.length - 2].equals("-" + RARITY_ARG)) {
				return getListOfStringsMatchingLastWord(args, Rarity.getNames());
			}
            else if (args[args.length - 2].equals("-" + FACING_ARG)) {
				return getListOfStringsMatchingLastWord(args, Direction.getHorizontalNames());
            }
            else if (args[args.length - 2].equals("-" + CHEST_ARG)) {
				return getListOfStringsMatchingLastWord(args, Chests.getNames());
            }
		}
		return Collections.emptyList();
	}
	
	/**
	 * Return the x, y, or z position for the whoever issued the command.
	 * coordinateValue should be 'x', 'y', or 'z'.
	 */
	private int getOriginValue(ICommandSender commandSender, char coordName) {
		switch(coordName) {
			case 'z': return commandSender.getPosition().getZ();
			case 'y':	return commandSender.getPosition().getY();
			default:	return commandSender.getPosition().getX();
		}
	}
	
	/**
	 * Return the x, y, or z position based on the string entered.
	 * designed to detect relative positioning using ~.
	 * coordinateValue should be 'x', 'y', or 'z'.
	 */
	private int getPositionValue(String coordStr, ICommandSender commandSender, char coordName) {
		int value = 0;
		int origin = 0;
		
		// just ~ notation
		if (coordStr.equals("~")) {
			origin = getOriginValue (commandSender, coordName);
		}
		
		// ~ plus number notation
		else if (coordStr.charAt(0) == '~') {
			try {
				value = Integer.parseInt(coordStr.substring(1));
			}
			catch (Exception e) {
				Treasure.LOGGER.error("Error with " + coordName + " coordinate: ", e);
				value = 0;
			}
			origin = getOriginValue (commandSender, coordName);
		}
		// just number notation
		else {
			try {
				value = Integer.parseInt(coordStr);
			}
			catch (Exception e) {
				Treasure.LOGGER.error("Error with " + coordName + " coordinate: ", e);
				value = 0;
			}
		}
		return value + origin;		
	}
}
