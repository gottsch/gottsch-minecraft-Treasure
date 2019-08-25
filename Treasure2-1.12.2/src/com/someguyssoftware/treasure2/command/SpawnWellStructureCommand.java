/**
 * 
 */
package com.someguyssoftware.treasure2.command;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.Configs;
import com.someguyssoftware.treasure2.enums.Wells;
import com.someguyssoftware.treasure2.generator.GeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.well.WellGenerator;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Aug 20, 2019
 *
 */
public class SpawnWellStructureCommand extends CommandBase {

	@Override
	public String getName() {
		return "t2!wellstructure";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/t2!wellstructure <x> <y> <z> [well | -n <name>]: spawns a Treasure! wishing well at location (x,y,z)";
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
			
			if (player != null) {
    			World world = commandSender.getEntityWorld();
    			ICoords coords = new Coords(x, y, z);
    			Treasure.logger.debug("Starting to build Treasure! well at -> {}", coords.toShortString());

    			Random random = new Random();
    			WellGenerator gen = new WellGenerator();
    			GeneratorResult<GeneratorData> result = gen.generate(world, random, coords, Configs.wellConfig);
    			Treasure.logger.debug("Well start coords at -> {}", result.getData().getSpawnCoords().toShortString());
    		}
		}
		catch(Exception e) {
			player.sendMessage(new TextComponentString("Error:  " + e.getMessage()));
			Treasure.logger.error("Error generating Treasure! well:", e);
			e.printStackTrace();
		}
	}
}
