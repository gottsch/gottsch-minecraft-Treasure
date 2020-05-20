package com.someguyssoftware.treasure2.inventory;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.item.PouchItem;
import com.someguyssoftware.treasure2.item.TreasureItems;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * 
 * @author Mark Gottschling on May 14, 2020
 *
 */
public class PouchContainer extends AbstractChestContainer {

	private ItemStack stack;
	/**
	 * 
	 * @param invPlayer
	 * @param inventory
	 */
	public PouchContainer(InventoryPlayer invPlayer, IInventory inventory, ItemStack stack) {
		super(invPlayer, inventory);

		this.stack = stack;
		
		// set the dimensions
		setHotbarYPos(176);
		setPlayerInventoryYPos(118);
		// pouch has 6 less columns - to center move the xpos over by xspacing*2
		setContainerInventoryXPos(8 + getSlotXSpacing() * 3);
		setContainerInventoryYPos(52);
		
        setContainerInventoryRowCount(3);
		setContainerInventoryColumnCount(3);

		// build the container
		buildContainer(invPlayer, inventory);
	}
	
	@Override
	public int getContainerInventorySlotCount() {
		return inventory.getSizeInventory();
	}
	
	/**
	 * Pouch prevents the held slot (which contains the pouch itself) from being moved.
	 */
	@Override
	public void buildHotbar(InventoryPlayer player) {
		for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
			int slotNumber = x;
			if (slotNumber == player.currentItem) {
				addSlotToContainer(new NoSlot(player, slotNumber, getHotbarXPos() + getSlotXSpacing() * x, getHotbarYPos()));
			}
			else {
				addSlotToContainer(new Slot(player, slotNumber, getHotbarXPos() + getSlotXSpacing() * x, getHotbarYPos()));
			}
		}
	}
	
	/**
	 * 
	 */
	public void buildContainerInventory() {
		int arcaneSlots = 0;
		PouchItem pouchItem = (PouchItem) this.stack.getItem();
		if (pouchItem == TreasureItems.LUCKY_POUCH) {
			arcaneSlots = 1;
		}
		else if (pouchItem == TreasureItems.APPRENTICES_POUCH) {
			arcaneSlots = 2;
		}
		else if (pouchItem == TreasureItems.MASTERS_POUCH) {
			arcaneSlots = 3;
		}
		else {
			Treasure.logger.debug("pouch is -> {}", pouchItem.getRegistryName());
		}
		Treasure.logger.debug("arcane slots -> {}", arcaneSlots);
		for (int y = 0; y < getContainerInventoryRowCount(); y++) {
			for (int x = 0; x < getContainerInventoryColumnCount(); x++) {
				int slotNumber = (y * getContainerInventoryColumnCount()) + x;
				int xpos = getContainerInventoryXPos() + x * getSlotXSpacing();
				int ypos = getContainerInventoryYPos() + y * getSlotYSpacing();
				// pouches use PouchSlot or ArcanePouchSlot
				if (slotNumber < arcaneSlots) {
					Treasure.logger.debug("adding arcane slot at index -> {}", slotNumber);
					addSlotToContainer(new ArcanePouchSlot(inventory, slotNumber, xpos, ypos));
				}
				else {
					addSlotToContainer(new PouchSlot(inventory, slotNumber, xpos, ypos));
				}
			}
		}
	}
}
