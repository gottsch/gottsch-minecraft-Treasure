/**
 * 
 */
package com.someguyssoftware.treasure2.client.render.tileentity;

import com.someguyssoftware.treasure2.client.model.CardboardBoxModel;
import com.someguyssoftware.treasure2.client.model.ITreasureChestModel;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.CardboardBoxTileEntity;
import com.someguyssoftware.treasure2.tileentity.CauldronChestTileEntity;

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

	/**
	 * 
	 * @param te
	 * @param x
	 * @param y
	 * @param z
	 * @param partialTicks
	 * @param destroyStage
	 * @param alpha
	 */
	@Override
	public void render(AbstractTreasureChestTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

		if (!(te instanceof CardboardBoxTileEntity)) return; // should never happen

		// add the destory textures
		if (destroyStage >= 0) {
			this.bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4.0F, 4.0F, 1.0F);
			GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
			GlStateManager.matrixMode(5888);
		}

		// get the model    	
		CardboardBoxModel model = (CardboardBoxModel) getModel();   

		// bind the texture
		this.bindTexture(getTexture());
		// get the chest rotation
		int meta = 0;
		if (te.hasWorld()) {
			meta = te.getBlockMetadata();
		}
		int rotation = getRotation(meta);        

		// start render matrix
		GlStateManager.pushMatrix();
		// initial position (centered moved up)
		// (chest entity were created in Techne and need different translations than vanilla tile entities)
		GlStateManager.translate((float)x + 0.5F, (float)y + 1.5F, (float)z + 0.5F);

		// This rotation part is very important! Without it, your model will render upside-down.
		// (rotate 180 degrees around the z-axis)
		GlStateManager.rotate(180F, 0F, 0F, 1.0F);
		// rotate block to the correct direction that it is facing.
		GlStateManager.rotate((float)rotation, 0.0F, 1.0F, 0.0F);

//		CardboardBoxTileEntity cte = (CardboardBoxTileEntity) te;
//		float lidRotation = cte.prevLidAngle + (cte.lidAngle - cte.prevLidAngle) * partialTicks;
//		lidRotation = 1.0F - lidRotation;
//		lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
//		// NOTE positive rotation here (getLid() returns lidLeft property)
//		model.getLid().rotateAngleZ = (lidRotation * (float)Math.PI / getAngleModifier());
		updateModelLidRotation(te, partialTicks);

		model.renderAll(te);

		GlStateManager.popMatrix();
		// end of rendering chest entity ////


		// pop the destroy stage matrix
		if (destroyStage >= 0) {
			GlStateManager.matrixMode(5890);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
		}

		////////////// render the locks //////////////////////////////////////
		if (!te.getLockStates().isEmpty()) {
			renderLocks(te, x, y, z);
		}
		////////////// end of render the locks //////////////////////////////////////
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public void updateModelLidRotation(AbstractTreasureChestTileEntity te, float partialTicks) {
		CardboardBoxTileEntity cte = (CardboardBoxTileEntity) te;
		
//		if (cte.isLidOpen) {
			// update in the inner lid
			float innerLidRotation = cte.prevInnerLidAngle + (cte.innerLidAngle - cte.prevInnerLidAngle) * partialTicks;
			innerLidRotation = 1.0F - innerLidRotation;
			innerLidRotation = 1.0F - innerLidRotation * innerLidRotation * innerLidRotation;
			((CardboardBoxModel)getModel()).getInnerLid().rotateAngleX = (innerLidRotation * (float) Math.PI / getAngleModifier()); // not negated
//		}
		
		float lidRotation = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
		lidRotation = 1.0F - lidRotation;
		lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
		getModel().getLid().rotateAngleZ = -(lidRotation * (float) Math.PI / getAngleModifier());
	}
	
	@Override
	public float getAngleModifier() {
		return 0.8F;
	}
	
	/**
	 * 
	 * @param te
	 * @param x
	 * @param y
	 * @param z
	 */
	@Override
	public void renderLocks(AbstractTreasureChestTileEntity te, double x, double y, double z) {
		/*
		 * UP and DOWN facing locks will not get rotated like the other directions do, so get the chest's rotation
		 */
		int meta = 0;
		if (te.hasWorld()) {
			meta = te.getBlockMetadata();
		}
		int rotation = getRotation(meta); 
		
		// render locks
		for (LockState lockState : te.getLockStates()) {
			// Treasure.logger.debug("Render LS:" + lockState);
			if (lockState.getLock() != null) {
				// convert lock to an item stack
				ItemStack lockStack = new ItemStack(lockState.getLock());

				GlStateManager.pushMatrix();
				// NOTE when rotating the item to match the face of chest, must adjust the
				// amount of offset to the x,z axises and not rotate() the item 
				// - rotate() just spins it in place, not around the axis of the block
				GlStateManager.translate((float) x + lockState.getSlot().getXOffset(), (float) y + lockState.getSlot().getYOffset(), (float) z + lockState.getSlot().getZOffset());
				// rotate the locks on the x axis to lay flat
				GlStateManager.rotate(90, 1.0F, 0.0F, 0.0F); // NOTE changed from Y to X axis
				// rotate lock to the correct direction that the block is facing.
				GlStateManager.rotate(/*(float)rotation*/lockState.getSlot().getRotation(), 0.0F, 0.0F, 1.0F); // NOTE now Z axis is the Y axis since we rotated on the X axis first.
				// shrink the locks a bit
				GlStateManager.scale(0.35F, 0.35F, 0.35F);
				Minecraft.getMinecraft().getRenderItem().renderItem(lockStack, ItemCameraTransforms.TransformType.NONE);
				GlStateManager.popMatrix();
			}
		}
	}
}