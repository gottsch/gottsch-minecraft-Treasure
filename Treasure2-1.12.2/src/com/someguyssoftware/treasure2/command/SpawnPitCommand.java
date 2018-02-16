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
import com.someguyssoftware.treasure2.enums.Rarity;
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
 * @author Mark Gottschling on Jan 25, 2018
 *
 */
public class SpawnPitCommand extends CommandBase {

	@Override
	public String getName() {
		return "treasurepit";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/treasurepit <x> <y> <z> [rarity | -n <name>]: spawns a Treasure! pit at location (x,y,z)";
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
    			ChestWorldGenerator chestGen = new ChestWorldGenerator();
    			chestGen.getGenerators().get(rarity).generate(world, random, new Coords(x, y, z), rarity, Configs.chestConfigs.get(rarity)); 
//    			// get surface pos
//    			ICoords surfaceCoords = WorldInfo.getDryLandSurfaceCoords(world, new Coords(x, 254, z));
//    			ICoords spawnCoords = new Coords(x, y, z);
//    			SimplePitGenerator pitGen = new SimplePitGenerator();
//    			boolean isGenerated = pitGen.generate(world, random, surfaceCoords, spawnCoords);
//    			AbstractTreasureChestTileEntity chest = null;
//    			if (isGenerated) {
//    				world.setBlockState(spawnCoords.toPos() , TreasureBlocks.WOOD_CHEST.getDefaultState(), 3);
//    				chest = (AbstractTreasureChestTileEntity) world.getTileEntity(spawnCoords.toPos());
//    			}
//    			Treasure.logger.debug("Chest TE @ {} {}", spawnCoords.toPos(), chest);
//    			if (chest != null) {
//    				// query to load the selected rarity chests
//    				List<LootContainer> containers = DbManager.getInstance().getContainersByRarity(rarity);
//
//    				// TODO create a new chestpopulator - simple populates a chest
////    				ChestPopulator pop = new ChestPopulator(chestSheet);
//
//    				if (containers != null)
//    					Treasure.logger.debug("Containers found:" + containers.size());
//					if (containers != null && !containers.isEmpty()) {
//						// add each container to the random prob collection
//						for (LootContainer c : containers) {
//							Treasure.logger.info("Selected chest container:" + c);
//						}
//						// get the first container
//						LootContainer c = containers.get(0);
////						RandomProbabilityCollection<ChestContainer> chestProbCol = new RandomProbabilityCollection<>(containers);
////						// select a container
////						ChestContainer container = (ChestContainer) chestProbCol.next();		
//						Treasure.logger.info("Chosen chest container:" + c);
//						InventoryPopulator pop = new InventoryPopulator();
//						pop.populate(chest.getInventoryProxy(), c);
//					}
//    			}
    		}
		}
		catch(Exception e) {
			player.sendMessage(new TextComponentString("Error:  " + e.getMessage()));
			Treasure.logger.error("Error generating Treasure! chest:", e);
			e.printStackTrace();
		}
	}
}
