/**
 * 
 */
package mod.gottsch.forge.treasure2.core.inventory;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.chest.ChestSlotCount;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;

/**
 * This is the base/standard container for chests that is similar configuration to that of a vanilla container.
 * @author Mark Gottschling on Jan 16, 2018
 *
 */
public class StrongboxChestContainer extends AbstractChestContainer {
	public static StrongboxChestContainer create(int windowID, PlayerInventory playerInventory, PacketBuffer extraData) {
		return new StrongboxChestContainer(windowID, TreasureContainers.STRONGBOX_CHEST_CONTAINER_TYPE, playerInventory, ChestSlotCount.STRONGBOX.getSize());
	}
	
	/**
	 * Client-side constructor
	 * @param windowID
	 * @param containerType
	 * @param playerInventory
	 * @param slotCount
	 */
	private StrongboxChestContainer(int windowID, ContainerType<?> containerType, PlayerInventory playerInventory, int slotCount) {
		this(windowID, containerType, playerInventory, new Inventory(slotCount));
	}

	/**
	 * Server-side constructor
	 * @param playerInventory
	 * @param inventory
	 */
	public StrongboxChestContainer(int windowId, ContainerType<?> containerType, PlayerInventory playerInventory, IInventory inventory) {
		super(windowId, containerType, playerInventory, inventory);
        
		// open the chest (rendering)
        inventory.startOpen(playerInventory.player);
        
		// set the dimensions
		setContainerInventoryColumnCount(5);
		// strongbox has 4 less columns - to center move the xpos over by xspacing*2
		setContainerInventoryXPos(8 + getSlotXSpacing() + getSlotXSpacing());
		
		// build the container
		buildContainer(playerInventory, inventory);
	}
}
