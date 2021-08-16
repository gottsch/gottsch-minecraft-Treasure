package com.someguyssoftware.treasure2.gui.render.tileentity;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.gui.model.SafeModel;
import com.someguyssoftware.treasure2.gui.model.WitherChestModel;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.SafeTileEntity;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// NOTE @OnlyIn extremely important! add to all Renderers
@OnlyIn(Dist.CLIENT)
public class SafeTileEntityRenderer extends AbstractChestTileEntityRenderer {

	public SafeTileEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
		super(tileEntityRendererDispatcher);
		setTexture(new ResourceLocation(Treasure.MODID + ":textures/entity/chest/safe.png"));
		setModel(new SafeModel());
	}

	@Override
	public void updateModelLidRotation(AbstractTreasureChestTileEntity tileEntity, float partialTicks) {
		SafeTileEntity ste = (SafeTileEntity) tileEntity;

		if (ste.isLidClosed) {
			float handleRotation = ste.prevHandleAngle + (ste.handleAngle - ste.prevHandleAngle) * partialTicks;
			handleRotation = 1.0F - handleRotation;
			handleRotation = 1.0F - handleRotation * handleRotation * handleRotation;
			((SafeModel)getModel()).getHandleA1().zRot = (handleRotation * (float)Math.PI / getAngleModifier());
		}
		else {
			// render handleB rotating around y-axis
		}
		float lidRotation = ste.prevLidAngle + (ste.lidAngle - ste.prevLidAngle) * partialTicks;
		lidRotation = 1.0F - lidRotation;
		lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
		getModel().getLid().yRot = (lidRotation * (float)Math.PI / getAngleModifier());
	}

	@Override
	public float getLocksScaleModifier() {
		return 0.3F;
	}
}
