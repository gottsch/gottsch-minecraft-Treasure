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
package mod.gottsch.forge.treasure2.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.client.model.blockentity.MilkCrateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// NOTE @OnlyIn extremely important! add to all Renderers
@OnlyIn(Dist.CLIENT)
public class MilkCrateRenderer extends AbstractChestBlockEntityRenderer {
	public static final ResourceLocation MILK_CRATE_RENDERER_ATLAS_TEXTURE = new ResourceLocation(Treasure.MODID, "entity/chest/milk_crate");

	public MilkCrateRenderer(BlockEntityRendererProvider.Context context) {
		setModel(new MilkCrateModel(context.bakeLayer(MilkCrateModel.LAYER_LOCATION)));
		setMaterial(new Material(TextureAtlas.LOCATION_BLOCKS, MILK_CRATE_RENDERER_ATLAS_TEXTURE));

	}

	@Override
	public float getLockScaleModifier() {
		return 0.28F;
	}

	@Override
	public void updateScale(PoseStack matrixStack) {
		// shrink the size of the chest 3/4 size
		matrixStack.scale(0.75F, 0.75F, 0.75F);
	}

	@Override
	public void updateTranslation(PoseStack matrixStack) {
		final Vec3 TRANSLATION_OFFSET = new Vec3(0.5, 1.125F, 0.5);
		matrixStack.translate(TRANSLATION_OFFSET.x, TRANSLATION_OFFSET.y, TRANSLATION_OFFSET.z);
	}
}
