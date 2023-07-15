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

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.SafeBlockEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

/**
 * @author Mark Gottschling on Feb 19, 2018
 *
 */
public class SafeModel extends AbstractTreasureChestModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Treasure.MODID, "safe"), "main");
	private final ModelPart safe;
	private final ModelPart lid;
	private final ModelPart handle;

	public SafeModel(ModelPart root) {
		super(root);
		this.safe = root.getChild("safe");
		this.lid = root.getChild("lid");
		this.handle = lid.getChild("handle");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition safe = partdefinition.addOrReplaceChild("safe", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, 0.0F, -9.0F, 12.0F, 12.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-6.0F, 11.0F, 6.0F));
		PartDefinition post = safe.addOrReplaceChild("post", CubeListBuilder.create().texOffs(43, 0).mirror().addBox(0.0F, 0.0F, 0.0F, 2.0F, 12.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(10.0F, 0.0F, -12.0F));
		PartDefinition foot1 = safe.addOrReplaceChild("foot1", CubeListBuilder.create().texOffs(43, 16).mirror().addBox(0.0F, 0.0F, 3.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(10.0F, 12.0F, -12.0F));
		PartDefinition foot2 = safe.addOrReplaceChild("foot2", CubeListBuilder.create().texOffs(43, 16).mirror().addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(10.0F, 12.0F, -2.0F));
		PartDefinition foot3 = safe.addOrReplaceChild("foot3", CubeListBuilder.create().texOffs(43, 16).mirror().addBox(0.0F, 0.0F, 3.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 12.0F, -12.0F));
		PartDefinition foot4 = safe.addOrReplaceChild("foot4", CubeListBuilder.create().texOffs(43, 16).mirror().addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 12.0F, -2.0F));
		PartDefinition hinge2 = safe.addOrReplaceChild("hinge2", CubeListBuilder.create().texOffs(54, 5).mirror().addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.0F, 8.0F, -9.0F));
		PartDefinition hinge1 = safe.addOrReplaceChild("hinge1", CubeListBuilder.create().texOffs(54, 0).mirror().addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.0F, 1.0F, -9.0F));
		PartDefinition lid = partdefinition.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(0.0F, -6.0F, -3.0F, 10.0F, 12.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-6.0F, 17.0F, -3.0F));
		PartDefinition handle = lid.addOrReplaceChild("handle", CubeListBuilder.create(), PartPose.offset(5.0F, 0.0F, -3.0F));
		PartDefinition handle1 = handle.addOrReplaceChild("handle1", CubeListBuilder.create().texOffs(27, 22).mirror().addBox(-3.5F, -0.5F, -1.0F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition handle2 = handle.addOrReplaceChild("handle2", CubeListBuilder.create().texOffs(27, 25).mirror().addBox(-0.5F, -3.5F, -1.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void updateModelLidRotation(AbstractTreasureChestBlockEntity blockEntity, float partialTicks) {
		SafeBlockEntity be = (SafeBlockEntity) blockEntity;

		if (be.isLidClosed) {
			float handleRotation = be.prevHandleAngle + (be.handleAngle - be.prevHandleAngle) * partialTicks;
			handleRotation = 1.0F - handleRotation;
			handleRotation = 1.0F - handleRotation * handleRotation * handleRotation;
			getHandle().zRot = (handleRotation * (float)Math.PI / getAngleModifier());
		}

		float lidRotation = be.prevLidAngle + (be.lidAngle - be.prevLidAngle) * partialTicks;
		lidRotation = 1.0F - lidRotation;
		lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
		getLid().yRot = (lidRotation * (float)Math.PI / getAngleModifier());
	}
	
	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, AbstractTreasureChestBlockEntity blockEntity) {
		safe.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		lid.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	@Override
	public ModelPart getLid() {
		return lid;
	}
	
	public ModelPart getHandle() {
		return handle;
	}
}