/*
 * This file is part of  Treasure2.
 * Copyright (c) 2024 Mark Gottschling (gottsch)
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
import mod.gottsch.forge.treasure2.core.entity.monster.WitherwoodGolem;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

/**
 * @author Mark Gottschling on AUg 27, 2024
 *
 */
public class WitherwoodGolemModel<T extends Entity> extends EntityModel<T>  implements IHumanlikeModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Treasure.MODID, "witherwood_golem"), "main");

	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart neck;
	private final ModelPart torso;
	private final ModelPart upperBody;
	private final ModelPart leftArm;
	private final ModelPart leftElbow;
	private final ModelPart rightArm;
	private final ModelPart rightElbow;
	private final ModelPart lowerBody;
	private final ModelPart legs;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;

	private float leftArmX;
	private float rightArmX;

	public WitherwoodGolemModel(ModelPart root) {
		this.root = root;
		this.head = root.getChild("head");
		this.neck = head.getChild("neck");
		this.torso = root.getChild("torso");
		this.upperBody = torso.getChild("upperBody");
		this.leftArm = upperBody.getChild("leftArm");
		this.leftElbow = leftArm.getChild("leftElbow");
		this.rightArm = upperBody.getChild("rightArm");
		this.rightElbow = rightArm.getChild("rightElbow");
		this.lowerBody = torso.getChild("lowerBody");
		this.legs = root.getChild("legs");
		this.rightLeg = legs.getChild("rightLeg");
		this.leftLeg = legs.getChild("leftLeg");

		rightArmX = rightArm.x;
		leftArmX = leftArm.x;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 24).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(17, 51).addBox(0.0F, -15.0F, 3.0F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-3.0F, -13.0F, 3.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(17, 43).addBox(3.0F, -13.0F, -1.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(46, 55).addBox(-1.0F, -12.0F, 0.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -10.0F, 0.1745F, 0.0F, 0.0F));

		PartDefinition neck = head.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 63).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 2.5F, 0.4363F, 0.0F, 0.0F));

		PartDefinition torso = partdefinition.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(46, 39).mirror().addBox(4.0F, -16.0F, -1.0F, 2.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(46, 39).addBox(-6.0F, -16.0F, -1.0F, 2.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.3054F, 0.0F, 0.0F));

		PartDefinition upperBody = torso.addOrReplaceChild("upperBody", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -7.0F, -5.0F, 14.0F, 14.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(29, 51).addBox(3.0F, -8.0F, 4.0F, 0.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r1 = upperBody.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(29, 51).addBox(0.0F, -8.0F, 0.0F, 0.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 6.0F, 4.0F, -0.2618F, 0.0F, 0.0F));

		PartDefinition leftArm = upperBody.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(0, 43).mirror().addBox(-1.0F, -2.5F, -2.0F, 4.0F, 15.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(49, 61).addBox(3.0F, -2.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, -4.0F, -1.0F, -0.3927F, 0.0F, -0.1745F));

		PartDefinition leftElbow = leftArm.addOrReplaceChild("leftElbow", CubeListBuilder.create().texOffs(29, 39).mirror().addBox(-2.0F, -0.5F, -2.0F, 4.0F, 15.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, 13.0F, 0.0F, -0.3491F, 0.0F, 0.0F));

		PartDefinition rightArm = upperBody.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(0, 43).addBox(-4.0F, -2.5F, -3.0F, 4.0F, 15.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -4.0F, 0.0F, -0.3927F, 0.0F, 0.1745F));

		PartDefinition rightElbow = rightArm.addOrReplaceChild("rightElbow", CubeListBuilder.create().texOffs(29, 39).addBox(-2.0F, -0.5F, -2.0F, 4.0F, 15.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 13.0F, -1.0F, -0.3491F, 0.0F, 0.0F));

		PartDefinition branch_r1 = rightElbow.addOrReplaceChild("branch_r1", CubeListBuilder.create().texOffs(49, 61).mirror().addBox(-8.0F, -6.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, 10.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition lowerBody = torso.addOrReplaceChild("lowerBody", CubeListBuilder.create().texOffs(33, 24).addBox(-3.5F, 0.0F, -3.0F, 7.0F, 8.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

		PartDefinition legs = partdefinition.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition rightLeg = legs.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(47, 0).addBox(-1.5F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 74).addBox(-2.0F, 9.0F, -2.5F, 5.0F, 9.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(56, 16).addBox(-1.0F, 16.0F, -4.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(56, 22).addBox(10.0F, 16.0F, -1.5F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -18.0F, 0.0F));

		PartDefinition leftLeg = legs.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(47, 0).mirror().addBox(-2.5F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 74).mirror().addBox(-3.0F, 9.0F, -2.5F, 5.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(56, 16).addBox(-2.0F, 16.0F, -4.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(56, 22).addBox(-12.0F, 16.0F, -1.5F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -18.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = headPitch * ((float)Math.PI / 180F);
		// 0.5235988F = 30 degrees
		float armSpeed = 0.25F;
		float radians = 0.5235988F; // 30
		this.rightArm.xRot = -0.3926991F + Mth.cos(limbSwing * armSpeed) * radians * 1.4F * limbSwingAmount;
		this.leftArm.xRot = -0.3926991F + Mth.cos(limbSwing * armSpeed + (float)Math.PI) * radians * 1.4F * limbSwingAmount;

		// legs
		this.rightLeg.xRot = -1.5F * Mth.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
		this.leftLeg.xRot = 1.5F * Mth.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;

		// TODO add swing arms
		// TODO add attack anim

		setupAttackAnimation(entity, ageInTicks);
	}

	public void resetSwing(T entity, ModelPart body, ModelPart rightArm, ModelPart leftArm) {
		body.yRot = 0;
		rightArm.x = rightArmX;
		rightArm.xRot = -0.3926991F;
		rightArm.zRot = 0;
		rightArm.yRot = 0;
		leftArm.x = leftArmX;
		leftArm.xRot = -0.3926991F;
		leftArm.yRot = 0;
		leftArm.zRot = 0;
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
	// TODO make both arms swing upwards instead of punch
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
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		torso.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		legs.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart getHead() {
		return this.head;
	}

	@Override
	public ModelPart getBody() {
		return torso;
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