/**
 * 
 */
package com.someguyssoftware.treasure2.command;

import java.util.List;
import java.util.Random;

import org.apache.commons.compress.changes.ChangeSetPerformer;

import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.lootbuilder.db.DbManager;
import com.someguyssoftware.lootbuilder.inventory.InventoryPopulator;
import com.someguyssoftware.lootbuilder.model.LootContainer;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.config.Configs;
import com.someguyssoftware.treasure2.enums.Pits;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.generator.pit.IPitGenerator;
import com.someguyssoftware.treasure2.generator.pit.SimplePitGenerator;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;
import com.someguyssoftware.treasure2.worldgen.ChestWorldGenerator;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Mar 8, 2018
 *
 */
public class SpawnPitOnlyCommand extends CommandBase {

	@Override
	public String getName() {
		return "trpit";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/trpit <x> <y> <z> [pit]: spawns a Treasure! pit at location (x,y,z)";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender commandSender, String[] args) {
		EntityPlayer player = (EntityPlayer) commandSender.getCommandSenderEntity();
		try {

			int x, y, z = 0;
			x = Integer.parseInt(args[0]);
			y = Integer.parseInt(args[1]);
			z = Integer.parseInt(args[2]);
			
			String pitName = "";
			if (args.length > 3) {
				pitName = args[3];
			}
			
			if (pitName.equals("")) pitName = Pits.SIMPLE_PIT.name();
			Pits pit = Pits.valueOf(pitName.toUpperCase());
			Treasure.logger.debug("Pit:" + pit + "; " + pit.ordinal());
			
			if (player != null) {
    			World world = commandSender.getEntityWorld();
    			Treasure.logger.debug("Starting to build Treasure! pit ...");

    			Random random = new Random();
    			ICoords spawnCoords = new Coords(x, y, z);
    			ICoords surfaceCoords = WorldInfo.getDryLandSurfaceCoords(world, spawnCoords);
    			ChestWorldGenerator chestGen = new ChestWorldGenerator();
    			IPitGenerator pitGen = chestGen.pitGenerators.get(pit);
    			pitGen.generate(world, random, surfaceCoords , spawnCoords);
    		}
		}
		catch(Exception e) {
			player.sendMessage(new TextComponentString("Error:  " + e.getMessage()));
			Treasure.logger.error("Error generating Treasure! pit:", e);
			e.printStackTrace();
		}
	}
}
