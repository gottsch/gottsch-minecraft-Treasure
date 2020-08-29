package com.someguyssoftware.treasure2.gui.render.tileentity;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.gui.model.WitherChestModel;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// NOTE @OnlyIn extremely important! add to all Renderers
@OnlyIn(Dist.CLIENT)
public class WitherChestTileEntityRenderer extends AbstractChestTileEntityRenderer {

	public WitherChestTileEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
		super(tileEntityRendererDispatcher);
		setTexture(new ResourceLocation(Treasure.MODID + ":textures/entity/chest/wither-chest.png"));
		setModel(new WitherChestModel());
	}
	
	@Override
	public void updateModelRotationAngles(AbstractTreasureChestTileEntity tileEntity, float partialTicks) {
		float lidRotation = tileEntity.prevLidAngle + (tileEntity.lidAngle - tileEntity.prevLidAngle) * partialTicks;
        lidRotation = 1.0F - lidRotation;
        lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
        ((WitherChestModel)getModel()).getRightFrontDoor().rotateAngleY = -(lidRotation * (float)Math.PI / 2.0F);  
	}
}
