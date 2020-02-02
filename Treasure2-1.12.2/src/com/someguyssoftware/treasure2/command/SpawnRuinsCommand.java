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
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.gottschcore.world.gen.structure.IDecayRuleSet;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.IChestConfig;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.enums.WorldGenerators;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.chest.IChestGenerator;
import com.someguyssoftware.treasure2.meta.StructureArchetype;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateHolder;
import com.someguyssoftware.treasure2.world.gen.structure.TreasureTemplateManager;
import com.someguyssoftware.treasure2.worldgen.SurfaceChestWorldGenerator;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Jan 25, 2018
 *
 */
public class SpawnRuinsCommand extends CommandBase {
	private static final String MOD_ID_ARG = "modid";
	private static final String ARCHETYPE_ARG = "archetype";
	private static final String NAME_ARG = "name";
	private static final String RULESET_ARG = "ruleset";
	private static final String RARITY_ARG = "rarity";
	
	@Override
	public String getName() {
		return "t2-ruins";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/t2-ruins <x> <y> <z> [-modid <mod id> -archetype <archetype> -name <name>] [-ruleset <relative filepath>] [-rarity <rarity>]: spawns ruins at location (x, y, z)";
	}

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel() {
        return 2;
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

			String[] parserArgs = (String[]) Arrays.copyOfRange(args, 3, args.length);
			
			// create the parser
			CommandLineParser parser = new DefaultParser();			

			// create Options object
			Options options = new Options();
			options.addOption(MOD_ID_ARG, true, "");
			options.addOption(ARCHETYPE_ARG, true, "");
			options.addOption(NAME_ARG, true, "");
			options.addOption(RULESET_ARG, true, "");
			options.addOption(RARITY_ARG, true, "");
			
			// parse the command line arguments
			CommandLine line = parser.parse( options, parserArgs);

			String modID = Treasure.MODID;
			if (line.hasOption(MOD_ID_ARG)) {
				modID = line.getOptionValue(MOD_ID_ARG);
			}
			
			String archetype = StructureArchetype.SURFACE.getValue();
			if (line.hasOption(ARCHETYPE_ARG)) {
				archetype = line.getOptionValue(ARCHETYPE_ARG).toLowerCase();
			}
			
			String name = line.getOptionValue(NAME_ARG);
			if (name != null && !name.contains(".nbt")) {
				name += ".nbt";
			}
			
			// get the ruleset to use from the decay manager
			IDecayRuleSet ruleSet = null;
			if (line.hasOption(RULESET_ARG)) {
				String ruleSetName = line.getOptionValue(RULESET_ARG);
				if (!ruleSetName.contains(".json")) {
					ruleSetName += ".json";
				}
				// build the key
				String key = (Treasure.MODID + ":" + "decay/" + ruleSetName).replace("\\", "/");
				ruleSet = Treasure.DECAY_MANAGER.getRuleSetMap().get(key);
			}
			
			Rarity rarity = null;
			if (line.hasOption(RARITY_ARG)) {
				String rarityArg = line.getOptionValue(RARITY_ARG);
				rarity = Rarity.valueOf(rarityArg.toUpperCase());			
			}	
			
			World world = commandSender.getEntityWorld();
			Random random = new Random();
			ICoords coords = new Coords(x, y, z);
			
			// get the structure generator
			SurfaceChestWorldGenerator worldGen = 
					(SurfaceChestWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGenerators.SURFACE_CHEST);
			
			// build the template key
			ResourceLocation templateKey = new ResourceLocation(Treasure.MODID + ":" + Treasure.TEMPLATE_MANAGER.getBaseResourceFolder()
							+ "/" + modID + "/" + archetype	+ "/" + name);
			
			TemplateHolder holder = Treasure.TEMPLATE_MANAGER.getTemplatesByResourceLocationMap().get(templateKey);
			if (holder == null) {
				Treasure.logger.debug("Unable to locate template by key -> {}", templateKey.toString());
			}
				
			// select a chest
			if (rarity == null) {
				rarity = Rarity.values()[random.nextInt(Rarity.values().length)];
			}
//			IChestConfig config = TreasureConfig.CHESTS.surfaceChests.configMap.get(rarity);
			
			// generate
			GeneratorResult<ChestGeneratorData> result = worldGen.generateSurfaceRuins(world, random,coords, holder, ruleSet, null);
			Treasure.logger.debug("result from t2-ruins -> {}", result);
			if (result.isSuccess() && result.getData().getChestCoords() != null) {
				IChestGenerator chestGen = worldGen.getChestGenMap().get(rarity).next();
				ICoords chestCoords = result.getData().getChestCoords();
				Treasure.logger.debug("chestCoords -> {}", chestCoords);
				// move the chest coords to the first dry land beneath it.
				chestCoords = WorldInfo.getDryLandSurfaceCoords(world, chestCoords);
				if (chestCoords == WorldInfo.EMPTY_COORDS) chestCoords = null;
				
				if (chestCoords != null) {
					GeneratorResult<ChestGeneratorData> chestResult = chestGen.generate(world, random, chestCoords, rarity, result.getData().getChestState());
				}
			}
		}
		catch(Exception e) {
			Treasure.logger.error("Error generating Treasure! ruins:", e);
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
        	else if(args[args.length - 2].equals("-" + ARCHETYPE_ARG)) {
        		return getListOfStringsMatchingLastWord(args, StructureArchetype.getNames());
        	}        	
        }		
		return Collections.emptyList();
    }
}
