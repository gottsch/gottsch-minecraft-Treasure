/**
 * 
 */
package com.someguyssoftware.treasure2.command;

import java.util.Random;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.world.gen.structure.IDecayRuleSet;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.IChestConfig;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.enums.WorldGenerators;
import com.someguyssoftware.treasure2.worldgen.SurfaceChestWorldGenerator;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Jan 25, 2018
 *
 */
public class SpawnRuinsCommand extends CommandBase {
	private static final String NAME_ARG = "name";
	private static final String RULESET_ARG = "ruleset";
	
	@Override
	public String getName() {
		return "t2-ruins";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/t2-ruins <x> <y> <z> [-ruleset <relative filepath>]: spawns ruins at location (x, y, z)";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender commandSender, String[] args) {
		Treasure.logger.debug("Starting to build Treasure! ruins ...");
		
		try {
			// extract the coords args
			int x, y, z = 0;
			x = Integer.parseInt(args[0]);
			y = Integer.parseInt(args[1]);
			z = Integer.parseInt(args[2]);

			// set the coords args to blank (so the cli parser doesn't puke on any negative values - thinks they are arguments
			args[0] = args[1] = args[2] = "";

			// create the parser
			CommandLineParser parser = new DefaultParser();			

			// create Options object
			Options options = new Options();
			options.addOption(NAME_ARG, true, "");
			options.addOption(RULESET_ARG, true, "");

			// parse the command line arguments
			CommandLine line = parser.parse( options, args );

			String name = line.getOptionValue(NAME_ARG);
			String ruleSetName = line.getOptionValue(RULESET_ARG);

			World world = commandSender.getEntityWorld();

			Random random = new Random();
			ICoords coords = new Coords(x, y, z);

			// get the structure generator
			SurfaceChestWorldGenerator worldGen = 
					(SurfaceChestWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGenerators.SURFACE_CHEST);

			// get the ruleset to use from the decay manager
			IDecayRuleSet ruleSet = null;
			if (ruleSetName != null && !ruleSetName.equals("")) {
				if (!ruleSetName.contains(".json")) {
					ruleSetName += ".json";
				}
				// build the key
				String key = (Treasure.MODID + ":" + "decay/" + ruleSetName).replace("\\", "/");
				ruleSet = Treasure.DECAY_MANAGER.getRuleSetMap().get(key);
			}
			Rarity rarity = Rarity.values()[random.nextInt(Rarity.values().length)];
			IChestConfig config = TreasureConfig.CHESTS.surfaceChests.configMap.get(rarity);
			// generate
			worldGen.generateSurfaceRuins(world, random,coords, ruleSet, config);
			}
		catch(Exception e) {
			Treasure.logger.error("Error generating Treasure! ruins:", e);
		}
	}
}
