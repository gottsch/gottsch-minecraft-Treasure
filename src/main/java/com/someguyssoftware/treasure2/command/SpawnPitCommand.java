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

import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.enums.WorldGeneratorType;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.chest.IChestGenerator;
import com.someguyssoftware.treasure2.worldgen.SurfaceChestWorldGenerator;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Jan 25, 2018
 *
 */
public class SpawnPitCommand extends CommandBase {
	private static final String RARITY_ARG = "rarity";
	
	@Override
	public String getName() {
		return "t2-pit";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/t2-pit <x> <y> <z> [-rarity <rarity>]: spawns a Treasure! pit at location (x,y,z)";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender commandSender, String[] args) {
		Treasure.logger.debug("Starting to build Treasure! pit ...");
		
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
			
			Rarity rarity = Rarity.COMMON;
			if (line.hasOption(RARITY_ARG)) {
				String rarityArg = line.getOptionValue(RARITY_ARG);
				rarity = Rarity.valueOf(rarityArg.toUpperCase());
			}
			
			Treasure.logger.debug("Rarity:" + rarity + "; " + rarity.ordinal());
			World world = commandSender.getEntityWorld();

			Random random = new Random();
			GeneratorResult<ChestGeneratorData> result = SurfaceChestWorldGenerator.generatePit(world, random, rarity, new Coords(x, y, z), TreasureConfig.CHESTS.surfaceChests.configMap.get(rarity));
			if (result.isSuccess()) {
				SurfaceChestWorldGenerator chestGens = (SurfaceChestWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGeneratorType.SURFACE_CHEST);
				IChestGenerator gen = chestGens.getChestGenMap().get(rarity).next();
				ICoords chestCoords = result.getData().getChestContext().getCoords();
				if (chestCoords != null) {
					GeneratorResult<ChestGeneratorData> chestResult = gen.generate(world, random, chestCoords, rarity, result.getData().getChestContext().getState());
				}
			}			
		}
		catch(Exception e) {
			Treasure.logger.error("Error generating Treasure! chest:", e);
		}
	}
	
    /**
     * Get a list of options for when the user presses the TAB key
     */
	@Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length > 3) {
        	if (args[args.length - 2].equals("-" + RARITY_ARG)) {
        		return getListOfStringsMatchingLastWord(args, Rarity.getNames());
        	}
        }		
		return Collections.emptyList();
    }
}
