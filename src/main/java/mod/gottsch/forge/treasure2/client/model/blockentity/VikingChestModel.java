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
 * @author Mark Gottschling on Apr 19, 2020
 *
 */
public class VikingChestModel extends AbstractTreasureChestModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Treasure.MODID, "viking_chest"), "main");
	private final ModelPart mainBox;
	private final ModelPart lid;

	/**
	 * 
	 * @param root
	 */
	public VikingChestModel(ModelPart root) {
		super(root);
		this.mainBox = root.getChild("mainBox");
		this.lid = root.getChild("lid");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition mainBox = partdefinition.addOrReplaceChild("mainBox", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -13.0F, -5.0F, 14.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(11, 42).addBox(-2.0F, -12.0F, -5.5F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 35).addBox(-8.0F, -5.5F, -2.0F, 16.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(39, 11).addBox(-7.0F, -3.0F, -5.0F, 2.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(31, 35).addBox(5.0F, -3.0F, -5.0F, 2.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 42).addBox(7.0F, -11.0F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(39, 25).addBox(-8.0F, -11.0F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition back = mainBox.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, 6).addBox(5.5F, -2.0F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(5.5F, -8.0F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(-6.5F, -8.0F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(-6.5F, -2.0F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition side1 = mainBox.addOrReplaceChild("side1", CubeListBuilder.create().texOffs(0, 6).addBox(6.5F, -5.0F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(-7.5F, -11.0F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(6.5F, -5.0F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(6.5F, -11.0F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition side2 = mainBox.addOrReplaceChild("side2", CubeListBuilder.create().texOffs(0, 6).addBox(-7.5F, -5.0F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(6.5F, -11.0F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(-7.5F, -5.0F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(-7.5F, -11.0F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition front = mainBox.addOrReplaceChild("front", CubeListBuilder.create().texOffs(0, 6).addBox(5.5F, -2.0F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(5.5F, -8.0F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(-6.5F, -8.0F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(-6.5F, -2.0F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lid = partdefinition.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(39, 0).addBox(-1.0F, -3.5F, -10.75F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 21).addBox(-7.0F, -3.0F, -10.0F, 14.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(5.5F, -2.0F, -10.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(-6.5F, -2.0F, -10.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(5.5F, -2.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(-6.5F, -2.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, -2.5F, -10.75F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 5.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}


	
	@Override
	public ModelPart getLid() {
		return lid;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha, AbstractTreasureChestBlockEntity blockEntity) {
		mainBox.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		lid.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}