package com.someguyssoftware.treasure2.inventory;

import com.someguyssoftware.treasure2.chest.ChestSlotCount;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;

/**
 * @author Mark Gottschling on Jan 16, 2018
 *
 */
public class CompressorChestContainer extends AbstractChestContainer {
	
	public static CompressorChestContainer create(int windowID, PlayerInventory playerInventory, PacketBuffer extraData) {
		return new CompressorChestContainer(windowID, TreasureContainers.compressorChestContainerType, playerInventory, ChestSlotCount.COMPRESOR.getSize());
	}
	
	/**
	 * Client-side constructor
	 * @param windowId
	 * @param containerType
	 * @param playerInventory
	 * @param slotCount
	 */
	private CompressorChestContainer(int windowId, ContainerType<?> containerType, PlayerInventory playerInventory, int slotCount) {
		this(windowId, containerType, playerInventory, new Inventory(slotCount));
	}
	
	/**
	 * Server-side constructor
	 * @param playerInventory
	 * @param inventory
	 */
	public CompressorChestContainer(int windowID, ContainerType<?> containerType, PlayerInventory playerInventory, IInventory inventory) {
		super(windowID, containerType, playerInventory, inventory);
		
		// open the chest (rendering)
        inventory.openInventory(playerInventory.player);
        
		// set the dimensions
		setHotbarXPos(44);
		setHotbarYPos(161);
		setPlayerInventoryXPos(44);
		setPlayerInventoryYPos(103);
        setContainerInventoryRowCount(4);
		setContainerInventoryColumnCount(13);

		// build the container
		buildContainer(playerInventory, inventory);
	}
}
