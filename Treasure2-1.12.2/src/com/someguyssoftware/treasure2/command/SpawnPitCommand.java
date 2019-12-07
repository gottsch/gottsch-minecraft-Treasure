/**
 * 
 */
package com.someguyssoftware.treasure2.command;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.enums.WorldGenerators;
import com.someguyssoftware.treasure2.generator.chest.IChestGenerator;
import com.someguyssoftware.treasure2.worldgen.SurfaceChestWorldGenerator;

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
public class SpawnPitCommand extends CommandBase {

	@Override
	public String getName() {
		return "t2-pit";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/t2-pit <x> <y> <z> [rarity]: spawns a Treasure! pit at location (x,y,z)";
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
    			//BlockPos pos = new BlockPos(x, y, z);
//    			SurfaceChestWorldGenerator chestGen = new SurfaceChestWorldGenerator();
//    			chestGen.getGenerators().get(rarity).generate(world, random, new Coords(x, y, z), rarity, Configs.chestConfigs.get(rarity)); 
    			SurfaceChestWorldGenerator chestGens = (SurfaceChestWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGenerators.SURFACE_CHEST);
    			IChestGenerator gen = chestGens.getChestGenMap().get(rarity).next();
    			gen.generate(world, random, new Coords(x, y, z), rarity, null);
    		}
		}
		catch(Exception e) {
			player.sendMessage(new TextComponentString("Error:  " + e.getMessage()));
			Treasure.logger.error("Error generating Treasure! chest:", e);
			e.printStackTrace();
		}
	}
}
