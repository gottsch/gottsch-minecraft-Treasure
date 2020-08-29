package com.someguyssoftware.treasure2.tileentity;

import com.someguyssoftware.treasure2.chest.ChestSlotCount;
import com.someguyssoftware.treasure2.inventory.StrongboxChestContainer;
import com.someguyssoftware.treasure2.inventory.TreasureContainers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * 
 * @author Mark Gottschling on Jan 19, 2018
 *
 */
public class GoldStrongboxTileEntity extends AbstractTreasureChestTileEntity {
	
	/**
	 * 
	 * @param texture
	 */
	public GoldStrongboxTileEntity() {
		super(TreasureTileEntities.goldStrongboxTileEntityType);
		setCustomName(new TranslationTextComponent("display.gold_strongbox.name"));
	}
	
	/**
	 * 
	 * @param windowID
	 * @param inventory
	 * @param player
	 * @return
	 */
	public Container createServerContainer(int windowID, PlayerInventory inventory, PlayerEntity player) {
		return new StrongboxChestContainer(windowID, TreasureContainers.strongboxChestContainerType,  inventory, this);
	}
	
	/**
	 * @return the numberOfSlots
	 */
	@Override
	public int getNumberOfSlots() {
		return ChestSlotCount.STRONGBOX.getSize();
	}
}
