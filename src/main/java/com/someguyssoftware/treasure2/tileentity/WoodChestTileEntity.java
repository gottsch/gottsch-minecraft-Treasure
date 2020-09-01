package com.someguyssoftware.treasure2.tileentity;

import javax.annotation.Nullable;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.inventory.StandardChestContainer;
import com.someguyssoftware.treasure2.inventory.TreasureContainers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * 
 * @author Mark Gottschling on Jan 19, 2018
 *
 */
public class WoodChestTileEntity extends AbstractTreasureChestTileEntity {
	
	/**
	 * 
	 * @param texture
	 */
	public WoodChestTileEntity() {
		super(TreasureTileEntities.woodChestTileEntityType);
		setCustomName(new TranslationTextComponent("display.wood_chest.name"));
	}

	// TODO why overriding?
	@Override
	public float getLidAngle(float partialTicks) {
		return super.getLidAngle(partialTicks);
	}

	  /**
	   * The name is misleading; createMenu has nothing to do with creating a Screen, it is used to create the Container on the server only
	   * @param windowID
	   * @param playerInventory
	   * @param playerEntity
	   * @return
	   */
	@Nullable
	@Override
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		Treasure.LOGGER.info("createMenu evoked.");
	    return createServerContainer(windowID, playerInventory, playerEntity);
	}
	
	/**
	 * 
	 * @param windowID
	 * @param inventory
	 * @param player
	 * @return
	 */
	public Container createServerContainer(int windowID, PlayerInventory inventory, PlayerEntity player) {
		Treasure.LOGGER.info("createServerContainer evoked.");
		return new StandardChestContainer(windowID, inventory, this);
	}
}
