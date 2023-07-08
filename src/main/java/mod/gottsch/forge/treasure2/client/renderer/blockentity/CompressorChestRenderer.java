/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021 Mark Gottschling (gottsch)
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

import org.joml.Vector3d;

import com.mojang.blaze3d.vertex.PoseStack;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.client.model.blockentity.CompressorChestModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CompressorChestRenderer extends AbstractChestBlockEntityRenderer {
	public static final ResourceLocation COMPRESSOR_CHEST_RENDERER_ATLAS_TEXTURE = new ResourceLocation(Treasure.MODID, "entity/chest/compressor_chest");

	/**
	 * 
	 * @param context
	 */
	public CompressorChestRenderer(BlockEntityRendererProvider.Context context) {
		setModel(new CompressorChestModel(context.bakeLayer(CompressorChestModel.LAYER_LOCATION)));
		setMaterial(new Material(TextureAtlas.LOCATION_BLOCKS, COMPRESSOR_CHEST_RENDERER_ATLAS_TEXTURE));
	}

	@Override
	public void updateScale(PoseStack poseStack) {
		// shrink the size of the chest by half
		poseStack.scale(0.5F, 0.5F, 0.5F);
	}

	@Override
	public void updateTranslation(PoseStack poseStack) {
		final Vector3d TRANSLATION_OFFSET = new Vector3d(0.5, 0.75, 0.5);
		poseStack.translate(TRANSLATION_OFFSET.x, TRANSLATION_OFFSET.y, TRANSLATION_OFFSET.z);
	}

	@Override
	public float getLockScaleModifier() {
		return 0.20F;
	}
}
