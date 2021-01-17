package com.someguyssoftware.treasure2.gui.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;

public class MilkCrateModel extends AbstractTreasureChestModel {
	private final ModelRenderer northFace;
	private final ModelRenderer lid;
	private final ModelRenderer padBottom;
	private final ModelRenderer southFace;
	private final ModelRenderer eastFace;
	private final ModelRenderer westFace;
	private final ModelRenderer bottomFace;

	public MilkCrateModel() {
		super(RenderType::getEntityCutout);
		
		textureWidth = 128;
		textureHeight = 128;

		northFace = new ModelRenderer(this);
		northFace.setRotationPoint(0.0F, 14.0F, 7.0F);
		northFace.setTextureOffset(41, 8).addBox(-7.0F, 7.0F, -14.0F, 14, 3, 1, false);
		northFace.setTextureOffset(42, 5).addBox(-7.0F, 3.0F, -14.0F, 14, 3, 1, false);
		northFace.setTextureOffset(42, 1).addBox(-7.0F, -0.9F, -14.0F, 14, 3, 1, false);
		northFace.setTextureOffset(0, 0).addBox(5.1F, -1.0F, -14.1F, 2, 11, 2, false);
		northFace.setTextureOffset(0, 0).addBox(-7.1F, -1.0F, -14.1F, 2, 11, 2, false);

		lid = new ModelRenderer(this);
		lid.setRotationPoint(0.0F, 13.0F, 7.0F);
		lid.setTextureOffset(0, 0).addBox(-7.0F, -2.0F, -14.0F, 14, 2, 14, false);
		lid.setTextureOffset(0, 16).addBox(-2.0F, -2.0F, -14.2F, 4, 2, 1, false);
		lid.setTextureOffset(8, 8).addBox(-1.0F, -1.0F, -15.0F, 2, 2, 1, false);
		
		padBottom = new ModelRenderer(this);
		padBottom.setRotationPoint(0.0F, 15.0F, 7.0F);
		padBottom.setTextureOffset(0, 19).addBox(-2.0F, -2.0F, -14.2F, 4, 2, 1, false);

		southFace = new ModelRenderer(this);
		southFace.setRotationPoint(0.0F, 14.0F, 7.0F);
		southFace.setTextureOffset(42, 8).addBox(-7.0F, 7.0F, -1.0F, 14, 3, 1, false);
		southFace.setTextureOffset(42, 5).addBox(-7.0F, 3.0F, -1.0F, 14, 3, 1, false);
		southFace.setTextureOffset(42, 1).addBox(-7.0F, -0.9F, -1.0F, 14, 3, 1, false);
		southFace.setTextureOffset(0, 0).addBox(5.1F, -1.0F, -1.9F, 2, 11, 2, false);
		southFace.setTextureOffset(0, 0).addBox(-7.1F, -1.0F, -1.9F, 2, 11, 2, false);

		eastFace = new ModelRenderer(this);
		eastFace.setRotationPoint(0.0F, 14.0F, 7.0F);
		eastFace.setTextureOffset(0, 44).addBox(-7.0F, 7.0F, -13.0F, 1, 3, 12, false);
		eastFace.setTextureOffset(26, 29).addBox(-7.0F, 3.0F, -13.0F, 1, 3, 12, false);
		eastFace.setTextureOffset(0, 29).addBox(-7.0F, -0.9F, -13.0F, 1, 3, 12, false);

		westFace = new ModelRenderer(this);
		westFace.setRotationPoint(0.0F, 14.0F, 7.0F);
		westFace.setTextureOffset(0, 44).addBox(6.0F, 7.0F, -13.0F, 1, 3, 12, false);
		westFace.setTextureOffset(26, 29).addBox(6.0F, 3.0F, -13.0F, 1, 3, 12, false);
		westFace.setTextureOffset(0, 29).addBox(6.0F, -0.9F, -13.0F, 1, 3, 12, false);

		bottomFace = new ModelRenderer(this);
		bottomFace.setRotationPoint(0.0F, 24.0F, 0.0F);
		bottomFace.setTextureOffset(0, 16).addBox(-6.0F, -1.0F, -6.0F, 12, 1, 12, false);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void renderAll(MatrixStack matrixStack, IVertexBuilder renderBuffer, int combinedLight, int combinedOverlay, AbstractTreasureChestTileEntity te) {
		northFace.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		southFace.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		eastFace.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		westFace.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		bottomFace.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		lid.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		padBottom.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
	}

	@Override
	public ModelRenderer getLid() {
		return lid;
	}
}