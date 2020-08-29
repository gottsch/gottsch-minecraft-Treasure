/**
 * 
 */
package com.someguyssoftware.treasure2.inventory;

import com.someguyssoftware.treasure2.chest.ChestSlotCount;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;

/**
 * @author Mark Gottschling on Aug 24, 2020
 *
 */
public class WitherChestContainer extends AbstractChestContainer {
	public static WitherChestContainer create(int windowID, PlayerInventory playerInventory, PacketBuffer extraData) {
		return new WitherChestContainer(windowID, TreasureContainers.witherChestContainerType, playerInventory, ChestSlotCount.WITHER.getSize());
	}
	
	/**
	 * Client-side constructor
	 * @param windowId
	 * @param containerType
	 * @param playerInventory
	 * @param slotCount
	 */
	private WitherChestContainer(int windowId, ContainerType<?> containerType, PlayerInventory playerInventory, int slotCount) {
		this(windowId, containerType, playerInventory, new Inventory(slotCount));
	}
	
	/**
	 * Server-side constructor
	 * @param windowID
	 * @param playerInventory
	 * @param inventory
	 */
	public WitherChestContainer(int windowID, ContainerType<?> containerType, PlayerInventory playerInventory, IInventory inventory) {
		super(windowID, containerType, playerInventory, inventory);
        
		// open the chest (rendering)
        inventory.openInventory(playerInventory.player);
        
		// set the dimensions
		setHotbarYPos(198);
		setPlayerInventoryYPos(139);
		setContainerInventoryColumnCount(7);
        setContainerInventoryRowCount(6);
		// wither has 2 less columns - to center move the xpos over by 8+xspacing
		setContainerInventoryXPos(8 + getSlotXSpacing());
		// build the container
		buildContainer(playerInventory, inventory);
	}

}
