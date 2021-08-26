package com.someguyssoftware.treasure2.gui.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.someguyssoftware.treasure2.block.StandardChestBlock;
import com.someguyssoftware.treasure2.gui.model.ITreasureChestModel;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public interface ITreasureChestTileEntityRenderer {

	void renderLocks(AbstractTreasureChestTileEntity tileEntity, MatrixStack matrixStack,
			IRenderTypeBuffer renderBuffer, int combinedLight, int combinedOverlay);

	/**
	 */
	default public void updateTranslation(MatrixStack matrixStack) {
		// The model is defined centred on [0,0,0], so if we drew it at the current render origin, its centre would be
		// at the corner of the block, sunk halfway into the ground and overlapping into the adjacent blocks.
		// We want it to hover above the centre of the hopper base, so we need to translate up and across to the desired position
		final Vector3d TRANSLATION_OFFSET = new Vector3d(0.5, 1.5, 0.5);
		matrixStack.translate(TRANSLATION_OFFSET.x, TRANSLATION_OFFSET.y, TRANSLATION_OFFSET.z);
	}

	default public void updateScale(MatrixStack matrixStack) {
		matrixStack.scale(1, 1, 1);
	}

	default public void updateRotation(MatrixStack matrixStack, Direction direction) {
		float angle = getHorizontalAngle(direction);
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(-angle));
	}

	/**
	 * 
	 * @param tileEntity
	 * @param partialTicks
	 */
	default public void  updateModelLidRotation(AbstractTreasureChestTileEntity tileEntity, float partialTicks) {
		float lidRotation = tileEntity.prevLidAngle + (tileEntity.lidAngle - tileEntity.prevLidAngle) * partialTicks;
		lidRotation = 1.0F - lidRotation;
		lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
		getModel().getLid().xRot = -(lidRotation * (float) Math.PI / getAngleModifier());
	}

	/**
	 * 
	 * @param tileEntity
	 * @return
	 */
	default public Direction getDirection(TileEntity tileEntity) {
		BlockState state = tileEntity.getBlockState();
		Direction direction = Direction.NORTH;
		if (state != null) {
			direction = state.getValue(StandardChestBlock.FACING);
		}
		return direction;
	}
	
	/**
	 * Helper method since all my models face the opposite direction of vanilla models
	 * @param facing
	 * @return
	 */
	default public int getHorizontalAngle(Direction facing) {
		switch (facing) {
		default:
		case NORTH:
			return 0;
		case SOUTH:
			return 180;
		case WEST:
			return 90;
		case EAST:
			return -90;
		}
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
	default public float getAngleModifier() {
		return 2.0F;
	}
	
	/**
	 * Modifies teh scale of the Lock item(s).
	 * Ranges from 0.0F to x.xF.
	 * Ex:
	 * Return 1.0F = full size
	 * Return 0.5 = half size
	 * 
	 * @return
	 */
	default public float getLockScaleModifier() {
		return 0.5F;
	}

	default public void updateLockScale(MatrixStack matrixStack) {
		matrixStack.scale(getLockScaleModifier(), getLockScaleModifier(), getLockScaleModifier());
	}

	/**
	 * 
	 * @param lockState
	 */
	default public void updateLockRotation(MatrixStack matrixStack, LockState lockState) {
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(lockState.getSlot().getRotation()));
	}

	/**
	 * 
	 * @return
	 */
	ResourceLocation getTexture();

	/**
	 * 
	 * @param texture
	 */
	void setTexture(ResourceLocation texture);

	/**
	 * 
	 * @return
	 */
	ITreasureChestModel getModel();

	/**
	 * 
	 * @param model
	 */
	void setModel(ITreasureChestModel model);

}
