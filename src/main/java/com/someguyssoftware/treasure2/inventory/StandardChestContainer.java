package com.someguyssoftware.treasure2.inventory;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

/**
 * This is the base/standard container for chests that is similar configuration to that of a vanilla container.
 * @author Mark Gottschling on Jan 16, 2018
 *
 */
public class StandardChestContainer extends Container implements ITreasureContainer {
	// Stores a reference to the tile entity instance for later use
	private IInventory contents;
	
	private static final int HOTBAR_SLOT_COUNT = 9;
	private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
	private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
	private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
	private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

	private static final int VANILLA_FIRST_SLOT_INDEX = 0;
	private static final int CONTAINER_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
	private static final int CONTAINER_INVENTORY_ROW_COUNT = 3;
	private static final int CONTAINER_INVENTORY_COLUMN_COUNT = 9;
	private static final int CONTAINER_INVENTORY_SLOT_COUNT = CONTAINER_INVENTORY_ROW_COUNT * CONTAINER_INVENTORY_COLUMN_COUNT;
	
	public static final int CONTAINER_INVENTORY_XPOS = 8;
	public static final int CONTAINER_INVENTORY_YPOS = 18;
	
	public static final int PLAYER_INVENTORY_XPOS = 8;
	public static final int PLAYER_INVENTORY_YPOS = 84;
		
	/*
	 * TODO  this class should extend an AbstractChestContainer where all the container consts are properties that can be
	 * set. As well as the pixel offsets etc. The init code can be put in the abstract, so only the contrete classes would set
	 * the property values.
	 */
	
	// NOTE these two static create() methods are not necessary. just call the constructors
	// NOTE actually the first one is to be use in the StandardContainerType creation
	/**
	 * AKA client-side create method
	 * @param windowID
	 * @param playerInventory
	 * @param extraData
	 * @return
	 */
	public static StandardChestContainer create(int windowID, PlayerInventory playerInventory, PacketBuffer extraData) {
		return new StandardChestContainer(windowID, playerInventory);
	}
	
	// THIS IS NEVER Called
//	public static StandardChestContainer create(int windowID, PlayerInventory playerInventory, IInventory inventory) {
//		Treasure.LOGGER.info("SERVER factory for standard chest container");
//		return new StandardChestContainer(windowID, playerInventory);
//	}
	
	/**
	 * AKA Client-side constructor
	 * @param windowID
	 * @param playerInventory
	 */
	public StandardChestContainer(int windowID, PlayerInventory playerInventory) {
		this(windowID, playerInventory, new Inventory(CONTAINER_INVENTORY_SLOT_COUNT));
		Treasure.LOGGER.info("standard chest container created for CLIENT");
	}
	
	/**
	 * @param playerInventory
	 * @param inventory
	 */
	public StandardChestContainer(int id, PlayerInventory playerInventory, IInventory inventory) {
		super(TreasureContainers.standardChestContainerType, id);
		Treasure.LOGGER.debug("standard chest container created");
		this.contents = inventory;

		// open the chest (rendering)
        inventory.openInventory(playerInventory.player);
        
		final int SLOT_X_SPACING = 18;
		final int SLOT_Y_SPACING = 18;
		final int HOTBAR_XPOS = 8;
		final int HOTBAR_YPOS = 142;
		
		/*
		 *  Add the players hotbar to the gui - the [xpos, ypos] location of each item
		 */
		for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
			int slotNumber = x;
			addSlot(new Slot(playerInventory, slotNumber, HOTBAR_XPOS + SLOT_X_SPACING * x, HOTBAR_YPOS));
		}
		
		/*
		 *  Add the rest of the players inventory to the gui
		 */
		for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
			for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
				int slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
				int xpos = PLAYER_INVENTORY_XPOS + x * SLOT_X_SPACING;
				int ypos = PLAYER_INVENTORY_YPOS + y * SLOT_Y_SPACING;
				addSlot(new Slot(playerInventory, slotNumber,  xpos, ypos));
			}
		}

		// ensure the  container's slot count is the same size of the backing IInventory
		if (CONTAINER_INVENTORY_SLOT_COUNT  != inventory.getSizeInventory()) {
			System.err.println("Mismatched slot count in ContainerBasic(" + CONTAINER_INVENTORY_SLOT_COUNT
												  + ") and TileInventory (" + inventory.getSizeInventory()+")");
		}


		
		/*
		 *  Add the tile inventory container to the gui
		 */
		for (int y = 0; y < CONTAINER_INVENTORY_ROW_COUNT; y++) {
			for (int x = 0; x < CONTAINER_INVENTORY_COLUMN_COUNT; x++) {
				int slotNumber = y * CONTAINER_INVENTORY_COLUMN_COUNT + x;
				int xpos = CONTAINER_INVENTORY_XPOS + x * SLOT_X_SPACING;
				int ypos = CONTAINER_INVENTORY_YPOS + y * SLOT_Y_SPACING;
				addSlot(new Slot(inventory, slotNumber, xpos, ypos));
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
			
			if (!mergeItemStack(sourceStack, CONTAINER_INVENTORY_FIRST_SLOT_INDEX, CONTAINER_INVENTORY_FIRST_SLOT_INDEX + CONTAINER_INVENTORY_SLOT_COUNT, false)){
				return ItemStack.EMPTY;
			}
		} else if (sourceSlotIndex >= CONTAINER_INVENTORY_FIRST_SLOT_INDEX && sourceSlotIndex < CONTAINER_INVENTORY_FIRST_SLOT_INDEX + CONTAINER_INVENTORY_SLOT_COUNT) {
			// This is a TE slot so merge the stack into the players inventory
			if (!mergeItemStack(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
				return ItemStack.EMPTY;
			}
		} else {
			System.err.print("Invalid slotIndex:" + sourceSlotIndex);
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

	@Override
	public IInventory getContents() {
		return contents;
	}

	@Override
	public void setContents(IInventory inventory) {
		this.contents = inventory;
	}
}
