/**
 * 
 */
package com.someguyssoftware.treasure2.client.render.tileentity;

import com.someguyssoftware.treasure2.client.model.CardboardBoxModel;
import com.someguyssoftware.treasure2.client.model.ITreasureChestModel;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.CardboardBoxTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;

/**
 * @author Mark Gottschling onJan 9, 2018
 *
 */
public class CardboardBoxTileEntityRenderer extends TreasureChestTileEntityRenderer {

	/**
	 * 
	 * @param texture
	 */
	public CardboardBoxTileEntityRenderer(String texture, ITreasureChestModel model) {
		super(texture, model);
	}

	@Override
	public void updateModelLidRotation(AbstractTreasureChestTileEntity te, float partialTicks) {
		CardboardBoxTileEntity cte = (CardboardBoxTileEntity) te;

		// update in the inner lid
		float innerLidRotation = cte.prevInnerLidAngle + (cte.innerLidAngle - cte.prevInnerLidAngle) * partialTicks;
		innerLidRotation = 1.0F - innerLidRotation;
		innerLidRotation = 1.0F - innerLidRotation * innerLidRotation * innerLidRotation;
		((CardboardBoxModel)getModel()).getInnerLid().rotateAngleX = (innerLidRotation * (float) Math.PI / getAngleModifier()); // not negated
		
		float lidRotation = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
		lidRotation = 1.0F - lidRotation;
		lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
		getModel().getLid().rotateAngleZ = -(lidRotation * (float) Math.PI / getAngleModifier());
	}
	
	@Override
	public float getAngleModifier() {
		return 0.8F;
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