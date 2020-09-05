/**
 * 
 */
package com.someguyssoftware.treasure2.client.gui;

import java.util.Random;

import com.someguyssoftware.gottschcore.loot.LootTable;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.client.gui.inventory.CompressorChestGui;
import com.someguyssoftware.treasure2.client.gui.inventory.KeyRingGui;
import com.someguyssoftware.treasure2.client.gui.inventory.MolluscChestGui;
import com.someguyssoftware.treasure2.client.gui.inventory.PouchGui;
import com.someguyssoftware.treasure2.client.gui.inventory.SkullChestGui;
import com.someguyssoftware.treasure2.client.gui.inventory.StandardChestGui;
import com.someguyssoftware.treasure2.client.gui.inventory.StrongboxChestGui;
import com.someguyssoftware.treasure2.client.gui.inventory.WitherChestGui;
import com.someguyssoftware.treasure2.generator.chest.IChestGenerator;
import com.someguyssoftware.treasure2.inventory.CompressorChestContainer;
import com.someguyssoftware.treasure2.inventory.KeyRingContainer;
import com.someguyssoftware.treasure2.inventory.KeyRingInventory;
import com.someguyssoftware.treasure2.inventory.MolluscChestContainer;
import com.someguyssoftware.treasure2.inventory.PouchContainer;
import com.someguyssoftware.treasure2.inventory.PouchInventory;
import com.someguyssoftware.treasure2.inventory.SkullChestContainer;
import com.someguyssoftware.treasure2.inventory.StandardChestContainer;
import com.someguyssoftware.treasure2.inventory.StrongboxChestContainer;
import com.someguyssoftware.treasure2.inventory.WitherChestContainer;
import com.someguyssoftware.treasure2.item.IPouch;
import com.someguyssoftware.treasure2.item.KeyRingItem;
import com.someguyssoftware.treasure2.item.PouchItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**

 *
 */
/**
 * @author Mark Gottschling on Jan 16, 2018
 *
 *         This class is used to get the client and server gui elements when a
 *         player opens a gui. There can only be one registered IGuiHandler
 *         instance handler per mod.
 */
public class GuiHandler implements IGuiHandler {
	public static final int STANDARD_CHEST_GUIID = 1;
	public static final int STRONGBOX_CHEST_GUIID = 2;
	public static final int KEY_RING_GUIID = 3;
	public static final int COMPRESSOR_CHEST_GUIID = 4;
	public static final int SKULL_CHEST_GUIID = 5;
	public static final int ARMOIRE_GUID = 6;
	public static final int WITHER_CHEST_GUIID = 7;
	public static final int MOLLUSCS_CHEST_GUIID = 8;
	public static final int POUCH_GUIID = 50;

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
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tileEntity = world.getTileEntity(pos);

		// TODO test the sealed property of the tileEntity to determine if it has been opened before and requires being filled with loot

		if (tileEntity instanceof AbstractTreasureChestTileEntity) {
			AbstractTreasureChestTileEntity chestTileEntity = (AbstractTreasureChestTileEntity) tileEntity;
			if (chestTileEntity.isSealed()) {
				chestTileEntity.setSealed(false);
				/*
				 TODO need to know the chest generator so that a proper loot table can be selected. selectLootTable() must be overridable.
				 ex Wither and Cauldron chests use a special loot tables
				 by interrogating the GUIID, a generator could be selected - that doesn't map as well, ex how to tell different between skull and gold skull
				 might have to save the generator context in the tile entity - worldGenerator enum, (add) chestGenerator enum, rarity
				*/
				// select a loot table
				LootTable lootTable = IChestGenerator.selectLootTable(Random::new, chestTileEntity.getLootRarity());
				if (lootTable == null) {
					Treasure.logger.warn("Unable to select a lootTable.");
					return null;
				}
				Treasure.logger.debug("Generating loot from loot table for rarity {}", chestTileEntity.getLootRarity());
				// TODO here we can alter the content
				lootTable.fillInventory((IInventory) tileEntity, new Random(), Treasure.LOOT_TABLES.getContext());
			}
		}
		else {
			return null;
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
			container = return new CompressorChestContainer(player.inventory, (IInventory) tileEntity);
			break;
		case SKULL_CHEST_GUIID:
			container = new SkullChestContainer(player.inventory, (IInventory) tileEntity);
			break;
		case WITHER_CHEST_GUIID:
			container = new WitherChestContainer(player.inventory, (IInventory) tileEntity);
			break;
		case MOLLUSCS_CHEST_GUIID:
			container = new MolluscChestContainer(player.inventory, (IInventory) tileEntity);
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

		case POUCH_GUIID:
			// get the held item
			ItemStack pouchStack = player.getHeldItemMainhand();
			if (pouchStack == null || !(pouchStack.getItem() instanceof IPouch)) {
				pouchStack = player.getHeldItemOffhand();
				if (pouchStack == null || !(pouchStack.getItem() instanceof IPouch))
					return null;
			}

			// create inventory from item
			IInventory pouchInventory = new PouchInventory(pouchStack);
			// open the container
			container = new PouchContainer(player.inventory, pouchInventory, pouchStack);			

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
		BlockPos xyz = new BlockPos(x, y, z);
		TileEntity tileEntity = world.getTileEntity(xyz);
		switch (ID) {
		case STANDARD_CHEST_GUIID:
			if (tileEntity instanceof AbstractTreasureChestTileEntity) {
				// NOTE could pass in the different bg textures here
				return new StandardChestGui(player.inventory, (AbstractTreasureChestTileEntity) tileEntity);
			} else {
				Treasure.logger.warn("Umm, GUI handler error - wrong tile entity.");
			}
			break;
		case STRONGBOX_CHEST_GUIID:
			if (tileEntity instanceof AbstractTreasureChestTileEntity) {
				return new StrongboxChestGui(player.inventory, (AbstractTreasureChestTileEntity) tileEntity);
			} else {
				Treasure.logger.warn("Umm, GUI handler error - wrong tile entity.");
			}
			break;
		case COMPRESSOR_CHEST_GUIID:
			if (tileEntity instanceof AbstractTreasureChestTileEntity) {
				return new CompressorChestGui(player.inventory, (AbstractTreasureChestTileEntity) tileEntity);
			} else {
				Treasure.logger.warn("Umm, GUI handler error - wrong tile entity.");
			}
			break;
		case SKULL_CHEST_GUIID:
			if (tileEntity instanceof AbstractTreasureChestTileEntity) {
				return new SkullChestGui(player.inventory, (AbstractTreasureChestTileEntity) tileEntity);
			} else {
				Treasure.logger.warn("Umm, GUI handler error - wrong tile entity.");
			}
			break;
		case WITHER_CHEST_GUIID:
			if (tileEntity instanceof AbstractTreasureChestTileEntity) {
				return new WitherChestGui(player.inventory, (AbstractTreasureChestTileEntity) tileEntity);
			} else {
				Treasure.logger.warn("Umm, GUI handler error - wrong tile entity.");
			}
			break;
		case MOLLUSCS_CHEST_GUIID:
			if (tileEntity instanceof AbstractTreasureChestTileEntity) {
				return new MolluscChestGui(player.inventory, (AbstractTreasureChestTileEntity) tileEntity);
			} else {
				Treasure.logger.warn("Umm, GUI handler error - wrong tile entity.");
			}
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

		default:
			return null;
		}
		return null;
	}
}
