/**
 * 
 */
package com.someguyssoftware.treasure2.client.render.tileentity;

import com.someguyssoftware.treasure2.client.model.ITreasureChestModel;

import net.minecraft.client.renderer.GlStateManager;

/**
 * 
 * @author Mark Gottschling on Mar 23, 2018
 *
 */
public class CompressorChestTileEntityRenderer extends TreasureChestTileEntityRenderer {
 
	 /**
	  * 
	  * @param texture
	  */
	 public CompressorChestTileEntityRenderer(String texture, ITreasureChestModel model) {
		 super(texture, model);
	 }
    
    @Override
	public void updateTranslation(double x, double y, double z) {
        // initial position (centered moved up only half as normal as the size if half as normal)
		GlStateManager.translate((float)x + 0.5F, (float)y + 0.75F, (float)z + 0.5F);
	}
	
    @Override
    public void updateScale() {
    	// shrink the size of the chest by half
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
	}
    
	 @Override
	 public void updateLockScale() {
		 GlStateManager.scale(0.20F, 0.20F, 0.20F);
	} 
}
