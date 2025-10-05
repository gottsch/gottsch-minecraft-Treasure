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
import mod.gottsch.forge.treasure2.client.model.blockentity.BarrelChestModel;
import mod.gottsch.forge.treasure2.client.model.blockentity.ITreasureChestModel;
import mod.gottsch.forge.treasure2.client.model.blockentity.InfernalChestModel;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 
 * @author Mark Gottschling on 9/28/2025
 *
 */
// NOTE @OnlyIn extremely important! add to all Renderers
@OnlyIn(Dist.CLIENT)
public class InfernalChestRenderer extends AbstractChestBlockEntityRenderer {
	public static final ResourceLocation INFERNAL_CHEST_RENDERER_ATLAS_TEXTURE = new ResourceLocation(Treasure.MODID, "entity/chest/infernal_chest");

	/**
	 *
	 * @param context
	 */
	public InfernalChestRenderer(BlockEntityRendererProvider.Context context) {
		setModel(new InfernalChestModel(context.bakeLayer(InfernalChestModel.LAYER_LOCATION)));
		setMaterial(new Material(TextureAtlas.LOCATION_BLOCKS, INFERNAL_CHEST_RENDERER_ATLAS_TEXTURE));
	}

	/*
	 * NOTE similar to CelestialChestRenderer because both have wings, but currently
	 *  don't have any common interface nor class hierarchy ie WingedChestModel/Renderer.
	 */
	@Override
	public void updateAdditionalRotation(PoseStack poseStack, ITreasureChestModel model, AbstractTreasureChestBlockEntity blockEntity, float partialTicks) {
		super.updateAdditionalRotation(poseStack, model, blockEntity, partialTicks);

		// flap wings
		long worldTime = blockEntity.getLevel().getGameTime();
		float ageInTicks = (float)worldTime + partialTicks;

		float amplitude = 7.0F; // max rotation in degrees
		float frequency = 0.25F; // adjust this for the speed of the flap

		// Calculate the instantaneous rotation in degrees
		float rotationDegrees = amplitude * Mth.sin(ageInTicks * frequency);

		// Convert to radians for use in PoseStack.rotate
		float rotationRadians = (float)Math.toRadians(rotationDegrees);

		ModelPart rightWing = getChestModel().getRightWing();
		ModelPart leftWing = getChestModel().getLeftWing();

		rightWing.yRot = rotationRadians;
		leftWing.yRot = -rotationRadians;

	}

	public InfernalChestModel getChestModel() {
		return (InfernalChestModel) getModel();
	}
}
