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

/**
 * 
 */
public class DreadPirateChestModel extends AbstractTreasureChestModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Treasure.MODID, "dread_pirate_chest"), "main");
	private final ModelPart lid;
	private final ModelPart main;

	/**
	 * 
	 * @param root
	 */
	public DreadPirateChestModel(ModelPart root) {
		super(root);
		this.lid = root.getChild("lid");
		this.main = root.getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition lid = partdefinition.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -4.0F, -14.0F, 14.0F, 5.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.0F, 7.0F));
		PartDefinition topHinge2 = lid.addOrReplaceChild("topHinge2", CubeListBuilder.create().texOffs(23, 37).addBox(0.0F, -4.5F, -4.5F, 2.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 0.0F, 0.0F));
		PartDefinition skullTop = lid.addOrReplaceChild("skullTop", CubeListBuilder.create().texOffs(43, 0).addBox(-3.0F, -3.0F, -15.0F, 6.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));
		PartDefinition topHinge1 = lid.addOrReplaceChild("topHinge1", CubeListBuilder.create().texOffs(23, 37).addBox(-1.0F, -4.5F, -4.5F, 2.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, 0.0F));
		PartDefinition corner1 = lid.addOrReplaceChild("corner1", CubeListBuilder.create().texOffs(0, 25).addBox(-1.0F, -4.25F, -14.25F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(6.25F, 0.0F, 0.0F));
		PartDefinition corner2 = lid.addOrReplaceChild("corner2", CubeListBuilder.create().texOffs(0, 20).addBox(-1.0F, -4.25F, -14.25F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.3F, 0.0F, 0.0F));
		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition baseMiddle = main.addOrReplaceChild("baseMiddle", CubeListBuilder.create().texOffs(45, 8).addBox(-3.0F, 0.0F, -11.0F, 6.0F, 9.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 5.0F));
		PartDefinition baseSide1 = main.addOrReplaceChild("baseSide1", CubeListBuilder.create().texOffs(37, 37).addBox(-12.0F, 0.0F, -12.0F, 4.0F, 9.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -10.0F, 5.0F));
		PartDefinition baseSide2 = main.addOrReplaceChild("baseSide2", CubeListBuilder.create().texOffs(0, 37).addBox(8.0F, 0.0F, -12.0F, 4.0F, 9.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -10.0F, 5.0F));
		PartDefinition bottom = main.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 20).addBox(-7.0F, 0.0F, -14.0F, 14.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 7.0F));
		PartDefinition skullBottom = main.addOrReplaceChild("skullBottom", CubeListBuilder.create().texOffs(0, 7).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, -6.0F));
		PartDefinition bottomHinge1 = main.addOrReplaceChild("bottomHinge1", CubeListBuilder.create().texOffs(7, 0).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -10.0F, 7.0F));
		PartDefinition bottomHinge2 = main.addOrReplaceChild("bottomHinge2", CubeListBuilder.create().texOffs(7, 0).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -10.0F, 7.0F));
		PartDefinition handle1 = main.addOrReplaceChild("handle1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, -9.0F, -2.0F));
		PartDefinition handle2 = main.addOrReplaceChild("handle2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, -9.0F, -2.0F));

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