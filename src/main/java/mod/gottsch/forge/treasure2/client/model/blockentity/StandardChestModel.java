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
 * @author Mark Gottschling on Nov 16, 2022
 *
 */
public class StandardChestModel extends AbstractTreasureChestModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Treasure.MODID, "standard_chest"), "main");
	private final ModelPart base;
	private final ModelPart lid;
	private final ModelPart latch2;
	private final ModelPart latch3;
	
	/**
	 * 
	 * @param root
	 */
	public StandardChestModel(ModelPart root) {
		super(root);
		this.base = root.getChild("base");
		this.lid = root.getChild("lid");
		this.latch2 = lid.getChild("latch2");
		this.latch3 = lid.getChild("latch3");
		
		latch2.visible = false;
		latch3.visible = false;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, 0.0F, -14.0F, 14.0F, 10.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.0F, 7.0F));
		PartDefinition hingeBottom1 = base.addOrReplaceChild("hingeBottom1", CubeListBuilder.create().texOffs(0, 10).addBox(-1.0F, 0.0F, -0.8F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 0.0F, 0.0F));
		PartDefinition hingeBottom2 = base.addOrReplaceChild("hingeBottom2", CubeListBuilder.create().texOffs(0, 10).addBox(-1.0F, -1.0F, -0.8F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 1.0F, 0.0F));
		PartDefinition padBottom = base.addOrReplaceChild("padBottom", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, -1.2F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -13.0F));
		PartDefinition lid = partdefinition.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 25).addBox(-7.0F, -5.0F, -14.0F, 14.0F, 5.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 7.0F));
		PartDefinition padTop = lid.addOrReplaceChild("padTop", CubeListBuilder.create().texOffs(0, 5).addBox(-2.0F, -3.0F, -14.2F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition latch1 = lid.addOrReplaceChild("latch1", CubeListBuilder.create().texOffs(7, 25).addBox(-1.0F, -2.0F, -15.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition latch2 = lid.addOrReplaceChild("latch2", CubeListBuilder.create().texOffs(0, 25).addBox(0.0F, -2.0F, -8.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, 0.0F, 0.0F));
		PartDefinition hingeTop1 = lid.addOrReplaceChild("hingeTop1", CubeListBuilder.create().texOffs(6, 31).addBox(-1.0F, 0.0F, -0.8F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -3.0F, 0.0F));
		PartDefinition hingeTop2 = lid.addOrReplaceChild("hingeTop2", CubeListBuilder.create().texOffs(6, 31).addBox(-1.0F, -3.0F, -0.8F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 0.0F, 0.0F));
		PartDefinition latch3 = lid.addOrReplaceChild("latch3", CubeListBuilder.create().texOffs(0, 25).addBox(-1.0F, -2.0F, -8.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}
	
	/**
	 * 
	 */
	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, AbstractTreasureChestBlockEntity blockEntity) {
		latch2.visible = false;
		latch3.visible = false;
		
		for (LockState state : blockEntity.getLockStates()) {
			if (state.getLock() != null) {
				switch(state.getSlot().getIndex()) {
					case 1:
						latch3.visible = true;
						break;
					case 2:	
						latch2.visible = true;
						break;
				}
			}
		}		
		base.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		lid.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart getLid() {
		return lid;
	}
}