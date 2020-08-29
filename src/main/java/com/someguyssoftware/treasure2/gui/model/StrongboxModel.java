package com.someguyssoftware.treasure2.gui.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;

public class StrongboxModel extends AbstractTreasureChestModel {
	private ModelRenderer box;
	private ModelRenderer lid;
	private ModelRenderer foot1;
	private ModelRenderer foot2;
	private ModelRenderer foot3;
	private ModelRenderer foot4;
	private ModelRenderer pad;
	private ModelRenderer latch;
	private ModelRenderer pad2;

	public StrongboxModel() {
		super(RenderType::getEntityCutout);
		textureWidth = 64;
		textureHeight = 32;

		box = new ModelRenderer(this, 0, 12);
		box.addBox(-5F, 0F, -8F, 10, 5, 8);
		box.setRotationPoint(0F, 18.5F, 4F);
		box.setTextureSize(64, 32);
		box.mirror = true;
		setRotation(box, 0F, 0F, 0F);
		lid = new ModelRenderer(this, 0, 0);
		lid.addBox(-5F, -3F, -8F, 10, 3, 8);
		lid.setRotationPoint(0F, 19.5F, 4F);
		lid.setTextureSize(64, 32);
		lid.mirror = true;
		setRotation(lid, 0F, 0F, 0F);
		foot1 = new ModelRenderer(this, 0, 26);
		foot1.addBox(0F, 0F, 0F, 2, 1, 1);
		foot1.setRotationPoint(3F, 23F, -4F);
		foot1.setTextureSize(64, 32);
		foot1.mirror = true;
		setRotation(foot1, 0F, 0F, 0F);
		foot2 = new ModelRenderer(this, 7, 26);
		foot2.addBox(0F, 0F, 0F, 2, 1, 1);
		foot2.setRotationPoint(-5F, 23F, -4F);
		foot2.setTextureSize(64, 32);
		foot2.mirror = true;
		setRotation(foot2, 0F, 0F, 0F);
		foot3 = new ModelRenderer(this, 14, 26);
		foot3.addBox(0F, 0F, 0F, 2, 1, 1);
		foot3.setRotationPoint(-5F, 23F, 3F);
		foot3.setTextureSize(64, 32);
		foot3.mirror = true;
		setRotation(foot3, 0F, 0F, 0F);
		foot4 = new ModelRenderer(this, 21, 26);
		foot4.addBox(0F, 0F, 0F, 2, 1, 1);
		foot4.setRotationPoint(3F, 23F, 3F);
		foot4.setTextureSize(64, 32);
		foot4.mirror = true;
		setRotation(foot4, 0F, 0F, 0F);
		pad = new ModelRenderer(this, 37, 0);
		pad.addBox(-2F, -2F, -8.5F, 4, 2, 1);
		pad.setRotationPoint(0F, 19.5F, 4F);
		pad.setTextureSize(64, 32);
		pad.mirror = true;
		setRotation(pad, 0F, 0F, 0F);
		latch = new ModelRenderer(this, 37, 11);
		latch.addBox(-1F, -1.5F, -9F, 2, 2, 1);
		latch.setRotationPoint(0F, 19.5F, 4F);
		latch.setTextureSize(64, 32);
		latch.mirror = true;
		setRotation(latch, 0F, 0F, 0F);
		pad2 = new ModelRenderer(this, 37, 4);
		pad2.addBox(-2F, 0F, -3F, 4, 3, 1);
		pad2.setRotationPoint(0F, 18.5F, -1.5F);
		pad2.setTextureSize(64, 32);
		pad2.mirror = true;
		setRotation(pad2, 0F, 0F, 0F);
	}

	/**
	 * 
	 */
	@Override
	public void renderAll(MatrixStack matrixStack, IVertexBuilder renderBuffer, int combinedLight, int combinedOverlay, AbstractTreasureChestTileEntity te) {
		latch.rotateAngleX = lid.rotateAngleX;
		pad.rotateAngleX = lid.rotateAngleX;

		box.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		lid.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		foot1.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		foot2.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		foot3.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		foot4.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		latch.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		pad.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		pad2.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
	}

	@Override
	public ModelRenderer getLid() {
		return lid;
	}
}
