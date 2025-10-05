/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.client.model.blockentity;

import java.util.function.Function;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;


// NECESSARY?
/**
 * @author Mark Gottschling on Aug 24, 2020
 *
 */
public abstract class AbstractTreasureChestModel extends Model implements ITreasureChestModel {

	public AbstractTreasureChestModel(ModelPart root) {
		super(RenderType::entitySolid);
	}

	public AbstractTreasureChestModel(ModelPart root, Function<ResourceLocation, RenderType> renderType) {
		super(renderType);
	}

	public Function<ResourceLocation, RenderType> getRenderType() {
		return this.renderType;
	}

	// TODO this is moot right now and does nothing
//	@Override
//	public final RenderType getChestRenderType(ResourceLocation location) {
//		return super.renderType(location);
//	}
//
	public final Function<ResourceLocation, RenderType> getChestRenderType() {
		return super.renderType;
	}
	
	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer renderBuffer, int combinedLight, int combinedOverlay,
			float f, float g, float h, float i) {
		// do nothing
	}
}
