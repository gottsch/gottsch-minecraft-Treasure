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
 * @author Mark Gottschling on Jan 9, 2018
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
		
	 @Override
	 public void updateLockScale() {
		 GlStateManager.scale(0.25F, 0.25F, 0.25F);
	} 
}
