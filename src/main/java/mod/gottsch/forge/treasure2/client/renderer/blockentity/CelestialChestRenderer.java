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
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.client.model.blockentity.CelestialChestModel;
import mod.gottsch.forge.treasure2.client.model.blockentity.ITreasureChestModel;
import mod.gottsch.forge.treasure2.client.renderer.entity.layer.BarrelMimicLayer;
import mod.gottsch.forge.treasure2.core.block.StandardChestBlock;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3d;

// NOTE @OnlyIn extremely important! add to all Renderers
@OnlyIn(Dist.CLIENT)
public class CelestialChestRenderer extends AbstractChestBlockEntityRenderer {
	public static final ResourceLocation CELESTIAL_CHEST_RENDERER_ATLAS_TEXTURE = new ResourceLocation(Treasure.MODID, "entity/chest/celestial_chest");

	public CelestialChestRenderer(BlockEntityRendererProvider.Context context) {
		setModel(new CelestialChestModel(context.bakeLayer(CelestialChestModel.LAYER_LOCATION)));
		setMaterial(new Material(TextureAtlas.LOCATION_BLOCKS, CELESTIAL_CHEST_RENDERER_ATLAS_TEXTURE));
	}

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

	//	@Override
//	public void updateScale(PoseStack poseStack) {
//		poseStack.scale(0.75F, 0.75F, 0.75F);
//
//		// 3. (Optional but recommended) Re-center the model after scaling
//		// Scaling often shifts the model's position relative to the block center.
//		// For a scale of 0.5f, you need to translate by 0.25f (which is 0.5f / 2)
//		// to re-center the model visually over the block.
////		float offset = (1.0f - 0.75f) / 2.0f;
////		poseStack.translate(offset, offset, offset);
//	}
//
//	public void updateTranslation(PoseStack poseStack) {
//		// The model is defined centred on [0,0,0], so if we drew it at the current render origin, its centre would be
//		// at the corner of the block, sunk halfway into the ground and overlapping into the adjacent blocks.
//		// We want it to hover above the centre of the hopper base, so we need to translate up and across to the desired position
//		final Vector3d TRANSLATION_OFFSET = new Vector3d(0.5, 1.20, 0.4);
//		poseStack.translate(TRANSLATION_OFFSET.x, TRANSLATION_OFFSET.y, TRANSLATION_OFFSET.z);
//	}
//
//	@Override
//	public void render(AbstractTreasureChestBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
//
//		if (!(blockEntity instanceof AbstractTreasureChestBlockEntity)) {
//			return; // should never happen
//		}
//
//		Level world = blockEntity.getLevel();
//		boolean hasWorld = (world != null);
//		BlockState state = blockEntity.getBlockState();
//		Direction facing = Direction.NORTH;
//		if (hasWorld) {
//			facing = state.getValue(StandardChestBlock.FACING);
//		}
//
//		// push the current transformation matrix + normals matrix
//		poseStack.pushPose();
//
//		// initial position (centered moved up)
//		updateTranslation(poseStack);
//
//		// setup scale
//		// scaling by -1 flips the model horizontally and vertically
//		poseStack.scale(-1, -1, 1);
//
//		// adjust the scale of the model
//		updateScale(poseStack);
//
//		updateRotation(poseStack, facing);
//		// float f = getHorizontalAngle(facing);
//		// PoseStack.mulPose(Vector3f.YP.rotationDegrees(-f));
//
//		// update the lid rotation
//		getModel().updateModelLidRotation(blockEntity, partialTicks);
//
//		VertexConsumer renderBuffer = getMaterial().buffer(bufferSource, getModel().getRenderType()); //RenderType::entitySolid);
//
//
//		// test just rendering the wings
//		getChestModel().getRightWing().render(
//				poseStack,
//				renderBuffer,
//				LightTexture.FULL_BRIGHT,
//				combinedOverlay
//		);
//
//		getChestModel().getLeftWing().render(
//				poseStack,
//				renderBuffer,
//				LightTexture.FULL_BRIGHT,
//				combinedOverlay
//		);
//
//		getChestModel().getLid().render(
//				poseStack,
//				renderBuffer,
//				combinedLight,
//				combinedOverlay
//		);
//
//		getChestModel().getTrunk().render(
//				poseStack,
//				renderBuffer,
//				combinedLight,
//				combinedOverlay
//		);
////		getModel().renderToBuffer(poseStack, renderBuffer, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F, blockEntity);
////
//
//		poseStack.popPose();
//
//		//	////////////// render the locks //////////////////////////////////////
//		renderLocks(blockEntity, poseStack, bufferSource, combinedLight, combinedOverlay);
//		//	////////////// end of render the locks //////////////////////////////////////
//	}

//	@Override
//	public void render(AbstractTreasureChestBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
//		VertexConsumer consumer = getMaterial().buffer(bufferSource, getModel().getRenderType());
//
//		// 1. Render the normal parts (head, body) using the parent's rotation (and normal light)
//		// The model's new method handles applying the 'bird' rotation for these parts.
//		poseStack.pushPose();
//				// initial position (centered moved up)
//		updateTranslation(poseStack);
//
//		// setup scale
//		// scaling by -1 flips the model horizontally and vertically
//		poseStack.scale(-1, -1, 1);
//
//		// adjust the scale of the model
//		updateScale(poseStack);
//
//		// update the lid rotation
//		getModel().updateModelLidRotation(blockEntity, partialTicks);
//
//		// TODO all the Abstract rendering should go here or in normal parts?
//		this.getChestModel().renderNormalParts(poseStack, consumer, combinedLight, combinedOverlay);
////		poseStack.popPose();
//
//		// 2. Render the wings (glowing part) with full bright light
////		poseStack.pushPose();
//		ModelPart rightWing = this.getChestModel().getRightWing();
//		ModelPart leftWing = this.getChestModel().getLeftWing();
//
//		// Apply the wings part's full transformation (including all parents up to the root)
//		rightWing.translateAndRotate(poseStack);
//		leftWing.translateAndRotate(poseStack);
//
//		// Render the wings' geometry using the full bright value
//		rightWing.render(
//				poseStack,
//				consumer,
//				LightTexture.FULL_BRIGHT,
//				combinedOverlay
//		);
//		poseStack.popPose();
//	}

	public CelestialChestModel getChestModel() {
		return (CelestialChestModel) getModel();
	}
}
