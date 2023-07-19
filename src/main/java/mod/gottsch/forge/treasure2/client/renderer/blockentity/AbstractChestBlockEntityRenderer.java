/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
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


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import mod.gottsch.forge.treasure2.client.model.blockentity.ITreasureChestModel;
import mod.gottsch.forge.treasure2.core.block.StandardChestBlock;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.lock.LockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author Mark Gottschling onJan 9, 2018
 *
 */
public abstract class AbstractChestBlockEntityRenderer implements BlockEntityRenderer<AbstractTreasureChestBlockEntity>, ITreasureChestBlockEntityRenderer {
	private ResourceLocation texture;
	private ITreasureChestModel model;
	private Material material;
	
	/**
	 * render the tile entity - called every frame while the tileentity is in view of the player
	 *
	 * @param tileEntityIn the associated tile entity
	 * @param partialTicks    the fraction of a tick that this frame is being rendered at - used to interpolate frames between
	 *                        ticks, to make animations smoother.  For example - if the frame rate is steady at 80 frames per second,
	 *                        this method will be called four times per tick, with partialTicks spaced 0.25 apart, (eg) 0, 0.25, 0.5, 0.75
	 * @param PoseStack     the PoseStack is used to track the current view transformations that have been applied - i.e translation, rotation, scaling
	 *                        it is needed for you to render the view properly.
	 * @param renderBuffers    the buffer that you should render your model to
	 * @param combinedLight   the blocklight + skylight value for the tileEntity.  see http://greyminecraftcoder.blogspot.com/2014/12/lighting-18.html (outdated, but the concepts are still valid)
	 * @param combinedOverlay value for the "combined overlay" which changes the render based on an overlay texture (see OverlayTexture class).
	 *                        Used by vanilla for (1) red tint when a living entity takes damage, and (2) "flash" effect for creeper when ignited
	 *                        CreeperRenderer.getOverlayProgress()
	 */
	@Override
	public void render(AbstractTreasureChestBlockEntity blockEntity, float partialTicks, PoseStack poseStack,
			MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {

		if (!(blockEntity instanceof AbstractTreasureChestBlockEntity)) {
			return; // should never happen
		}

		Level world = blockEntity.getLevel();
		boolean hasWorld = (world != null);
		BlockState state = blockEntity.getBlockState();
		Direction facing = Direction.NORTH;
		if (hasWorld) {
			facing = state.getValue(StandardChestBlock.FACING);
		}

		// push the current transformation matrix + normals matrix
		poseStack.pushPose(); 

		// initial position (centered moved up)
		updateTranslation(poseStack);

		// The model is defined centred on [0,0,0], so if we drew it at the current render origin, its centre would be
		// at the corner of the block, sunk halfway into the ground and overlapping into the adjacent blocks.
		// We want it to hover above the centre of the hopper base, so we need to translate up and across to the desired position
		// final Vector3d TRANSLATION_OFFSET = new Vector3d(0.5, 1.5, 0.5);
		// PoseStack.translate(TRANSLATION_OFFSET.x, TRANSLATION_OFFSET.y, TRANSLATION_OFFSET.z); // translate

		// setup scale
		poseStack.scale(-1, -1, 1);

		// adjust the scale of the model
		updateScale(poseStack);
		
		updateRotation(poseStack, facing);
		// float f = getHorizontalAngle(facing);
		// PoseStack.mulPose(Vector3f.YP.rotationDegrees(-f));

		// update the lid rotation
		 getModel().updateModelLidRotation(blockEntity, partialTicks);
			
		VertexConsumer renderBuffer = material.buffer(bufferSource, RenderType::entitySolid);		
		model.renderToBuffer(poseStack, renderBuffer, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F, blockEntity);
		poseStack.popPose();		

		//	////////////// render the locks //////////////////////////////////////
		renderLocks(blockEntity, poseStack, bufferSource, combinedLight, combinedOverlay);
		//	////////////// end of render the locks //////////////////////////////////////
	}

	/**
	 * 
	 * @param blockEntity
	 * @param combinedOverlay 
	 * @param combinedLight 
	 * @param renderBuffer 
	 * @param poseStack 
	 * @param x
	 * @param y
	 * @param z
	 */
	@Override
	public void renderLocks(AbstractTreasureChestBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource renderBuffer, int combinedLight, int combinedOverlay) {
		if (blockEntity.getLockStates().isEmpty()) {
			return;
		}

		// render locks
		for (LockState lockState : blockEntity.getLockStates()) {

			if (lockState.getLock() != null) {
				// convert lock to an item stack
				ItemStack lockStack = new ItemStack(lockState.getLock());
				poseStack.pushPose();
				
				// NOTE when rotating the item to match the face of chest, must adjust the
				// amount of offset to the x,z axises and
				// not rotate() the item - rotate() just spins it in place, not around the axis
				// of the block
				poseStack.translate(lockState.getSlot().getXOffset(), lockState.getSlot().getYOffset(), lockState.getSlot().getZOffset());

				updateLockRotation(poseStack, lockState);
				// PoseStack.mulPose(Vector3f.YP.rotationDegrees(lockState.getSlot().getRotation()));
				updateLockScale(poseStack);
				// PoseStack.scale(getLocksScaleModifier(), getLocksScaleModifier(), getLocksScaleModifier());
				Minecraft.getInstance().getItemRenderer().renderStatic(
						lockStack, 
						ItemDisplayContext.NONE,
						combinedLight, 
						OverlayTexture.NO_OVERLAY, 
						poseStack, 
						renderBuffer,
						blockEntity.getLevel(),
						OverlayTexture.NO_OVERLAY);
				poseStack.popPose();
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
	 * @param texture the texture to set
	 */
	public void setTexture(ResourceLocation texture) {
		this.texture = texture;
	}

	/**
	 * @return the model
	 */
	@Override
	public ITreasureChestModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	@Override
	public void setModel(ITreasureChestModel model) {
		this.model = model;
	}
	
	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}
}
