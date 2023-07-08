/*
 * This file is part of  Treasure2.
 * Copyright (c) 2018 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.forge.treasure2.client.renderer.blockentity;

import org.joml.Vector3d;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import mod.gottsch.forge.treasure2.client.model.blockentity.ITreasureChestModel;
import mod.gottsch.forge.treasure2.core.block.StandardChestBlock;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.lock.LockState;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author Mark Gottschling on Jan 9, 2018
 *
 */
public interface ITreasureChestBlockEntityRenderer {

	void renderLocks(AbstractTreasureChestBlockEntity tileEntity, PoseStack PoseStack,
			MultiBufferSource renderBuffer, int combinedLight, int combinedOverlay);

	/**
	 */
	default public void updateTranslation(PoseStack PoseStack) {
		// The model is defined centred on [0,0,0], so if we drew it at the current render origin, its centre would be
		// at the corner of the block, sunk halfway into the ground and overlapping into the adjacent blocks.
		// We want it to hover above the centre of the hopper base, so we need to translate up and across to the desired position
		final Vector3d TRANSLATION_OFFSET = new Vector3d(0.5, 1.5, 0.5);
		PoseStack.translate(TRANSLATION_OFFSET.x, TRANSLATION_OFFSET.y, TRANSLATION_OFFSET.z);
	}

	default public void updateScale(PoseStack PoseStack) {
		PoseStack.scale(1, 1, 1);
	}

	default public void updateRotation(PoseStack PoseStack, Direction direction) {
		float angle = getHorizontalAngle(direction);
		PoseStack.mulPose(Axis.YP.rotationDegrees(-angle));
	}

	/**
	 * 
	 * @param blockEntity
	 * @param partialTicks
	 */
//	default public void  updateModelLidRotation(AbstractTreasureChestBlockEntity blockEntity, float partialTicks) {
//		float lidRotation = blockEntity.prevLidAngle + (blockEntity.lidAngle - blockEntity.prevLidAngle) * partialTicks;
//		lidRotation = 1.0F - lidRotation;
//		lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
//		getModel().getLid().xRot = -(lidRotation * (float) Math.PI / getAngleModifier());
//	}

	/**
	 * 
	 * @param tileEntity
	 * @return
	 */
	default public Direction getDirection(BlockEntity tileEntity) {
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
//	default public float getAngleModifier() {
//		return 2.0F;
//	}
	
	/**
	 * Modifies the scale of the Lock item(s).
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

	default public void updateLockScale(PoseStack PoseStack) {
		PoseStack.scale(getLockScaleModifier(), getLockScaleModifier(), getLockScaleModifier());
	}

	/**
	 * 
	 * @param lockState
	 */
	default public void updateLockRotation(PoseStack PoseStack, LockState lockState) {
		PoseStack.mulPose(Axis.YP.rotationDegrees(lockState.getSlot().getRotation()));
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
