/*
 * This file is part of  Treasure2.
 * Copyright (c) 2020 Mark Gottschling (gottsch)
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
import mod.gottsch.forge.treasure2.core.block.entity.CardboardBoxBlockEntity;
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
 * 
 * @author Mark Gottschling on Nov 29, 2020
 *
 */
public class CardboardBoxModel extends AbstractTreasureChestModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Treasure.MODID, "cardboard_box"), "main");
	private final ModelPart base;
	private final ModelPart lid;
	private final ModelPart northFlap;
	private final ModelPart southFlap;
	private final ModelPart rightFlap;
	private final ModelPart leftFlap;
	
	public CardboardBoxModel(ModelPart root) {
		super(root);
		this.base = root.getChild("base");
		this.lid = root.getChild("lid");
		this.northFlap = lid.getChild("northFlap");
		this.southFlap = lid.getChild("southFlap");
		this.rightFlap = lid.getChild("rightFlap");
		this.leftFlap = lid.getChild("leftFlap");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-14.0F, 0.0F, 0.0F, 14.0F, 14.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, 10.0F, -7.0F));

		PartDefinition lid = partdefinition.addOrReplaceChild("lid", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition northFlap = lid.addOrReplaceChild("northFlap", CubeListBuilder.create().texOffs(43, 0).addBox(-7.0F, -0.999F, 0.001F, 14.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -14.0F, -7.0F));

		PartDefinition southFlap = lid.addOrReplaceChild("southFlap", CubeListBuilder.create().texOffs(0, 47).addBox(-7.0F, -0.999F, -6.001F, 14.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -14.0F, 7.0F));

		PartDefinition leftFlap = lid.addOrReplaceChild("leftFlap", CubeListBuilder.create().texOffs(0, 29).addBox(0.0F, -1.0F, -7.0F, 7.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, -14.0F, 0.0F));

		PartDefinition rightFlap = lid.addOrReplaceChild("rightFlap", CubeListBuilder.create().texOffs(29, 31).addBox(-7.0F, -1.0F, -7.0F, 7.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, -14.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void updateModelLidRotation(AbstractTreasureChestBlockEntity blockEntity, float partialTicks) {
		CardboardBoxBlockEntity be = (CardboardBoxBlockEntity) blockEntity;
		
		// update in the inner lid
		float innerLidRotation = be.prevInnerLidAngle + (be.innerLidAngle - be.prevInnerLidAngle) * partialTicks;
		innerLidRotation = 1.0F - innerLidRotation;
		innerLidRotation = 1.0F - innerLidRotation * innerLidRotation * innerLidRotation;
		getInnerLid().xRot = (innerLidRotation * (float) Math.PI / getAngleModifier()); // not negated
		
		float lidRotation = blockEntity.prevLidAngle + (blockEntity.lidAngle - blockEntity.prevLidAngle) * partialTicks;
		lidRotation = 1.0F - lidRotation;
		lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
		getLid().zRot = -(lidRotation * (float) Math.PI / getAngleModifier());
	}
	
	@Override
	public float getAngleModifier() {
		return 0.8F;
	}
	
	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha, AbstractTreasureChestBlockEntity blockEntity) {
		rightFlap.zRot = -leftFlap.zRot;
		southFlap.xRot = -northFlap.xRot;
		
		base.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		lid.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart getLid() {
		return leftFlap;
	}
	
	public ModelPart getInnerLid() {
		return northFlap;
	}

}