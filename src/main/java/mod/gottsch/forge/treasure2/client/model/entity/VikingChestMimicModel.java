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
import mod.gottsch.forge.treasure2.core.entity.monster.VikingChestMimic;
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
 * @author Mark Gottschling on Jun 8, 2023
 *
 * @param <T>
 */
public class VikingChestMimicModel<T extends Entity> extends EntityModel<T> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Treasure.MODID, "viking_chest_mimic"), "main");

	private final ModelPart body;
	private final ModelPart lid;
	private final ModelPart base;
	private final ModelPart tongue;
	private final ModelPart eye1;
	private final ModelPart topLatch;
	private final ModelPart frontLatch;

	private float bodyY;

	/**
	 * 
	 * @param root
	 */
	public VikingChestMimicModel(ModelPart root) {
		this.body = root.getChild("body");
		this.lid = body.getChild("lid");
		this.base = body.getChild("base");
		this.eye1 = lid.getChild("eye1");
		this.tongue = base.getChild("tongue");
		this.topLatch = lid.getChild("topLatch");
		this.frontLatch = body.getChild("topLatchFront");
		bodyY = body.y;
	}

	/**
	 * 
	 * @return
	 */
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -5.0F));
		PartDefinition base = body.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-15.0F, -13.0F, 3.0F, 14.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(22, 42).addBox(-10.0F, -12.0F, 2.5F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 35).addBox(-16.0F, -5.5F, 6.0F, 16.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(39, 11).addBox(-15.0F, -3.0F, 3.0F, 2.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(39, 11).addBox(-3.0F, -3.0F, 3.0F, 2.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(11, 42).addBox(-1.0F, -11.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 42).addBox(-16.0F, -11.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, -3.0F));

		PartDefinition tongue = base.addOrReplaceChild("tongue", CubeListBuilder.create().texOffs(33, 35).addBox(-3.0F, -0.6F, -9.0F, 6.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, -12.5F, 13.0F));
		PartDefinition front = base.addOrReplaceChild("front", CubeListBuilder.create().texOffs(0, 21).addBox(-2.5F, -2.0F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 21).addBox(-2.5F, -8.0F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 21).addBox(-14.5F, -8.0F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 21).addBox(-14.5F, -2.0F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition side1 = base.addOrReplaceChild("side1", CubeListBuilder.create().texOffs(0, 21).addBox(-1.5F, -5.0F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 21).addBox(-15.5F, -11.0F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 21).addBox(-1.5F, -5.0F, 11.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 21).addBox(-1.5F, -11.0F, 11.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition side2 = base.addOrReplaceChild("side2", CubeListBuilder.create().texOffs(0, 21).addBox(-15.5F, -5.0F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 21).addBox(-1.5F, -11.0F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 21).addBox(-15.5F, -5.0F, 11.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 21).addBox(-15.5F, -11.0F, 11.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition back = base.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, 21).addBox(-2.5F, -2.0F, 12.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 21).addBox(-2.5F, -8.0F, 12.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 21).addBox(-14.5F, -8.0F, 12.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 21).addBox(-14.5F, -2.0F, 12.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bottomTooth1 = base.addOrReplaceChild("bottomTooth1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.9393F, -1.7678F, 0.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -13.0F, 3.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition bottomTooth2 = base.addOrReplaceChild("bottomTooth2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.9393F, -1.7678F, 0.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, -13.0F, 3.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition bottomTooth3 = base.addOrReplaceChild("bottomTooth3", CubeListBuilder.create(), PartPose.offsetAndRotation(-14.182F, -12.2322F, 5.5F, 0.0F, -1.5708F, 0.0F));
		PartDefinition t_r1 = bottomTooth3.addOrReplaceChild("t_r1", CubeListBuilder.create().texOffs(0, 5).addBox(-3.0F, 2.0F, 0.6F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.182F, -1.7678F, -0.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition bottomTooth4 = base.addOrReplaceChild("bottomTooth4", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.182F, -12.2322F, 5.5F, 0.0F, -1.5708F, 0.0F));
		PartDefinition t_r2 = bottomTooth4.addOrReplaceChild("t_r2", CubeListBuilder.create().texOffs(0, 5).addBox(-3.0F, 2.0F, -0.1F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.182F, -1.7678F, -0.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition topLatchFront = body.addOrReplaceChild("topLatchFront", CubeListBuilder.create().texOffs(1, 49).addBox(-1.0F, -4.0F, -0.25F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.5F, -0.5F));
		PartDefinition lid = body.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 21).addBox(-7.0F, -3.0F, -10.0F, 14.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(0, 21).addBox(5.5F, -2.0F, -10.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 21).addBox(-6.5F, -2.0F, -10.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 21).addBox(5.5F, -2.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 21).addBox(-6.5F, -2.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 10.0F));

		PartDefinition topLatch = lid.addOrReplaceChild("topLatch", CubeListBuilder.create().texOffs(39, 0).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -2.75F));
		PartDefinition topTooth6 = lid.addOrReplaceChild("topTooth6", CubeListBuilder.create(), PartPose.offsetAndRotation(6.818F, -0.2322F, -7.5F, 0.0F, -1.5708F, 0.0F));
		PartDefinition t_r3 = topTooth6.addOrReplaceChild("t_r3", CubeListBuilder.create().texOffs(0, 5).addBox(-2.5F, 2.0F, 1.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.182F, -1.7678F, -0.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition topTooth7 = lid.addOrReplaceChild("topTooth7", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.182F, -0.2322F, -7.5F, 0.0F, -1.5708F, 0.0F));
		PartDefinition t_r4 = topTooth7.addOrReplaceChild("t_r4", CubeListBuilder.create().texOffs(0, 5).addBox(-2.5F, 2.0F, 0.5F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.182F, -1.7678F, -0.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition topTooth1 = lid.addOrReplaceChild("topTooth1", CubeListBuilder.create(), PartPose.offset(-2.5F, -6.0F, -11.5F));
		PartDefinition t_r5 = topTooth1.addOrReplaceChild("t_r5", CubeListBuilder.create().texOffs(0, 5).addBox(0.0F, 6.5F, 2.6F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition topTooth2 = lid.addOrReplaceChild("topTooth2", CubeListBuilder.create(), PartPose.offset(0.5F, -6.0F, -11.5F));
		PartDefinition t_r6 = topTooth2.addOrReplaceChild("t_r6", CubeListBuilder.create().texOffs(0, 5).addBox(0.0F, 6.5F, 2.6F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition topTooth4 = lid.addOrReplaceChild("topTooth4", CubeListBuilder.create(), PartPose.offset(3.5F, -6.0F, -11.5F));
		PartDefinition t_r7 = topTooth4.addOrReplaceChild("t_r7", CubeListBuilder.create().texOffs(0, 5).addBox(0.0F, 6.5F, 2.6F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition topTooth5 = lid.addOrReplaceChild("topTooth5", CubeListBuilder.create(), PartPose.offset(6.5F, -6.0F, -11.5F));
		PartDefinition t_r8 = topTooth5.addOrReplaceChild("t_r8", CubeListBuilder.create().texOffs(0, 5).addBox(0.0F, 6.5F, 2.6F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition topTooth3 = lid.addOrReplaceChild("topTooth3", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -4.0F, -1.5F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 1.0F, -8.0F, 0.0F, 0.0F, 0.7854F));
		PartDefinition eye1 = lid.addOrReplaceChild("eye1", CubeListBuilder.create().texOffs(39, 25).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -8.1F));
		PartDefinition skin1 = lid.addOrReplaceChild("skin1", CubeListBuilder.create().texOffs(5, 49).addBox(0.0F, -3.0F, -3.0F, 0.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 1.0F, -3.0F, 0.5236F, 0.0F, 0.0F));
		PartDefinition skin2 = lid.addOrReplaceChild("skin2", CubeListBuilder.create().texOffs(5, 49).addBox(0.0F, -3.0F, -3.0F, 0.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 1.0F, -3.0F, 0.5236F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	/**
	 * 
	 */
	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		VikingChestMimic mimic = (VikingChestMimic)entity;
		if (mimic.isActive()) {
			body.xRot = 0.2618F; // 15 degrees

			// chomp lid
			if (mimic.hasTarget()) {
				lid.xRot = -degToRad(mimic.getAmount() * 45);
			} else {
//				lid.xRot = -degToRad(22.5f);
				bobMouth(lid, 22.5f, 25f, ageInTicks);
			}
			topLatch.xRot = -0.2618F;
			eye1.xRot = -1.003564F;
			tongue.xRot = -0.174533F; // 10
			frontLatch.xRot = 3.14159F;

			bob(body, bodyY, ageInTicks);
		} else {
			if (mimic.getAmount() < 1F) {
				body.xRot = mimic.getAmount() * 0.2618F;
				lid.xRot = mimic.getAmount() * -0.7854F;
				topLatch.xRot = mimic.getAmount() * -0.2618F;
				frontLatch.xRot = mimic.getAmount() * 3.14159F;
				eye1.xRot = mimic.getAmount() * -1.003564F;
				tongue.xRot = mimic.getAmount() * -0.174533F;
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