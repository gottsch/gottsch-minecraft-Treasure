package com.someguyssoftware.treasure2.inventory;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.ChestSlotCount;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;

/**
 * 
 *  @author Mark Gottschling on Jan 16, 2018
 *
 */
public class SkullChestContainer extends AbstractChestContainer {
	public static SkullChestContainer create(int windowID, PlayerInventory playerInventory, PacketBuffer extraData) {
		return new SkullChestContainer(windowID, TreasureContainers.skullChestContainerType, playerInventory, ChestSlotCount.SKULL.getSize());
	}
	
	/**
	 * Client-side constructor
	 * @param windowID
	 * @param skullChestContainerType
	 * @param playerInventory
	 * @param slotCount
	 */
	private SkullChestContainer(int windowID, ContainerType<?> skullChestContainerType,
			PlayerInventory playerInventory, int slotCount) {
		this(windowID, skullChestContainerType, playerInventory, new Inventory(slotCount));

	}
	
	/**
	 * Server-side constructor
	 * @param windowID
	 * @param playerInventory
	 * @param inventory
	 */
	public SkullChestContainer(int windowID, ContainerType<?> containerType, PlayerInventory playerInventory, IInventory inventory) {
		super(windowID, containerType, playerInventory, inventory);
        
		// open the chest (rendering)
        inventory.openInventory(playerInventory.player);
        
		// set the dimensions
		setContainerInventoryColumnCount(3);
        setContainerInventoryRowCount(3);
		// skull has 13 less columns - to center move the xpos over by xspacing*2
		setContainerInventoryXPos(8 + getSlotXSpacing() * 3);
		
		// build the container
		buildContainer(playerInventory, inventory);
	}

}
