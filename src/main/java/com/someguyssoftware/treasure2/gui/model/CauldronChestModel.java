package com.someguyssoftware.treasure2.gui.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;

public class CauldronChestModel extends AbstractTreasureChestModel {
	// fields
	ModelRenderer front;
	ModelRenderer back;
	ModelRenderer left;
	ModelRenderer right;
	ModelRenderer bottom;
	ModelRenderer flFoot;
	ModelRenderer frFoot;
	ModelRenderer blFoot;
	ModelRenderer brFoot;
	ModelRenderer fl2;
	ModelRenderer fr2;
	ModelRenderer bl2;
	ModelRenderer br2;
	ModelRenderer lidLeft;
	ModelRenderer lidRight;

	public CauldronChestModel() {
		super(RenderType::getEntityCutout);

		textureWidth = 128;
		textureHeight = 128;

		front = new ModelRenderer(this, 0, 0);
		front.addBox(0F, 0F, 0F, 12, 13, 2);
		front.setRotationPoint(-6F, 8F, -8F);
		front.setTextureSize(64, 32);
		front.mirror = true;
		setRotation(front, 0F, 0F, 0F);
		back = new ModelRenderer(this, 32, 0);
		back.addBox(0F, 0F, 0F, 12, 13, 2);
		back.setRotationPoint(-6F, 8F, 6F);
		back.setTextureSize(64, 32);
		back.mirror = true;
		setRotation(back, 0F, 0F, 0F);
		left = new ModelRenderer(this, 0, 18);
		left.addBox(0F, 0F, 0F, 2, 13, 16);
		left.setRotationPoint(6F, 8F, -8F);
		left.setTextureSize(64, 32);
		left.mirror = true;
		setRotation(left, 0F, 0F, 0F);
		right = new ModelRenderer(this, 38, 18);
		right.addBox(0F, 0F, 0F, 2, 13, 16);
		right.setRotationPoint(-8F, 8F, -8F);
		right.setTextureSize(64, 32);
		right.mirror = true;
		setRotation(right, 0F, 0F, 0F);
		bottom = new ModelRenderer(this, 0, 50);
		bottom.addBox(0F, 0F, 0F, 12, 1, 12);
		bottom.setRotationPoint(-6F, 20F, -6F);
		bottom.setTextureSize(64, 32);
		bottom.mirror = true;
		setRotation(bottom, 0F, 0F, 0F);
		flFoot = new ModelRenderer(this, 0, 80);
		flFoot.addBox(0F, 0F, 0F, 4, 3, 2);
		flFoot.setRotationPoint(4F, 21F, -8F);
		flFoot.setTextureSize(64, 32);
		flFoot.mirror = true;
		setRotation(flFoot, 0F, 0F, 0F);
		frFoot = new ModelRenderer(this, 14, 80);
		frFoot.addBox(0F, 0F, 0F, 4, 3, 2);
		frFoot.setRotationPoint(-8F, 21F, -8F);
		frFoot.setTextureSize(64, 32);
		frFoot.mirror = true;
		setRotation(frFoot, 0F, 0F, 0F);
		blFoot = new ModelRenderer(this, 28, 80);
		blFoot.addBox(0F, 0F, 0F, 4, 3, 2);
		blFoot.setRotationPoint(4F, 21F, 6F);
		blFoot.setTextureSize(64, 32);
		blFoot.mirror = true;
		setRotation(blFoot, 0F, 0F, 0F);
		brFoot = new ModelRenderer(this, 42, 80);
		brFoot.addBox(0F, 0F, 0F, 4, 3, 2);
		brFoot.setRotationPoint(-8F, 21F, 6F);
		brFoot.setTextureSize(64, 32);
		brFoot.mirror = true;
		setRotation(brFoot, 0F, 0F, 0F);
		fl2 = new ModelRenderer(this, 0, 88);
		fl2.addBox(0F, 0F, 0F, 2, 3, 2);
		fl2.setRotationPoint(6F, 21F, -6F);
		fl2.setTextureSize(64, 32);
		fl2.mirror = true;
		setRotation(fl2, 0F, 0F, 0F);
		fr2 = new ModelRenderer(this, 10, 88);
		fr2.addBox(0F, 0F, 0F, 2, 3, 2);
		fr2.setRotationPoint(-8F, 21F, -6F);
		fr2.setTextureSize(64, 32);
		fr2.mirror = true;
		setRotation(fr2, 0F, 0F, 0F);
		bl2 = new ModelRenderer(this, 20, 88);
		bl2.addBox(0F, 0F, 0F, 2, 3, 2);
		bl2.setRotationPoint(6F, 21F, 4F);
		bl2.setTextureSize(64, 32);
		bl2.mirror = true;
		setRotation(bl2, 0F, 0F, 0F);
		br2 = new ModelRenderer(this, 30, 88);
		br2.addBox(0F, 0F, 0F, 2, 3, 2);
		br2.setRotationPoint(-8F, 21F, 4F);
		br2.setTextureSize(64, 32);
		br2.mirror = true;
		setRotation(br2, 0F, 0F, 0F);
		lidLeft = new ModelRenderer(this, 0, 65);
		lidLeft.addBox(-6F, 0F, 0F, 6, 1, 12);
		lidLeft.setRotationPoint(6F, 9F, -6F);
		lidLeft.setTextureSize(64, 32);
		lidLeft.mirror = true;
		setRotation(lidLeft, 0F, 0F, 0F);
		lidRight = new ModelRenderer(this, 38, 65);
		lidRight.addBox(0F, 0F, 0F, 6, 1, 12);
		lidRight.setRotationPoint(-6F, 9F, -6F);
		lidRight.setTextureSize(64, 32);
		lidRight.mirror = true;
		setRotation(lidRight, 0F, 0F, 0F);
	}

	/**
	 * 
	 */
	@Override
	public void renderAll(MatrixStack matrixStack, IVertexBuilder renderBuffer, int combinedLight, int combinedOverlay, AbstractTreasureChestTileEntity te) {
		// set the angles of the latch to same as the lib
		lidRight.rotateAngleZ = -lidLeft.rotateAngleZ;

		front.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		back.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		left.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		right.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		bottom.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		lidLeft.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		lidRight.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		flFoot.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		fl2.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		frFoot.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		fr2.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		blFoot.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		bl2.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		brFoot.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		br2.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);		
	}

	@Override
	public ModelRenderer getLid() {
		return lidLeft;
	}

}