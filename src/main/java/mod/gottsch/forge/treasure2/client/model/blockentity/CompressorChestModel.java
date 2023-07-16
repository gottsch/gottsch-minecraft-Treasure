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
 * @author Mark Gottschling on Mar 18, 2018
 *
 */
public class CompressorChestModel extends AbstractTreasureChestModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Treasure.MODID, "compressor_chest"), "main");
	private final ModelPart base;
	private final ModelPart lid;
	private final ModelPart lid1;
	private final ModelPart lid2;
	private final ModelPart lid3;
	private final ModelPart lid4;

	/**
	 * 
	 */
	public CompressorChestModel(ModelPart root) {
		super(root);
		this.base = root.getChild("base");
		this.lid = root.getChild("lid");
		this.lid1 = lid.getChild("lid1");
		this.lid2 = lid.getChild("lid2");
		this.lid3 = lid.getChild("lid3");
		this.lid4 = lid.getChild("lid4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, 0.0F, -14.0F, 14.0F, 10.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.0F, 7.0F));
		PartDefinition hinge1 = base.addOrReplaceChild("hinge1", CubeListBuilder.create().texOffs(0, 7).addBox(-2.0F, 0.0F, 0.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 0.5F, -14.5F));
		PartDefinition hinge2 = base.addOrReplaceChild("hinge2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -3.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.5F, 0.5F, -10.0F));
		PartDefinition hinge3 = base.addOrReplaceChild("hinge3", CubeListBuilder.create().texOffs(0, 7).addBox(-2.0F, 0.0F, 0.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.5F, -0.5F));
		PartDefinition hinge4 = base.addOrReplaceChild("hinge4", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -3.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(6.5F, 0.5F, -3.0F));
		PartDefinition lid = partdefinition.addOrReplaceChild("lid", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition lid1 = lid.addOrReplaceChild("lid1", CubeListBuilder.create().texOffs(29, 38).addBox(-4.0F, -5.0F, 0.0F, 7.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -9.0F, -7.0F));
		PartDefinition latch1 = lid1.addOrReplaceChild("latch1", CubeListBuilder.create().texOffs(43, 0).addBox(5.0F, -2.0F, 2.0F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.0F, 0.0F));
		PartDefinition lid2 = lid.addOrReplaceChild("lid2", CubeListBuilder.create().texOffs(0, 38).addBox(0.0F, -5.0F, -4.0F, 7.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, -9.0F, -3.0F));
		PartDefinition latch2 = lid2.addOrReplaceChild("latch2", CubeListBuilder.create().texOffs(22, 25).addBox(2.0F, -2.0F, -5.0F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition lid3 = lid.addOrReplaceChild("lid3", CubeListBuilder.create().texOffs(29, 25).addBox(-4.0F, -5.0F, -7.0F, 7.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -9.0F, 7.0F));
		PartDefinition latch3 = lid3.addOrReplaceChild("latch3", CubeListBuilder.create().texOffs(43, 0).addBox(-5.0F, -2.0F, -5.0F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition lid4 = lid.addOrReplaceChild("lid4", CubeListBuilder.create().texOffs(0, 25).addBox(-7.0F, -5.0F, -3.0F, 7.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, -9.0F, 3.0F));
		PartDefinition latch4 = lid4.addOrReplaceChild("latch4", CubeListBuilder.create().texOffs(22, 25).addBox(-5.0F, -2.0F, 5.0F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -1.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void updateModelLidRotation(AbstractTreasureChestBlockEntity tileEntity, float partialTicks) {
		float lidRotation = tileEntity.prevLidAngle + (tileEntity.lidAngle - tileEntity.prevLidAngle) * partialTicks;
		lidRotation = 1.0F - lidRotation;
		lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
		getLid().xRot = (lidRotation * (float)Math.PI / 2.0F);  
		getLid2().zRot = -getLid().xRot;
		getLid3().xRot = -getLid().xRot;
		getLid4().zRot = getLid().xRot;
	}
		
	/**
	 * 
	 */
	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, AbstractTreasureChestBlockEntity blockEntity) {
//		float originalAngle = lid1.xRot;

		// reverse the angle direction
//		lid1.xRot = -(lid1.xRot);
//		lid2.zRot = originalAngle;
//		lid3.xRot = originalAngle;
//		lid4.zRot = lid1.xRot;


		base.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		lid.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	/**
	 * @return the lid
	 */
	@Override
	public ModelPart getLid() {
		return lid1;
	}

	public ModelPart getLid2() {
		return lid2;
	}
	
	public ModelPart getLid3() {
		return lid3;
	}
	
	public ModelPart getLid4() {
		return lid4;
	}
}
