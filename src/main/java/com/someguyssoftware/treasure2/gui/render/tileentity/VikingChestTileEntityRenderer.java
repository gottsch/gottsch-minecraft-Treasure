package com.someguyssoftware.treasure2.gui.render.tileentity;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.gui.model.VikingChestModel;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// NOTE @OnlyIn extremely important! add to all Renderers
@OnlyIn(Dist.CLIENT)
public class VikingChestTileEntityRenderer extends AbstractChestTileEntityRenderer {

	public VikingChestTileEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
		super(tileEntityRendererDispatcher);
		setTexture(new ResourceLocation(Treasure.MODID + ":textures/entity/chest/viking-chest.png"));
		setModel(new VikingChestModel());
	}
}
