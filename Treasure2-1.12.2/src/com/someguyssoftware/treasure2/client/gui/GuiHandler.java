/**
 * 
 */
package com.someguyssoftware.treasure2.client.gui;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.client.gui.inventory.TreasureChestGui;
import com.someguyssoftware.treasure2.inventory.TreasureChestContainer;
import com.someguyssoftware.treasure2.tileentity.TreasureChestTileEntity;

import net.minecraft.entity.player.EntityPlayer;
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
 * This class is used to get the client and server gui elements when a player opens a gui. There can only be one registered
 * IGuiHandler instance handler per mod.
 */
public class GuiHandler implements IGuiHandler {
	public static final int TREASURE_CHEST_GUIID = 1;

	
	/* (non-Javadoc)
	 * @see net.minecraftforge.fml.common.network.IGuiHandler#getServerGuiElement(int, net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, int, int, int)
	 */
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tileEntity = world.getTileEntity(pos);
		
		switch(ID) {
			case TREASURE_CHEST_GUIID:
				if (tileEntity instanceof TreasureChestTileEntity) {
					Treasure.logger.debug("Tile entity is of type TreasureChestTileEntity");
					return new TreasureChestContainer(player.inventory, ((TreasureChestTileEntity)tileEntity).getInventoryProxy());
				}
				break;
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
			case TREASURE_CHEST_GUIID:
				if (tileEntity instanceof TreasureChestTileEntity) {
					// TODO could pass in the different bg textures here
					return new TreasureChestGui(player.inventory, (TreasureChestTileEntity) tileEntity);
				}
				else {
					Treasure.logger.warn("Umm, GUI handler error - wrong tile entity.");
				}
				break;
			default: return null;
		}
		return null;
	}
}
