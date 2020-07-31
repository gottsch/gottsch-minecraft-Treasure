/**
 * 
 */
package com.someguyssoftware.treasure2.client.render.tileentity;

import com.someguyssoftware.treasure2.client.model.ITreasureChestModel;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;

/**
 * @author Mark Gottschling onJan 9, 2018
 *
 */
public class SkullChestTileEntityRenderer extends TreasureChestTileEntityRenderer {
 
	 /**
	  * 
	  * @param texture
	  */
	 public SkullChestTileEntityRenderer(String texture, ITreasureChestModel model) {
		 super(texture, model);
	 }	
    
	 /**
	  * Set the max swing angle of 60 degrees
	  */
	 @Override
	public float getAngleModifier() {
		return 3.0F;
	}
		
	 // TODO this is exactly the same as Strongbox
    /**
     * 
     * @param te
     * @param x
     * @param y
     * @param z
     */
    public void renderLocks(AbstractTreasureChestTileEntity te, double x, double y, double z) {
        // render locks
        for (LockState lockState : te.getLockStates()) {
        	if (lockState.getLock() != null) {        		
        		// convert lock to an item stack
        		ItemStack lockStack = new ItemStack(lockState.getLock());
        		
                GlStateManager.pushMatrix();                
                // NOTE when rotating the item to match the face of chest, must adjust the amount of offset to the x,z axises and 
                // not rotate() the item - rotate() just spins it in place, not around the axis of the block
    	        GlStateManager.translate(
    	        		(float)x + lockState.getSlot().getXOffset(),
    	        		(float)y + lockState.getSlot().getYOffset(),
    	        		(float)z + lockState.getSlot().getZOffset());
    	        GlStateManager.rotate(lockState.getSlot().getRotation(), 0F, 1.0F, 0.0F);
    	        GlStateManager.scale(0.25F, 0.25F, 0.25F);
	        	Minecraft.getMinecraft().getRenderItem().renderItem(lockStack, ItemCameraTransforms.TransformType.NONE);
	        	GlStateManager.popMatrix();
        	}
        }
    }  
}
