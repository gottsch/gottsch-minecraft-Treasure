/**
 * 
 */
package com.someguyssoftware.treasure2.gui.render.tileentity;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.gui.model.StrongboxModel;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;

/**
 * @author Mark Gottschling onJan 9, 2018
 *
 */
public class IronStrongboxTileEntityRenderer extends AbstractChestTileEntityRenderer {

	/**
	 * 
	 * @param texture
	 */
	public IronStrongboxTileEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
		super(tileEntityRendererDispatcher);
		setTexture(new ResourceLocation(Treasure.MODID + ":textures/entity/chest/iron-strongbox.png"));
		setModel(new StrongboxModel());
	}	

	/**
	 * 
	 * @param te
	 * @param x
	 * @param y
	 * @param z
	 */
//	public void renderLocks(AbstractTreasureChestTileEntity tileEntity, MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, int combinedLight, int combinedOverlay) {
//		if (tileEntity.getLockStates().isEmpty()) {
//			return;
//		}
//
//		// render locks
//		tileEntity.getLockStates().forEach(lockState -> {
//			//		for (LockState lockState : tileEntity.getLockStates()) {
//
//			if (lockState.getLock() != null) {
//				// convert lock to an item stack
//				ItemStack lockStack = new ItemStack(lockState.getLock());
//
//				matrixStack.push();
//				matrixStack.translate(lockState.getSlot().getXOffset(), lockState.getSlot().getYOffset(), lockState.getSlot().getZOffset());
//				matrixStack.rotate(Vector3f.YP.rotationDegrees(lockState.getSlot().getRotation()));
//// if this works, can remove this method altogether and update abstract super to use getLocksScaleModifier
//				matrixStack.scale(getLocksScaleModifier(), getLocksScaleModifier(), getLocksScaleModifier());
//				Minecraft.getInstance().getItemRenderer().renderItem(lockStack, ItemCameraTransforms.TransformType.NONE, combinedLight, OverlayTexture.NO_OVERLAY, matrixStack, renderBuffer);
//				matrixStack.pop();
//			}
//			//		}
//		});
//	}

	@Override
	public float getLocksScaleModifier() {
		return 0.25F;
	}
}
