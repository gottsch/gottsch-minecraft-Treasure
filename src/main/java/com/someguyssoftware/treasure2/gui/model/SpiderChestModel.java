package com.someguyssoftware.treasure2.gui.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestBlockEntity;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * 
 * @author Mark Gottschling on Jan 22, 2020
 *
 */
public class SpiderChestModel extends AbstractTreasureChestModel {
	private final ModelRenderer chest;
	private final ModelRenderer headBone;
	private final ModelRenderer bone;
	private final ModelRenderer bone2;
	private ModelRenderer lid;

	public SpiderChestModel() {
		super(RenderType::entityCutout);
		
		texWidth = 64;
		texHeight = 64;

		chest = new ModelRenderer(this);
		chest.setPos(-8.0F, 16.0F, 8.0F);
		chest.texOffs(0, 15).addBox(2.0F, -4.0F, -10.0F, 12.0F, 5.0F, 10.0F, 0.0F, false);
		chest.texOffs(44, 44).addBox(14.0F, -2.0F, -10.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
		chest.texOffs(16, 44).addBox(14.0F, -2.0F, -6.5F, 2.0F, 10.0F, 2.0F, 0.0F, false);
		chest.texOffs(8, 44).addBox(14.0F, -2.0F, -3.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
		chest.texOffs(0, 44).addBox(0.0F, -2.0F, -10.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
		chest.texOffs(42, 13).addBox(0.0F, -2.0F, -6.5F, 2.0F, 10.0F, 2.0F, 0.0F, false);
		chest.texOffs(26, 42).addBox(0.0F, -2.0F, -3.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);

		headBone = new ModelRenderer(this);
		headBone.setPos(0.0F, 13.0F, 8.0F);
		headBone.texOffs(0, 30).addBox(-4.0F, -4.0F, -16.0F, 8.0F, 8.0F, 6.0F, 0.0F, false);

		bone = new ModelRenderer(this);
		bone.setPos(0.0F, 13.0F, 8.0F);
		setRotation(bone, -0.3927F, 0.0F, 0.0F);
		bone.texOffs(36, 36).addBox(6.0F, 4.7507F, -10.8561F, 2.0F, 10.0F, 2.0F, 0.0F, false);

		bone2 = new ModelRenderer(this);
		bone2.setPos(0.0F, 13.0F, 8.0F);
		setRotation(bone2, -0.3927F, 0.0F, 0.0F);
		bone2.texOffs(28, 30).addBox(-8.0F, 4.7507F, -10.8561F, 2.0F, 10.0F, 2.0F, 0.0F, false);

		lid = new ModelRenderer(this);
		lid.setPos(0.0F, 13.0F, 8.0F);
		lid.texOffs(0, 0).addBox(-6.0F, -5.0F, -10.0F, 12.0F, 5.0F, 10.0F, 0.01F, false);
	}

	@Override
	public void renderAll(MatrixStack matrixStack, IVertexBuilder renderBuffer, int combinedLight, int combinedOverlay, AbstractTreasureChestTileEntity te) {
		headBone.xRot = lid.xRot;
		chest.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		bone.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		bone2.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		headBone.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		lid.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
	}
	
	/**
	 * @return the lid
	 */
	@Override
	public ModelRenderer getLid() {
		return lid;
	}

	/**
	 * @param lid the lid to set
	 */
	public void setLid(ModelRenderer lid) {
		this.lid = lid;
	}
}