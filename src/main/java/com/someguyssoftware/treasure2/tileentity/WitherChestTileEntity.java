package com.someguyssoftware.treasure2.tileentity;

import com.someguyssoftware.treasure2.chest.ChestSlotCount;
import com.someguyssoftware.treasure2.inventory.TreasureContainers;
import com.someguyssoftware.treasure2.inventory.WitherChestContainer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * NOTE Wither ChestConfig does not have a "lid", but has doors, however, the use of the lidAngle from the super class
 * will still be used.  The rendered will use that value to determine how to render the doors.
 * 
 * @author Mark Gottschling on Jun 17, 2018
 *
 */
public class WitherChestTileEntity extends AbstractTreasureChestTileEntity {
    
	/**
	 * 
	 * @param texture
	 */
	public WitherChestTileEntity() {
		super(TreasureTileEntities.witherChestTileEntityType);
		setCustomName(new TranslationTextComponent("display.wither_chest.name"));
	}
	
	/**
	 * 
	 * @param windowID
	 * @param inventory
	 * @param player
	 * @return
	 */
	public Container createServerContainer(int windowID, PlayerInventory inventory, PlayerEntity player) {
		return new WitherChestContainer(windowID, TreasureContainers.witherChestContainerType, inventory, this);
	}
	
	/**
	 * @return the numberOfSlots
	 */
	@Override
	public int getNumberOfSlots() {
		return ChestSlotCount.WITHER.getSize();
	}
}
