/**
 * 
 */
package com.someguyssoftware.treasure2.inventory;

import com.someguyssoftware.treasure2.block.TreasureChestBlock;
import com.someguyssoftware.treasure2.tileentity.TreasureChestTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * @author Mark Gottschling on Jan 16, 2018
 *
 */
public class InventoryProxy implements IInventory {
	/*
	 * Reference to the owning Tile Entity
	 */
	private TreasureChestTileEntity tileEntity;

	/**
	 * 
	 * @param entity
	 */
	public InventoryProxy(TileEntity entity) {
		setTileEntity((TreasureChestTileEntity) entity);
	}
	
	/**
	 * @return the tileEntity
	 */
	public TreasureChestTileEntity getTileEntity() {
		return tileEntity;
	}

	/**
	 * @param tileEntity the tileEntity to set
	 */
	public void setTileEntity(TreasureChestTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}
	
	/////////////////////////// IInventory Implementation ///////////////////////////////
	@Override
	public String getName() {
		return getTileEntity().getName();
	}

	/**
	 * 
	 */
	@Override
	public boolean hasCustomName() {
		return getTileEntity().hasCustomName();
	}

	/**
	 * 
	 */
	@Override
	public int getSizeInventory() {
		return getTileEntity().getNumberOfSlots();
	}

	/**
	 * 
	 */
	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : getTileEntity().getItems()) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 */
	@Override
	public ItemStack getStackInSlot(int index) {
		return getTileEntity().getItems().get(index);
	}

	/**
	 * 
	 */
	@Override
	public ItemStack decrStackSize(int index, int count) {
        ItemStack itemstack = ItemStackHelper.getAndSplit(getTileEntity().getItems(), index, count);
        if (!itemstack.isEmpty()) {
            this.markDirty();
        }
        return itemstack;
	}

	/**
	 * 
	 */
	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(getTileEntity().getItems(), index);
	}

	/**
	 * 
	 */
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
        getTileEntity().getItems().set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
        this.markDirty();
	}

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	/**
	 * 
	 */
	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		TreasureChestTileEntity te = getTileEntity();
        if (te.getWorld().getTileEntity(te.getPos()) != te) {
            return false;
        }
        else {
            return player.getDistanceSq((double)te.getPos().getX() + 0.5D, (double)te.getPos().getY() + 0.5D, (double)te.getPos().getZ() + 0.5D) <= 64.0D;
        }
	}

	/**
	 * 
	 */
	@Override
	public void openInventory(EntityPlayer player) {
		if (!player.isSpectator()) {
			TreasureChestTileEntity te = getTileEntity();
			if (te.numPlayersUsing < 0) {
				te.numPlayersUsing = 0;
			}
			++te.numPlayersUsing;
			te.getWorld().addBlockEvent(te.getPos(), te.getBlockType(), 1, te.numPlayersUsing);
			te.getWorld().notifyNeighborsOfStateChange(te.getPos(), te.getBlockType(), false);
		}
	}

	/**
	 * 
	 */
	@Override
	public void closeInventory(EntityPlayer player) {
		if (!player.isSpectator() && this.getTileEntity().getBlockType() instanceof TreasureChestBlock) {
			TreasureChestTileEntity te = getTileEntity();
			--te.numPlayersUsing;
			te.getWorld().addBlockEvent(te.getPos(), te.getBlockType(), 1, te.numPlayersUsing);
			te.getWorld().notifyNeighborsOfStateChange(te.getPos(), te.getBlockType(), false);
		}
	}

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {		
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
        this.getTileEntity().getItems().clear();
	}

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
	@Override
	public ITextComponent getDisplayName() {
		return this.getTileEntity().getDisplayName();
	}

	@Override
	public void markDirty() {
		getTileEntity().markDirty();
	}
}
