/*
 * This file is part of Legacy Vault.
 * Copyright (c) 2022, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
 *
 * Legacy Vault is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Legacy Vault is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Legacy Vault.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.forge.treasure2.core.inventory;

import java.util.Optional;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.ITreasureChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

/**
 * 
 * @author Mark Gottschling on Jun 19, 2022
 *
 */
public abstract class AbstractTreasureContainerMenu extends AbstractContainerMenu implements ITreasureContainer {
	// the backing block entity
	private AbstractTreasureChestBlockEntity blockEntity;
	// the player opening the vault
	protected Player playerEntity;
	// original player's inventory
	protected Inventory playerInventory;
	
	// the player's item handler wrapper
	protected IItemHandler playerItemHandler;
	// the chest inventory
	protected IItemHandler itemHandler;

	///////////////
	protected final int HOTBAR_SLOT_COUNT = 9;
	protected final int PLAYER_INVENTORY_ROW_COUNT = 3;
	protected final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
	protected final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
	protected final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
	protected final int VANILLA_FIRST_SLOT_INDEX = 0;
	protected final int CONTAINER_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

	private int menuInventoryRowCount = 3; // default value
	private int menuInventoryColumnCount = 9; // default value

	// default values for vanilla containers
	private int slotXSpacing = 18;
	private int slotYSpacing = 18;
	private int hotbarXPos = 8;
	private int hotbarYPos = 142;
	private int playerInventoryXPos = 8;
	private int playerInventoryYPos = 84;
	private int menuInventoryXPos = 8;
	private int menuInventoryYPos = 18;

	/**
	 * Constructor for blocks/chests that have a backing BlockEntity.
	 * @param menuType
	 * @param containerId
	 * @param pos
	 * @param playerInventory
	 * @param player
	 */
	public AbstractTreasureContainerMenu(int containerId, MenuType<?> type, BlockPos pos, Inventory playerInventory, Player player) {
		super(type, containerId);

		this.playerEntity =  player;
		this.playerInventory = playerInventory;
		this.playerItemHandler = new InvWrapper(playerInventory);

		// get the block entity
		blockEntity = (AbstractTreasureChestBlockEntity)player.getCommandSenderWorld().getBlockEntity(pos);				
		if (blockEntity != null) {
			Optional<IItemHandler> handler = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).map(h -> h);
			if (handler.isPresent()) {
				itemHandler = handler.get();
			}
			blockEntity.openCount++;
		}
	}
	
	/**
	 * Constructor for items/other when an inventory is passed in.
	 * @param containerId
	 * @param type
	 * @param pos
	 * @param playerInventory
	 * @param itemHandler
	 */
	public AbstractTreasureContainerMenu(int containerId, MenuType<?> type, Inventory playerInventory, IItemHandler itemHandler) {
		super(type, containerId);
		
		this.playerInventory = playerInventory;
		this.playerItemHandler = new InvWrapper(playerInventory);
		this.itemHandler = itemHandler;
	}

	public void buildContainer() {
		buildContainer(this.playerItemHandler);
	}
	
	/**
	 * 
	 * @param playerInventory
	 * @param itemHandler
	 */
	private void buildContainer(IItemHandler playerInventory) {
		// build inventories
		buildHotbar(playerInventory);
		buildPlayerInventory(playerInventory);
		buildContainerInventory();
	}

	/**
	 * 
	 */
	//	@Override
	public void buildHotbar(IItemHandler inventory) {
		for (int slotNumber = 0; slotNumber < HOTBAR_SLOT_COUNT; slotNumber++) {
			addSlot(new SlotItemHandler(inventory, slotNumber, getHotbarXPos() + getSlotXSpacing() * slotNumber, getHotbarYPos()));
		}
	}

	/**
	 * 
	 * @param playerItemHandler
	 */
	public void buildPlayerInventory(IItemHandler inventory) {
		/*
		 *  Add the rest of the players inventory to the gui
		 */
		for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
			for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
				int slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
				int xpos = getPlayerInventoryXPos() + x * getSlotXSpacing();
				int ypos = getPlayerInventoryYPos() + y * getSlotYSpacing();
				addSlot(new SlotItemHandler(inventory, slotNumber,  xpos, ypos));
			}
		}
	}	

	/**
	 *  Add the vault inventory to the gui
	 */
	public void buildContainerInventory() {		
		if (itemHandler == null ) {
			Treasure.LOGGER.info("vaultInventory is null");
			return;
		}

		// build slots for display inventory
		for (int y = 0; y < getMenuInventoryRowCount(); y++) {
			for (int x = 0; x < getMenuInventoryColumnCount(); x++) {
				int slotNumber = (y * getMenuInventoryColumnCount()) + x;
				int xpos = getMenuInventoryXPos() + x * getSlotXSpacing();
				int ypos = getMenuInventoryYPos() + y * getSlotYSpacing();
				addSlot(new SlotItemHandler(this.itemHandler, slotNumber, xpos, ypos));
			}
		}
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		ItemStack itemStackCopy = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack stack = slot.getItem();
			itemStackCopy = stack.copy();

			if (index >= VANILLA_FIRST_SLOT_INDEX && index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
				// TODO add a test Slot.isValid()            	
				if (!this.moveItemStackTo(stack, CONTAINER_INVENTORY_FIRST_SLOT_INDEX, CONTAINER_INVENTORY_FIRST_SLOT_INDEX + getMenuInventorySlotCount(), true)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (index >= CONTAINER_INVENTORY_FIRST_SLOT_INDEX && index < CONTAINER_INVENTORY_FIRST_SLOT_INDEX + getMenuInventorySlotCount()) {
					if (!this.moveItemStackTo(stack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
						return ItemStack.EMPTY;
					}
				} else {
					Treasure.LOGGER.warn("Invalid slotIndex:" + index);
					return ItemStack.EMPTY;
				}
			}

			if (stack.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (stack.getCount() == itemStackCopy.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(playerIn, stack);
		}
		return itemStackCopy;
	}

	@Override
	public boolean stillValid(Player player) {
		// is player within distance
		if (blockEntity != null) {
			BlockPos pos = this.blockEntity.getBlockPos();
			boolean withinDistance = player.distanceToSqr((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D) <= 64.0D;
			return withinDistance && (blockEntity instanceof ITreasureChestBlockEntity);
		}
		return true;
	}

	@Override
	public void removed(Player player) {

		// update the open count
		if (blockEntity != null) {
			blockEntity.openCount--;
			if (blockEntity.openCount < 0) {
				blockEntity.openCount = 0; 
			}
		}
		super.removed(player);
	}

	public int getHotbarXPos() {
		return hotbarXPos;
	}

	public void setHotbarXPos(int hotbarXPos) {
		this.hotbarXPos = hotbarXPos;
	}

	public int getHotbarYPos() {
		return hotbarYPos;
	}

	public void setHotbarYPos(int hotbarYPos) {
		this.hotbarYPos = hotbarYPos;
	}

	public int getSlotXSpacing() {
		return slotXSpacing;
	}

	public void setSlotXSpacing(int slotXSpacing) {
		this.slotXSpacing = slotXSpacing;
	}

	public int getPlayerInventoryXPos() {
		return playerInventoryXPos;
	}

	public void setPlayerInventoryXPos(int playerInventoryXPos) {
		this.playerInventoryXPos = playerInventoryXPos;
	}

	public int getPlayerInventoryYPos() {
		return playerInventoryYPos;
	}

	public void setPlayerInventoryYPos(int playerInventoryYPos) {
		this.playerInventoryYPos = playerInventoryYPos;
	}

	public int getSlotYSpacing() {
		return slotYSpacing;
	}

	public void setSlotYSpacing(int slotYSpacing) {
		this.slotYSpacing = slotYSpacing;
	}

	/**
	 * @return the containerInventorySlotCount
	 */
	public int getMenuInventorySlotCount() {
		return getMenuInventoryRowCount() * getMenuInventoryColumnCount();
	}

	public int getMenuInventoryRowCount() {
		return menuInventoryRowCount;
	}

	public void setMenuInventoryRowCount(int menuInventoryRowCount) {
		this.menuInventoryRowCount = menuInventoryRowCount;
	}

	public int getMenuInventoryColumnCount() {
		return menuInventoryColumnCount;
	}

	public void setMenuInventoryColumnCount(int menuInventoryColumnCount) {
		this.menuInventoryColumnCount = menuInventoryColumnCount;
	}

	public int getMenuInventoryXPos() {
		return menuInventoryXPos;
	}

	public void setMenuInventoryXPos(int menuInventoryXPos) {
		this.menuInventoryXPos = menuInventoryXPos;
	}

	public int getMenuInventoryYPos() {
		return menuInventoryYPos;
	}

	public void setMenuInventoryYPos(int menuInventoryYPos) {
		this.menuInventoryYPos = menuInventoryYPos;
	}
}
