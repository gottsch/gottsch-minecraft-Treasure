/**
 * 
 */
package com.someguyssoftware.treasure2.client.gui;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.client.gui.inventory.KeyRingGui;
import com.someguyssoftware.treasure2.client.gui.inventory.StandardChestGui;
import com.someguyssoftware.treasure2.client.gui.inventory.StrongboxChestGui;
import com.someguyssoftware.treasure2.inventory.KeyRingInventory;
import com.someguyssoftware.treasure2.inventory.KeyRingContainer;
import com.someguyssoftware.treasure2.inventory.StandardChestContainer;
import com.someguyssoftware.treasure2.inventory.StrongboxChestContainer;
import com.someguyssoftware.treasure2.item.KeyRingItem;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**

 *
 */
/**
 * @author Mark Gottschling on Jan 16, 2018
 *
 * This class is used to get the client and server gui elements when a player opens a gui. There can only be one registered
 * IGuiHandler instance handler per mod.
 */
public class GuiHandler implements IGuiHandler {
	public static final int STANDARD_CHEST_GUIID = 1;
	public static final int STRONGBOX_CHEST_GUIID = 2;
	public static final int KEY_RING_GUIID = 3;

	
	/* (non-Javadoc)
	 * @see net.minecraftforge.fml.common.network.IGuiHandler#getServerGuiElement(int, net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, int, int, int)
	 */
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tileEntity = world.getTileEntity(pos);
		
		switch(ID) {
			case STANDARD_CHEST_GUIID:
				if (tileEntity instanceof AbstractTreasureChestTileEntity) {
					return new StandardChestContainer(player.inventory, ((AbstractTreasureChestTileEntity)tileEntity).getInventoryProxy());
				}
				break;
			case STRONGBOX_CHEST_GUIID:
				if (tileEntity instanceof AbstractTreasureChestTileEntity) {
					Treasure.logger.debug("Tile entity is of type TreasureChestTileEntity");
					return new StrongboxChestContainer(player.inventory, ((AbstractTreasureChestTileEntity)tileEntity).getInventoryProxy());
				}
				break;				
			case KEY_RING_GUIID:
				// get the held item
				ItemStack keyRingItem = player.getHeldItemMainhand();
				if (keyRingItem == null || !(keyRingItem.getItem() instanceof KeyRingItem)) {
					keyRingItem = player.getHeldItemOffhand();
					if (keyRingItem == null || !(keyRingItem.getItem() instanceof KeyRingItem)) return null;
				}			
				
				// create inventory from item
				IInventory inventory = new KeyRingInventory(keyRingItem);
				// open the container
				return new KeyRingContainer(player.inventory, inventory);

			default: return null;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see net.minecraftforge.fml.common.network.IGuiHandler#getClientGuiElement(int, net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, int, int, int)
	 */
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos xyz = new BlockPos(x, y, z);
		TileEntity tileEntity = world.getTileEntity(xyz);
		switch(ID) {
			case STANDARD_CHEST_GUIID:
				if (tileEntity instanceof AbstractTreasureChestTileEntity) {
					// NOTE could pass in the different bg textures here
					return new StandardChestGui(player.inventory, (AbstractTreasureChestTileEntity) tileEntity);
				}
				else {
					Treasure.logger.warn("Umm, GUI handler error - wrong tile entity.");
				}
				break;
			case STRONGBOX_CHEST_GUIID:
				if (tileEntity instanceof AbstractTreasureChestTileEntity) {
					return new StrongboxChestGui(player.inventory, (AbstractTreasureChestTileEntity) tileEntity);
				}
				else {
					Treasure.logger.warn("Umm, GUI handler error - wrong tile entity.");
				}
				break;
			case KEY_RING_GUIID:
				// get the held item
				ItemStack keyRingItem = player.getHeldItemMainhand();
				if (keyRingItem == null || !(keyRingItem.getItem() instanceof KeyRingItem)) {
					keyRingItem = player.getHeldItemOffhand();
					if (keyRingItem == null || !(keyRingItem.getItem() instanceof KeyRingItem)) return null;
				}			
				
				// create inventory from item
				IInventory inventory = new KeyRingInventory(keyRingItem);
				// open the container
				return new KeyRingGui(player.inventory, inventory);
			default: return null;
		}
		return null;
	}
}
