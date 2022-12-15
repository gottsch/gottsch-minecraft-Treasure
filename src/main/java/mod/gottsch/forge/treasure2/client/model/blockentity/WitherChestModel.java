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
 * @author Mark Gottschling on Jun 17, 2018
 *
 */
public class WitherChestModel extends AbstractTreasureChestModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Treasure.MODID, "wither_chest"), "main");
	private final ModelPart westDoor;
	private final ModelPart eastDoor;
	private final ModelPart main;

	public WitherChestModel(ModelPart root) {
		super(root);
		this.westDoor = root.getChild("westDoor");
		this.eastDoor = root.getChild("eastDoor");
		this.main = root.getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition westDoor = partdefinition.addOrReplaceChild("westDoor", CubeListBuilder.create(), PartPose.offset(7.0F, 11.0F, 3.0F));
		PartDefinition westFront = westDoor.addOrReplaceChild("westFront", CubeListBuilder.create().texOffs(23, 88).mirror().addBox(-7.0F, 0.0F, -10.0F, 7.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -9.0F, 0.0F));
		PartDefinition westSide = westDoor.addOrReplaceChild("westSide", CubeListBuilder.create().texOffs(21, 62).mirror().addBox(-4.0F, 0.0F, -6.0F, 4.0F, 18.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -9.0F, 0.0F));
		PartDefinition eastDoor = partdefinition.addOrReplaceChild("eastDoor", CubeListBuilder.create(), PartPose.offset(-7.0F, 11.0F, 3.0F));
		PartDefinition eastSide = eastDoor.addOrReplaceChild("eastSide", CubeListBuilder.create().texOffs(0, 62).mirror().addBox(0.0F, 0.0F, -6.0F, 4.0F, 18.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -9.0F, 0.0F));
		PartDefinition eastFront = eastDoor.addOrReplaceChild("eastFront", CubeListBuilder.create().texOffs(0, 88).mirror().addBox(0.0F, 0.0F, -10.0F, 7.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -9.0F, 0.0F));
		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition root2 = main.addOrReplaceChild("root2", CubeListBuilder.create().texOffs(47, 94).mirror().addBox(1.0F, 0.0F, 0.0F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, -2.0F, -8.0F));
		PartDefinition root1 = main.addOrReplaceChild("root1", CubeListBuilder.create().texOffs(47, 88).mirror().addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(7.0F, -1.0F, -4.0F));
		PartDefinition sideBark = main.addOrReplaceChild("sideBark", CubeListBuilder.create().texOffs(32, 112).mirror().addBox(-12.0F, 0.0F, 0.0F, 2.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, -29.0F, -3.0F));
		PartDefinition backBark2 = main.addOrReplaceChild("backBark2", CubeListBuilder.create().texOffs(19, 112).mirror().addBox(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, -30.0F, 5.0F));
		PartDefinition backBark = main.addOrReplaceChild("backBark", CubeListBuilder.create().texOffs(0, 112).mirror().addBox(-11.0F, 0.0F, -2.0F, 7.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(4.0F, -32.0F, 7.0F));
		PartDefinition top = main.addOrReplaceChild("top", CubeListBuilder.create().texOffs(0, 42).mirror().addBox(-7.0F, 0.0F, -14.0F, 14.0F, 4.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -26.0F, 7.0F));
		PartDefinition back = main.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, 19).mirror().addBox(-7.0F, 0.0F, -4.0F, 14.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -22.0F, 7.0F));
		PartDefinition bottom = main.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-7.0F, 0.0F, -14.0F, 14.0F, 4.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -4.0F, 7.0F));

		return LayerDefinition.create(meshdefinition, 64, 128);
	}
	
	@Override
	public void updateModelLidRotation(AbstractTreasureChestBlockEntity tileEntity, float partialTicks) {
		float lidRotation = tileEntity.prevLidAngle + (tileEntity.lidAngle - tileEntity.prevLidAngle) * partialTicks;
        lidRotation = 1.0F - lidRotation;
        lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
        getLid().yRot = -(lidRotation * (float)Math.PI / getAngleModifier());
        getLid2().yRot = -getLid().yRot;
	}
	
	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, AbstractTreasureChestBlockEntity blockEntity) {
		westDoor.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		eastDoor.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	@Override
	public ModelPart getLid() {
		return this.westDoor;
	}

	public ModelPart getLid2() {
		return this.eastDoor;
	}
}