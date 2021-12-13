package com.someguyssoftware.treasure2.tileentity;

import com.someguyssoftware.treasure2.chest.ChestSlotCount;
import com.someguyssoftware.treasure2.inventory.CompressorChestContainer;
import com.someguyssoftware.treasure2.inventory.TreasureContainers;

import net.minecraft.world.entity.player.Player;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * 
 * @author Mark Gottschling on Mar 17, 2018
 *
 */
public class CompressorChestTileEntity extends AbstractTreasureChestTileEntity {
	
	/**
	 * 
	 * @param texture
	 */
	public CompressorChestTileEntity() {
		super(TreasureTileEntities.COMPRESSOR_CHEST_TILE_ENTITY_TYPE);
		setCustomName(new TranslatableComponent("display.compressor_chest.name"));
	}
	
	/**
	 * 
	 * @param windowID
	 * @param inventory
	 * @param player
	 * @return
	 */
	public Container createServerContainer(int windowID, PlayerInventory inventory, Player player) {
		return new CompressorChestContainer(windowID, TreasureContainers.COMPRESSOR_CHEST_CONTAINER_TYPE, inventory, this);
	}
	
	/**
	 * @return the numberOfSlots
	 */
	@Override
	public int getNumberOfSlots() {
		return ChestSlotCount.COMPRESOR.getSize();
	}
}
