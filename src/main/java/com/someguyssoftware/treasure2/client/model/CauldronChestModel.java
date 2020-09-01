package com.someguyssoftware.treasure2.client.model;

import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class CauldronChestModel extends ModelBase implements ITreasureChestModel {
	// fields
	ModelRenderer front;
	ModelRenderer back;
	ModelRenderer left;
	ModelRenderer right;
	ModelRenderer bottom;
	ModelRenderer flFoot;
	ModelRenderer frFoot;
	ModelRenderer blFoot;
	ModelRenderer brFoot;
	ModelRenderer fl2;
	ModelRenderer fr2;
	ModelRenderer bl2;
	ModelRenderer br2;
	ModelRenderer lidLeft;
	ModelRenderer lidRight;

	public CauldronChestModel() {
		textureWidth = 128;
		textureHeight = 128;

		front = new ModelRenderer(this, 0, 0);
		front.addBox(0F, 0F, 0F, 12, 13, 2);
		front.setRotationPoint(-6F, 8F, -8F);
		front.setTextureSize(64, 32);
		front.mirror = true;
		setRotation(front, 0F, 0F, 0F);
		back = new ModelRenderer(this, 32, 0);
		back.addBox(0F, 0F, 0F, 12, 13, 2);
		back.setRotationPoint(-6F, 8F, 6F);
		back.setTextureSize(64, 32);
		back.mirror = true;
		setRotation(back, 0F, 0F, 0F);
		left = new ModelRenderer(this, 0, 18);
		left.addBox(0F, 0F, 0F, 2, 13, 16);
		left.setRotationPoint(6F, 8F, -8F);
		left.setTextureSize(64, 32);
		left.mirror = true;
		setRotation(left, 0F, 0F, 0F);
		right = new ModelRenderer(this, 38, 18);
		right.addBox(0F, 0F, 0F, 2, 13, 16);
		right.setRotationPoint(-8F, 8F, -8F);
		right.setTextureSize(64, 32);
		right.mirror = true;
		setRotation(right, 0F, 0F, 0F);
		bottom = new ModelRenderer(this, 0, 50);
		bottom.addBox(0F, 0F, 0F, 12, 1, 12);
		bottom.setRotationPoint(-6F, 20F, -6F);
		bottom.setTextureSize(64, 32);
		bottom.mirror = true;
		setRotation(bottom, 0F, 0F, 0F);
		flFoot = new ModelRenderer(this, 0, 80);
		flFoot.addBox(0F, 0F, 0F, 4, 3, 2);
		flFoot.setRotationPoint(4F, 21F, -8F);
		flFoot.setTextureSize(64, 32);
		flFoot.mirror = true;
		setRotation(flFoot, 0F, 0F, 0F);
		frFoot = new ModelRenderer(this, 14, 80);
		frFoot.addBox(0F, 0F, 0F, 4, 3, 2);
		frFoot.setRotationPoint(-8F, 21F, -8F);
		frFoot.setTextureSize(64, 32);
		frFoot.mirror = true;
		setRotation(frFoot, 0F, 0F, 0F);
		blFoot = new ModelRenderer(this, 28, 80);
		blFoot.addBox(0F, 0F, 0F, 4, 3, 2);
		blFoot.setRotationPoint(4F, 21F, 6F);
		blFoot.setTextureSize(64, 32);
		blFoot.mirror = true;
		setRotation(blFoot, 0F, 0F, 0F);
		brFoot = new ModelRenderer(this, 42, 80);
		brFoot.addBox(0F, 0F, 0F, 4, 3, 2);
		brFoot.setRotationPoint(-8F, 21F, 6F);
		brFoot.setTextureSize(64, 32);
		brFoot.mirror = true;
		setRotation(brFoot, 0F, 0F, 0F);
		fl2 = new ModelRenderer(this, 0, 88);
		fl2.addBox(0F, 0F, 0F, 2, 3, 2);
		fl2.setRotationPoint(6F, 21F, -6F);
		fl2.setTextureSize(64, 32);
		fl2.mirror = true;
		setRotation(fl2, 0F, 0F, 0F);
		fr2 = new ModelRenderer(this, 10, 88);
		fr2.addBox(0F, 0F, 0F, 2, 3, 2);
		fr2.setRotationPoint(-8F, 21F, -6F);
		fr2.setTextureSize(64, 32);
		fr2.mirror = true;
		setRotation(fr2, 0F, 0F, 0F);
		bl2 = new ModelRenderer(this, 20, 88);
		bl2.addBox(0F, 0F, 0F, 2, 3, 2);
		bl2.setRotationPoint(6F, 21F, 4F);
		bl2.setTextureSize(64, 32);
		bl2.mirror = true;
		setRotation(bl2, 0F, 0F, 0F);
		br2 = new ModelRenderer(this, 30, 88);
		br2.addBox(0F, 0F, 0F, 2, 3, 2);
		br2.setRotationPoint(-8F, 21F, 4F);
		br2.setTextureSize(64, 32);
		br2.mirror = true;
		setRotation(br2, 0F, 0F, 0F);
		lidLeft = new ModelRenderer(this, 0, 65);
		lidLeft.addBox(-6F, 0F, 0F, 6, 1, 12);
		lidLeft.setRotationPoint(6F, 9F, -6F);
		lidLeft.setTextureSize(64, 32);
		lidLeft.mirror = true;
		setRotation(lidLeft, 0F, 0F, 0F);
		lidRight = new ModelRenderer(this, 38, 65);
		lidRight.addBox(0F, 0F, 0F, 6, 1, 12);
		lidRight.setRotationPoint(-6F, 9F, -6F);
		lidRight.setTextureSize(64, 32);
		lidRight.mirror = true;
		setRotation(lidRight, 0F, 0F, 0F);
	}

	/**
	 * 
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		front.render(f5);
		back.render(f5);
		left.render(f5);
		right.render(f5);
		bottom.render(f5);
		flFoot.render(f5);
		frFoot.render(f5);
		blFoot.render(f5);
		brFoot.render(f5);
		fl2.render(f5);
		fr2.render(f5);
		bl2.render(f5);
		br2.render(f5);
		lidLeft.render(f5);
		lidRight.render(f5);
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
	public void renderAll(AbstractTreasureChestTileEntity te) {
		// set the angles of the latch to same as the lib
		lidRight.rotateAngleZ = -lidLeft.rotateAngleZ;
		
		float angle = 0.0625F;
		front.render(angle);
		back.render(angle);
		left.render(angle);
		right.render(angle);
		bottom.render(angle);
		lidLeft.render(angle);
		lidRight.render(angle);
		flFoot.render(angle);
		fl2.render(angle);
		frFoot.render(angle);
		fr2.render(angle);
		blFoot.render(angle);
		bl2.render(angle);
		brFoot.render(angle);
		br2.render(angle);		
	}

	@Override
	public ModelRenderer getLid() {
		return lidLeft;
	}

}