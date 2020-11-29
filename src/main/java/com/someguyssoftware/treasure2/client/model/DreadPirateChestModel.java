package com.someguyssoftware.treasure2.client.model;

import com.someguyssoftware.treasure2.tileentity.ITreasureChestTileEntity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * 
 * @author Mark Gottschling on Mar 13, 2018
 *
 */
public class DreadPirateChestModel extends ModelBase implements ITreasureChestModel {
	// fields
	ModelRenderer lid;
	ModelRenderer baseMiddle;
	ModelRenderer baseSide1;
	ModelRenderer baseSide2;
	ModelRenderer bottom;
	ModelRenderer skullTop;
	ModelRenderer skullBottom;
	ModelRenderer topHinge1;
	ModelRenderer topHinge2;
	ModelRenderer bottomHinge1;
	ModelRenderer bottomHinge2;
	ModelRenderer corner1;
	ModelRenderer corner2;
	ModelRenderer handle1;
	ModelRenderer handle2;

	/**
	 * 
	 */
	public DreadPirateChestModel() {
		textureWidth = 128;
		textureHeight = 128;

		lid = new ModelRenderer(this, 0, 0);
		lid.addBox(-7F, -4F, -14F, 14, 5, 14);
		lid.setRotationPoint(0F, 14F, 7F);
		lid.setTextureSize(128, 128);
		lid.mirror = true;
		setRotation(lid, 0F, 0F, 0F);
		baseMiddle = new ModelRenderer(this, 0, 21);
		baseMiddle.addBox(-3F, 0F, -11F, 6, 9, 12);
		baseMiddle.setRotationPoint(0F, 14F, 5F);
		baseMiddle.setTextureSize(128, 128);
		baseMiddle.mirror = true;
		setRotation(baseMiddle, 0F, 0F, 0F);
		baseSide1 = new ModelRenderer(this, 0, 44);
		baseSide1.addBox(-2F, 0F, -12F, 4, 9, 14);
		baseSide1.setRotationPoint(5F, 14F, 5F);
		baseSide1.setTextureSize(128, 128);
		baseSide1.mirror = true;
		setRotation(baseSide1, 0F, 0F, 0F);
		baseSide2 = new ModelRenderer(this, 0, 70);
		baseSide2.addBox(-2F, 0F, -12F, 4, 9, 14);
		baseSide2.setRotationPoint(-5F, 14F, 5F);
		baseSide2.setTextureSize(128, 128);
		baseSide2.mirror = true;
		setRotation(baseSide2, 0F, 0F, 0F);
		bottom = new ModelRenderer(this, 0, 96);
		bottom.addBox(-7F, 0F, -14F, 14, 2, 14);
		bottom.setRotationPoint(0F, 22F, 7F);
		bottom.setTextureSize(128, 128);
		bottom.mirror = true;
		setRotation(bottom, 0F, 0F, 0F);
		skullTop = new ModelRenderer(this, 57, 0);
		skullTop.addBox(-3F, -3F, -15F, 6, 5, 2);
		skullTop.setRotationPoint(0F, 15F, 7F);
		skullTop.setTextureSize(128, 128);
		skullTop.mirror = true;
		setRotation(skullTop, 0F, 0F, 0F);
		skullBottom = new ModelRenderer(this, 57, 8);
		skullBottom.addBox(-2F, 0F, -1F, 4, 3, 1);
		skullBottom.setRotationPoint(0F, 17F, -6F);
		skullBottom.setTextureSize(128, 128);
		skullBottom.mirror = true;
		setRotation(skullBottom, 0F, 0F, 0F);
		topHinge1 = new ModelRenderer(this, 57, 14);
		topHinge1.addBox(-1F, -4.5F, -4.5F, 2, 4, 5);
		topHinge1.setRotationPoint(4F, 14F, 7F);
		topHinge1.setTextureSize(128, 128);
		topHinge1.mirror = true;
		setRotation(topHinge1, 0F, 0F, 0F);
		topHinge2 = new ModelRenderer(this, 57, 14);
		topHinge2.addBox(-1F, -4.5F, -4.5F, 2, 4, 5);
		topHinge2.setRotationPoint(-4F, 14F, 7F);
		topHinge2.setTextureSize(128, 128);
		topHinge2.mirror = true;
		setRotation(topHinge2, 0F, 0F, 0F);
		bottomHinge1 = new ModelRenderer(this, 57, 24);
		bottomHinge1.addBox(-1F, -0.5F, -0.5F, 2, 2, 1);
		bottomHinge1.setRotationPoint(4F, 14F, 7F);
		bottomHinge1.setTextureSize(128, 128);
		bottomHinge1.mirror = true;
		setRotation(bottomHinge1, 0F, 0F, 0F);
		bottomHinge2 = new ModelRenderer(this, 57, 24);
		bottomHinge2.addBox(-1F, -0.5F, -0.5F, 2, 2, 1);
		bottomHinge2.setRotationPoint(-4F, 14F, 7F);
		bottomHinge2.setTextureSize(128, 128);
		bottomHinge2.mirror = true;
		setRotation(bottomHinge2, 0F, 0F, 0F);
		corner1 = new ModelRenderer(this, 57, 28);
		corner1.addBox(-1F, -4.25F, -14.25F, 2, 2, 2);
		corner1.setRotationPoint(6.25F, 14F, 7F);
		corner1.setTextureSize(64, 32);
		corner1.mirror = true;
		setRotation(corner1, 0F, 0F, 0F);
		corner2 = new ModelRenderer(this, 66, 28);
		corner2.addBox(-1F, -4.25F, -14.25F, 2, 2, 2);
		corner2.setRotationPoint(-6.3F, 14F, 7F);
		corner2.setTextureSize(64, 32);
		corner2.mirror = true;
		setRotation(corner2, 0F, 0F, 0F);
		handle1 = new ModelRenderer(this, 57, 34);
		handle1.addBox(0F, 0F, 0F, 1, 2, 4);
		handle1.setRotationPoint(7F, 15F, -2F);
		handle1.setTextureSize(128, 128);
		handle1.mirror = true;
		setRotation(handle1, 0F, 0F, 0F);
		handle2 = new ModelRenderer(this, 57, 34);
		handle2.addBox(0F, 0F, 0F, 1, 2, 4);
		handle2.setRotationPoint(-8F, 15F, -2F);
		handle2.setTextureSize(128, 128);
		handle2.mirror = true;
		setRotation(handle2, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		lid.render(f5);
		baseMiddle.render(f5);
		baseSide1.render(f5);
		baseSide2.render(f5);
		bottom.render(f5);
		skullTop.render(f5);
		skullBottom.render(f5);
		topHinge1.render(f5);
		topHinge2.render(f5);
		bottomHinge1.render(f5);
		bottomHinge2.render(f5);
		corner1.render(f5);
		corner2.render(f5);
		handle1.render(f5);
		handle2.render(f5);
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
	public void renderAll(ITreasureChestTileEntity te) {
		float f5 = 0.0625F;

		// set the angles
		skullTop.rotateAngleX = lid.rotateAngleX;
		topHinge1.rotateAngleX = lid.rotateAngleX;
		topHinge2.rotateAngleX = lid.rotateAngleX;
		corner1.rotateAngleX = lid.rotateAngleX;
		corner2.rotateAngleX = lid.rotateAngleX;

		lid.render(f5);
		baseMiddle.render(f5);
		baseSide1.render(f5);
		baseSide2.render(f5);
		bottom.render(f5);
		skullTop.render(f5);
		skullBottom.render(f5);
		topHinge1.render(f5);
		topHinge2.render(f5);
		bottomHinge1.render(f5);
		bottomHinge2.render(f5);
		corner1.render(f5);
		corner2.render(f5);
		handle1.render(f5);
		handle2.render(f5);
	}

	@Override
	public ModelRenderer getLid() {
		return lid;
	}

}
