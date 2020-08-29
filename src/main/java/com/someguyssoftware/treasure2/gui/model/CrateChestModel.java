package com.someguyssoftware.treasure2.gui.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * 
 * @author Mark Gottschling on Feb 3, 2018
 *
 */
public class CrateChestModel extends AbstractTreasureChestModel {

	ModelRenderer box;
	ModelRenderer lid;
	ModelRenderer base;
	ModelRenderer eastPost;
	ModelRenderer westPost;
	ModelRenderer rearEastPost;
	ModelRenderer rearWestPost;
	ModelRenderer cross;
	ModelRenderer crossSouth;
	ModelRenderer pad;
	ModelRenderer latch1;
	ModelRenderer extraBoard;
	ModelRenderer padTop;
	ModelRenderer crossEast;
	ModelRenderer crossWest;

	/**
	 * 
	 */
	public CrateChestModel() {
		super(RenderType::getEntityCutout);

		textureWidth = 128;
		textureHeight = 128;

		box = new ModelRenderer(this, 0, 17);
		box.addBox(-6F, 0F, -12F, 12, 10, 12);
		box.setRotationPoint(0F, 12F, 6F);
		box.setTextureSize(64, 32);
		box.mirror = true;
		setRotation(box, 0F, 0F, 0F);
		lid = new ModelRenderer(this, 0, 0);
		lid.addBox(-1F, 0F, -13F, 14, 2, 14);
		lid.setRotationPoint(-6F, 9F, 6F);
		lid.setTextureSize(64, 32);
		lid.mirror = true;
		setRotation(lid, 0F, 0F, 0F);
		base = new ModelRenderer(this, 0, 42);
		base.addBox(-7F, 0F, -14F, 14, 2, 14);
		base.setRotationPoint(0F, 22F, 7F);
		base.setTextureSize(64, 32);
		base.mirror = true;
		setRotation(base, 0F, 0F, 0F);
		eastPost = new ModelRenderer(this, 0, 59);
		eastPost.addBox(-1F, 0F, -2F, 2, 10, 2);
		eastPost.setRotationPoint(-6F, 12F, -5F);
		eastPost.setTextureSize(64, 32);
		eastPost.mirror = true;
		setRotation(eastPost, 0F, 0F, 0F);
		westPost = new ModelRenderer(this, 10, 59);
		westPost.addBox(-1F, 0F, -2F, 2, 10, 2);
		westPost.setRotationPoint(6F, 12F, -5F);
		westPost.setTextureSize(64, 32);
		westPost.mirror = true;
		setRotation(westPost, 0F, 0F, 0F);
		rearEastPost = new ModelRenderer(this, 20, 59);
		rearEastPost.addBox(-1F, 0F, -2F, 2, 10, 2);
		rearEastPost.setRotationPoint(-6F, 12F, 7F);
		rearEastPost.setTextureSize(64, 32);
		rearEastPost.mirror = true;
		setRotation(rearEastPost, 0F, 0F, 0F);
		rearWestPost = new ModelRenderer(this, 30, 59);
		rearWestPost.addBox(-1F, 0F, -2F, 2, 10, 2);
		rearWestPost.setRotationPoint(6F, 12F, 7F);
		rearWestPost.setTextureSize(64, 32);
		rearWestPost.mirror = true;
		setRotation(rearWestPost, 0F, 0F, 0F);
		cross = new ModelRenderer(this, 58, 0);
		cross.addBox(-1F, 0F, -2F, 2, 15, 1);
		cross.setRotationPoint(-5F, 11.5F, -4.5F);
		cross.setTextureSize(64, 32);
		cross.mirror = true;
		setRotation(cross, 0F, 0F, -0.8028515F);
		crossSouth = new ModelRenderer(this, 58, 0);
		crossSouth.addBox(-1F, 0F, -2F, 2, 15, 1);
		crossSouth.setRotationPoint(5F, 11.5F, 7.5F);
		crossSouth.setTextureSize(64, 32);
		crossSouth.mirror = true;
		setRotation(crossSouth, 0F, 0F, 0.8028515F);
		pad = new ModelRenderer(this, 0, 72);
		pad.addBox(-2F, -0.2F, -1F, 4, 1, 1);
		pad.setRotationPoint(0F, 11F, -6.2F);
		pad.setTextureSize(64, 32);
		pad.mirror = true;
		setRotation(pad, 0F, 0F, 0F);
		latch1 = new ModelRenderer(this, 28, 72);
		latch1.addBox(5F, 0F, -14F, 2, 2, 1);
		latch1.setRotationPoint(-6F, 9F, 6F);
		latch1.setTextureSize(64, 32);
		latch1.mirror = true;
		setRotation(latch1, 0F, 0F, 0F);
		extraBoard = new ModelRenderer(this, 0, 79);
		extraBoard.addBox(0F, 0F, -14F, 14, 1, 14);
		extraBoard.setRotationPoint(-7F, 11F, 7F);
		extraBoard.setTextureSize(64, 32);
		extraBoard.mirror = true;
		setRotation(extraBoard, 0F, 0F, 0F);
		padTop = new ModelRenderer(this, 11, 72);
		padTop.addBox(4F, -0.2F, -13.2F, 4, 2, 3);
		padTop.setRotationPoint(-6F, 9F, 6F);
		padTop.setTextureSize(64, 32);
		padTop.mirror = true;
		setRotation(padTop, 0F, 0F, 0F);
		crossEast = new ModelRenderer(this, 58, 0);
		crossEast.addBox(0F, 0F, 0F, 1, 15, 2);
		crossEast.setRotationPoint(-6.5F, 12.5F, -6.5F);
		crossEast.setTextureSize(64, 32);
		crossEast.mirror = true;
		setRotation(crossEast, 0.8028515F, 0F, 0F);
		crossWest = new ModelRenderer(this, 58, 0);
		crossWest.addBox(0F, 0F, 0F, 1, 15, 2);
		crossWest.setRotationPoint(5.5F, 11.5F, 4.5F);
		crossWest.setTextureSize(64, 32);
		crossWest.mirror = true;
		setRotation(crossWest, -0.8028515F, 0F, 0F);
	}

	/**
	 * 
	 */
	@Override
	public void renderAll(MatrixStack matrixStack, IVertexBuilder renderBuffer, int combinedLight, int combinedOverlay, AbstractTreasureChestTileEntity te) {
		// set the angles
		padTop.rotateAngleY = lid.rotateAngleY;
		latch1.rotateAngleY = lid.rotateAngleY;

		box.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		lid.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		base.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		eastPost.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		westPost.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		rearEastPost.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		rearWestPost.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		cross.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		crossSouth.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		pad.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		latch1.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		extraBoard.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		padTop.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		crossEast.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		crossWest.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
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

	/**
	 * @return the base
	 */
	public ModelRenderer getBase() {
		return base;
	}

	/**
	 * @param base the base to set
	 */
	public void setBase(ModelRenderer base) {
		this.base = base;
	}

	/**
	 * @return the latch1
	 */
	public ModelRenderer getLatch1() {
		return latch1;
	}

	/**
	 * @param latch1 the latch1 to set
	 */
	public void setLatch1(ModelRenderer latch1) {
		this.latch1 = latch1;
	}
}
