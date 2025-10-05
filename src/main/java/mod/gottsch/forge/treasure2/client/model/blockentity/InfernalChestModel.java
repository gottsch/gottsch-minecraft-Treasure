/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.client.model.blockentity;// Made with Blockbench 4.12.6

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

/**
 *
 * @author Mark Gottschling on Sept 27, 2025
 *
 */
public class InfernalChestModel extends AbstractTreasureChestModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Treasure.MODID, "infernal_chest"), "main");

	private final ModelPart chest;
	private final ModelPart trunk;
	private final ModelPart leftFrontStrutJoint1;
	private final ModelPart leftFrontStrutJoint2;
	private final ModelPart leftBackStrutJoint1;
	private final ModelPart leftBackStrutJoint2;
	private final ModelPart rightFrontStrutJoint1;
	private final ModelPart rightFrontStrutJoint2;
	private final ModelPart rightBackStrutJoint1;
	private final ModelPart rightBackStrutJoint2;
	private final ModelPart bottom;
	private final ModelPart feet;
	private final ModelPart lid;
	private final ModelPart leftLid;
	private final ModelPart leftFrontLidStruts;
	private final ModelPart leftFrontLidStrut;
	private final ModelPart leftBackLidStruts;
	private final ModelPart leftBackTopLidStrut;
	private final ModelPart rightLid;
	private final ModelPart rightBackLidStruts;
	private final ModelPart rightBackTopLidStrut;
	private final ModelPart rightFrontLidStruts;
	private final ModelPart rightFrontLidStrut;
	private final ModelPart centerLid;
	private final ModelPart frontLidPanels;
	private final ModelPart frontTopPanel;
	private final ModelPart backLidPanels;
	private final ModelPart backTopPanel;
	private final ModelPart bottomLid;
	private final ModelPart leftWing;
	private final ModelPart leftWingAxis;
	private final ModelPart leftWingClaw;
	private final ModelPart leftWingMedius;
	private final ModelPart rightWing;
	private final ModelPart rightWingAxis;
	private final ModelPart rightWingClaw;
	private final ModelPart rightWingMedius;

	public InfernalChestModel(ModelPart root) {
		super(root, RenderType::entityCutout);

		this.chest = root.getChild("chest");
		this.trunk = this.chest.getChild("trunk");
		this.leftFrontStrutJoint1 = this.trunk.getChild("leftFrontStrutJoint1");
		this.leftFrontStrutJoint2 = this.leftFrontStrutJoint1.getChild("leftFrontStrutJoint2");
		this.leftBackStrutJoint1 = this.trunk.getChild("leftBackStrutJoint1");
		this.leftBackStrutJoint2 = this.leftBackStrutJoint1.getChild("leftBackStrutJoint2");
		this.rightFrontStrutJoint1 = this.trunk.getChild("rightFrontStrutJoint1");
		this.rightFrontStrutJoint2 = this.rightFrontStrutJoint1.getChild("rightFrontStrutJoint2");
		this.rightBackStrutJoint1 = this.trunk.getChild("rightBackStrutJoint1");
		this.rightBackStrutJoint2 = this.rightBackStrutJoint1.getChild("rightBackStrutJoint2");
		this.bottom = this.trunk.getChild("bottom");
		this.feet = this.trunk.getChild("feet");
		this.lid = this.chest.getChild("lid");
		this.leftLid = this.lid.getChild("leftLid");
		this.leftFrontLidStruts = this.leftLid.getChild("leftFrontLidStruts");
		this.leftFrontLidStrut = this.leftFrontLidStruts.getChild("leftFrontLidStrut");
		this.leftBackLidStruts = this.leftLid.getChild("leftBackLidStruts");
		this.leftBackTopLidStrut = this.leftBackLidStruts.getChild("leftBackTopLidStrut");
		this.rightLid = this.lid.getChild("rightLid");
		this.rightBackLidStruts = this.rightLid.getChild("rightBackLidStruts");
		this.rightBackTopLidStrut = this.rightBackLidStruts.getChild("rightBackTopLidStrut");
		this.rightFrontLidStruts = this.rightLid.getChild("rightFrontLidStruts");
		this.rightFrontLidStrut = this.rightFrontLidStruts.getChild("rightFrontLidStrut");
		this.centerLid = this.lid.getChild("centerLid");
		this.frontLidPanels = this.centerLid.getChild("frontLidPanels");
		this.frontTopPanel = this.frontLidPanels.getChild("frontTopPanel");
		this.backLidPanels = this.centerLid.getChild("backLidPanels");
		this.backTopPanel = this.backLidPanels.getChild("backTopPanel");
		this.bottomLid = this.lid.getChild("bottomLid");
		this.leftWing = this.chest.getChild("leftWing");
		this.leftWingAxis = this.leftWing.getChild("leftWingFlapper");
		this.leftWingClaw = this.leftWingAxis.getChild("leftWingClaw");
		this.leftWingMedius = this.leftWingAxis.getChild("leftWingMedius");
		this.rightWing = this.chest.getChild("rightWing");
		this.rightWingAxis = this.rightWing.getChild("rightWingFlapper");
		this.rightWingClaw = this.rightWingAxis.getChild("rightWingClaw");
		this.rightWingMedius = this.rightWingAxis.getChild("rightWingMedius");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition chest = partdefinition.addOrReplaceChild("chest", CubeListBuilder.create(), PartPose.offset(0.0F, 24.5F, 0.0F));

		PartDefinition trunk = chest.addOrReplaceChild("trunk", CubeListBuilder.create().texOffs(0, 38).addBox(-7.5F, -7.6F, -7.5F, 15.0F, 5.0F, 15.0F, new CubeDeformation(0.0F))
				.texOffs(0, 68).addBox(-2.0F, -7.5F, -7.8F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(61, 56).addBox(3.5F, -5.5F, -8.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(-4.5F, -5.5F, -8.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(3.5F, -5.5F, 7.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(-4.5F, -5.5F, 7.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(20, 68).addBox(3.0F, -7.5F, -7.7F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(20, 68).mirror().addBox(-5.0F, -7.5F, -7.7F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(20, 68).mirror().addBox(3.0F, -7.5F, 6.7F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(20, 68).addBox(-5.0F, -7.5F, 6.7F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftFrontStrutJoint1 = trunk.addOrReplaceChild("leftFrontStrutJoint1", CubeListBuilder.create(), PartPose.offsetAndRotation(6.65F, -2.6F, -6.55F, 0.0873F, 0.0F, 0.0F));

		PartDefinition leftFrontStrutJoint2 = leftFrontStrutJoint1.addOrReplaceChild("leftFrontStrutJoint2", CubeListBuilder.create().texOffs(61, 48).addBox(-1.0F, -5.0F, 0.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition leftBackStrutJoint1 = trunk.addOrReplaceChild("leftBackStrutJoint1", CubeListBuilder.create(), PartPose.offsetAndRotation(6.65F, -2.6F, 7.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition leftBackStrutJoint2 = leftBackStrutJoint1.addOrReplaceChild("leftBackStrutJoint2", CubeListBuilder.create().texOffs(61, 48).mirror().addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -0.25F, -0.0873F, 0.0F, 0.0F));

		PartDefinition rightFrontStrutJoint1 = trunk.addOrReplaceChild("rightFrontStrutJoint1", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.65F, -2.75F, -6.55F, 0.0873F, 0.0F, 0.0F));

		PartDefinition rightFrontStrutJoint2 = rightFrontStrutJoint1.addOrReplaceChild("rightFrontStrutJoint2", CubeListBuilder.create().texOffs(61, 48).mirror().addBox(-1.35F, -4.85F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.35F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

		PartDefinition rightBackStrutJoint1 = trunk.addOrReplaceChild("rightBackStrutJoint1", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.65F, -2.6F, 6.65F, -0.0873F, 0.0F, 0.0F));

		PartDefinition rightBackStrutJoint2 = rightBackStrutJoint1.addOrReplaceChild("rightBackStrutJoint2", CubeListBuilder.create().texOffs(61, 48).addBox(-1.35F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.35F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

		PartDefinition bottom = trunk.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(61, 56).addBox(6.5F, -2.0F, -8.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(7.5F, -2.0F, -7.3F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(-7.5F, -2.0F, -8.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(-8.5F, -2.0F, -7.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(-8.5F, -2.0F, 6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(-7.5F, -2.0F, 7.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(6.5F, -2.0F, 7.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(7.5F, -2.0F, 6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(0, 19).addBox(-8.0F, -2.5F, -8.0F, 16.0F, 2.0F, 16.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition feet = trunk.addOrReplaceChild("feet", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lid = chest.addOrReplaceChild("lid", CubeListBuilder.create(), PartPose.offset(0.0F, -7.5F, 8.0F));

		PartDefinition leftLid = lid.addOrReplaceChild("leftLid", CubeListBuilder.create().texOffs(43, 59).addBox(0.5F, -3.0F, 2.0F, 0.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(65, 0).addBox(0.5F, -5.0F, 4.0F, 0.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(65, 12).addBox(-1.0F, -6.45F, 6.5F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(27, 68).addBox(-1.0F, -6.45F, 4.4F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F))
				.texOffs(27, 68).mirror().addBox(-1.0F, -6.45F, 9.6F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F)).mirror(false)
				.texOffs(61, 56).addBox(-0.5F, -7.1F, 5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(-0.5F, -7.1F, 10.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(0.7F, -6.1F, 5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(0.7F, -6.1F, 10.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offset(7.0F, -2.0F, -16.0F));

		PartDefinition leftFrontLidStruts = leftLid.addOrReplaceChild("leftFrontLidStruts", CubeListBuilder.create().texOffs(65, 18).addBox(-1.01F, -4.0F, 0.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.4363F, 0.0F, 0.0F));

		PartDefinition leftFrontLidStrut = leftFrontLidStruts.addOrReplaceChild("leftFrontLidStrut", CubeListBuilder.create().texOffs(65, 25).addBox(-1.01F, -4.0F, 0.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition leftBackLidStruts = leftLid.addOrReplaceChild("leftBackLidStruts", CubeListBuilder.create().texOffs(65, 18).mirror().addBox(-1.01F, -3.5F, -2.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.005F)).mirror(false), PartPose.offsetAndRotation(0.0F, -0.5F, 16.0F, 0.4363F, 0.0F, 0.0F));

		PartDefinition leftBackTopLidStrut = leftBackLidStruts.addOrReplaceChild("leftBackTopLidStrut", CubeListBuilder.create().texOffs(65, 25).mirror().addBox(-1.01F, -4.0F, -2.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -3.5F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition rightLid = lid.addOrReplaceChild("rightLid", CubeListBuilder.create().texOffs(65, 12).mirror().addBox(-15.0F, -5.95F, 3.5F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.05F)).mirror(false)
				.texOffs(43, 59).addBox(-14.5F, -2.5F, -1.0F, 0.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(65, 0).addBox(-14.5F, -4.5F, 1.0F, 0.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(27, 68).mirror().addBox(-15.0F, -5.95F, 1.4F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F)).mirror(false)
				.texOffs(61, 56).addBox(-15.7F, -5.6F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(-15.7F, -5.6F, 7.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(-14.5F, -6.6F, 7.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(-14.5F, -6.6F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(27, 68).addBox(-15.0F, -5.95F, 6.6F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.offset(7.0F, -2.5F, -13.0F));

		PartDefinition rightBackLidStruts = rightLid.addOrReplaceChild("rightBackLidStruts", CubeListBuilder.create().texOffs(65, 18).addBox(-1.01F, -3.5F, -2.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(-14.0F, 0.0F, 13.0F, 0.4363F, 0.0F, 0.0F));

		PartDefinition rightBackTopLidStrut = rightBackLidStruts.addOrReplaceChild("rightBackTopLidStrut", CubeListBuilder.create().texOffs(65, 25).addBox(-1.01F, -4.0F, -2.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.5F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition rightFrontLidStruts = rightLid.addOrReplaceChild("rightFrontLidStruts", CubeListBuilder.create().texOffs(65, 18).mirror().addBox(-1.01F, -4.0F, 0.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.005F)).mirror(false), PartPose.offsetAndRotation(-14.0F, 0.5F, -3.0F, -0.4363F, 0.0F, 0.0F));

		PartDefinition rightFrontLidStrut = rightFrontLidStruts.addOrReplaceChild("rightFrontLidStrut", CubeListBuilder.create().texOffs(65, 25).mirror().addBox(-1.01F, -4.0F, 0.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition centerLid = lid.addOrReplaceChild("centerLid", CubeListBuilder.create().texOffs(0, 59).addBox(-7.0F, -16.7F, -3.5F, 14.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(61, 56).addBox(3.5F, -17.2F, -0.6F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(-4.5F, -17.2F, -0.6F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, 8.5F, -8.0F));

		PartDefinition frontLidPanels = centerLid.addOrReplaceChild("frontLidPanels", CubeListBuilder.create().texOffs(61, 38).addBox(-7.0F, -4.0F, 0.5F, 14.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(36, 68).addBox(3.0F, -4.0F, 0.3F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(36, 68).mirror().addBox(-5.0F, -4.0F, 0.3F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(61, 56).addBox(3.5F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(-4.5F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, -10.5F, -8.0F, -0.4363F, 0.0F, 0.0F));

		PartDefinition frontTopPanel = frontLidPanels.addOrReplaceChild("frontTopPanel", CubeListBuilder.create().texOffs(68, 60).addBox(3.0F, -3.9F, -0.2F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(68, 60).mirror().addBox(-5.0F, -3.9F, -0.2F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(61, 43).addBox(-7.0F, -4.0F, 0.0F, 14.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.5F, -0.3927F, 0.0F, 0.0F));

		PartDefinition backLidPanels = centerLid.addOrReplaceChild("backLidPanels", CubeListBuilder.create().texOffs(61, 38).addBox(-7.0F, -4.0F, -0.5F, 14.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(36, 68).mirror().addBox(3.0F, -4.0F, -1.3F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(36, 68).addBox(-5.0F, -4.0F, -1.3F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(61, 56).addBox(3.5F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(-4.5F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, -10.5F, 8.0F, 0.4363F, 0.0F, 0.0F));

		PartDefinition backTopPanel = backLidPanels.addOrReplaceChild("backTopPanel", CubeListBuilder.create().texOffs(61, 43).addBox(-7.0F, -4.0F, 0.0F, 14.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(68, 60).mirror().addBox(3.0F, -3.9F, -0.8F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(68, 60).addBox(-5.0F, -3.9F, -0.8F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, -0.5F, 0.3927F, 0.0F, 0.0F));

		PartDefinition bottomLid = lid.addOrReplaceChild("bottomLid", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -10.5F, -8.0F, 16.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(11, 68).addBox(6.0F, -10.5F, -7.9F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F))
				.texOffs(51, 26).addBox(-1.5F, -11.5F, -8.1F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(11, 68).mirror().addBox(-8.0F, -10.5F, -7.9F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F)).mirror(false)
				.texOffs(11, 68).addBox(-8.0F, -10.5F, 6.1F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F))
				.texOffs(61, 56).addBox(6.5F, -10.0F, -8.6F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(6.5F, -10.0F, 7.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(-7.5F, -10.0F, -8.6F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(-7.5F, -10.0F, 7.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(7.7F, -10.0F, -7.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(7.7F, -10.0F, 6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(-8.7F, -10.0F, -7.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(61, 56).addBox(-8.7F, -10.0F, 6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
				.texOffs(11, 68).mirror().addBox(6.0F, -10.5F, 6.1F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F)).mirror(false), PartPose.offset(0.0F, 8.5F, -8.0F));

		PartDefinition leftWing = chest.addOrReplaceChild("leftWing", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, -9.5F, 9.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition leftWingFlapper = leftWing.addOrReplaceChild("leftWingFlapper", CubeListBuilder.create().texOffs(32, 101).addBox(0.0F, -0.5F, 0.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.02F))
				.texOffs(1, 76).mirror().addBox(0.0F, -0.5F, 0.5F, 12.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(2, 90).addBox(11.0F, 0.5F, 0.0F, 1.0F, 11.0F, 1.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, 0.2182F, 0.0F));

		PartDefinition leftWingClaw = leftWingFlapper.addOrReplaceChild("leftWingClaw", CubeListBuilder.create().texOffs(33, 75).addBox(-2.5F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.5F, 0.0F, 0.5F, 0.0F, -0.7854F, 0.0F));

		PartDefinition leftWingMedius = leftWingFlapper.addOrReplaceChild("leftWingMedius", CubeListBuilder.create().texOffs(0, 63).addBox(0.0F, -0.5F, -11.5F, 0.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(8, 91).addBox(-0.5F, -0.5F, -11.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(11.5F, 0.0F, 0.5F, 0.0F, -0.3054F, 0.0F));

		PartDefinition rightWing = chest.addOrReplaceChild("rightWing", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.0F, -9.5F, 9.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition rightWingFlapper = rightWing.addOrReplaceChild("rightWingFlapper", CubeListBuilder.create().texOffs(32, 101).addBox(-12.0F, -0.5F, 0.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.02F))
				.texOffs(1, 76).addBox(-12.0F, -0.5F, 0.5F, 12.0F, 12.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(2, 91).addBox(-12.0F, 0.5F, 0.0F, 1.0F, 11.0F, 1.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, -0.2182F, 0.0F));

		PartDefinition rightWingClaw = rightWingFlapper.addOrReplaceChild("rightWingClaw", CubeListBuilder.create().texOffs(33, 75).mirror().addBox(-0.5F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-11.5F, 0.0F, 0.5F, 0.0F, 0.7854F, 0.0F));

		PartDefinition rightWingMedius = rightWingFlapper.addOrReplaceChild("rightWingMedius", CubeListBuilder.create().texOffs(1, 63).addBox(0.0F, -0.5F, -11.5F, 0.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(8, 91).addBox(-0.5F, -0.5F, -11.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(-11.5F, 0.0F, 0.5F, 0.0F, 0.3054F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public ModelPart getLid() {
		return lid;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, AbstractTreasureChestBlockEntity blockEntity) {
		chest.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public ModelPart getRightWing() {
		return rightWing;
	}

	public ModelPart getLeftWing() {
		return leftWing;
	}
}