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

import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.CauldronChestBlockEntity;
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
 * @author Mark Gottschling on 2020
 *
 */
public class CauldronChestModel extends AbstractTreasureChestModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "cauldronchestmodel"), "main");
	private final ModelPart main;
	private final ModelPart lid;
	private final ModelPart leftLid;
	private final ModelPart rightLid;

	/*
	 * 
	 */
	public CauldronChestModel(ModelPart root) {
		super(root);
		this.main = root.getChild("main");
		this.lid = root.getChild("lid");
		this.leftLid = lid.getChild("leftLid");
		this.rightLid = lid.getChild("rightLid");
	}

	/**
	 * 
	 */
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 80).mirror().addBox(4.0F, -3.0F, -8.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(14, 80).mirror().addBox(-8.0F, -3.0F, -8.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(42, 80).mirror().addBox(-8.0F, -3.0F, 6.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(28, 80).mirror().addBox(4.0F, -3.0F, 6.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(10, 88).mirror().addBox(-8.0F, -3.0F, -6.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(30, 88).mirror().addBox(-8.0F, -3.0F, 4.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(20, 88).mirror().addBox(6.0F, -3.0F, 4.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 88).mirror().addBox(6.0F, -3.0F, -6.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 18).mirror().addBox(6.0F, -16.0F, -8.0F, 2.0F, 13.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(38, 18).mirror().addBox(-8.0F, -16.0F, -8.0F, 2.0F, 13.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 50).mirror().addBox(-6.0F, -4.0F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(32, 0).mirror().addBox(-6.0F, -16.0F, 6.0F, 12.0F, 13.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(-6.0F, -16.0F, -8.0F, 12.0F, 13.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition lid = partdefinition.addOrReplaceChild("lid", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition leftLid = lid.addOrReplaceChild("leftLid", CubeListBuilder.create().texOffs(0, 65).mirror().addBox(-6.0F, 0.0F, 0.0F, 6.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(6.0F, -15.0F, -6.0F));
		PartDefinition rightLid = lid.addOrReplaceChild("rightLid", CubeListBuilder.create().texOffs(38, 65).mirror().addBox(0.0F, 0.0F, 0.0F, 6.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-6.0F, -15.0F, -6.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);	
	}

	@Override
	public void updateModelLidRotation(AbstractTreasureChestBlockEntity blockEntity, float partialTicks) {
		CauldronChestBlockEntity cte = (CauldronChestBlockEntity) blockEntity;
		float lidRotation = cte.prevLidAngle + (cte.lidAngle - cte.prevLidAngle) * partialTicks;
		lidRotation = 1.0F - lidRotation;
		lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
		// NOTE positive rotation here (getLid() returns lidLeft property)
		getLid().zRot = (lidRotation * (float)Math.PI / getAngleModifier());
		getRightLid().zRot = -getLid().zRot;
	}
	
	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, AbstractTreasureChestBlockEntity blockEntity) {
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		lid.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
		
	@Override
	public ModelPart getLid() {
		return leftLid;
	}
	public ModelPart getRightLid() {
		return rightLid;
	}
}