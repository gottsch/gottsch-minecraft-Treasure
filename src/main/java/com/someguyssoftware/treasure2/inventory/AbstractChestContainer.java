/**
 * 
 */
package com.someguyssoftware.treasure2.inventory;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

/**
 * @author Mark Gottschling on Feb 15, 2018
 *
 */
public abstract class AbstractChestContainer extends Container implements ITreasureContainer {
	// stores a reference to the tile entity instance for later use
	protected  IInventory contents;
	
	protected final int HOTBAR_SLOT_COUNT = 9;
	protected final int PLAYER_INVENTORY_ROW_COUNT = 3;
	protected final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
	protected final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
	protected final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
	protected final int VANILLA_FIRST_SLOT_INDEX = 0;
	protected final int CONTAINER_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

	private int containerInventoryRowCount = 3; // default value
	private int containerInventoryColumnCount = 9; // default value
	
	// default values for vanilla containers
	private int slotXSpacing = 18;
	private int slotYSpacing = 18;
	private int hotbarXPos = 8;
	private int hotbarYPos = 142;
	private int playerInventoryXPos = 8;
	private int playerInventoryYPos = 84;
	private int containerInventoryXPos = 8;
	private int containerInventoryYPos = 18;
	
	/**
	 * Client constructor
	 * @param windowID
	 * @param playerInventory
	 * @param slotCount
	 */
//	public AbstractChestContainer(int windowID, ContainerType<?> containerType, PlayerInventory playerInventory, int slotCount) {
//		this(windowID, containerType, playerInventory, new Inventory(slotCount));
//	}
	
	// NEW contstructor
	public AbstractChestContainer(int windowID, ContainerType<?> containerType, PlayerInventory playerInventory, IInventory inventory) {
		super(containerType, windowID);
		this.contents = inventory;
		Treasure.LOGGER.info("inventory slot count -> {}", inventory.getSizeInventory());
		
		// open the chest (rendering)
//        inventory.openInventory(playerInventory.player);
        
        // NOTE the parent class then calls buildContainer() in it's constructor

	}
	
	/**
	 * OLD
	 * @param playerInventory
	 * @param inventory
	 */
//	public AbstractChestContainer(int windowID, PlayerInventory playerInventory, IInventory inventory) {
//		super(TreasureContainers.standardChestContainerType, windowID);
//		this.contents = inventory;
//
//		// open the chest (rendering)
//        inventory.openInventory(playerInventory.player);
//	}
	
	/**
	 * 
	 * @param playerInventory
	 * @param inventory
	 */
	public void buildContainer(PlayerInventory playerInventory, IInventory inventory) {
		Treasure.LOGGER.info("in buildContainer...");
		// ensure the  container's slot count is the same size of the backing IInventory
		if (getContainerInventorySlotCount()  != inventory.getSizeInventory()) {
			Treasure.LOGGER.error("Mismatched slot count in Container(" + getContainerInventorySlotCount()
				  + ") and TileInventory (" + inventory.getSizeInventory()+")");
		}
		        
        buildHotbar(playerInventory);
        buildPlayerInventory(playerInventory);
        buildContainerInventory();
	}

	/**
	 * 
	 */
	public void buildHotbar(PlayerInventory playerInventory) {
		for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
			int slotNumber = x;
			addSlot(new Slot(playerInventory, slotNumber, getHotbarXPos() + getSlotXSpacing() * x, getHotbarYPos()));
		}
	}
	
	/**
	 * 
	 * @param playerInventory
	 */
	public void buildPlayerInventory(PlayerInventory playerInventory) {
		/*
		 *  Add the rest of the players inventory to the gui
		 */
		for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
			for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
				int slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
				int xpos = getPlayerInventoryXPos() + x * getSlotXSpacing();
				int ypos = getPlayerInventoryYPos() + y * getSlotYSpacing();
				addSlot(new Slot(playerInventory, slotNumber,  xpos, ypos));
			}
		}
	}

	/**
	 * 
	 */
	public void buildContainerInventory() {		
		/*
		 *  Add the tile inventory container to the gui
		 */
		for (int y = 0; y < getContainerInventoryRowCount(); y++) {
			for (int x = 0; x < getContainerInventoryColumnCount(); x++) {
				int slotNumber = /*CONTAINER_INVENTORY_FIRST_SLOT_INDEX +*/ (y * getContainerInventoryColumnCount()) + x;
				int xpos = getContainerInventoryXPos() + x * getSlotXSpacing();
				int ypos = getContainerInventoryYPos() + y * getSlotYSpacing();
				addSlot(new Slot(contents, slotNumber, xpos, ypos));
			}
		}
	}
	
	// This is where you specify what happens when a player shift clicks a slot in the gui
	//  (when you shift click a slot in the TileEntity Inventory, it moves it to the first available position in the hotbar and/or
	//    player inventory.  When you you shift-click a hotbar or player inventory item, it moves it to the first available
	//    position in the TileEntity inventory)
	// At the very least you must override this and return EMPTY_ITEM or the game will crash when the player shift clicks a slot
	// returns EMPTY_ITEM if the source slot is empty, or if none of the the source slot items could be moved
	//   otherwise, returns a copy of the source stack
	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int sourceSlotIndex) {
		Slot sourceSlot = (Slot)inventorySlots.get(sourceSlotIndex);
		if (sourceSlot == null || !sourceSlot.getHasStack()) return ItemStack.EMPTY;
		ItemStack sourceStack = sourceSlot.getStack();
		ItemStack copyOfSourceStack = sourceStack.copy();
	 
		// Check if the slot clicked is one of the vanilla container slots
		if (sourceSlotIndex >= VANILLA_FIRST_SLOT_INDEX && sourceSlotIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
			/*
			 *  This is a vanilla container slot so merge the stack into the tile inventory
			 */
			// first ensure that the sourcStack is a valid item for the container
			if (!contents.isItemValidForSlot(sourceSlotIndex, sourceStack)) {
				return ItemStack.EMPTY;
			}
			
			if (!mergeItemStack(sourceStack, CONTAINER_INVENTORY_FIRST_SLOT_INDEX, CONTAINER_INVENTORY_FIRST_SLOT_INDEX + getContainerInventorySlotCount(), false)){
				return ItemStack.EMPTY;
			}
		} else if (sourceSlotIndex >= CONTAINER_INVENTORY_FIRST_SLOT_INDEX && sourceSlotIndex < CONTAINER_INVENTORY_FIRST_SLOT_INDEX + getContainerInventorySlotCount()) {
			// This is a TE slot so merge the stack into the players inventory
			if (!mergeItemStack(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
				return ItemStack.EMPTY;
			}
		} else {
			Treasure.LOGGER.warn("Invalid slotIndex:" + sourceSlotIndex);
			return ItemStack.EMPTY;
		}

		// If stack size == 0 (the entire stack was moved) set slot contents to null
		if (sourceStack.getCount() == 0) {  // getStackSize
			sourceSlot.putStack(ItemStack.EMPTY);
		} else {
			sourceSlot.onSlotChanged();
		}

		sourceSlot.onTake(player, sourceStack);  //onPickupFromSlot()
		return copyOfSourceStack;
	}
	
	/*
	 * pass the close container message to the tileEntity
	 * @see ContainerChest
	 * @see TileEntityChest
	 * @see net.minecraft.inventory.Container#onContainerClosed(net.minecraft.entity.player.EntityPlayer)
	 */
	@Override
	public void onContainerClosed(PlayerEntity playerIn) {
		super.onContainerClosed(playerIn);
		this.contents.closeInventory(playerIn);
	}

	/**
	 * 
	 */
	@Override
	public boolean canInteractWith(PlayerEntity player) {
		return contents.isUsableByPlayer(player);
	}
	
	/**
	 * @return the containerInventoryRowCount
	 */
	public int getContainerInventoryRowCount() {
		return containerInventoryRowCount;
	}
	
	/**
	 * @param containerInventoryRowCount the containerInventoryRowCount to set
	 */
	public void setContainerInventoryRowCount(int containerInventoryRowCount) {
		this.containerInventoryRowCount = containerInventoryRowCount;
	}
	/**
	 * @return the containerInventoruColumnCount
	 */
	public int getContainerInventoryColumnCount() {
		return containerInventoryColumnCount;
	}
	/**
	 * @param containerInventoruColumnCount the containerInventoruColumnCount to set
	 */
	public void setContainerInventoryColumnCount(int containerInventoryColumnCount) {
		this.containerInventoryColumnCount = containerInventoryColumnCount;
	}
	
	/**
	 * @return the containerInventorySlotCount
	 */
	public int getContainerInventorySlotCount() {
		return getContainerInventoryRowCount() * getContainerInventoryColumnCount();
	}

	/**
	 * @return the slotXSpacing
	 */
	public int getSlotXSpacing() {
		return slotXSpacing;
	}

	/**
	 * @param slotXSpacing the slotXSpacing to set
	 */
	public void setSlotXSpacing(int slotXSpacing) {
		this.slotXSpacing = slotXSpacing;
	}

	/**
	 * @return the slotYSpacing
	 */
	public int getSlotYSpacing() {
		return slotYSpacing;
	}

	/**
	 * @param slotYSpacing the slotYSpacing to set
	 */
	public void setSlotYSpacing(int slotYSpacing) {
		this.slotYSpacing = slotYSpacing;
	}

	/**
	 * @return the hotbarYPos
	 */
	public int getHotbarYPos() {
		return hotbarYPos;
	}

	/**
	 * @param hotbarYPos the hotbarYPos to set
	 */
	public void setHotbarYPos(int hotbarYPos) {
		this.hotbarYPos = hotbarYPos;
	}

	/**
	 * @return the hotbarXPos
	 */
	public int getHotbarXPos() {
		return hotbarXPos;
	}

	/**
	 * @param hotbarXPos the hotbarXPos to set
	 */
	public void setHotbarXPos(int hotbarXPos) {
		this.hotbarXPos = hotbarXPos;
	}

	/**
	 * @return the playerInventoryXPos
	 */
	public int getPlayerInventoryXPos() {
		return playerInventoryXPos;
	}

	/**
	 * @param playerInventoryXPos the playerInventoryXPos to set
	 */
	public void setPlayerInventoryXPos(int playerInventoryXPos) {
		this.playerInventoryXPos = playerInventoryXPos;
	}

	/**
	 * @return the playerInventoryYPos
	 */
	public int getPlayerInventoryYPos() {
		return playerInventoryYPos;
	}

	/**
	 * @param playerInventoryYPos the playerInventoryYPos to set
	 */
	public void setPlayerInventoryYPos(int playerInventoryYPos) {
		this.playerInventoryYPos = playerInventoryYPos;
	}

	/**
	 * @return the containerInventoryXPos
	 */
	public int getContainerInventoryXPos() {
		return containerInventoryXPos;
	}

	/**
	 * @param containerInventoryXPos the containerInventoryXPos to set
	 */
	public void setContainerInventoryXPos(int containerInventoryXPos) {
		this.containerInventoryXPos = containerInventoryXPos;
	}

	/**
	 * @return the containerInventoryYPos
	 */
	public int getContainerInventoryYPos() {
		return containerInventoryYPos;
	}

	/**
	 * @param containerInventoryYPos the containerInventoryYPos to set
	 */
	public void setContainerInventoryYPos(int containerInventoryYPos) {
		this.containerInventoryYPos = containerInventoryYPos;
	}

	/**
	 * @return the inventory
	 */
//	public IInventory getChestInventory() {
//		return contents;
//	}
//
//	/**
//	 * @param inventory the inventory to set
//	 */
//	public void setChestInventory(IInventory inventory) {
//		this.contents = inventory;
//	}

	public IInventory getContents() {
		return contents;
	}

	public void setContents(IInventory contents) {
		this.contents = contents;
	}
}
