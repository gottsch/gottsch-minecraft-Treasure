/*
 * This file is part of  Treasure2.
 * Copyright (c) 2018 Mark Gottschling (gottsch)
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

public class StrongboxModel extends AbstractTreasureChestModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Treasure.MODID, "iron_strongbox"), "main");
	public static final ModelLayerLocation GOLD_LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Treasure.MODID, "gold_strongbox"), "main");
	
	private final ModelPart box;
	private final ModelPart lid;

	public StrongboxModel(ModelPart root) {
		super(root);
		this.box = root.getChild("box");
		this.lid = root.getChild("lid");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition box = partdefinition.addOrReplaceChild("box", CubeListBuilder.create().texOffs(0, 12).mirror().addBox(-5.0F, 0.0F, -8.0F, 10.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 18.5F, 4.0F));
		PartDefinition foot1 = box.addOrReplaceChild("foot1", CubeListBuilder.create().texOffs(0, 26).mirror().addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.0F, 4.5F, -8.0F));
		PartDefinition foot2 = box.addOrReplaceChild("foot2", CubeListBuilder.create().texOffs(7, 26).mirror().addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.0F, 4.5F, -8.0F));
		PartDefinition foot3 = box.addOrReplaceChild("foot3", CubeListBuilder.create().texOffs(14, 26).mirror().addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.0F, 4.5F, -1.0F));
		PartDefinition foot4 = box.addOrReplaceChild("foot4", CubeListBuilder.create().texOffs(21, 26).mirror().addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.0F, 4.5F, -1.0F));
		PartDefinition pad2 = box.addOrReplaceChild("pad2", CubeListBuilder.create().texOffs(37, 4).mirror().addBox(-2.0F, 0.0F, -3.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, -5.5F));
		PartDefinition lid = partdefinition.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-5.0F, -3.0F, -8.0F, 10.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 19.5F, 4.0F));
		PartDefinition latch = lid.addOrReplaceChild("latch", CubeListBuilder.create().texOffs(37, 11).mirror().addBox(-1.0F, -1.5F, -9.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition pad = lid.addOrReplaceChild("pad", CubeListBuilder.create().texOffs(37, 0).mirror().addBox(-2.0F, -2.0F, -8.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, AbstractTreasureChestBlockEntity blockEntity) {
		box.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		lid.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	@Override
	public ModelPart getLid() {
		return lid;
	}
}