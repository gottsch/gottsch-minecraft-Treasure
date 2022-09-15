/**
 * 
 */
package com.someguyssoftware.treasure2.inventory;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.IKeyRingCapability;
import com.someguyssoftware.treasure2.capability.KeyRingCapabilityProvider;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * @author Mark Gottschling on Mar 9, 2018
 *
 */
public class KeyRingInventory implements IInventory {
	public static int INVENTORY_SIZE = 14;
	
	/*
	 * Reference to the owning ItemStack
	 */
	private ItemStack itemStack;
	
    /** IInventory properties */
    private NonNullList<ItemStack> items = NonNullList.<ItemStack>withSize(INVENTORY_SIZE, ItemStack.EMPTY);
    
	/**
	 * 
	 * @param stack
	 */
	public KeyRingInventory(ItemStack stack) {
        // save a ref to the item stack
		this.itemStack = stack;
		
		if (stack.hasCapability(KeyRingCapabilityProvider.KEY_RING_INVENTORY_CAPABILITY, null)) {
			IItemHandler itemHandler = stack.getCapability(KeyRingCapabilityProvider.KEY_RING_INVENTORY_CAPABILITY, null);
			readInventoryFromHandler(itemHandler);
		}
	}
	
	/**
	 * 
	 * @param handler
	 */
	public void readInventoryFromHandler(IItemHandler handler) {
		try {
			// read the inventory
			for (int i = 0; i < INVENTORY_SIZE; i++) {
                items.set(i, handler.getStackInSlot(i));
			}
		}
		catch(Exception e) {
			Treasure.LOGGER.error("Error reading items from IItemHandler:",  e);
		}
	}
	
	/**
	 * 
	 * @param handler
	 */
	public void writeInventoryToHandler(IItemHandler handler) {
		/* 
		 * NOTE must clear the ItemStackHandler first because it retains it's inventory, the
		 * when insertItem is called, it actually appends, not replaces, items into it's inventory
		 * causing doubling of items.
		 */
		// clear the item handler capability			
		((ItemStackHandler)handler).setSize(INVENTORY_SIZE);
		// copy all the items over
		try {
			for (int i = 0; i < items.size(); i++) {
				handler.insertItem(i, items.get(i), false);
			}
		}
		catch(Exception e) {
			Treasure.LOGGER.error("Error writing Inventory to IItemHandler:",  e);
		}
	}
	
	///////////// IInventory Method
	
	/* (non-Javadoc)
	 * @see net.minecraft.world.IWorldNameable#getName()
	 */
	@Override
	public String getName() {
		return "display.key_ring.name";
	}

	/* (non-Javadoc)
	 * @see net.minecraft.world.IWorldNameable#hasCustomName()
	 */
	@Override
	public boolean hasCustomName() {
		return false;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.world.IWorldNameable#getDisplayName()
	 */
	@Override
	public ITextComponent getDisplayName() {
	       return (ITextComponent) new TextComponentTranslation(this.getName(), new Object[0]);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#getSizeInventory()
	 */
	@Override
	public int getSizeInventory() {
//		return getNumberOfSlots();
		return INVENTORY_SIZE;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : getItems()) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#getStackInSlot(int)
	 */
	@Override
	public ItemStack getStackInSlot(int index) {
		return getItems().get(index);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#decrStackSize(int, int)
	 */
	@Override
	public ItemStack decrStackSize(int index, int count) {
        ItemStack itemstack = ItemStackHelper.getAndSplit(getItems(), index, count);
        if (!itemstack.isEmpty()) {
            this.markDirty();
        }
        return itemstack;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#removeStackFromSlot(int)
	 */
	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(getItems(), index);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#setInventorySlotContents(int, net.minecraft.item.ItemStack)
	 */
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
        getItems().set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#getInventoryStackLimit()
	 * 
	 * If using custom Slots, this value must equal Slot.getItemStackLimit()
	 */
	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#markDirty()
	 */
	@Override
	public void markDirty() {
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#isUsableByPlayer(net.minecraft.entity.player.EntityPlayer)
	 */
	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#openInventory(net.minecraft.entity.player.EntityPlayer)
	 */
	@Override
	public void openInventory(EntityPlayer player) {
		/*
		 *  clear the items. prevents duplicating keys.
		 *  this is to prevent the player from taking the keys from the key ring inventory gui, then dropping the key ring
		 *  while the inventory is open, then picking up the key ring again with all its items intact.
		 *  so now, if the player does dropping the key ring, it will not have any items in it's inventory and the player will lose any
		 *  keys that are left in the gui when closed.
		 */
		if (getItemStack().hasCapability(KeyRingCapabilityProvider.KEY_RING_CAPABILITY, null)) {
			IKeyRingCapability cap = getItemStack().getCapability(KeyRingCapabilityProvider.KEY_RING_CAPABILITY, null);
			cap.setOpen(true);
		}
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#closeInventory(net.minecraft.entity.player.EntityPlayer)
	 */
	@Override
	public void closeInventory(EntityPlayer player) {
		/*
		 *  write the locked state to the nbt
		 */
		if (getItemStack().hasCapability(KeyRingCapabilityProvider.KEY_RING_INVENTORY_CAPABILITY, null)) {
			IItemHandler itemHandler = getItemStack().getCapability(KeyRingCapabilityProvider.KEY_RING_INVENTORY_CAPABILITY, null);
			writeInventoryToHandler(itemHandler);
		}
		if (getItemStack().hasCapability(KeyRingCapabilityProvider.KEY_RING_CAPABILITY, null)) {
			IKeyRingCapability cap = getItemStack().getCapability(KeyRingCapabilityProvider.KEY_RING_CAPABILITY, null);
			cap.setOpen(false);
		}
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#getField(int)
	 */
	@Override
	public int getField(int id) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#setField(int, int)
	 */
	@Override
	public void setField(int id, int value) {
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#getFieldCount()
	 */
	@Override
	public int getFieldCount() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#clear()
	 */
	@Override
	public void clear() {
		getItems().clear();
	}

	/**
	 * @return the itemStack
	 */
	public ItemStack getItemStack() {
		return itemStack;
	}

	/**
	 * @param itemStack the itemStack to set
	 */
	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

//	/**
//	 * @return the numberOfSlots
//	 */
//	public int getNumberOfSlots() {
//		return numberOfSlots;
//	}

	/////////// End of IInventory methods
	
	/**
	 * @return the items
	 */
	public NonNullList<ItemStack> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(NonNullList<ItemStack> items) {
		this.items = items;
	}

}
