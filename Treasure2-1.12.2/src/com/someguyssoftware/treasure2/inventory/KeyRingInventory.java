/**
 * 
 */
package com.someguyssoftware.treasure2.inventory;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.item.KeyItem;
import com.someguyssoftware.treasure2.item.KeyRingItem;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * @author Mark Gottschling on Mar 9, 2018
 *
 */
public class KeyRingInventory implements IInventory {

	/*
	 * Reference to the owning ItemStack
	 */
	private ItemStack itemStack;
	
    /** IInventory properties */
    private int numberOfSlots = 14; // default size
    private NonNullList<ItemStack> items = NonNullList.<ItemStack>withSize(numberOfSlots, ItemStack.EMPTY);
    
	/**
	 * 
	 * @param stack
	 */
	public KeyRingInventory(ItemStack stack) {
        // save a ref to the item stack
		this.itemStack = stack;
		
		// copy items from stack to items property;
        if (stack.hasTagCompound()) {
        	readInventoryFromNBT(stack.getTagCompound());
        }
	}
	
	/**
	 * 
	 * @param parentNBT
	 */
	public void readInventoryFromNBT(NBTTagCompound parentNBT) {
		try {
			// read the inventory
			ItemStackHelper.loadAllItems(parentNBT, this.getItems());
		}
		catch(Exception e) {
			Treasure.logger.error("Error reading Properties from NBT:",  e);
		}
	}
	
	/**
	 * Writes the inventory to NBT
	 * @param parentNBT
	 * @return
	 */
	public NBTTagCompound writeInventoryToNBT(NBTTagCompound parentNBT) {
		try {
			// write inventory
			ItemStackHelper.saveAllItems(parentNBT, this.getItems());
		}
		catch(Exception e) {
			Treasure.logger.error("Error writing Inventory to NBT:",  e);
		}
		return parentNBT;
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
		return getNumberOfSlots();
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
		return 1; // 8
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
		if (getItemStack().getTagCompound() == null) return;
		
		getItemStack().getTagCompound().setTag("Items", new NBTTagList());

		// set the IS_OPEN tag
		getItemStack().getTagCompound().setBoolean(KeyRingItem.IS_OPEN, true);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#closeInventory(net.minecraft.entity.player.EntityPlayer)
	 */
	@Override
	public void closeInventory(EntityPlayer player) {
		/*
		 *  write the locked state to the nbt
		 */
		if (!getItemStack().hasTagCompound()) {
			getItemStack().setTagCompound(new NBTTagCompound());
		}		        
		writeInventoryToNBT(getItemStack().getTagCompound());
		
		// update the IS_OPEN flag
		getItemStack().getTagCompound().setBoolean(KeyRingItem.IS_OPEN, false);
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

	/**
	 * @return the numberOfSlots
	 */
	public int getNumberOfSlots() {
		return numberOfSlots;
	}

	/**
	 * @param numberOfSlots the numberOfSlots to set
	 */
	public void setNumberOfSlots(int numberOfSlots) {
		this.numberOfSlots = numberOfSlots;
	}

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
