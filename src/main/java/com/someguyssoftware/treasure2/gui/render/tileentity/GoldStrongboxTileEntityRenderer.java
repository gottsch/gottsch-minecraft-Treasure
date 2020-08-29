/**
 * 
 */
package com.someguyssoftware.treasure2.gui.render.tileentity;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.gui.model.StandardChestModel;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;

/**
 * @author Mark Gottschling onJan 9, 2018
 *
 */
public class GoldStrongboxTileEntityRenderer extends IronStrongboxTileEntityRenderer {
 
	 /**
	  * 
	  * @param texture
	  */
	 public GoldStrongboxTileEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
			super(tileEntityRendererDispatcher);
			setTexture(new ResourceLocation(Treasure.MODID + ":textures/entity/chest/gold-strongbox.png"));
	 }	
   
}
