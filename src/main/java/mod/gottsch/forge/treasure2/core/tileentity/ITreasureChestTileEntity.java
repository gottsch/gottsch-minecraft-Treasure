/**
 * 
 */
package mod.gottsch.forge.treasure2.core.tileentity;

import java.util.List;

import mod.gottsch.forge.treasure2.core.lock.LockState;
import mod.gottsch.forge.treasure2.core.tileentity.AbstractTreasureChestTileEntity.GenerationContext;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

/**
 * @author Mark Gottschling on Dec 15, 2020
 *
 */
public interface ITreasureChestTileEntity extends IInventory {
    public List<LockState> getLockStates();
    public void setLockStates(List<LockState> lockStates);
    // TODO can be removed as a required method
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
	
	ResourceLocation getMimic();
	void setMimic(ResourceLocation mimic);
}
