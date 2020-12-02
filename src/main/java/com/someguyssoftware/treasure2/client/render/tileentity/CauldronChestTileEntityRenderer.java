/**
 * 
 */
package com.someguyssoftware.treasure2.client.render.tileentity;

import com.someguyssoftware.treasure2.client.model.ITreasureChestModel;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.CauldronChestTileEntity;

import net.minecraft.client.renderer.GlStateManager;

/**
 * @author Mark Gottschling onJan 9, 2018
 *
 */
public class CauldronChestTileEntityRenderer extends TreasureChestTileEntityRenderer {

	/**
	 * 
	 * @param texture
	 */
	public CauldronChestTileEntityRenderer(String texture, ITreasureChestModel model) {
		super(texture, model);
	}

	@Override
	public void updateModelLidRotation(AbstractTreasureChestTileEntity te, float partialTicks) {
		CauldronChestTileEntity cte = (CauldronChestTileEntity) te;
		float lidRotation = cte.prevLidAngle + (cte.lidAngle - cte.prevLidAngle) * partialTicks;
		lidRotation = 1.0F - lidRotation;
		lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
		// NOTE positive rotation here (getLid() returns lidLeft property)
		getModel().getLid().rotateAngleZ = (lidRotation * (float)Math.PI / getAngleModifier());
	}
	
	@Override
	public void updateLockRotation(LockState lockState) {
		// rotate the locks on the x axis to lay flat
		GlStateManager.rotate(90, 1.0F, 0.0F, 0.0F); // NOTE changed from Y to X axis
		// rotate lock to the correct direction that the block is facing.
		GlStateManager.rotate(lockState.getSlot().getRotation(), 0.0F, 0.0F, 1.0F); // NOTE now Z axis is the Y axis since we rotated on the X axis first.
	}
	
	@Override
	public void updateLockScale() {
		GlStateManager.scale(0.35F, 0.35F, 0.35F);
	}
}