/**
 * 
 */
package com.someguyssoftware.treasure2.command;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.loot.LootTable;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.enums.WorldGenerators;
import com.someguyssoftware.treasure2.generator.chest.IChestGenerator;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;
import com.someguyssoftware.treasure2.worldgen.ChestWorldGenerator;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Feb 10, 2017
 *
 */
public class SpawnChestCommand extends CommandBase {

	@Override
	public String getName() {
		return "t2-chest";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/t2-chest <x> <y> <z> [rarity | -n <name>]: generates a Treasure! chest at location (x,y,z)";
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
			
			// get the rarity enum
			Rarity rarity = Rarity.valueOf(rarityName.toUpperCase());
			Treasure.logger.debug("Rarity:" + rarity + "; " + rarity.ordinal());
			
			if (player != null) {
    			World world = commandSender.getEntityWorld();
    			Treasure.logger.debug("Starting to build Treasure! chest ...");
    			Random random = new Random();
    			
				// get the chest world generator
				ChestWorldGenerator chestGens = (ChestWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGenerators.CHEST);
				// get the rarity chest generator
//				IChestGenerator gen = chestGens.getGenerators().get(rarity);
				IChestGenerator gen = chestGens.getChestCollectionGeneratorsMap().get(rarity).next();
//				CommonChestGenerator gen = new CommonChestGenerator();
    			BlockPos pos = new BlockPos(x, y, z);
    			
    			AbstractChestBlock chest = gen.selectChest(random, rarity);

    			world.setBlockState(pos , chest.getDefaultState());
    			AbstractTreasureChestTileEntity tileEntity = (AbstractTreasureChestTileEntity) world.getTileEntity(pos);
				
    			if (tileEntity != null) {
				
    				   				
    				// get the loot table
    				LootTable lootTable = gen.selectLootTable(new Random(), rarity);
    				
    				if (lootTable == null) {
    					Treasure.logger.warn("Unable to select a lootTable.");
    					player.sendMessage(new TextComponentString("Unable to select a lootTable."));
    				}
    				
    				Treasure.logger.debug("Generating loot from loot table for rarity {}", rarity);
    				List<ItemStack> stacks = lootTable.generateLootFromPools(new Random(), Treasure.LOOT_TABLES.getContext());
    				Treasure.logger.debug("Generated loot:");
    				for (ItemStack stack : stacks) {
    					Treasure.logger.debug(stack.getDisplayName());
    				}	    				
    				
    				lootTable.fillInventory(tileEntity.getInventoryProxy(), 
							new Random(), 	Treasure.LOOT_TABLES.getContext());
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
