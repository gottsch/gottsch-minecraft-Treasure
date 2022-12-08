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

public class MilkCrateModel extends AbstractTreasureChestModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Treasure.MODID, "milk_crate"), "main");
	private final ModelPart lid;
	private final ModelPart main;

	public MilkCrateModel(ModelPart root) {
		super(root);
		this.lid = root.getChild("lid");
		this.main = root.getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition lid = partdefinition.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -2.0F, -14.0F, 14.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 21).addBox(-2.0F, -2.0F, -14.2F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 25).addBox(-1.0F, -1.0F, -15.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, 7.0F));

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition northFace = main.addOrReplaceChild("northFace", CubeListBuilder.create().texOffs(0, 47).addBox(-7.0F, 7.0F, -14.0F, 14.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(43, 0).addBox(-7.0F, 3.0F, -14.0F, 14.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(42, 35).addBox(-7.0F, -1.0F, -14.0F, 14.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(5.1F, -1.0F, -14.1F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.1F, -1.0F, -14.1F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 7.0F));

		PartDefinition southFace = main.addOrReplaceChild("southFace", CubeListBuilder.create().texOffs(43, 5).addBox(-7.0F, 7.0F, -1.0F, 14.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(43, 0).addBox(-7.0F, 3.0F, -1.0F, 14.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(42, 35).addBox(-7.0F, -1.0F, -1.0F, 14.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(5.1F, -1.0F, -1.9F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.1F, -1.0F, -1.9F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 7.0F));

		PartDefinition eastFace = main.addOrReplaceChild("eastFace", CubeListBuilder.create().texOffs(42, 19).addBox(-7.0F, 7.0F, -13.0F, 1.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(27, 31).addBox(-7.0F, 3.0F, -13.0F, 1.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 31).addBox(-7.0F, -1.0F, -13.0F, 1.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 7.0F));

		PartDefinition westFace = main.addOrReplaceChild("westFace", CubeListBuilder.create().texOffs(42, 19).addBox(6.0F, 7.0F, -13.0F, 1.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(27, 31).addBox(6.0F, 3.0F, -13.0F, 1.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 31).addBox(6.0F, -1.0F, -13.0F, 1.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 7.0F));

		PartDefinition bottomFace = main.addOrReplaceChild("bottomFace", CubeListBuilder.create().texOffs(0, 17).addBox(-6.0F, -1.0F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition padBottom = main.addOrReplaceChild("padBottom", CubeListBuilder.create().texOffs(0, 17).addBox(-2.0F, -2.0F, -14.2F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 7.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}


	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, AbstractTreasureChestBlockEntity blockEntity) {
		lid.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	@Override
	public ModelPart getLid() {
		return lid;
	}
}