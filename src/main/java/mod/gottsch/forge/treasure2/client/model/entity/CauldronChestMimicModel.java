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
import mod.gottsch.forge.treasure2.core.entity.monster.CauldronChestMimic;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

/**
 * 
 * @author Mark Gottschling on Jun 8, 2023
 *
 * @param <T>
 */
public class CauldronChestMimicModel<T extends Entity> extends EntityModel<T> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Treasure.MODID, "cauldron_chest_mimic"), "main");
	
	private final ModelPart body;
	private final ModelPart lid;
	private final ModelPart base;
	private final ModelPart tongue;
	private final ModelPart eye1;
	private final ModelPart eye2;
	private final ModelPart eye3;
	private final ModelPart legs;
	private final ModelPart frontLeg1;
	private final ModelPart frontLeg2;
	private final ModelPart backLeg1;
	private final ModelPart backLeg2;
	private final ModelPart frontSwing1;
	private final ModelPart frontSwing2;
	private final ModelPart backSwing1;
	private final ModelPart backSwing2;
	
	private final ModelPart rightLid;
	private final ModelPart leftLid;
	private final ModelPart spike1;
	private final ModelPart spike2;
	private final ModelPart spike3;
	
	/**
	 * 
	 * @param root
	 */
	public CauldronChestMimicModel(ModelPart root) {
		this.body = root.getChild("body");
		this.lid = body.getChild("lid");
		this.base = body.getChild("base");
		this.legs = root.getChild("legs");
		
		this.tongue = base.getChild("tongue");
		
		this.eye1 = lid.getChild("eye1");
		this.eye2 = lid.getChild("eye2");
		this.eye3 = lid.getChild("eye3");
		this.rightLid = lid.getChild("rightLid");
		this.leftLid = lid.getChild("leftLid");
		this.spike1 = lid.getChild("spike1");
		this.spike2 = lid.getChild("spike2");
		this.spike3 = lid.getChild("spike3");
		
		this.frontLeg1 = legs.getChild("frontLeg1");
		this.frontLeg2 = legs.getChild("frontLeg2");
		this.backLeg1 = legs.getChild("backLeg1");
		this.backLeg2 = legs.getChild("backLeg2");
		
		this.frontSwing1 = frontLeg1.getChild("frontSwing1");
		this.frontSwing2 = frontLeg2.getChild("frontSwing2");
		this.backSwing1 = backLeg1.getChild("backSwing1");
		this.backSwing2 = backLeg2.getChild("backSwing2");
	}

	/**
	 * 
	 * @return
	 */
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -8.0F));
		PartDefinition base = body.addOrReplaceChild("base", CubeListBuilder.create().texOffs(42, 51).addBox(6.0F, -4.0F, 0.0F, 2.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(21, 46).addBox(-8.0F, -4.0F, 0.0F, 2.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 20).addBox(-6.0F, -3.0F, 2.0F, 12.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(63, 60).addBox(-6.0F, -4.0F, 14.0F, 12.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(42, 72).addBox(-6.0F, -4.0F, 0.0F, 12.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 0.0F));

		PartDefinition bottomTooth5 = base.addOrReplaceChild("bottomTooth5", CubeListBuilder.create(), PartPose.offsetAndRotation(4.5F, -7.0F, 2.5F, -0.2618F, 0.0F, 0.0F));
		PartDefinition t_r1 = bottomTooth5.addOrReplaceChild("t_r1", CubeListBuilder.create().texOffs(37, 26).addBox(-8.5F, 5.0F, 0.5F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.5F, 3.0F, -0.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition bottomTooth6 = base.addOrReplaceChild("bottomTooth6", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.5F, -7.0F, 2.5F, -0.2618F, 0.0F, 0.0F));
		PartDefinition t_r2 = bottomTooth6.addOrReplaceChild("t_r2", CubeListBuilder.create().texOffs(37, 26).addBox(-8.5F, 5.0F, 0.5F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.5F, 3.0F, -0.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition bottomTooth1 = base.addOrReplaceChild("bottomTooth1", CubeListBuilder.create().texOffs(50, 0).addBox(4.0F, 1.5F, -3.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -10.0F, 3.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition bottomTooth2 = base.addOrReplaceChild("bottomTooth2", CubeListBuilder.create().texOffs(50, 0).addBox(4.0F, 1.5F, -3.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, 3.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition bottomTooth3 = base.addOrReplaceChild("bottomTooth3", CubeListBuilder.create().texOffs(50, 0).addBox(4.0F, 1.5F, -3.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -10.0F, 3.5F, 0.0F, 0.0F, 0.7854F));

		PartDefinition bottomTooth4 = base.addOrReplaceChild("bottomTooth4", CubeListBuilder.create().texOffs(50, 0).addBox(4.0F, 1.5F, -3.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -10.0F, 3.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition skin1 = base.addOrReplaceChild("skin1", CubeListBuilder.create().texOffs(0, 36).addBox(1.0F, -5.0F, -4.0F, 0.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -3.0F, 11.5F, -0.5236F, 0.0F, 0.0F));
		PartDefinition skin2 = base.addOrReplaceChild("skin2", CubeListBuilder.create().texOffs(0, 36).addBox(0.0F, -5.0F, -4.0F, 0.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -3.0F, 11.5F, -0.5236F, 0.0F, 0.0F));
		PartDefinition tongue = base.addOrReplaceChild("tongue", CubeListBuilder.create().texOffs(21, 71).addBox(-3.0F, 4.5F, -11.0F, 6.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 15.0F, -0.1745F, 0.0F, 0.0F));
		PartDefinition lid = body.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -7.6F, -14.0F, 12.0F, 7.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(63, 48).addBox(-6.0F, -8.5F, -16.0F, 12.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 67).addBox(-6.0F, -8.5F, -2.0F, 12.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 36).addBox(6.0F, -8.5F, -16.0F, 2.0F, 9.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(33, 20).addBox(-8.0F, -8.5F, -16.0F, 2.0F, 9.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.5F, 16.0F));

		PartDefinition leftLid = lid.addOrReplaceChild("leftLid", CubeListBuilder.create().texOffs(54, 14).addBox(-6.0F, -0.2F, 0.0F, 6.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, -7.5F, -14.0F));
		PartDefinition rightLid = lid.addOrReplaceChild("rightLid", CubeListBuilder.create().texOffs(49, 0).addBox(0.0F, -0.2F, 0.0F, 6.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -7.5F, -14.0F));
		PartDefinition topTooth1 = lid.addOrReplaceChild("topTooth1", CubeListBuilder.create(), PartPose.offset(-3.5F, -0.5F, -14.5F));
		PartDefinition t_r3 = topTooth1.addOrReplaceChild("t_r3", CubeListBuilder.create().texOffs(37, 0).addBox(-3.0F, 2.5F, 0.6F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition topTooth2 = lid.addOrReplaceChild("topTooth2", CubeListBuilder.create(), PartPose.offset(-0.5F, -0.5F, -14.5F));
		PartDefinition t_r4 = topTooth2.addOrReplaceChild("t_r4", CubeListBuilder.create().texOffs(37, 0).addBox(-3.0F, 2.5F, 0.6F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition topTooth3 = lid.addOrReplaceChild("topTooth3", CubeListBuilder.create(), PartPose.offset(2.5F, -0.5F, -14.5F));
		PartDefinition t_r5 = topTooth3.addOrReplaceChild("t_r5", CubeListBuilder.create().texOffs(37, 0).addBox(-3.0F, 2.5F, 0.6F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition topTooth4 = lid.addOrReplaceChild("topTooth4", CubeListBuilder.create(), PartPose.offset(5.5F, -0.5F, -14.5F));
		PartDefinition t_r6 = topTooth4.addOrReplaceChild("t_r6", CubeListBuilder.create().texOffs(37, 0).addBox(-3.0F, 2.5F, 0.6F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F));
		PartDefinition eye1 = lid.addOrReplaceChild("eye1", CubeListBuilder.create().texOffs(21, 36).addBox(-1.5F, 0.0F, -2.1F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, -4.5F, -14.0F));
		PartDefinition eye2 = lid.addOrReplaceChild("eye2", CubeListBuilder.create().texOffs(0, 25).addBox(-1.5F, 0.0F, -2.1F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, -4.5F, -14.0F));
		PartDefinition eye3 = lid.addOrReplaceChild("eye3", CubeListBuilder.create().texOffs(0, 20).addBox(-1.5F, 0.0F, -2.1F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -6.5F, -14.0F));
		PartDefinition spike1 = lid.addOrReplaceChild("spike1", CubeListBuilder.create().texOffs(58, 34).addBox(-1.0F, -0.1F, 0.0F, 2.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.5F, -14.0F));
		PartDefinition spike2 = lid.addOrReplaceChild("spike2", CubeListBuilder.create().texOffs(37, 0).addBox(-1.0F, -0.1F, 0.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.5F, -10.0F));
		PartDefinition spike3 = lid.addOrReplaceChild("spike3", CubeListBuilder.create().texOffs(21, 42).addBox(-1.0F, -0.1F, 0.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.5F, -6.0F));
		PartDefinition legs = partdefinition.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -8.0F));
		PartDefinition frontLeg1 = legs.addOrReplaceChild("frontLeg1", CubeListBuilder.create(), PartPose.offset(6.0F, 0.0F, 0.0F));
		PartDefinition frontSwing1 = frontLeg1.addOrReplaceChild("frontSwing1", CubeListBuilder.create().texOffs(37, 20).addBox(0.0F, 0.0F, 0.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(54, 28).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 2.0F));
		PartDefinition frontLeg2 = legs.addOrReplaceChild("frontLeg2", CubeListBuilder.create(), PartPose.offset(-6.0F, 0.0F, 0.0F));
		PartDefinition frontSwing2 = frontLeg2.addOrReplaceChild("frontSwing2", CubeListBuilder.create().texOffs(7, 36).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(49, 14).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 2.0F));
		PartDefinition backLeg1 = legs.addOrReplaceChild("backLeg1", CubeListBuilder.create(), PartPose.offset(6.4F, 0.0F, 11.9F));
		PartDefinition backSwing1 = backLeg1.addOrReplaceChild("backSwing1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.1F, 0.0F, -2.1F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(42, 46).addBox(-2.1F, 0.0F, -0.1F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.4F, -6.0F, 2.1F));
		PartDefinition backLeg2 = legs.addOrReplaceChild("backLeg2", CubeListBuilder.create(), PartPose.offset(-6.0F, 0.0F, 12.0F));
		PartDefinition backSwing2 = backLeg2.addOrReplaceChild("backSwing2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.9F, 0.0F, -2.1F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(42, 46).addBox(-1.9F, 0.0F, -0.1F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 2.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	/**
	 * 
	 */
	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		CauldronChestMimic mimic = (CauldronChestMimic)entity;
		if (mimic.isActive()) {
			body.xRot = 0.2618F; // 15 degrees
			frontLeg1.xRot = body.xRot;
			frontLeg2.xRot = body.xRot;
			backLeg1.xRot = body.xRot;
			backLeg2.xRot = body.xRot;
			
			// chomp lid
			if (mimic.hasTarget()) {
				lid.xRot = mimic.getAmount() * -0.7854F; // TODO could use sin or cos method like legs
			}
			else {
//				lid.xRot = -degToRad(22.5f);
				bobMouth(lid, 22.5f, 25f, ageInTicks);
			}
			rightLid.zRot = -2.26893F; //130
			leftLid.zRot = -rightLid.zRot;
			spike1.xRot =  1.13446F; // 65
			spike2.xRot =  0.959931F; //
			spike3.xRot = 0.7854F; // 45
			
			eye1.xRot = -1.003564F;
			eye2.xRot = eye1.xRot;
			eye3.xRot = eye1.xRot;
			tongue.xRot = -0.174533F; // 10
			
			// swing legs
			frontSwing1.xRot = Mth.cos(limbSwing * 0.349066f * 1f) * 1.4F * limbSwingAmount;
			backSwing1.xRot = Mth.cos(limbSwing * 0.349066f  * 1f + (float)Math.PI) * 1.4F * limbSwingAmount;
			
			frontSwing2.xRot = backSwing1.xRot;
			backSwing2.xRot = frontSwing1.xRot;
			
			// bob spikes
			bob(spike1, ageInTicks, 0.26f, -1);
			bob(spike2, ageInTicks, 0.26f, -1);
			bob(spike3, ageInTicks, 0.26f, -1);
			
		} else {
			if (mimic.getAmount() < 1F) {
				body.xRot = mimic.getAmount() * 0.2618F;
				frontLeg1.xRot = body.xRot;
				frontLeg2.xRot = body.xRot;
				backLeg1.xRot = body.xRot;
				backLeg2.xRot = body.xRot;				
				
				lid.xRot = mimic.getAmount() * -0.7854F;
				rightLid.zRot = mimic.getAmount() * -2.26893F; //130
				leftLid.zRot = -rightLid.zRot;
				spike1.xRot =  mimic.getAmount() * 1.13446F; // 65
				spike2.xRot =  mimic.getAmount() * 0.959931F; //
				spike3.xRot = mimic.getAmount() * 0.7854F; // 45
				
				eye1.xRot = mimic.getAmount() * -1.003564F;
				eye2.xRot = eye1.xRot;
				eye3.xRot = eye1.xRot;
				tongue.xRot = mimic.getAmount() * -0.174533F;
			}
		}
	}

	public static void bob(ModelPart part, float age, float radians, int direction) {
		part.xRot += direction * (Mth.sin(age * 0.05F) * radians + 0.05F);
	}
	
	public static void bobMouth(ModelPart mouth, float originRot, float maxRot, float age) {
		mouth.xRot = -(degToRad(originRot + Mth.cos(age * 0.25f) * 3f));
	}
	
	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		legs.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	protected static float degToRad(float degrees) {
		return degrees * (float)Math.PI / 180 ;
	}
}