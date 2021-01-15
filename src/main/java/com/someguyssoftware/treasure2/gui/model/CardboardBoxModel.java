package com.someguyssoftware.treasure2.gui.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.ITreasureChestTileEntity;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * 
 * @author Mark Gottschling on Nov 29, 2020
 *
 */
public class CardboardBoxModel extends AbstractTreasureChestModel {
	private final ModelRenderer base;
	private final ModelRenderer rightFlap;
	private final ModelRenderer leftFlap;
	private final ModelRenderer southFlap;
	private final ModelRenderer northFlap;

	public CardboardBoxModel() {
		super(RenderType::getEntityCutout);
		
		textureWidth = 128;
		textureHeight = 128;

		base = new ModelRenderer(this);
		base.setRotationPoint(7.0F, 10.0F, -7.0F);
		base.setTextureOffset(0, 0).addBox(-14.0F, 0.0F, 0.0F, 14, 14, 14, 0.0F, false);

		rightFlap = new ModelRenderer(this);
		rightFlap.setRotationPoint(7.0F, 10.0F, 0.0F);
		rightFlap.setTextureOffset(28, 29).addBox(-7.0F, -1.0F, -7.0F, 7, 1, 14, 0.0F, false);

		leftFlap = new ModelRenderer(this);
		leftFlap.setRotationPoint(-7.0F, 10.0F, 0.0F);
		leftFlap.setTextureOffset(0, 28).addBox(0.0F, -1.0F, -7.0F, 7, 1, 14, 0.0F, false);

		southFlap = new ModelRenderer(this);
		southFlap.setRotationPoint(0.0F, 10.0F, 7.0F);
		southFlap.setTextureOffset(42, 7).addBox(-7.0F, -0.999F, -6.001F, 14, 1, 6, 0.0F, false);

		northFlap = new ModelRenderer(this);
		northFlap.setRotationPoint(0.0F, 10.0F, -7.0F);
		northFlap.setTextureOffset(42, 0).addBox(-7.0F, -0.999F, 0.001F, 14, 1, 6, 0.0F, false);
	}

	@Override
	public void renderAll(MatrixStack matrixStack, IVertexBuilder renderBuffer, int combinedLight, int combinedOverlay, AbstractTreasureChestTileEntity te) {
		base.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		rightFlap.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		leftFlap.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		northFlap.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		southFlap.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
	}

	@Override
	public ModelRenderer getLid() {
		return leftFlap;
	}
	
	public ModelRenderer getInnerLid() {
		return northFlap;
	}
}