package com.someguyssoftware.treasure2.tileentity;

import com.someguyssoftware.treasure2.inventory.StandardChestContainer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * 
 * @author Mark Gottschling on Jan 19, 2018
 *
 */
public class IronboundChestTileEntity extends AbstractTreasureChestTileEntity {
	
	/**
	 * 
	 * @param texture
	 */
	public IronboundChestTileEntity() {
		super(TreasureTileEntities.ironboundChestTileEntityType);
		setCustomName(new TranslationTextComponent("display.ironbound_chest.name"));
	}
	
	/**
	 * 
	 * @param windowID
	 * @param inventory
	 * @param player
	 * @return
	 */
	public Container createServerContainer(int windowID, PlayerInventory inventory, PlayerEntity player) {
		return new StandardChestContainer(windowID, inventory, this);
	}
}
