/**
 * 
 */
package com.someguyssoftware.treasure2.gui.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * @author Mark Gottschling on Apr 19, 2020
 *
 */
public class VikingChestModel  extends AbstractTreasureChestModel {
	private final ModelRenderer mainBox;
	private final ModelRenderer lid;
	private final ModelRenderer front;
	private final ModelRenderer side1;
	private final ModelRenderer side2;
	private final ModelRenderer back;

	/**
	 * 
	 */
	public VikingChestModel() {
		super(RenderType::getEntityCutout);
		textureWidth = 64;
		textureHeight = 64;

		mainBox = new ModelRenderer(this);
		mainBox.setRotationPoint(0.0F, 24.0F, 0.0F);
		mainBox.setTextureOffset(0, 0).addBox(-7.0F, -13.0F, -5.0F, 14.0F, 10.0F, 10.0F, 0.0F, false);
		mainBox.setTextureOffset(0, 20).addBox(-2.0F, -12.0F, -5.5F, 4.0F, 4.0F, 1.0F, 0.0F, false);
		mainBox.setTextureOffset(0, 33).addBox(-8.0F, -5.5F, -2.0F, 16.0F, 2.0F, 4.0F, 0.0F, false);
		mainBox.setTextureOffset(38, 10).addBox(-7.0F, -3.0F, -5.0F, 2.0F, 3.0F, 10.0F, 0.0F, false);
		mainBox.setTextureOffset(30, 33).addBox(5.0F, -3.0F, -5.0F, 2.0F, 3.0F, 10.0F, 0.0F, false);
		mainBox.setTextureOffset(0, 5).addBox(7.0F, -11.0F, -2.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
		mainBox.setTextureOffset(0, 0).addBox(-8.0F, -11.0F, -2.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);

		lid = new ModelRenderer(this);
		lid.setRotationPoint(0.0F, 12.0F, 5.0F);
		lid.setTextureOffset(38, 0).addBox(-1.0F, -3.5F, -10.75F, 2.0F, 1.0F, 8.0F, 0.0F, false);
		lid.setTextureOffset(0, 20).addBox(-7.0F, -3.0F, -10.0F, 14.0F, 3.0F, 10.0F, 0.0F, false);
		lid.setTextureOffset(38, 27).addBox(5.5F, -2.0F, -10.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		lid.setTextureOffset(38, 25).addBox(-6.5F, -2.0F, -10.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		lid.setTextureOffset(38, 23).addBox(5.5F, -2.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		lid.setTextureOffset(38, 6).addBox(-6.5F, -2.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		lid.setTextureOffset(0, 25).addBox(-1.0F, -2.5F, -10.75F, 2.0F, 4.0F, 1.0F, 0.0F, false);

		front = new ModelRenderer(this);
		front.setRotationPoint(0.0F, 24.0F, 0.0F);
		front.setTextureOffset(38, 4).addBox(5.5F, -2.0F, -5.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		front.setTextureOffset(38, 2).addBox(5.5F, -8.0F, -5.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		front.setTextureOffset(38, 0).addBox(-6.5F, -8.0F, -5.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		front.setTextureOffset(36, 35).addBox(-6.5F, -2.0F, -5.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		side1 = new ModelRenderer(this);
		side1.setRotationPoint(0.0F, 24.0F, 0.0F);
		side1.setTextureOffset(36, 33).addBox(6.5F, -5.0F, -4.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		side1.setTextureOffset(0, 35).addBox(-7.5F, -11.0F, -4.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		side1.setTextureOffset(0, 33).addBox(6.5F, -5.0F, 3.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		side1.setTextureOffset(6, 27).addBox(6.5F, -11.0F, 3.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		side2 = new ModelRenderer(this);
		side2.setRotationPoint(0.0F, 24.0F, 0.0F);
		side2.setTextureOffset(6, 25).addBox(-7.5F, -5.0F, -4.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		side2.setTextureOffset(0, 7).addBox(6.5F, -11.0F, -4.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		side2.setTextureOffset(6, 2).addBox(-7.5F, -5.0F, 3.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		side2.setTextureOffset(6, 0).addBox(-7.5F, -11.0F, 3.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		back = new ModelRenderer(this);
		back.setRotationPoint(0.0F, 24.0F, 0.0F);
		back.setTextureOffset(6, 6).addBox(5.5F, -2.0F, 4.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		back.setTextureOffset(0, 5).addBox(5.5F, -8.0F, 4.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		back.setTextureOffset(0, 2).addBox(-6.5F, -8.0F, 4.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		back.setTextureOffset(0, 0).addBox(-6.5F, -2.0F, 4.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
	}

	@Override
	public void renderAll(MatrixStack matrixStack, IVertexBuilder renderBuffer, int combinedLight, int combinedOverlay, AbstractTreasureChestTileEntity te) {
		mainBox.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		lid.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		side1.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		side2.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		front.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		back.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
	}

	@Override
	public ModelRenderer getLid() {
		return lid;
	}
}
