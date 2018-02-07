package com.someguyssoftware.treasure2.client.model;

import com.someguyssoftware.treasure2.tileentity.TreasureChestTileEntity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * 
 * @author Mark Gottschling on Jan 19, 2018
 *
 */
public class BandedChestModel extends ModelBase implements ITreasureChestModel {
	// fields
	ModelRenderer base;
	ModelRenderer RightBottomBand;
	ModelRenderer lid;
	ModelRenderer RightTopBand;
	ModelRenderer LeftTopBand;
	ModelRenderer FrontLeftTopBand;
	ModelRenderer BackRightTopBand;
	ModelRenderer BackLeftTopBand;
	ModelRenderer LeftBottomBand;
	ModelRenderer FrontRightTopBand;
	ModelRenderer Ledge;
	ModelRenderer Ledge2;
	ModelRenderer Ledge3;
    ModelRenderer hinge1;
    ModelRenderer hinge2;
    ModelRenderer pad;

	/**
	 * 
	 */
	public BandedChestModel() {
		textureWidth = 128;
		textureHeight = 128;

		base = new ModelRenderer(this, 0, 20);
		base.addBox(-7F, 0F, -14F, 14, 9, 14);
		base.setRotationPoint(0F, 14.5F, 7F);
		base.setTextureSize(64, 32);
		base.mirror = true;
		setRotation(base, 0F, 0F, 0F);
		RightBottomBand = new ModelRenderer(this, 0, 44);
		RightBottomBand.addBox(-1F, 0F, -15F, 2, 9, 15);
		RightBottomBand.setRotationPoint(-4F, 15F, 7.5F);
		RightBottomBand.setTextureSize(64, 32);
		RightBottomBand.mirror = true;
		setRotation(RightBottomBand, 0F, 0F, 0F);
		lid = new ModelRenderer(this, 0, 0);
		lid.addBox(-7F, -5F, -14F, 14, 5, 14);
		lid.setRotationPoint(0F, 15.5F, 7F);
		lid.setTextureSize(64, 32);
		lid.mirror = true;
		setRotation(lid, 0F, 0F, 0F);
		RightTopBand = new ModelRenderer(this, 0, 69);
		RightTopBand.addBox(0F, -5F, -15F, 2, 1, 15);
		RightTopBand.setRotationPoint(-5F, 15F, 7.5F);
		RightTopBand.setTextureSize(64, 32);
		RightTopBand.mirror = true;
		setRotation(RightTopBand, 0F, 0F, 0F);
		LeftTopBand = new ModelRenderer(this, 36, 69);
		LeftTopBand.addBox(0F, -5F, -15F, 2, 1, 15);
		LeftTopBand.setRotationPoint(3F, 15F, 7.5F);
		LeftTopBand.setTextureSize(64, 32);
		LeftTopBand.mirror = true;
		setRotation(LeftTopBand, 0F, 0F, 0F);
		FrontLeftTopBand = new ModelRenderer(this, 64, 0);
		FrontLeftTopBand.addBox(-1F, -4F, -15F, 2, 5, 1);
		FrontLeftTopBand.setRotationPoint(4F, 15F, 7.5F);
		FrontLeftTopBand.setTextureSize(64, 32);
		FrontLeftTopBand.mirror = true;
		setRotation(FrontLeftTopBand, 0F, 0F, 0F);
		BackRightTopBand = new ModelRenderer(this, 57, 7);
		BackRightTopBand.addBox(-1F, -5F, -1F, 2, 5, 1);
		BackRightTopBand.setRotationPoint(-4F, 15F, 7.5F);
		BackRightTopBand.setTextureSize(64, 32);
		BackRightTopBand.mirror = true;
		setRotation(BackRightTopBand, 0F, 0F, 0F);
		BackLeftTopBand = new ModelRenderer(this, 64, 7);
		BackLeftTopBand.addBox(-1F, -5F, -1F, 2, 5, 1);
		BackLeftTopBand.setRotationPoint(4F, 15F, 7.5F);
		BackLeftTopBand.setTextureSize(64, 32);
		BackLeftTopBand.mirror = true;
		setRotation(BackLeftTopBand, 0F, 0F, 0F);
		LeftBottomBand = new ModelRenderer(this, 35, 44);
		LeftBottomBand.addBox(-1F, 0F, -15F, 2, 9, 15);
		LeftBottomBand.setRotationPoint(4F, 15F, 7.5F);
		LeftBottomBand.setTextureSize(64, 32);
		LeftBottomBand.mirror = true;
		setRotation(LeftBottomBand, 0F, 0F, 0F);
		FrontRightTopBand = new ModelRenderer(this, 57, 0);
		FrontRightTopBand.addBox(-1F, -4F, -15F, 2, 5, 1);
		FrontRightTopBand.setRotationPoint(-4F, 15F, 7.5F);
		FrontRightTopBand.setTextureSize(64, 32);
		FrontRightTopBand.mirror = true;
		setRotation(FrontRightTopBand, 0F, 0F, 0F);
		Ledge = new ModelRenderer(this, 58, 16);
		Ledge.addBox(-2F, -1F, -15F, 4, 1, 1);
		Ledge.setRotationPoint(0F, 15F, 7F);
		Ledge.setTextureSize(64, 32);
		Ledge.mirror = true;
		setRotation(Ledge, 0F, 0F, 0F);
		Ledge2 = new ModelRenderer(this, 58, 16);
		Ledge2.addBox(0F, -1F, -9F, 1, 1, 4);
		Ledge2.setRotationPoint(7F, 15F, 7F);
		Ledge2.setTextureSize(128, 128);
		Ledge2.mirror = true;
		setRotation(Ledge2, 0F, 0F, 0F);
		Ledge3 = new ModelRenderer(this, 0, 0);
		Ledge3.addBox(-1F, -1F, -9F, 1, 1, 4);
		Ledge3.setRotationPoint(-7F, 15F, 7F);
		Ledge3.setTextureSize(128, 128);
		Ledge3.mirror = true;
		setRotation(Ledge3, 0F, 0F, 0F);
	      hinge1 = new ModelRenderer(this, 11, 86);
	      hinge1.addBox(-1F, 0F, -0.8F, 2, 2, 1);
	      hinge1.setRotationPoint(2F, 14.5F, 7F);
	      hinge1.setTextureSize(128, 128);
	      hinge1.mirror = true;
	      setRotation(hinge1, 0F, 0F, 0F);
	      hinge2 = new ModelRenderer(this, 11, 86);
	      hinge2.addBox(-1F, 0F, -0.8F, 2, 2, 1);
	      hinge2.setRotationPoint(-2F, 14.5F, 7F);
	      hinge2.setTextureSize(128, 128);
	      hinge2.mirror = true;
	      setRotation(hinge2, 0F, 0F, 0F);
	      pad = new ModelRenderer(this, 0, 86);
	      pad.addBox(-2F, -1F, -14.2F, 4, 4, 1);
	      pad.setRotationPoint(0F, 15.5F, 7F);
	      pad.setTextureSize(128, 128);
	      pad.mirror = true;
	      setRotation(pad, 0F, 0F, 0F);
	}

	/**
	 * 
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		base.render(f5);
		RightBottomBand.render(f5);
		lid.render(f5);
		RightTopBand.render(f5);
		LeftTopBand.render(f5);
		FrontLeftTopBand.render(f5);
		BackRightTopBand.render(f5);
		BackLeftTopBand.render(f5);
		LeftBottomBand.render(f5);
		FrontRightTopBand.render(f5);
		Ledge.render(f5);
		Ledge2.render(f5);
		Ledge3.render(f5);
	    hinge1.render(f5);
	    hinge2.render(f5);
	    pad.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}

	/**
	 * 
	 */
	@Override
	public void renderAll(TreasureChestTileEntity te) {
		// set the angles of the latch to same as the lib
		RightTopBand.rotateAngleX = lid.rotateAngleX;
		LeftTopBand.rotateAngleX = lid.rotateAngleX;
		FrontLeftTopBand.rotateAngleX = lid.rotateAngleX;
		FrontRightTopBand.rotateAngleX = lid.rotateAngleX;
		BackLeftTopBand.rotateAngleX = lid.rotateAngleX;
		BackRightTopBand.rotateAngleX = lid.rotateAngleX;
		Ledge.rotateAngleX = lid.rotateAngleX;
		Ledge2.rotateAngleX = lid.rotateAngleX;
		Ledge3.rotateAngleX = lid.rotateAngleX;
		
		base.render(0.0625F);
		lid.render(0.0625F);
		RightBottomBand.render(0.0625F);
		RightTopBand.render(0.0625F);
		LeftTopBand.render(0.0625F);
		FrontLeftTopBand.render(0.0625F);
		BackRightTopBand.render(0.0625F);
		BackLeftTopBand.render(0.0625F);
		LeftBottomBand.render(0.0625F);
		FrontRightTopBand.render(0.0625F);
		Ledge.render(0.0625F);
		Ledge2.render(0.0625F);
		Ledge3.render(0.0625F);
	    hinge1.render(0.0625F);
	    hinge2.render(0.0625F);
	    pad.render(0.0625F);
	}

	@Override
	public ModelRenderer getLid() {
		return lid;
	}
}
