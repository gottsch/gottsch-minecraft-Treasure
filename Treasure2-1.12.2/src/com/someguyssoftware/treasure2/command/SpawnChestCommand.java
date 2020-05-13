/**
 * 
 */
package com.someguyssoftware.treasure2.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import com.someguyssoftware.gottschcore.loot.LootTable;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.enums.WorldGenerators;
import com.someguyssoftware.treasure2.generator.chest.IChestGenerator;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;
import com.someguyssoftware.treasure2.worldgen.SurfaceChestWorldGenerator;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Feb 10, 2017
 *
 */
public class SpawnChestCommand extends CommandBase {
	private static final String RARITY_ARG = "rarity";

	@Override
	public String getName() {
		return "t2-chest";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/t2-chest <x> <y> <z> [-rarity <rarity>]: generates a Treasure! chest at location (x,y,z)";
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
		Treasure.logger.debug("Starting to spawn Treasure! chest ...");
		try {

			int x, y, z = 0;
			x = Integer.parseInt(args[0]);
			y = Integer.parseInt(args[1]);
			z = Integer.parseInt(args[2]);

			String[] parserArgs = (String[]) Arrays.copyOfRange(args, 3, args.length);

			// create the parser
			CommandLineParser parser = new DefaultParser();

			// create Options object
			Options options = new Options();
			options.addOption(RARITY_ARG, true, "");

			// parse the command line arguments
			CommandLine line = parser.parse(options, parserArgs);

			// get the rarity enum
			Rarity rarity = Rarity.COMMON;
			if (line.hasOption(RARITY_ARG)) {
				String rarityArg = line.getOptionValue(RARITY_ARG);
				rarity = Rarity.valueOf(rarityArg.toUpperCase());
			}
			Treasure.logger.debug("Rarity:" + rarity + "; " + rarity.ordinal());

			// if (player != null) {
			World world = commandSender.getEntityWorld();
			Random random = new Random();

			// get the chest world generator
			SurfaceChestWorldGenerator chestGens = (SurfaceChestWorldGenerator) Treasure.WORLD_GENERATORS
					.get(WorldGenerators.SURFACE_CHEST);
			// get the rarity chest generator
			IChestGenerator gen = chestGens.getChestGenMap().get(rarity).next();
			BlockPos pos = new BlockPos(x, y, z);

			AbstractChestBlock chest = gen.selectChest(random, rarity);

			world.setBlockState(pos, chest.getDefaultState());
			AbstractTreasureChestTileEntity tileEntity = (AbstractTreasureChestTileEntity) world.getTileEntity(pos);

			if (tileEntity != null) {

				// get the loot table
				LootTable lootTable = gen.selectLootTable(new Random(), rarity);

				if (lootTable == null) {
					Treasure.logger.warn("Unable to select a lootTable for rarity -> {}", rarity);
				}

				Treasure.logger.debug("Generating loot from loot table for rarity {}", rarity);
				List<ItemStack> stacks = lootTable.generateLootFromPools(new Random(),
						Treasure.LOOT_TABLES.getContext());
				Treasure.logger.debug("Generated loot:");
				for (ItemStack stack : stacks) {
					Treasure.logger.debug(stack.getDisplayName());
				}

				lootTable.fillInventory((IInventory) tileEntity, new Random(), Treasure.LOOT_TABLES.getContext());
			}
		} catch (Exception e) {
			Treasure.logger.error("Error generating Treasure! chest: ", e);
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
		}
		return Collections.emptyList();
	}
}
