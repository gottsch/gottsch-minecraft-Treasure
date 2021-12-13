/**
 * 
 */
package com.someguyssoftware.treasure2.tileentity;

import java.util.List;

import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity.GenerationContext;

import net.minecraft.inventory.IInventory;
import net.minecraft.core.Direction;

/**
 * @author Mark Gottschling on Dec 15, 2020
 *
 */
public interface ITreasureChestTileEntity extends IInventory {
    public List<LockState> getLockStates();
    public void setLockStates(List<LockState> lockStates);
	boolean hasLocks();
	
    public Direction getFacing();
    public void setFacing(int facing);
    
    public boolean isSealed();
    public void setSealed(boolean sealed);
	
    public GenerationContext getGenerationContext();
    public void setGenerationContext(GenerationContext context);
    
	int getNumberOfSlots();
	void setNumberOfSlots(int numberOfSlots);
	void sendUpdates();
	/**
	 * 
	 */
	void updateEntityState();
}
