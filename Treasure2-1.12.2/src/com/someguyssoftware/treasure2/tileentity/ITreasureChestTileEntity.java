package com.someguyssoftware.treasure2.tileentity;

import java.util.List;

import com.someguyssoftware.treasure2.inventory.InventoryProxy;
import com.someguyssoftware.treasure2.lock.LockState;

import net.minecraft.util.text.ITextComponent;

/**
 * 
 * @author Mark Gottschling on Mar 6, 2018
 *
 */
public interface ITreasureChestTileEntity {

	public List<LockState> getLockStates();
	public void setLockStates(List<LockState> lockStates);

	public boolean hasLocks();

	public int getNumberOfSlots();
	public void setNumberOfSlots(int numberOfSlots);

	public InventoryProxy getInventoryProxy();
	public void setInventoryProxy(InventoryProxy inventoryProxy);
	public ITextComponent getDisplayName();

}
