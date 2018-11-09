/**
 * 
 */
package com.someguyssoftware.treasure2.command;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.generator.chest.CommonChestGenerator;
import com.someguyssoftware.treasure2.loot.TreasureLootTable;
import com.someguyssoftware.treasure2.loot.TreasureLootTables;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTable;

/**
 * 
 * @author Mark Gottschling on Feb 10, 2017
 *
 */
public class TreasureChestCommand extends CommandBase {

	@Override
	public String getName() {
		return "treasurechest";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/treasurechest <x> <y> <z> [rarity | -n <name>]: generates a Treasure! chest at location (x,y,z)";
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
    			Treasure.logger.debug("Starting to build Treasure! chest ...");

    			BlockPos pos = new BlockPos(x, y, z);
    			world.setBlockState(pos , TreasureBlocks.WOOD_CHEST.getDefaultState());
    			AbstractTreasureChestTileEntity chest = (AbstractTreasureChestTileEntity) world.getTileEntity(pos);
				
    			if (chest != null) {
//    				// query to load the selected rarity chests
//    				List<LootContainer> containers = DbManager.getInstance().getContainersByRarity(rarity);
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
    				CommonChestGenerator gen = new CommonChestGenerator();
    				TreasureLootTable lootTable = gen.selectLootTable(new Random(), rarity);
    				
    				if (lootTable == null) {
    					Treasure.logger.warn("Unable to select a lootTable.");
    					player.sendMessage(new TextComponentString("Unable to select a lootTable."));
    				}
    				
    				Treasure.logger.debug("Generating loot from loot table for rarity {}", rarity);
    				List<ItemStack> stacks = lootTable.generateLootFromPools(new Random(), TreasureLootTables.CONTEXT);
    				Treasure.logger.debug("Generated loot:");
    				for (ItemStack stack : stacks) {
    					Treasure.logger.debug(stack.getDisplayName());
    				}	
    				
    				
    				lootTable.fillInventory(chest.getInventoryProxy(), 
							new Random(), 	TreasureLootTables.CONTEXT);
    			}
    		}
		}
		catch(Exception e) {
			player.sendMessage(new TextComponentString("Error:  " + e.getMessage()));
			Treasure.logger.error("Error generating Treasure! chest:", e);
			e.printStackTrace();
		}
	}
}
