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
package mod.gottsch.forge.treasure2.client.model.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.ITreasureChestBlockEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;


/**
 * @author Mark Gottschling onJan 9, 2018
 *
 */
public interface ITreasureChestModel {
	public ModelPart getLid();
	
	default public void  updateModelLidRotation(AbstractTreasureChestBlockEntity blockEntity, float partialTicks) {
		float lidRotation = blockEntity.prevLidAngle + (blockEntity.lidAngle - blockEntity.prevLidAngle) * partialTicks;
		lidRotation = 1.0F - lidRotation;
		lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
		getLid().xRot = -(lidRotation * (float) Math.PI / getAngleModifier());
	}
	
	default public float getAngleModifier() {
		return 2.0F;
	}
	
	/**
	 * wrapper for vanilla Model.getRenderType()
	 * @param locationIn
	 * @return
	 */
	@Deprecated
	public RenderType getChestRenderType(ResourceLocation location);

	/**
	 * 
	 * @param poseStack
	 * @param vertexConsumer
	 * @param combinedLight
	 * @param combinedOverlay
	 * @param f
	 * @param g
	 * @param h
	 * @param i
	 */
//	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int combinedLight, int combinedOverlay,
//			float f, float g, float h, float i);

	public void renderToBuffer(PoseStack poseStack, VertexConsumer renderBuffer, int combinedLight, int combinedOverlay,
			float f, float g, float h, float i, AbstractTreasureChestBlockEntity blockEntity);


}
