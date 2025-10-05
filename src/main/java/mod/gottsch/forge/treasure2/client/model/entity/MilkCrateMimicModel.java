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
package mod.gottsch.forge.treasure2.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.entity.monster.MilkCrateMimic;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

/**
 *
 * @author Mark Gottschling on Apr 23, 2024
 *
 * @param <T>
 */
public class MilkCrateMimicModel<T extends Entity> extends MimicModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Treasure.MODID, "milk_crate_mimic"), "main");

	private final ModelPart body;
	private final ModelPart lid;
	private final ModelPart base;
	private final ModelPart tongue;
	private final ModelPart eye;

	private float bodyY;
	private float lidXRot;

	public MilkCrateMimicModel(ModelPart root) {
		this.body = root.getChild("body");
		this.lid = body.getChild("lid");
		this.base = body.getChild("main");
		this.eye = lid.getChild("eye");

		this.tongue = base.getChild("tongue");

		bodyY = body.y;
		lidXRot = lid.xRot;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -7.0F));

		PartDefinition skin = body.addOrReplaceChild("skin", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.5F, -2.0F, 0.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(6.5F, -9.5F, 10.0F));

		PartDefinition skin2 = body.addOrReplaceChild("skin2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.5F, -2.0F, 0.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.5F, -9.5F, 10.0F));

		PartDefinition lid = body.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -2.0F, -14.0F, 14.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 31).addBox(-6.0F, 2.0F, -13.0F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, 14.0F));

		PartDefinition northFace = lid.addOrReplaceChild("northFace", CubeListBuilder.create().texOffs(30, 48).addBox(-7.0F, -1.0F, -14.0F, 14.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 31).addBox(5.1F, -1.0F, -14.1F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 31).addBox(-7.1F, -1.0F, -14.1F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition lidLeft = lid.addOrReplaceChild("lidLeft", CubeListBuilder.create().texOffs(37, 32).addBox(6.0F, 0.0F, -13.0F, 1.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lidRight = lid.addOrReplaceChild("lidRight", CubeListBuilder.create().texOffs(37, 32).addBox(-7.0F, 0.0F, -13.0F, 1.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition eye = lid.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(57, 10).addBox(-2.0F, -0.01F, -1.2F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(37, 24).addBox(-1.0F, 1.0F, -2.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -13.0F));

		PartDefinition topTooth6 = lid.addOrReplaceChild("topTooth6", CubeListBuilder.create(), PartPose.offset(5.5F, 7.0F, -10.6F));

		PartDefinition cube_r1 = topTooth6.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(37, 17).addBox(-5.0F, 1.0F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -6.0F, -1.9F, 0.7854F, 0.0F, 0.0F));

		PartDefinition topTooth7 = lid.addOrReplaceChild("topTooth7", CubeListBuilder.create(), PartPose.offset(-7.5F, 7.0F, -10.6F));

		PartDefinition cube_r2 = topTooth7.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(37, 17).addBox(-5.0F, 1.0F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -6.0F, -1.9F, 0.7854F, 0.0F, 0.0F));

		PartDefinition topTooth5 = lid.addOrReplaceChild("topTooth5", CubeListBuilder.create(), PartPose.offset(5.5F, 7.0F, -6.6F));

		PartDefinition cube_r3 = topTooth5.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(37, 17).addBox(-5.0F, 1.0F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -6.0F, -1.9F, 0.7854F, 0.0F, 0.0F));

		PartDefinition topTooth8 = lid.addOrReplaceChild("topTooth8", CubeListBuilder.create(), PartPose.offset(-7.5F, 7.0F, -6.6F));

		PartDefinition cube_r4 = topTooth8.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(37, 17).addBox(-5.0F, 1.0F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -6.0F, -1.9F, 0.7854F, 0.0F, 0.0F));

		PartDefinition topRpwTeeth = lid.addOrReplaceChild("topRpwTeeth", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.5F, -13.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition topTooth1 = topRpwTeeth.addOrReplaceChild("topTooth1", CubeListBuilder.create(), PartPose.offset(-3.0F, -1.5F, -0.5F));

		PartDefinition t_r1 = topTooth1.addOrReplaceChild("t_r1", CubeListBuilder.create().texOffs(7, 0).addBox(-2.5F, 3.5F, 0.6F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F));

		PartDefinition topTooth2 = topRpwTeeth.addOrReplaceChild("topTooth2", CubeListBuilder.create(), PartPose.offset(0.25F, -1.5F, -0.5F));

		PartDefinition t_r2 = topTooth2.addOrReplaceChild("t_r2", CubeListBuilder.create().texOffs(7, 0).addBox(-2.5F, 3.5F, 0.5F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F));

		PartDefinition topTooth3 = topRpwTeeth.addOrReplaceChild("topTooth3", CubeListBuilder.create(), PartPose.offset(3.5F, -1.5F, -0.5F));

		PartDefinition t_r3 = topTooth3.addOrReplaceChild("t_r3", CubeListBuilder.create().texOffs(7, 0).addBox(-2.5F, 3.5F, 0.6F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F));

		PartDefinition topTooth4 = topRpwTeeth.addOrReplaceChild("topTooth4", CubeListBuilder.create(), PartPose.offset(6.5F, -1.5F, -0.5F));

		PartDefinition t_r4 = topTooth4.addOrReplaceChild("t_r4", CubeListBuilder.create().texOffs(7, 0).addBox(-2.5F, 3.5F, 0.5F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F));

		PartDefinition main = body.addOrReplaceChild("main", CubeListBuilder.create().texOffs(37, 19).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 7.0F));

		PartDefinition base = main.addOrReplaceChild("base", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition baseNorth = base.addOrReplaceChild("baseNorth", CubeListBuilder.create().texOffs(30, 53).addBox(-7.0F, -3.0F, -7.0F, 14.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(52, 32).addBox(-7.0F, -7.0F, -7.0F, 14.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 17).addBox(5.1F, -8.0F, -7.1F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 17).addBox(-7.1F, -8.0F, -7.1F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bottomFace = base.addOrReplaceChild("bottomFace", CubeListBuilder.create().texOffs(0, 17).addBox(-6.0F, -1.0F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition westFace = base.addOrReplaceChild("westFace", CubeListBuilder.create().texOffs(15, 48).addBox(6.0F, 7.0F, -13.0F, 1.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 44).addBox(6.0F, 3.0F, -13.0F, 1.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 7.0F));

		PartDefinition eastFace = base.addOrReplaceChild("eastFace", CubeListBuilder.create().texOffs(15, 48).addBox(-7.0F, 7.0F, -13.0F, 1.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 44).addBox(-7.0F, 3.0F, -13.0F, 1.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 7.0F));

		PartDefinition southFace = base.addOrReplaceChild("southFace", CubeListBuilder.create().texOffs(52, 37).addBox(-7.0F, 7.0F, -1.0F, 14.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(52, 32).addBox(-7.0F, 3.0F, -1.0F, 14.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(30, 48).addBox(-7.0F, -0.99F, -1.0F, 14.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(42, 58).addBox(5.1F, -1.0F, -1.9F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(42, 58).addBox(-7.1F, -1.0F, -1.9F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 7.0F));

		PartDefinition bottomTooth1 = main.addOrReplaceChild("bottomTooth1", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.5F, -7.0F, -6.5F, -0.1745F, 0.0F, 0.0F));
		PartDefinition t_r5 = bottomTooth1.addOrReplaceChild("t_r5", CubeListBuilder.create().texOffs(0, 37).addBox(-8.0F, 5.5F, 0.5F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition bottomTooth2 = main.addOrReplaceChild("bottomTooth2", CubeListBuilder.create(), PartPose.offsetAndRotation(3.5F, -7.0F, -6.5F, -0.1745F, 0.0F, 0.0F));
		PartDefinition t_r6 = bottomTooth2.addOrReplaceChild("t_r6", CubeListBuilder.create().texOffs(0, 37).addBox(-8.0F, 5.5F, 0.5F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition bottomTooth3 = main.addOrReplaceChild("bottomTooth3", CubeListBuilder.create(), PartPose.offsetAndRotation(6.1109F, -9.2322F, -3.4F, 0.0F, -1.5708F, 0.0F));
		PartDefinition t_r7 = bottomTooth3.addOrReplaceChild("t_r7", CubeListBuilder.create().texOffs(7, 0).addBox(-2.5F, 2.0F, 0.5F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.8891F, 1.2322F, -0.6F, 0.0F, 0.0F, 0.7854F));
		PartDefinition bottomTooth5 = main.addOrReplaceChild("bottomTooth5", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.3891F, -9.2322F, -3.4F, 0.0F, -1.5708F, 0.0F));
		PartDefinition t_r8 = bottomTooth5.addOrReplaceChild("t_r8", CubeListBuilder.create().texOffs(7, 0).addBox(-2.5F, 2.0F, 0.5F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.8891F, 1.2322F, -0.6F, 0.0F, 0.0F, 0.7854F));
		PartDefinition bottomTooth4 = main.addOrReplaceChild("bottomTooth4", CubeListBuilder.create(), PartPose.offsetAndRotation(6.1109F, -9.2322F, -1.4F, 0.0F, -1.5708F, 0.0F));
		PartDefinition t_r9 = bottomTooth4.addOrReplaceChild("t_r9", CubeListBuilder.create().texOffs(7, 0).addBox(-2.5F, 2.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.8891F, 1.2322F, -0.6F, 0.0F, 0.0F, 0.7854F));
		PartDefinition bottomTooth6 = main.addOrReplaceChild("bottomTooth6", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.3891F, -9.2322F, -1.4F, 0.0F, -1.5708F, 0.0F));
		PartDefinition t_r10 = bottomTooth6.addOrReplaceChild("t_r10", CubeListBuilder.create().texOffs(7, 0).addBox(-2.5F, 2.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.8891F, 1.2322F, -0.6F, 0.0F, 0.0F, 0.7854F));
		PartDefinition bottomTooth7 = main.addOrReplaceChild("bottomTooth7", CubeListBuilder.create(), PartPose.offset(1.75F, -8.75F, -6.7F));
		PartDefinition t_r11 = bottomTooth7.addOrReplaceChild("t_r11", CubeListBuilder.create().texOffs(7, 0).addBox(-2.5F, 3.5F, 0.5F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition tongue = main.addOrReplaceChild("tongue", CubeListBuilder.create().texOffs(43, 0).addBox(-3.0F, 0.9F, -12.0F, 6.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, 7.0F, -0.1745F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	/**
	 *
	 */
//	@Override
//	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
//		MilkCrateMimic mimic = (MilkCrateMimic)entity;
//		if (mimic.isActive()) {
//			body.xRot = 0.2618F; // 15 degrees
//
//			// chomp lid
//			if (mimic.hasTarget()) {
//				bobMouth(lid, 22.5f, 22.5f, ageInTicks);
//			} else {
//				bobMouth(lid, 22.5f, 3f, ageInTicks);
//			}
//			eye.xRot = -1.003564F;
//			tongue.xRot = -0.174533F; // 10
//
//			bob(body, bodyY, ageInTicks);
//		} else {
//			if (mimic.getAmount() < 1F) {
//				body.xRot = mimic.getAmount() * 0.2618F;
//				lid.xRot = mimic.getAmount() * -0.7854F;
//				eye.xRot = mimic.getAmount() * -1.003564F;
//				tongue.xRot = mimic.getAmount() * -0.174533F;
//			}
//		}
//	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void activeAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.activeAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		eye.xRot = -1.003564F;
		tongue.xRot = -0.174533F; // 10
	}

	@Override
	public void openAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float timeSlice) {
		body.xRot = timeSlice * 0.2618F;
		lid.xRot = timeSlice * getMaxOpenAngle();
		eye.xRot = timeSlice * -1.003564F;
		tongue.xRot = timeSlice * -0.174533F;
	}

	@Override
	public ModelPart getBody() {
		return body;
	}

	@Override
	public float getBodyY() {
		return bodyY;
	}

	@Override
	public ModelPart getLid() {
		return lid;
	}
}