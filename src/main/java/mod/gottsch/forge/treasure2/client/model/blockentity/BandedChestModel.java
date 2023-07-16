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
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class BandedChestModel extends AbstractTreasureChestModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Treasure.MODID, "ironbound_chest"), "main");
	private final ModelPart main;
	private final ModelPart lid;

	public BandedChestModel(ModelPart root) {
		super(root);
		this.main = root.getChild("main");
		this.lid = root.getChild("lid");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 20).mirror().addBox(-7.0F, 0.0F, -14.0F, 14.0F, 9.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 14.5F, 7.0F));
		PartDefinition hinge2 = main.addOrReplaceChild("hinge2", CubeListBuilder.create().texOffs(11, 86).mirror().addBox(-1.0F, 0.0F, -0.8F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 0.0F, 0.0F));
		PartDefinition hinge1 = main.addOrReplaceChild("hinge1", CubeListBuilder.create().texOffs(11, 86).addBox(-1.0F, 0.0F, -0.8F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, 0.0F));
		PartDefinition leftBottomBand = main.addOrReplaceChild("leftBottomBand", CubeListBuilder.create().texOffs(35, 44).addBox(-1.0F, 0.0F, -15.0F, 2.0F, 9.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.5F, 0.5F));
		PartDefinition rightBottomBand = main.addOrReplaceChild("rightBottomBand", CubeListBuilder.create().texOffs(0, 44).mirror().addBox(-1.0F, 0.0F, -15.0F, 2.0F, 9.0F, 15.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, 0.5F, 0.5F));
		PartDefinition pad = main.addOrReplaceChild("pad", CubeListBuilder.create().texOffs(0, 86).mirror().addBox(-2.0F, -1.0F, -14.2F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 1.0F, 0.0F));
		PartDefinition lid = partdefinition.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-7.0F, -5.0F, -14.0F, 14.0F, 5.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 15.5F, 7.0F));
		PartDefinition frontRightTopBand = lid.addOrReplaceChild("frontRightTopBand", CubeListBuilder.create().texOffs(57, 0).mirror().addBox(-1.0F, -4.0F, -15.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, -0.5F, 0.5F));
		PartDefinition backLeftTopBand = lid.addOrReplaceChild("backLeftTopBand", CubeListBuilder.create().texOffs(64, 7).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -0.5F, 0.5F));
		PartDefinition rightTopBand = lid.addOrReplaceChild("rightTopBand", CubeListBuilder.create().texOffs(0, 69).mirror().addBox(0.0F, -5.0F, -15.0F, 2.0F, 1.0F, 15.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.0F, -0.5F, 0.5F));
		PartDefinition leftTopBand = lid.addOrReplaceChild("leftTopBand", CubeListBuilder.create().texOffs(36, 69).addBox(0.0F, -5.0F, -15.0F, 2.0F, 1.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -0.5F, 0.5F));
		PartDefinition frontLeftTopBand = lid.addOrReplaceChild("frontLeftTopBand", CubeListBuilder.create().texOffs(64, 0).addBox(-1.0F, -4.0F, -15.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -0.5F, 0.5F));
		PartDefinition backRightTopBand = lid.addOrReplaceChild("backRightTopBand", CubeListBuilder.create().texOffs(57, 7).mirror().addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, -0.5F, 0.5F));
		PartDefinition ledge3 = lid.addOrReplaceChild("ledge3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -1.0F, -9.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-7.0F, -0.5F, 0.0F));
		PartDefinition ledge = lid.addOrReplaceChild("ledge", CubeListBuilder.create().texOffs(58, 16).mirror().addBox(-2.0F, -1.0F, -15.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -0.5F, 0.0F));
		PartDefinition ledge2 = lid.addOrReplaceChild("ledge2", CubeListBuilder.create().texOffs(58, 16).mirror().addBox(0.0F, -1.0F, -9.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(7.0F, -0.5F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}


	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, AbstractTreasureChestBlockEntity blockEntity) {
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		lid.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	@Override
	public ModelPart getLid() {
		return lid;
	}
}