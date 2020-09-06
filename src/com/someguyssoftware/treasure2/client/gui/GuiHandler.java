/**
 * 
 */
package com.someguyssoftware.treasure2.client.gui;

import static com.someguyssoftware.treasure2.Treasure.logger;

import java.util.Random;

import com.someguyssoftware.gottschcore.loot.LootContext;
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
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
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
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		if (tileEntity instanceof AbstractTreasureChestTileEntity) {
			AbstractTreasureChestTileEntity chestTileEntity = (AbstractTreasureChestTileEntity) tileEntity;
//			logger.debug("is chest sealed -> {}", chestTileEntity.isSealed());
			if (chestTileEntity.isSealed()) {
				chestTileEntity.setSealed(false);
//                logger.debug("chest gen type -> {}", chestTileEntity.getGenerationContext().getChestGeneratorType()); 
                // construct the chest generator used to create the tile entity
                IChestGenerator chestGenerator = chestTileEntity.getGenerationContext().getChestGeneratorType().getChestGenerator();
//                logger.debug("chest gen  -> {}", chestTileEntity.getGenerationContext().getChestGeneratorType().getChestGenerator().getClass().getSimpleName());
                
				// select a loot table
				LootTable lootTable = chestGenerator.selectLootTable(Random::new, chestTileEntity.getGenerationContext().getLootRarity());
				if (lootTable == null) {
					logger.warn("Unable to select a lootTable.");
					return null;
				}
				logger.debug("Generating loot from loot table for rarity {}", chestTileEntity.getGenerationContext().getLootRarity());
				LootContext lootContext = new LootContext.Builder((WorldServer) world, Treasure.LOOT_TABLES.getLootTableManager())
						.withLuck(player.getLuck())
						.withPlayer(player)
						.build();
				if (lootContext == null) {
					lootContext = Treasure.LOOT_TABLES.getContext();
				}
				lootTable.fillInventory((IInventory) tileEntity, new Random(), lootContext);
				
				// TODO future IChestGenerator.addSpecialLoot(world, random, chestTileEntity);
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
			container = new CompressorChestContainer(player.inventory, (IInventory) tileEntity);
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
            break;
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
        AbstractTreasureChestTileEntity chestTileEntity = (tileEntity instanceof AbstractTreasureChestTileEntity) 
            ? (AbstractTreasureChestTileEntity) tileEntity : null;
        if (chestTileEntity == null) {
            logger.warn("Umm, GUI handler error - wrong tile entity.");
            return null;
        }

		switch (ID) {
		case STANDARD_CHEST_GUIID:
			// NOTE could pass in the different bg textures here
			return new StandardChestGui(player.inventory, (AbstractTreasureChestTileEntity) tileEntity);
		case STRONGBOX_CHEST_GUIID:
			return new StrongboxChestGui(player.inventory, (AbstractTreasureChestTileEntity) tileEntity);
		case COMPRESSOR_CHEST_GUIID:
			return new CompressorChestGui(player.inventory, (AbstractTreasureChestTileEntity) tileEntity);
		case SKULL_CHEST_GUIID:
			return new SkullChestGui(player.inventory, (AbstractTreasureChestTileEntity) tileEntity);
		case WITHER_CHEST_GUIID:
			return new WitherChestGui(player.inventory, (AbstractTreasureChestTileEntity) tileEntity);
		case MOLLUSCS_CHEST_GUIID:
			return new MolluscChestGui(player.inventory, (AbstractTreasureChestTileEntity) tileEntity);
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
	}
}