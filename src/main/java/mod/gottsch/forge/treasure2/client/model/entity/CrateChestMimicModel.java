/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.client.model.entity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.entity.monster.CrateChestMimic;
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
 * @author Mark Gottschling on Jun 26, 2023
 *
 * @param <T>
 */
public class CrateChestMimicModel<T extends Entity> extends EntityModel<T> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Treasure.MODID, "crate_chest_mimic"), "main");
	
	private final ModelPart body;
	private final ModelPart lid;
	private final ModelPart base;
	private final ModelPart tongue;
	private final ModelPart eye1;
	private final ModelPart eyeSocket;
	private final ModelPart bigTeeth;


	private float bodyY;

	public CrateChestMimicModel(ModelPart root) {
		this.body = root.getChild("body");
		this.lid = body.getChild("lid");
		this.base = body.getChild("base");
		this.eye1 = lid.getChild("eye1");
		this.eyeSocket = lid.getChild("eyeSocket");
		this.tongue = base.getChild("tongue");
		this.bigTeeth = base.getChild("bigTeeth");
		
		bodyY = body.y;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -7.0F));
		PartDefinition lid = body.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 17).addBox(-7.0F, -2.0F, -14.0F, 14.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.0F, 14.0F));
		PartDefinition eye1 = lid.addOrReplaceChild("eye1", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, -11.0F));
		PartDefinition eyeSocket = lid.addOrReplaceChild("eyeSocket", CubeListBuilder.create().texOffs(67, 31).addBox(-3.0F, -2.9F, -13.0F, 6.0F, 3.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));
		PartDefinition padTop = eye1.addOrReplaceChild("padTop", CubeListBuilder.create().texOffs(43, 34).addBox(4.0F, -0.2F, -13.2F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 0.0F, 10.0F));
		PartDefinition latch1 = eye1.addOrReplaceChild("latch1", CubeListBuilder.create().texOffs(0, 20).addBox(5.0F, 0.0F, -14.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 0.0F, 10.0F));
		PartDefinition skin1 = lid.addOrReplaceChild("skin1", CubeListBuilder.create().texOffs(17, 46).addBox(0.9F, -7.0F, -0.5F, 0.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 6.0F, -4.5F, 0.5236F, 0.0F, 0.0F));
		PartDefinition skin2 = lid.addOrReplaceChild("skin2", CubeListBuilder.create().texOffs(17, 46).addBox(0.1F, -7.0F, -0.5F, 0.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 6.0F, -4.5F, 0.5236F, 0.0F, 0.0F));
		PartDefinition topTooth1 = lid.addOrReplaceChild("topTooth1", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.2426F, -0.1213F, -13.4F, 0.4363F, 0.0F, 0.0F));
		PartDefinition t_r1 = topTooth1.addOrReplaceChild("t_r1", CubeListBuilder.create().texOffs(53, 52).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));
		PartDefinition topTooth2 = lid.addOrReplaceChild("topTooth2", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.2426F, -0.1213F, -13.5F, 0.4363F, 0.0F, 0.0F));
		PartDefinition t_r2 = topTooth2.addOrReplaceChild("t_r2", CubeListBuilder.create().texOffs(53, 52).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));
		PartDefinition topTooth3 = lid.addOrReplaceChild("topTooth3", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.2426F, -0.1213F, -13.6F, 0.4363F, 0.0F, 0.0F));
		PartDefinition t_r3 = topTooth3.addOrReplaceChild("t_r3", CubeListBuilder.create().texOffs(53, 52).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));
		PartDefinition topTooth5 = lid.addOrReplaceChild("topTooth5", CubeListBuilder.create(), PartPose.offsetAndRotation(3.7574F, -0.1213F, -13.4F, 0.4363F, 0.0F, 0.0F));
		PartDefinition t_r4 = topTooth5.addOrReplaceChild("t_r4", CubeListBuilder.create().texOffs(53, 52).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));
		PartDefinition topTooth4 = lid.addOrReplaceChild("topTooth4", CubeListBuilder.create(), PartPose.offsetAndRotation(1.7574F, -0.1213F, -13.5F, 0.4363F, 0.0F, 0.0F));
		PartDefinition t_r5 = topTooth4.addOrReplaceChild("t_r5", CubeListBuilder.create().texOffs(53, 52).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));
		PartDefinition topTooth6 = lid.addOrReplaceChild("topTooth6", CubeListBuilder.create(), PartPose.offsetAndRotation(6.0109F, -0.2929F, -11.3749F, 0.0F, -1.5708F, 0.4363F));
		PartDefinition t_r6 = topTooth6.addOrReplaceChild("t_r6", CubeListBuilder.create().texOffs(53, 52).addBox(-1.318F, -1.7678F, 0.2F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.318F, 0.0607F, -0.2F, 0.0F, 0.0F, 0.7854F));
		PartDefinition topTooth8 = lid.addOrReplaceChild("topTooth8", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.9891F, -0.2929F, -11.3749F, 0.0F, -1.5708F, -0.4363F));
		PartDefinition t_r7 = topTooth8.addOrReplaceChild("t_r7", CubeListBuilder.create().texOffs(53, 52).addBox(-1.318F, -1.7678F, 0.2F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.318F, 0.0607F, -0.2F, 0.0F, 0.0F, 0.7854F));
		PartDefinition topTooth7 = lid.addOrReplaceChild("topTooth7", CubeListBuilder.create(), PartPose.offsetAndRotation(6.534F, -0.3574F, -8.8536F, 0.0F, -1.5708F, 0.4363F));
		PartDefinition t_r8 = topTooth7.addOrReplaceChild("t_r8", CubeListBuilder.create().texOffs(53, 52).addBox(-2.4092F, -0.7678F, 0.277F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.1607F, 0.1251F, -0.277F, 0.0F, 0.0F, 0.7854F));
		PartDefinition topTooth9 = lid.addOrReplaceChild("topTooth9", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.466F, -0.3574F, -8.8536F, 0.0F, -1.5708F, -0.4363F));
		PartDefinition t_r9 = topTooth9.addOrReplaceChild("t_r9", CubeListBuilder.create().texOffs(53, 52).addBox(-2.4092F, -0.7678F, 0.277F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.1607F, 0.1251F, -0.277F, 0.0F, 0.0F, 0.7854F));
		PartDefinition base = body.addOrReplaceChild("base", CubeListBuilder.create().texOffs(45, 5).addBox(-6.0F, -10.0F, -13.0F, 12.0F, 10.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.0F, 0.0F, -14.0F, 14.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-7.0F, -10.0F, -14.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(5.0F, -10.0F, -14.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.0F, -10.0F, -2.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(5.0F, -10.0F, -2.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 34).addBox(-7.0F, -11.0F, -14.0F, 14.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 14.0F));

		PartDefinition crossWest = base.addOrReplaceChild("crossWest", CubeListBuilder.create().texOffs(0, 50).addBox(0.0F, 0.0F, 0.0F, 1.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, -10.5F, -2.5F, -0.8029F, 0.0F, 0.0F));
		PartDefinition crossEast = base.addOrReplaceChild("crossEast", CubeListBuilder.create().texOffs(0, 50).addBox(0.0F, 0.0F, 0.0F, 1.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.5F, -9.5F, -13.5F, 0.8029F, 0.0F, 0.0F));
		PartDefinition crossSouth = base.addOrReplaceChild("crossSouth", CubeListBuilder.create().texOffs(7, 50).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 15.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -10.5F, 0.5F, 0.0F, 0.0F, 0.8029F));
		PartDefinition cross = base.addOrReplaceChild("cross", CubeListBuilder.create().texOffs(7, 50).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 15.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -10.5F, -11.5F, 0.0F, 0.0F, -0.8029F));
		PartDefinition pad = base.addOrReplaceChild("pad", CubeListBuilder.create().texOffs(0, 17).addBox(-2.0F, -0.2F, -1.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, -13.2F));
		PartDefinition bottomTooth1 = pad.addOrReplaceChild("bottomTooth1", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.5F, 3.0F, 0.7F, -0.2618F, 0.0F, 0.0F));
		PartDefinition t_r10 = bottomTooth1.addOrReplaceChild("t_r10", CubeListBuilder.create().texOffs(43, 30).addBox(-8.0F, 5.5F, 0.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition tongue = base.addOrReplaceChild("tongue", CubeListBuilder.create().texOffs(5, 76).addBox(-3.0F, 0.9F, -12.5F, 6.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -11.0F, 1.0F, -0.1745F, 0.0F, 0.0F));
		PartDefinition bigTeeth = base.addOrReplaceChild("bigTeeth", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -10.0F, -12.0F, -0.8727F, 0.0F, 0.0F));
		PartDefinition bottomTooth2 = bigTeeth.addOrReplaceChild("bottomTooth2", CubeListBuilder.create(), PartPose.offset(-3.5F, 2.0F, -1.5F));
		PartDefinition t_r11 = bottomTooth2.addOrReplaceChild("t_r11", CubeListBuilder.create().texOffs(41, 57).addBox(-2.0F, -1.7071F, 0.5F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3076F, -3.2929F, 0.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition bottomTooth5 = bigTeeth.addOrReplaceChild("bottomTooth5", CubeListBuilder.create(), PartPose.offset(2.5F, 2.0F, -0.5F));
		PartDefinition t_r12 = bottomTooth5.addOrReplaceChild("t_r12", CubeListBuilder.create().texOffs(41, 57).addBox(-2.0F, -2.0607F, 0.5F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6612F, -2.9393F, -0.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition bottomTooth4 = base.addOrReplaceChild("bottomTooth4", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.3F, -11.5F, -13.5F, 0.2182F, 0.0F, 0.0F));
		PartDefinition t3_r1 = bottomTooth4.addOrReplaceChild("t3_r1", CubeListBuilder.create().texOffs(40, 52).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3607F, 0.9749F, 0.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition bottomTooth3 = base.addOrReplaceChild("bottomTooth3", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.3F, -11.5F, -13.5F, 0.2182F, 0.0F, 0.0F));
		PartDefinition t4_r1 = bottomTooth3.addOrReplaceChild("t4_r1", CubeListBuilder.create().texOffs(40, 52).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3607F, 0.9749F, 0.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition bottomTooth6 = base.addOrReplaceChild("bottomTooth6", CubeListBuilder.create(), PartPose.offsetAndRotation(3.7F, -11.5F, -13.5F, 0.2182F, 0.0F, 0.0F));
		PartDefinition t5_r1 = bottomTooth6.addOrReplaceChild("t5_r1", CubeListBuilder.create().texOffs(40, 52).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3607F, 0.9749F, 0.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition bottomTooth7 = base.addOrReplaceChild("bottomTooth7", CubeListBuilder.create(), PartPose.offsetAndRotation(6.1109F, -10.909F, -11.0088F, 0.0F, -1.5708F, -0.4363F));
		PartDefinition t_r13 = bottomTooth7.addOrReplaceChild("t_r13", CubeListBuilder.create().texOffs(40, 52).addBox(-1.9948F, -1.2981F, 0.1F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4926F, 0.2071F, -0.1F, 0.0F, 0.0F, 0.7854F));
		PartDefinition bottomTooth9 = base.addOrReplaceChild("bottomTooth9", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.0891F, -10.909F, -11.0088F, 0.0F, -1.5708F, 0.4363F));
		PartDefinition t_r14 = bottomTooth9.addOrReplaceChild("t_r14", CubeListBuilder.create().texOffs(40, 52).addBox(-1.9948F, -1.2981F, 0.3F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4926F, 0.2071F, -0.3F, 0.0F, 0.0F, 0.7854F));
		PartDefinition bottomTooth8 = base.addOrReplaceChild("bottomTooth8", CubeListBuilder.create(), PartPose.offsetAndRotation(6.2839F, -10.779F, -8.8678F, 0.0F, -1.5708F, -0.4363F));
		PartDefinition t_r15 = bottomTooth8.addOrReplaceChild("t_r15", CubeListBuilder.create().texOffs(40, 52).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));
		PartDefinition bottomTooth10 = base.addOrReplaceChild("bottomTooth10", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.2839F, -10.7824F, -8.9729F, 0.0F, -1.5708F, 0.4363F));
		PartDefinition t_r16 = bottomTooth10.addOrReplaceChild("t_r16", CubeListBuilder.create().texOffs(40, 52).addBox(-2.2839F, -0.721F, -0.4322F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.1051F, 0.0035F, 0.4322F, 0.0F, 0.0F, 0.7854F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		CrateChestMimic mimic = (CrateChestMimic)entity;
		if (mimic.isActive()) {
			body.xRot = 0.2618F; // 15 degrees
			
			// chomp lid
			if (mimic.hasTarget()) {
				lid.xRot = -degToRad(mimic.getAmount() * 45);
			} else {
//				lid.xRot = -degToRad(22.5f);
				bobMouth(lid, 22.5f, 25f, ageInTicks);
			}
			eye1.xRot = -1.003564F;
			eyeSocket.xRot = -0.174533F;
			tongue.xRot = -0.174533F; // 10
			bigTeeth.xRot = 0.174533F; // 10
			
			bob(body, bodyY, ageInTicks);
		} else {
			if (mimic.getAmount() < 1F) {
				body.xRot = mimic.getAmount() * 0.2618F;
				lid.xRot = mimic.getAmount() * -0.7854F;
				eye1.xRot = mimic.getAmount() * -1.003564F;
				eyeSocket.xRot = mimic.getAmount() * -0.174533F;
				tongue.xRot = mimic.getAmount() * -0.174533F;
				bigTeeth.xRot = mimic.getAmount() * 0.174533F;
			}
		}
	}

	public static void bob(ModelPart part, float originY, float age) {
		part.y = originY + (Mth.cos(age * 0.25F) * 0.5F + 0.05F);
	}
	
	public static void bobMouth(ModelPart mouth, float originRot, float maxRot, float age) {
		mouth.xRot = -(degToRad(originRot + Mth.cos(age * 0.25f) * 3f));
	}
	
	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	protected static float degToRad(float degrees) {
		return degrees * (float)Math.PI / 180 ;
	}
}