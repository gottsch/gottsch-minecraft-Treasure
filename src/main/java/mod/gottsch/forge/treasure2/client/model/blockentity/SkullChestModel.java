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

public class SkullChestModel extends AbstractTreasureChestModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Treasure.MODID, "skull_chest"), "main");
	public static final ModelLayerLocation GOLD_LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Treasure.MODID, "gold_skull_chest"), "main");
	public static final ModelLayerLocation CRYSTAL_LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Treasure.MODID, "crystal_skull_chest"), "main");
	
	private final ModelPart lid;
	private final ModelPart main;

	public SkullChestModel(ModelPart root) {
		super(root);
		this.lid = root.getChild("lid");
		this.main = root.getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition lid = partdefinition.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-3.0F, -7.0F, -5.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 22.0F, 2.0F));
		PartDefinition head = lid.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 8).mirror().addBox(-4.0F, -5.0F, -6.0F, 8.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));
		PartDefinition jaw = lid.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-3.0F, 0.0F, -6.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));
		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 21.0F, 2.0F));
		PartDefinition jawBottom = main.addOrReplaceChild("jawBottom", CubeListBuilder.create().texOffs(0, 30).mirror().addBox(-3.0F, 0.0F, -5.0F, 6.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 1.0F, -1.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	 /**
	  * Set the max swing angle of 60 degrees
	  */
	 @Override
	public float getAngleModifier() {
		return 3.0F;
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