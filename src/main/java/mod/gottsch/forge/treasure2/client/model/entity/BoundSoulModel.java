/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021 Mark Gottschling (gottsch)
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
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

/**
 * 
 * @author Mark Gottschling
 *
 * @param <T>
 */
public class BoundSoulModel<T extends Entity> extends EntityModel<T> implements IHumanlikeModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Treasure.MODID, "bound_soul_model"), "main");
	
	private final ModelPart head;
	private final ModelPart body_legs;
	private final ModelPart body;
	private final ModelPart leftArm;
	private final ModelPart rightArm;
	private final ModelPart leftLeg;
	private final ModelPart rightLeg;

	private float leftArmX;
	private float rightArmX;
	
	/**
	 * 
	 * @param root
	 */
	public BoundSoulModel(ModelPart root) {
		super(RenderType::entityTranslucentCull);
		this.head = root.getChild("head");
		this.body_legs = root.getChild("body_legs");
		this.body = body_legs.getChild("body");
		this.leftArm = root.getChild("left_arm");
		this.rightArm = root.getChild("right_arm");
		this.leftLeg = body_legs.getChild("left_leg");
		this.rightLeg = body_legs.getChild("right_leg");
		
		rightArmX = rightArm.x;
		leftArmX = leftArm.x;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 17).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition body_legs = partdefinition.addOrReplaceChild("body_legs", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));
		PartDefinition right_leg = body_legs.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(33, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 12.0F, 0.0F));
		PartDefinition left_leg = body_legs.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 34).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 12.0F, 0.0F));
		PartDefinition body = body_legs.addOrReplaceChild("body", CubeListBuilder.create().texOffs(29, 30).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(46, 13).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 2.0F, 0.0F, -0.9599F, 0.0F, 0.0F));
		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(13, 47).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 2.0F, 0.0F, -0.8727F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// head
		this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = headPitch * ((float)Math.PI / 180F);
		
		// legs
		float f = 1.0F;
		float radians = 0.3490659F; // 20 degrees
		float walkSpeed = 0.5F; // half speed = 0.5
		this.rightLeg.xRot = Mth.cos(limbSwing * walkSpeed) * radians * 1.4F * limbSwingAmount / f;
		this.leftLeg.xRot = Mth.cos(limbSwing  * walkSpeed + (float)Math.PI) * radians * 1.4F * limbSwingAmount / f;
		
		setupAttackAnimation(entity, ageInTicks);
		
		// reset arm rotations before bobbing, because bobbing is an addition to current rotation
		this.leftArm.zRot = 0F;
		this.leftArm.xRot = -0.8726646F; // 50 degrees

		this.rightArm.zRot = 0F;
		this.rightArm.xRot = -0.9599311F; // 55 dgrees

		// bob the arms
		bobModelPart(this.rightArm, ageInTicks, 1.0F);
		bobModelPart(this.leftArm, ageInTicks, -1.0F);
	}

	/**
	 * 
	 * @param part
	 * @param age
	 * @param direction
	 */
	public static void bobModelPart(ModelPart part, float age, float direction) {
		part.xRot += direction * (Mth.cos(age * /*0.09F*/ 0.15F) * 0.15F + 0.05F);
	}

	public void resetSwing(T entity, ModelPart body, ModelPart rightArm, ModelPart leftArm) {
		body.yRot = 0;
		rightArm.x = rightArmX;
		rightArm.xRot = -0.9599311F;
		rightArm.zRot = 0;
		rightArm.yRot = 0;
		leftArm.x = leftArmX;
		leftArm.xRot = -0.8726646F;
		leftArm.yRot = 0;
		leftArm.zRot = 0;
	}
	
	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, buffer, packedLight, packedOverlay);
//		headwear.render(poseStack, buffer, packedLight, packedOverlay);
//		body.render(poseStack, buffer, packedLight, packedOverlay);
		body_legs.render(poseStack, buffer, packedLight, packedOverlay);
		leftArm.render(poseStack, buffer, packedLight, packedOverlay);
		rightArm.render(poseStack, buffer, packedLight, packedOverlay);
//		left_leg.render(poseStack, buffer, packedLight, packedOverlay);
//		right_leg.render(poseStack, buffer, packedLight, packedOverlay);
	}
	
	/**
	 * 
	 * @return
	 */
	public ModelPart getAttackArm() {
		return getRightArm();
	}
	
	/**
	 * from vanilla
	 * @param entity
	 * @param age
	 */
	protected void setupAttackAnimation(T entity, float age) {
		resetSwing(entity, getBody(), getRightArm(), getLeftArm());
		if (!(this.attackTime <= 0.0F)) {
			ModelPart attackArm = getAttackArm();
			float f = this.attackTime;
			getBody().yRot = Mth.sin(Mth.sqrt(f) * ((float)Math.PI * 2F)) * 0.2F;
			if (attackArm == getLeftArm()) {
				getBody().yRot *= -1.0F;
			}

			getRightArm().z = Mth.sin(getBody().yRot) * 5.0F;
			getRightArm().x = -Mth.cos(getBody().yRot) * 5.0F;
			getLeftArm().z = -Mth.sin(getBody().yRot) * 5.0F;
			getLeftArm().x = Mth.cos(getBody().yRot) * 5.0F;
			getRightArm().yRot += getBody().yRot;
			getLeftArm().yRot += getBody().yRot;
			getLeftArm().xRot += getBody().yRot;

			f = 1.0F - this.attackTime;
			f *= f;
			f *= f;
			f = 1.0F - f;
			float f1 = Mth.sin(f * (float)Math.PI);
			float f2 = Mth.sin(this.attackTime * (float)Math.PI) * -(getHead().xRot - 0.7F) * 0.75F;
			attackArm.xRot = (float)((double)attackArm.xRot - ((double)f1 * 1.2D + (double)f2));
			attackArm.yRot += getBody().yRot * 2.0F;
			attackArm.zRot += Mth.sin(this.attackTime * (float)Math.PI) * -0.4F;
		}
	}
	
	@Override
	public ModelPart getHead() {
		return this.head;
	}

	@Override
	public ModelPart getBody() {
		return body;
	}

	@Override
	public ModelPart getRightArm() {
		return rightArm;
	}

	@Override
	public ModelPart getLeftArm() {
		return leftArm;
	}
}