package com.someguyssoftware.treasure2.client.render.tileentity;

import com.someguyssoftware.treasure2.client.model.ITreasureChestModel;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public interface ITreasureChestTileEntityRenderer {

	int NORTH = 2;
	int SOUTH = 3;
	int WEST = 4;
	int EAST = 5;

	/**
	 * 
	 * @param meta
	 * @return
	 */
	default public int getRotation(int meta) {
		switch (meta) {
		case NORTH:
			return 0;
		case SOUTH:
			return 180;
		case WEST:
			return -90;
		case EAST:
			return 90;
		}
		return 0;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	default public void updateTranslation(double x, double y, double z) {
		GlStateManager.translate((float)x + 0.5F, (float)y + 1.5F, (float)z + 0.5F);
	}
	
	/**
	 * 
	 */
	default public void updateScale() {
		GlStateManager.scale(1F, 1F, 1F);
	}
	
	/**
	 * 
	 */
	default public void updateLockScale() {
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
	}
	
	/**
	 * 
	 * @param lockState
	 */
	default public void updateLockRotation(LockState lockState) {
		GlStateManager.rotate(lockState.getSlot().getRotation(), 0F, 1.0F, 0.0F);
	}
	
	/**
	 * 
	 * @param te
	 * @param partialTicks
	 */
	default public void updateModelLidRotation(AbstractTreasureChestTileEntity te, float partialTicks) {
		float lidRotation = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
		lidRotation = 1.0F - lidRotation;
		lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
		getModel().getLid().rotateAngleX = -(lidRotation * (float) Math.PI / getAngleModifier());
	}

	/**
	 * @return the texture
	 */
	ResourceLocation getTexture();

	/**
	 * @param texture
	 *            the texture to set
	 */
	void setTexture(ResourceLocation texture);

	/**
	 * @return the model
	 */
	ITreasureChestModel getModel();

	/**
	 * @param model
	 *            the model to set
	 */
	void setModel(ITreasureChestModel model);

	/**
	 * Modifies the max angle that the lid can swing.
	 * The max swing angle by default is 180 degrees. The max swing angle is divided the modifier.
	 * Increasing the size of the modifier reduces the size of the max swing angle.
	 * Ex:
	 * Return 0.8 = 225 degrees
	 * Return 1.0 = 180 degrees
	 * Return 2.0 = 90 degrees
	 * Return 3.0 = 60 degrees
	 * Return 4.0 = 45 degrees
	 * 
	 * @return
	 */
	default public float getAngleModifier() {
		return 2.0F;
	}

}