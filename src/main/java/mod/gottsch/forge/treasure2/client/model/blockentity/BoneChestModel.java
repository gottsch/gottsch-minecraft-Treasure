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
package mod.gottsch.forge.treasure2.client.model.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.BoneChestBlockEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class BoneChestModel extends AbstractTreasureChestModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Treasure.MODID, "bone_chest"), "main");

	private final ModelPart chest;
	private final ModelPart lid;
	private final ModelPart trunk;
	private final ModelPart skull;
	private final ModelPart jaw;
	private final ModelPart lock;

	public BoneChestModel(ModelPart root) {
		super(root);
		this.chest = root.getChild("chest");
		this.lid = this.chest.getChild("lid");
		this.trunk = this.chest.getChild("trunk");
		this.skull = this.trunk.getChild("skull");
		this.jaw = this.trunk.getChild("jaw");
		this.lock = this.trunk.getChild("lock");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition chest = partdefinition.addOrReplaceChild("chest", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition lid = chest.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 27).addBox(-6.2F, -4.3F, -13.7F, 14.0F, 4.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(51, 51).addBox(6.3F, -3.4F, -1.2F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(51, 51).addBox(-6.7F, -3.3F, -14.2F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.01F))
		.texOffs(43, 7).addBox(5.8F, -5.3F, -14.7F, 3.0F, 3.0F, 3.0F, new CubeDeformation(-0.2F))
		.texOffs(9, 46).addBox(5.8F, -5.3F, -1.7F, 3.0F, 3.0F, 3.0F, new CubeDeformation(-0.2F))
		.texOffs(9, 46).addBox(-7.2F, -5.3F, -14.7F, 3.0F, 3.0F, 3.0F, new CubeDeformation(-0.2F))
		.texOffs(43, 7).addBox(-7.45F, -5.3F, -1.95F, 3.0F, 3.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offset(-0.8F, -10.7F, 6.7F));

		PartDefinition lidStud2_r1 = lid.addOrReplaceChild("lidStud2_r1", CubeListBuilder.create().texOffs(9, 0).addBox(-0.6F, -0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(-1.5F, -1.6F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(-1.5F, -1.6F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(-0.6F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.3F, -3.8F, -9.7F, 0.0F, 0.0F, -0.0873F));

		PartDefinition lidStud8_r1 = lid.addOrReplaceChild("lidStud8_r1", CubeListBuilder.create().texOffs(9, 0).addBox(-1.5F, 0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(-1.5F, 0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(-0.5F, -0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.7F, -4.8F, -3.7F, 0.0F, -0.0436F, 0.0436F));

		PartDefinition lidBrace4_r1 = lid.addOrReplaceChild("lidBrace4_r1", CubeListBuilder.create().texOffs(9, 53).addBox(-1.1F, -1.0F, -7.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F))
		.texOffs(9, 53).addBox(-1.1F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(-5.7F, -3.8F, -3.7F, 0.0F, -0.0436F, 0.0436F));

		PartDefinition lidBrace2_r1 = lid.addOrReplaceChild("lidBrace2_r1", CubeListBuilder.create().texOffs(9, 53).addBox(-0.7F, -0.8F, 6.3F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F))
		.texOffs(9, 53).addBox(-0.7F, -0.8F, 0.3F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(7.0F, -4.0F, -11.0F, 0.0F, 0.0F, -0.0873F));

		PartDefinition bone5_r1 = lid.addOrReplaceChild("bone5_r1", CubeListBuilder.create().texOffs(45, 15).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.7F, -3.8F, -6.7F, 0.0F, -0.0436F, 0.0436F));

		PartDefinition bone4_r1 = lid.addOrReplaceChild("bone4_r1", CubeListBuilder.create().texOffs(42, 31).addBox(-1.0F, -1.0F, -7.5F, 2.0F, 2.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.2F, -3.8F, -6.7F, -0.0436F, 0.0436F, 0.0F));

		PartDefinition bone3_r1 = lid.addOrReplaceChild("bone3_r1", CubeListBuilder.create().texOffs(42, 31).addBox(-1.0F, -1.0F, -7.5F, 2.0F, 2.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.8F, -3.8F, -6.7F, 0.0436F, 0.0F, 0.0F));

		PartDefinition bone2_r1 = lid.addOrReplaceChild("bone2_r1", CubeListBuilder.create().texOffs(42, 31).addBox(-1.0F, -1.0F, -7.5F, 2.0F, 2.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.8F, -3.8F, -6.7F, 0.0F, -0.0436F, -0.0873F));

		PartDefinition bone1_r1 = lid.addOrReplaceChild("bone1_r1", CubeListBuilder.create().texOffs(45, 15).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.3F, -3.8F, -6.7F, 0.0F, 0.0F, -0.0873F));

		PartDefinition rightBone2_r1 = lid.addOrReplaceChild("rightBone2_r1", CubeListBuilder.create().texOffs(51, 51).addBox(-7.5F, -14.0F, 5.5F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.8F, 10.6F, -6.7F, 0.0F, -0.0436F, 0.0F));

		PartDefinition leftBone1_r1 = lid.addOrReplaceChild("leftBone1_r1", CubeListBuilder.create().texOffs(51, 51).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(7.3F, -1.8F, -13.2F, 0.0F, 0.0873F, 0.0F));

		PartDefinition trunk = chest.addOrReplaceChild("trunk", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -7.0F, -7.0F, 14.0F, 12.0F, 14.0F, new CubeDeformation(0.01F))
		.texOffs(0, 46).addBox(5.5F, -4.0F, -4.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(31, 46).addBox(5.5F, -2.0F, 2.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 0.0F));

		PartDefinition stud14_r1 = trunk.addOrReplaceChild("stud14_r1", CubeListBuilder.create().texOffs(9, 0).addBox(0.7F, -0.5F, 0.6F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(0.7F, 6.5F, 0.6F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(-0.3F, 6.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(-0.3F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.7F, -4.0F, 6.5F, -0.0436F, 0.0F, -0.0436F));

		PartDefinition stud10_r1 = trunk.addOrReplaceChild("stud10_r1", CubeListBuilder.create().texOffs(9, 0).addBox(-0.6F, -7.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(-1.6F, -7.5F, 0.2F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(-1.6F, -0.5F, 0.2F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(-0.6F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.4F, 3.0F, 6.5F, -0.0436F, 0.0F, 0.0436F));

		PartDefinition stud4_r1 = trunk.addOrReplaceChild("stud4_r1", CubeListBuilder.create().texOffs(9, 0).addBox(-0.1F, 3.5F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(-0.1F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(-1.0F, 3.5F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(-1.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 53).addBox(-1.5F, 3.0F, -0.6F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F))
		.texOffs(9, 53).addBox(-1.5F, -4.0F, -0.6F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(7.0F, -1.0F, -7.0F, 0.0436F, 0.0873F, 0.0436F));

		PartDefinition stud8_r1 = trunk.addOrReplaceChild("stud8_r1", CubeListBuilder.create().texOffs(9, 0).addBox(-1.0F, -1.5F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(-1.0F, 5.5F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(0.0F, 5.5F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(0.0F, -1.5F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(38, 53).addBox(-0.6F, 5.0F, -0.6F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F))
		.texOffs(38, 53).addBox(-0.6F, -2.0F, -0.6F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(-7.0F, -3.0F, -7.0F, 0.0436F, 0.0F, -0.0436F));

		PartDefinition brace8_r1 = trunk.addOrReplaceChild("brace8_r1", CubeListBuilder.create().texOffs(9, 53).addBox(-14.0F, 2.5F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F))
		.texOffs(9, 53).addBox(-14.0F, -4.5F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(6.5F, -1.0F, 6.5F, -0.0436F, 0.0F, -0.0436F));

		PartDefinition brace6_r1 = trunk.addOrReplaceChild("brace6_r1", CubeListBuilder.create().texOffs(9, 53).addBox(-1.0F, 3.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F))
		.texOffs(9, 53).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(6.5F, -1.0F, 6.5F, -0.0436F, 0.0F, 0.0436F));

		PartDefinition rightBone2_r2 = trunk.addOrReplaceChild("rightBone2_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.55F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.5F, -0.5F, 6.5F, -0.0436F, 0.0F, -0.0436F));

		PartDefinition rightBone1_r1 = trunk.addOrReplaceChild("rightBone1_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.55F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.5F, -0.5F, -6.5F, 0.0436F, 0.0F, -0.0436F));

		PartDefinition leftBone2_r1 = trunk.addOrReplaceChild("leftBone2_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.55F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, -0.5F, 6.5F, -0.0436F, 0.0F, 0.0436F));

		PartDefinition rightBone5_r1 = trunk.addOrReplaceChild("rightBone5_r1", CubeListBuilder.create().texOffs(22, 46).addBox(-7.5F, -8.0F, 2.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.0F, 0.0F, -0.0436F));

		PartDefinition leftBone4_r1 = trunk.addOrReplaceChild("leftBone4_r1", CubeListBuilder.create().texOffs(43, 30).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, -1.0F, 0.0F, -0.0873F, 0.0F, -0.0436F));

		PartDefinition rightBone3_r1 = trunk.addOrReplaceChild("rightBone3_r1", CubeListBuilder.create().texOffs(0, 27).addBox(-7.5F, -10.0F, -4.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition leftBone1_r2 = trunk.addOrReplaceChild("leftBone1_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.55F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, -0.5F, -6.5F, 0.0436F, 0.0873F, 0.0436F));

		PartDefinition skull = trunk.addOrReplaceChild("skull", CubeListBuilder.create().texOffs(43, 0).addBox(-3.5F, -5.0F, -7.5F, 7.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition jaw = trunk.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(40, 49).addBox(-2.5F, 0.0F, -7.5F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lock = trunk.addOrReplaceChild("lock", CubeListBuilder.create().texOffs(18, 55).addBox(-1.5F, -1.5F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -6.6F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public ModelPart getLid() {
		return lid;
	}

	public ModelPart getSkull() {
		return skull;
	}

	public ModelPart getJaw() {
		return jaw;
	}

	public ModelPart getLock() {
		return this.lock;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, AbstractTreasureChestBlockEntity blockEntity) {
		chest.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void  updateModelLidRotation(AbstractTreasureChestBlockEntity blockEntity, float partialTicks) {
		BoneChestBlockEntity be = (BoneChestBlockEntity) blockEntity;
		final float MAX_DIST = 1.0F;
		// don't recalculate skull position if lid is open ie. the values shouldn't change so don't waste ticks
		if (be.isLidClosed) {
			float amount = be.prevSkullYPosition + (be.skullYPosition - be.prevSkullYPosition) * partialTicks;
			getJaw().y = -(amount * MAX_DIST);
			getSkull().y = (amount * MAX_DIST);
		}

		// TODO lock property is not being synced and thereofr in the TE the angles aren't being calculated on the client
		float lockRotation = be.prevLockAngle + (be.lockAngle - be.prevLockAngle) * partialTicks;
		lockRotation = 1.0F - lockRotation;
		lockRotation = 1.0F - lockRotation * lockRotation * lockRotation;
		getLock().zRot = -(lockRotation * (float)Math.PI / getAngleModifier());

		float lidRotation = be.prevLidAngle + (be.lidAngle - be.prevLidAngle) * partialTicks;
		lidRotation = 1.0F - lidRotation;
		lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
		getLid().xRot = -(lidRotation * (float)Math.PI / getAngleModifier());
	}
}