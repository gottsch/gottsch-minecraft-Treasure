package com.someguyssoftware.treasure2.tileentity;

import com.someguyssoftware.treasure2.inventory.StandardChestContainer;

import net.minecraft.world.entity.player.Player;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * 
 * @author Mark Gottschling on Apr 19, 2020
 *
 */
public class VikingChestTileEntity extends AbstractTreasureChestTileEntity {
	
	/**
	 * 
	 * @param texture
	 */
	public VikingChestTileEntity() {
		super(TreasureTileEntities.VIKING_CHEST_TILE_ENTITY_TYPE);
		setCustomName(new TranslatableComponent("display.viking_chest.name"));
	}
	
	/**
	 * 
	 * @param windowID
	 * @param inventory
	 * @param player
	 * @return
	 */
	public Container createServerContainer(int windowID, PlayerInventory inventory, Player player) {
		return new StandardChestContainer(windowID, inventory, this);
	}
}
