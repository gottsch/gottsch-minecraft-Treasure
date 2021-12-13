package com.someguyssoftware.treasure2.tileentity;

import com.someguyssoftware.treasure2.inventory.StandardChestContainer;

import net.minecraft.world.entity.player.Player;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * 
 * @author Mark Gottschling on Aug 26, 2019
 *
 */
public class SpiderChestTileEntity extends AbstractTreasureChestTileEntity {
	
	/**
	 * 
	 * @param texture
	 */
	public SpiderChestTileEntity() {
		super(TreasureTileEntities.SPIDER_CHEST_TILE_ENTITY_TYPE);
		setCustomName(new TranslatableComponent("display.spider_chest.name"));
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
