package com.someguyssoftware.treasure2.gui.render.tileentity;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.gui.model.SkullChestModel;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// NOTE @OnlyIn extremely important! add to all Renderers
@OnlyIn(Dist.CLIENT)
public class SkullChestTileEntityRenderer extends AbstractChestTileEntityRenderer {

	/**
	 * 
	 * @param tileEntityRendererDispatcher
	 * @return
	 */
	public static SkullChestTileEntityRenderer createGoldSkull(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
		return new SkullChestTileEntityRenderer(tileEntityRendererDispatcher, new ResourceLocation(Treasure.MODID + ":textures/entity/chest/gold-skull-chest.png"));
	}
	
	/**
	 * 
	 * @param tileEntityRendererDispatcher
	 */
	public SkullChestTileEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
		super(tileEntityRendererDispatcher);
		setTexture(new ResourceLocation(Treasure.MODID + ":textures/entity/chest/skull-chest.png"));
		setModel(new SkullChestModel());
	}
	
	/**
	 * 
	 * @param tileEntityRendererDispatcher
	 * @param texture
	 */
	public SkullChestTileEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher, ResourceLocation texture) {
		super(tileEntityRendererDispatcher);
		setTexture(texture);
		setModel(new SkullChestModel());
	}
	
	 /**
	  * Set the max swing angle of 60 degrees
	  */
	 @Override
	public float getAngleModifier() {
		return 3.0F;
	}
	 
	@Override
	public float getLocksScaleModifier() {
		return 0.25F;
	}
}
