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
import mod.gottsch.forge.treasure2.core.block.entity.CrateChestBlockEntity;
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
 * @author Mark Gottschling on Feb 3, 2018
 *
 */
public class CrateChestModel extends AbstractTreasureChestModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Treasure.MODID, "crate_chest"), "main");
	public static final ModelLayerLocation MOLDY_LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Treasure.MODID, "moldy_crate_chest"), "main");
		
	private final ModelPart lid;
	private final ModelPart main;
	private final ModelPart latch1;
	
	/**
	 * 
	 */
	public CrateChestModel(ModelPart root) {
		super(root);
		this.lid = root.getChild("lid");
		this.main = root.getChild("main");
		this.latch1 = lid.getChild("latch1");
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition lid = partdefinition.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 17).addBox(-1.0F, 0.0F, -13.0F, 14.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 9.0F, 6.0F));

		PartDefinition padTop = lid.addOrReplaceChild("padTop", CubeListBuilder.create().texOffs(43, 34).addBox(4.0F, -0.2F, -13.2F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition latch1 = lid.addOrReplaceChild("latch1", CubeListBuilder.create().texOffs(0, 20).addBox(5.0F, 0.0F, -14.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 34).addBox(-7.0F, -11.0F, -14.0F, 14.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(45, 5).addBox(-6.0F, -10.0F, -13.0F, 12.0F, 10.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.0F, 0.0F, -14.0F, 14.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-7.0F, -10.0F, -14.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(5.0F, -10.0F, -14.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.0F, -10.0F, -2.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(5.0F, -10.0F, -2.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 22.0F, 7.0F));

		PartDefinition pad = main.addOrReplaceChild("pad", CubeListBuilder.create().texOffs(0, 17).addBox(-2.0F, -0.2F, -1.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, -13.2F));

		PartDefinition crossWest = main.addOrReplaceChild("crossWest", CubeListBuilder.create().texOffs(0, 50).addBox(0.0F, 0.0F, 0.0F, 1.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, -10.5F, -2.5F, -0.8029F, 0.0F, 0.0F));

		PartDefinition crossEast = main.addOrReplaceChild("crossEast", CubeListBuilder.create().texOffs(0, 50).addBox(0.0F, 0.0F, 0.0F, 1.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.5F, -9.5F, -13.5F, 0.8029F, 0.0F, 0.0F));

		PartDefinition crossSouth = main.addOrReplaceChild("crossSouth", CubeListBuilder.create().texOffs(7, 50).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 15.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -10.5F, 0.5F, 0.0F, 0.0F, 0.8029F));

		PartDefinition cross = main.addOrReplaceChild("cross", CubeListBuilder.create().texOffs(7, 50).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 15.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -10.5F, -11.5F, 0.0F, 0.0F, -0.8029F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}
	
	@Override
	public void updateModelLidRotation(AbstractTreasureChestBlockEntity tileEntity, float partialTicks) {
    	CrateChestBlockEntity cte = (CrateChestBlockEntity) tileEntity;
        float latchRotation = cte.prevLatchAngle + (cte.latchAngle - cte.prevLatchAngle) * partialTicks;
        latchRotation = 1.0F - latchRotation;
        latchRotation = 1.0F - latchRotation * latchRotation * latchRotation;
        getLatch1().xRot = -(latchRotation * (float)Math.PI / getAngleModifier());        	
        
        float lidRotation = cte.prevLidAngle + (cte.lidAngle - cte.prevLidAngle) * partialTicks;
        lidRotation = 1.0F - lidRotation;
        lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
        getLid().yRot = -(lidRotation * (float)Math.PI / getAngleModifier());
	}
	
	/**
	 * 
	 */
	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, AbstractTreasureChestBlockEntity blockEntity) {
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		lid.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);

	}
	
	/**
	 * @return the lid
	 */
	@Override
	public ModelPart getLid() {
		return lid;
	}

	/**
	 * @return the base
	 */
	public ModelPart getBase() {
		return main;
	}


	/**
	 * @return the latch1
	 */
	public ModelPart getLatch1() {
		return latch1;
	}
}
