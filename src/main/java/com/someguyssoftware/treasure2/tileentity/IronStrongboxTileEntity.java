package com.someguyssoftware.treasure2.tileentity;

import com.someguyssoftware.treasure2.chest.ChestSlotCount;
import com.someguyssoftware.treasure2.inventory.StrongboxChestContainer;
import com.someguyssoftware.treasure2.inventory.TreasureContainers;

import net.minecraft.world.entity.player.Player;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * 
 * @author Mark Gottschling on Jan 19, 2018
 *
 */
public class IronStrongboxTileEntity extends AbstractTreasureChestTileEntity {
	
	/**
	 * 
	 * @param texture
	 */
	public IronStrongboxTileEntity() {
		super(TreasureTileEntities.IRON_STRONGBOX_TILE_ENTITY_TYPE);
		setCustomName(new TranslatableComponent("display.iron_strongbox.name"));
	}
	
	/**
	 * 
	 * @param windowID
	 * @param inventory
	 * @param player
	 * @return
	 */
	public Container createServerContainer(int windowID, PlayerInventory inventory, Player player) {
		return new StrongboxChestContainer(windowID, TreasureContainers.STRONGBOX_CHEST_CONTAINER_TYPE, inventory, this);
	}
	
	/**
	 * @return the numberOfSlots
	 */
	@Override
	public int getNumberOfSlots() {
		return ChestSlotCount.STRONGBOX.getSize();
	}
}
