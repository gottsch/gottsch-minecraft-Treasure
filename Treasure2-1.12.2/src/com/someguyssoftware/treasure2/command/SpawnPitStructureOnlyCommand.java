/**
 * 
 */
package com.someguyssoftware.treasure2.command;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.Configs;
import com.someguyssoftware.treasure2.enums.PitTypes;
import com.someguyssoftware.treasure2.enums.Pits;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.generator.pit.IPitGenerator;
import com.someguyssoftware.treasure2.generator.pit.StructurePitGenerator;
import com.someguyssoftware.treasure2.worldgen.ChestWorldGenerator;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Jan 25, 2018
 *
 */
public class SpawnPitStructureOnlyCommand extends CommandBase {

	@Override
	public String getName() {
		return "t2-pitstructureonly";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/t2-pitstructureonly <x> <y> <z> [rarity]: spawns a Treasure! pit structure at location (x,y,z)";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender commandSender, String[] args) {
		EntityPlayer player = (EntityPlayer) commandSender.getCommandSenderEntity();
		try {

			int x, y, z = 0;
			x = Integer.parseInt(args[0]);
			y = Integer.parseInt(args[1]);
			z = Integer.parseInt(args[2]);
			
			String rarityName = "";
			if (args.length > 3) {
				rarityName = args[3];
			}
			
			if (rarityName.equals("")) rarityName = Rarity.COMMON.name();
			Rarity rarity = Rarity.valueOf(rarityName.toUpperCase());
			Treasure.logger.debug("Rarity:" + rarity + "; " + rarity.ordinal());
			
			if (player != null) {
    			World world = commandSender.getEntityWorld();
    			Treasure.logger.debug("Starting to build Treasure! pit ...");

    			Random random = new Random();
//    			ChestWorldGenerator chestGen = new ChestWorldGenerator();
//    			chestGen.getGenerators().get(rarity).generate(world, random, new Coords(x, y, z), rarity, Configs.chestConfigs.get(rarity));
    			ICoords spawnCoords = new Coords(x, y, z);
    			ICoords surfaceCoords = WorldInfo.getDryLandSurfaceCoords(world, new Coords(x, WorldInfo.getHeightValue(world, spawnCoords), z));

    			// select a pit generator
//    			Pits pit = Pits.values()[random.nextInt(Pits.values().length)];
    			IPitGenerator pitGenerator = null;
    			List<IPitGenerator> pitGenerators = ChestWorldGenerator.pitGens.row(PitTypes.STRUCTURE).values().stream()
    					.collect(Collectors.toList());
    			pitGenerator = pitGenerators.get(random.nextInt(pitGenerators.size()));
    					
//				IPitGenerator parentPit = ((List<IPitGenerator>)ChestWorldGenerator.structurePitGenerators.values()).get(random.nextInt(ChestWorldGenerator.structurePitGenerators.size()));
//				// create a new pit instance (new instance as it contains state)
//				pitGenerator = new StructurePitGenerator(ChestWorldGenerator.structurePitGenerators.get(parentPit));
				pitGenerator.generate(world, random, surfaceCoords , spawnCoords);
			}
		}
		catch(Exception e) {
			player.sendMessage(new TextComponentString("Error:  " + e.getMessage()));
			Treasure.logger.error("Error generating Treasure! chest:", e);
			e.printStackTrace();
		}
	}
}
