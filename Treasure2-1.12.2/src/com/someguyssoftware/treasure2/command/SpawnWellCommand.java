/**
 * 
 */
package com.someguyssoftware.treasure2.command;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.Configs;
import com.someguyssoftware.treasure2.enums.Wells;
import com.someguyssoftware.treasure2.worldgen.WellWorldGenerator;

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
public class SpawnWellCommand extends CommandBase {

	@Override
	public String getName() {
		return "t2well";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/t2well <x> <y> <z> [well | -n <name>]: spawns a Treasure! wishing well at location (x,y,z)";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender commandSender, String[] args) {
		EntityPlayer player = (EntityPlayer) commandSender.getCommandSenderEntity();
		try {

			int x, y, z = 0;
			x = Integer.parseInt(args[0]);
			y = Integer.parseInt(args[1]);
			z = Integer.parseInt(args[2]);
			
			String wellName = "";
			if (args.length > 3) {
				wellName = args[3];
			}
			
			if (wellName.equals("")) wellName = Wells.WISHING_WELL.name();
			Wells well = Wells.valueOf(wellName.toUpperCase());
			Treasure.logger.debug("Well:" + well + "; " + well.ordinal());
			
			if (player != null) {
    			World world = commandSender.getEntityWorld();
    			Treasure.logger.debug("Starting to build Treasure! well ...");

    			Random random = new Random();
    			//BlockPos pos = new BlockPos(x, y, z);
    			WellWorldGenerator wellGen = new WellWorldGenerator();
    			wellGen.getGenerators().get(well).generate(world, random, new Coords(x, y, z), Configs.wellConfigs.get(well)); 
    		}
		}
		catch(Exception e) {
			player.sendMessage(new TextComponentString("Error:  " + e.getMessage()));
			Treasure.logger.error("Error generating Treasure! well:", e);
			e.printStackTrace();
		}
	}
}
