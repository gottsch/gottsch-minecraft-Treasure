/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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
import mod.gottsch.forge.treasure2.core.lock.LockState;
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
 * @author Mark Gottschling on Jan 22, 2020
 *
 */
public class SpiderChestModel extends AbstractTreasureChestModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Treasure.MODID, "spider_chest"), "main");
	private final ModelPart chest;
	private final ModelPart lid;

	public SpiderChestModel(ModelPart root) {
		super(root);
		this.chest = root.getChild("chest");
		this.lid = root.getChild("lid");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition chest = partdefinition.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(0, 16).addBox(2.0F, -4.0F, -10.0F, 12.0F, 5.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(29, 32).addBox(14.0F, -2.0F, -10.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(38, 32).addBox(14.0F, -2.0F, -6.5F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(29, 32).addBox(14.0F, -2.0F, -3.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(38, 32).addBox(0.0F, -2.0F, -10.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(29, 32).addBox(0.0F, -2.0F, -6.5F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(38, 32).addBox(0.0F, -2.0F, -3.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, 16.0F, 8.0F));

		PartDefinition bone = chest.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(38, 32).addBox(6.0F, 4.7507F, -10.8561F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, -3.0F, 0.0F, -0.3927F, 0.0F, 0.0F));
		PartDefinition bone2 = chest.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(29, 32).mirror().addBox(-8.0F, 4.7507F, -10.8561F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(8.0F, -3.0F, 0.0F, -0.3927F, 0.0F, 0.0F));
		PartDefinition lid = partdefinition.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -5.0F, -10.0F, 12.0F, 5.0F, 10.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 13.0F, 8.0F));
		PartDefinition headBone = lid.addOrReplaceChild("headBone", CubeListBuilder.create().texOffs(0, 32).addBox(-4.0F, -4.0F, -16.0F, 8.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, AbstractTreasureChestBlockEntity blockEntity) {
		chest.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		lid.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	@Override
	public ModelPart getLid() {
		return lid;
	}
}