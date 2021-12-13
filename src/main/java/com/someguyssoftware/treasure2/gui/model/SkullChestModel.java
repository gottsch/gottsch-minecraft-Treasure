package com.someguyssoftware.treasure2.gui.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestBlockEntity;

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
		super(RenderType::entityCutout);
		texWidth = 64;
		texHeight = 64;

		top = new ModelRenderer(this, 0, 0);
		top.addBox(-3F, -6F, -5F, 6, 1, 6);
		top.setPos(0F, 21F, 2F);
		top.setTexSize(64, 64);
		top.mirror = true;
		setRotation(top, 0F, 0F, 0F);
		head = new ModelRenderer(this, 0, 8);
		head.addBox(-4F, -5F, -6F, 8, 5, 8);
		head.setPos(0F, 21F, 2F);
		head.setTexSize(64, 64);
		head.mirror = true;
		setRotation(head, 0F, 0F, 0F);
		jaw = new ModelRenderer(this, 0, 22);
		jaw.addBox(-3F, 0F, -6F, 6, 1, 6);
		jaw.setPos(0F, 21F, 2F);
		jaw.setTexSize(64, 64);
		jaw.mirror = true;
		setRotation(jaw, 0F, 0F, 0F);
		jawBottom = new ModelRenderer(this, 0, 30);
		jawBottom.addBox(-3F, 0F, -5F, 6, 2, 5);
		jawBottom.setPos(0F, 22F, 1F);
		jawBottom.setTexSize(64, 64);
		jawBottom.mirror = true;
		setRotation(jawBottom, 0F, 0F, 0F);
	}

	/**
	 * 
	 */
	@Override
	public void renderAll(MatrixStack matrixStack, IVertexBuilder renderBuffer, int combinedLight, int combinedOverlay, AbstractTreasureChestTileEntity te) {
		head.xRot = top.xRot;
		jaw.xRot = top.xRot;
		
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
