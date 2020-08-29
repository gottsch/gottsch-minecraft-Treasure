package com.someguyssoftware.treasure2.gui.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * 
 * @author Mark Gottschling on Nov 28, 2018
 *
 */
public class SkullChestModel extends AbstractTreasureChestModel {
	// fields
	ModelRenderer top;
	ModelRenderer head;
	ModelRenderer jaw;
	ModelRenderer jawBottom;

	/**
	 * 
	 */
	public SkullChestModel() {
		super(RenderType::getEntityCutout);
		textureWidth = 64;
		textureHeight = 64;

		top = new ModelRenderer(this, 0, 0);
		top.addBox(-3F, -6F, -5F, 6, 1, 6);
		top.setRotationPoint(0F, 21F, 2F);
		top.setTextureSize(64, 64);
		top.mirror = true;
		setRotation(top, 0F, 0F, 0F);
		head = new ModelRenderer(this, 0, 8);
		head.addBox(-4F, -5F, -6F, 8, 5, 8);
		head.setRotationPoint(0F, 21F, 2F);
		head.setTextureSize(64, 64);
		head.mirror = true;
		setRotation(head, 0F, 0F, 0F);
		jaw = new ModelRenderer(this, 0, 22);
		jaw.addBox(-3F, 0F, -6F, 6, 1, 6);
		jaw.setRotationPoint(0F, 21F, 2F);
		jaw.setTextureSize(64, 64);
		jaw.mirror = true;
		setRotation(jaw, 0F, 0F, 0F);
		jawBottom = new ModelRenderer(this, 0, 30);
		jawBottom.addBox(-3F, 0F, -5F, 6, 2, 5);
		jawBottom.setRotationPoint(0F, 22F, 1F);
		jawBottom.setTextureSize(64, 64);
		jawBottom.mirror = true;
		setRotation(jawBottom, 0F, 0F, 0F);
	}

	/**
	 * 
	 */
	@Override
	public void renderAll(MatrixStack matrixStack, IVertexBuilder renderBuffer, int combinedLight, int combinedOverlay, AbstractTreasureChestTileEntity te) {
		head.rotateAngleX = top.rotateAngleX;
		jaw.rotateAngleX = top.rotateAngleX;
		
		// render all the parts
		top.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		head.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		jaw.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		jawBottom.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
	}

	@Override
	public ModelRenderer getLid() {
		return top;
	}

	public void setLid(ModelRenderer lid) {
		this.top = lid;
	}
}
