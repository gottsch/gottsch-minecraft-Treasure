/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package com.someguyssoftware.treasure2.client.gui;

import static com.someguyssoftware.treasure2.Treasure.logger;

import java.util.Random;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.client.gui.inventory.CharmingBenchGui;
import com.someguyssoftware.treasure2.client.gui.inventory.CompressorChestGui;
import com.someguyssoftware.treasure2.client.gui.inventory.JewelerBenchGui;
import com.someguyssoftware.treasure2.client.gui.inventory.KeyRingGui;
import com.someguyssoftware.treasure2.client.gui.inventory.MolluscChestGui;
import com.someguyssoftware.treasure2.client.gui.inventory.PouchGui;
import com.someguyssoftware.treasure2.client.gui.inventory.SkullChestGui;
import com.someguyssoftware.treasure2.client.gui.inventory.StandardChestGui;
import com.someguyssoftware.treasure2.client.gui.inventory.StrongboxChestGui;
import com.someguyssoftware.treasure2.client.gui.inventory.WitherChestGui;
import com.someguyssoftware.treasure2.generator.chest.IChestGenerator;
import com.someguyssoftware.treasure2.inventory.CharmingBenchContainer;
import com.someguyssoftware.treasure2.inventory.CompressorChestContainer;
import com.someguyssoftware.treasure2.inventory.JewelerBenchContainer;
import com.someguyssoftware.treasure2.inventory.KeyRingContainer;
import com.someguyssoftware.treasure2.inventory.KeyRingInventory;
import com.someguyssoftware.treasure2.inventory.MolluscChestContainer;
import com.someguyssoftware.treasure2.inventory.PouchContainer;
import com.someguyssoftware.treasure2.inventory.PouchInventory;
import com.someguyssoftware.treasure2.inventory.SkullChestContainer;
import com.someguyssoftware.treasure2.inventory.StandardChestContainer;
import com.someguyssoftware.treasure2.inventory.StrongboxChestContainer;
import com.someguyssoftware.treasure2.inventory.WitherChestContainer;
import com.someguyssoftware.treasure2.item.KeyRingItem;
import com.someguyssoftware.treasure2.item.PouchItem;
import com.someguyssoftware.treasure2.tileentity.ITreasureChestTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * @author Mark Gottschling on Jan 16, 2018
 *
 * This class is used to get the client and server gui elements when a
 * player opens a gui. There can only be one registered IGuiHandler
 * instance handler per mod.
 */
public class GuiHandler implements IGuiHandler {
	public static final int STANDARD_CHEST_GUIID = 1;
	public static final int STRONGBOX_CHEST_GUIID = 2;
	public static final int KEY_RING_GUIID = 3;
	public static final int COMPRESSOR_CHEST_GUIID = 4;
	public static final int SKULL_CHEST_GUIID = 5;
	public static final int ARMOIRE_GUID = 6;
	public static final int WITHER_CHEST_GUIID = 7;
	public static final int POUCH_GUIID = 50;
	public static final int JEWELER_BENCH = 51;
	public static final int CHARMING_BENCH = 52;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.minecraftforge.fml.common.network.IGuiHandler#getServerGuiElement(int,
	 * net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, int,
	 * int, int)
	 */
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		if (tileEntity instanceof ITreasureChestTileEntity) {
			ITreasureChestTileEntity chestTileEntity = (ITreasureChestTileEntity) tileEntity;
			logger.debug("is chest sealed -> {}", chestTileEntity.isSealed());
			if (chestTileEntity.isSealed()) {
				chestTileEntity.setSealed(false);
                logger.debug("chest gen type -> {}", chestTileEntity.getGenerationContext().getChestGeneratorType()); 
                // construct the chest generator used to create the tile entity
                IChestGenerator chestGenerator = chestTileEntity.getGenerationContext().getChestGeneratorType().getChestGenerator();
                logger.debug("chest gen  -> {}", chestTileEntity.getGenerationContext().getChestGeneratorType().getChestGenerator().getClass().getSimpleName());
                
                // fill the chest with loot
                chestGenerator.fillChest(world, new Random(), tileEntity, chestTileEntity.getGenerationContext().getLootRarity(), player);

			}
		}

		Container container = null;
		switch (ID) {
		case STANDARD_CHEST_GUIID:
			container = new StandardChestContainer(player.inventory, (IInventory) tileEntity);
			break;
		case STRONGBOX_CHEST_GUIID:
			container = new StrongboxChestContainer(player.inventory, (IInventory) tileEntity);
			break;
		case COMPRESSOR_CHEST_GUIID:
			container = new CompressorChestContainer(player.inventory, (IInventory) tileEntity);
			break;
		case SKULL_CHEST_GUIID:
			container = new SkullChestContainer(player.inventory, (IInventory) tileEntity);
			break;
		case WITHER_CHEST_GUIID:
			container = new WitherChestContainer(player.inventory, (IInventory) tileEntity);
			break;
		case KEY_RING_GUIID:
			// get the held item
			ItemStack keyRingItem = player.getHeldItemMainhand();
			if (keyRingItem == null || !(keyRingItem.getItem() instanceof KeyRingItem)) {
				keyRingItem = player.getHeldItemOffhand();
				if (keyRingItem == null || !(keyRingItem.getItem() instanceof KeyRingItem))
					return null;
			}

			// create inventory from item
			IInventory inventory = new KeyRingInventory(keyRingItem);
			// open the container
			container = new KeyRingContainer(player.inventory, inventory);
            break;
		case POUCH_GUIID:
			// get the held item
			ItemStack pouchStack = player.getHeldItemMainhand();
			if (pouchStack == null || !(pouchStack.getItem() instanceof PouchItem)) {
				pouchStack = player.getHeldItemOffhand();
				if (pouchStack == null || !(pouchStack.getItem() instanceof PouchItem))
					return null;
			}

			// create inventory from item
			IInventory pouchInventory = new PouchInventory(pouchStack);
			// open the container
			container = new PouchContainer(player.inventory, pouchInventory, pouchStack);			
            break;
		case JEWELER_BENCH:
			Treasure.logger.debug("creating jeweler bench container server-side");
			container = new JewelerBenchContainer(player.inventory, world, new BlockPos(x, y, z), player);
			break;
		case CHARMING_BENCH:
			Treasure.logger.debug("creating charming bench container server-side");
			container = new CharmingBenchContainer(player.inventory, world, new BlockPos(x, y, z), player);
			break;
		default:

		}
		return container;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.minecraftforge.fml.common.network.IGuiHandler#getClientGuiElement(int,
	 * net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, int,
	 * int, int)
	 */
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        ITreasureChestTileEntity chestTileEntity = null;
        
		switch (ID) {
		case STANDARD_CHEST_GUIID:
			// NOTE could pass in the different bg textures here
			if ((chestTileEntity = getChestTileEntity(tileEntity)) == null) return null;
			return new StandardChestGui(player.inventory, chestTileEntity);			
		case STRONGBOX_CHEST_GUIID:
			if ((chestTileEntity = getChestTileEntity(tileEntity)) == null) return null;
			return new StrongboxChestGui(player.inventory, chestTileEntity);
		case COMPRESSOR_CHEST_GUIID:
			if ((chestTileEntity = getChestTileEntity(tileEntity)) == null) return null;
			return new CompressorChestGui(player.inventory, chestTileEntity);
		case SKULL_CHEST_GUIID:
			if ((chestTileEntity = getChestTileEntity(tileEntity)) == null) return null;
			return new SkullChestGui(player.inventory, chestTileEntity);
		case WITHER_CHEST_GUIID:
			if ((chestTileEntity = getChestTileEntity(tileEntity)) == null) return null;
			return new WitherChestGui(player.inventory, chestTileEntity);
		case KEY_RING_GUIID:
			// get the held item
			ItemStack keyRingItem = player.getHeldItemMainhand();
			if (keyRingItem == null || !(keyRingItem.getItem() instanceof KeyRingItem)) {
				keyRingItem = player.getHeldItemOffhand();
				if (keyRingItem == null || !(keyRingItem.getItem() instanceof KeyRingItem))
					return null;
			}

			// create inventory from item
			IInventory inventory = new KeyRingInventory(keyRingItem);
			// open the container
			return new KeyRingGui(player.inventory, inventory, keyRingItem);

		case POUCH_GUIID:
			// get the held item
			ItemStack pouchStack = player.getHeldItemMainhand();
			if (pouchStack == null || !(pouchStack.getItem() instanceof PouchItem)) {
				pouchStack = player.getHeldItemOffhand();
				if (pouchStack == null || !(pouchStack.getItem() instanceof PouchItem))
					return null;
			}

			// create inventory from item
			IInventory pouchInventory = new PouchInventory(pouchStack);

			// open the container
			return new PouchGui(player.inventory, pouchInventory, pouchStack);	

		case JEWELER_BENCH:
			return new JewelerBenchGui(player.inventory, world);
		case CHARMING_BENCH:
			Treasure.logger.debug("creating charming bench container client-side");
			try {
				return new CharmingBenchGui(player.inventory, world);
			}
			catch(Exception e) {
				Treasure.logger.error("charming bench error:", e);
			}
		default:
			return null;
		}
	}

	private ITreasureChestTileEntity getChestTileEntity(TileEntity tileEntity) {        
      ITreasureChestTileEntity chestTileEntity = (tileEntity instanceof ITreasureChestTileEntity) ? (ITreasureChestTileEntity) tileEntity : null;
      if (chestTileEntity == null) {
          logger.warn("Umm, GUI handler error - wrong tile entity.");
          return null;
      }
      return chestTileEntity;
	}
}