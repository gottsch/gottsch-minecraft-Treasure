/**
 * 
 */
package com.someguyssoftware.treasure2.client.render.tileentity;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.client.model.ITreasureChestModel;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * @author Mark Gottschling onJan 9, 2018
 *
 */
public class TreasureChestTileEntityRenderer extends TileEntitySpecialRenderer<AbstractTreasureChestTileEntity> {
	public static final int NORTH = 2;
	public static final int SOUTH = 3;
	public static final int WEST = 4;
	public static final int EAST = 5;

	private ResourceLocation texture;
	private ITreasureChestModel model;

	/**
	 * 
	 * @param texture
	 */
	public TreasureChestTileEntityRenderer(String texture, ITreasureChestModel model) {
		super();
		setTexture(new ResourceLocation(Treasure.MODID + ":textures/entity/chest/" + texture + ".png"));
		setModel(model);
	}

	/**
	 * 
	 * @param destroyStage
	 */
	public void applyDestroyGlState(int destroyStage) {
		if (destroyStage >= 0) {
			this.bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4.0F, 4.0F, 1.0F);
			GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
			GlStateManager.matrixMode(5888);
		}
	}

	/**
	 * 
	 * 
	 * @param destroyStage
	 */
	public void popDestroyGlState(int destroyStage) {
		if (destroyStage >= 0) {
			GlStateManager.matrixMode(5890);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
		}
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
	public void render(AbstractTreasureChestTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

		if (!(te instanceof AbstractTreasureChestTileEntity))
			return; // should never happen

		// apply the destory gl state (if any)
		applyDestroyGlState(destroyStage);

		// get the model
		ITreasureChestModel model = getModel();
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
		// (chest entity were created in Techne and need different translations than
		// vanilla tile entities)
		GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);

		// This rotation part is very important! Without it, your model will render
		// upside-down.
		// (rotate 180 degrees around the z-axis)
		GlStateManager.rotate(180F, 0F, 0F, 1.0F);
		// rotate block to the correct direction that it is facing.
		GlStateManager.rotate((float) rotation, 0.0F, 1.0F, 0.0F);

		// update the lid rotation
		updateModelLidRotation(te, partialTicks);

		// render the model
		model.renderAll(te);

		GlStateManager.popMatrix();
		// end of rendering chest entity ////

		// pop the destroy stage matrix
		popDestroyGlState(destroyStage);

		////////////// render the locks //////////////////////////////////////
		if (!te.getLockStates().isEmpty()) {
			renderLocks(te, x, y, z);
		}
		////////////// end of render the locks //////////////////////////////////////
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

	// TODO move out to interface
	/**
	 * 
	 * @param te
	 * @param partialTicks
	 */
	public void updateModelLidRotation(AbstractTreasureChestTileEntity te, float partialTicks) {
		float lidRotation = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
		lidRotation = 1.0F - lidRotation;
		lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
		model.getLid().rotateAngleX = -(lidRotation * (float) Math.PI / getAngleModifier());
	}

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
	public float getAngleModifier() {
		return 2.0F;
	}
	
	/**
	 * 
	 * @param meta
	 * @return
	 */
	public int getRotation(int meta) {
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
	 * @param te
	 * @param x
	 * @param y
	 * @param z
	 */
	public void renderLocks(AbstractTreasureChestTileEntity te, double x, double y, double z) {
		// Treasure.logger.debug("=====================================================================");
		// render locks
		for (LockState lockState : te.getLockStates()) {
			// Treasure.logger.debug("Render LS:" + lockState);
			if (lockState.getLock() != null) {
				// convert lock to an item stack
				ItemStack lockStack = new ItemStack(lockState.getLock());

				GlStateManager.pushMatrix();
				// NOTE when rotating the item to match the face of chest, must adjust the
				// amount of offset to the x,z axises and
				// not rotate() the item - rotate() just spins it in place, not around the axis
				// of the block
				GlStateManager.translate((float) x + lockState.getSlot().getXOffset(), (float) y + lockState.getSlot().getYOffset(), (float) z + lockState.getSlot().getZOffset());
				GlStateManager.rotate(lockState.getSlot().getRotation(), 0F, 1.0F, 0.0F);
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
				Minecraft.getMinecraft().getRenderItem().renderItem(lockStack, ItemCameraTransforms.TransformType.NONE);
				GlStateManager.popMatrix();
			}
		}
	}

	/**
	 * @return the texture
	 */
	public ResourceLocation getTexture() {
		return texture;
	}

	/**
	 * @param texture
	 *            the texture to set
	 */
	public void setTexture(ResourceLocation texture) {
		this.texture = texture;
	}

	/**
	 * @return the model
	 */
	public ITreasureChestModel getModel() {
		return model;
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(ITreasureChestModel model) {
		this.model = model;
	}

}
