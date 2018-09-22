package com.someguyssoftware.treasure2.client.model;

import com.someguyssoftware.treasure2.entity.monster.WoodMimicEntity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;

/**
 * 
 * @author Mark Gottschling on Aug 21, 2018
 *
 */
public class MimicModel extends ModelBase {

	//fields
	ModelRenderer base;
	ModelRenderer lid;
	ModelRenderer padTop;
	ModelRenderer padBottom;
	ModelRenderer latch1;
	ModelRenderer hingeBottom1;
	ModelRenderer hingeBottom2;
	ModelRenderer toothBottom1;
	ModelRenderer toothBottom2;
	ModelRenderer toothTop1;
	ModelRenderer toothTop2;
    ModelRenderer toothBottom3;
    ModelRenderer toothBottom4;
    ModelRenderer toothTop3;
    ModelRenderer toothTop4;
//    ModelRenderer rowTop;
//    ModelRenderer rowBottom;
    
	/**
	 * 
	 */
	public MimicModel() {
		textureWidth = 128;
		textureHeight = 64;

		base = new ModelRenderer(this, 0, 21);
		base.addBox(-7F, 0F, -14F, 14, 10, 14);
		base.setRotationPoint(0F, 14F, 7F);
		base.setTextureSize(128, 64);
		base.mirror = true;
		// sets the initial rotation of the model
		// for some reason this is not being updated from entity.setLocationAndAngles((double)pos.getX() + 0.5D,  (double)pos.getY(), (double)pos.getZ() + 0.5D, yaw, 0.0F);
		//		setRotation(base, 0F, 0F, 0F);

		lid = new ModelRenderer(this, 0, 0);
		lid.addBox(-7F, -5F, -14F, 14, 5, 14);
		lid.setRotationPoint(0F, 15F, 7F);
		lid.setTextureSize(128, 64);
		lid.mirror = true;
		//		setRotation(lid, 0F, 0F, 0F);

		padTop = new ModelRenderer(this, 18, 47);
		padTop.addBox(-2F, -3F, -14.2F, 4, 3, 1);
		padTop.setRotationPoint(0F, 15F, 7F);
		padTop.setTextureSize(128, 64);
		padTop.mirror = true;
		//		setRotation(padTop, 0F, 0F, 0F);

		padBottom = new ModelRenderer(this, 7, 47);
		padBottom.addBox(-2F, 0F, -1.2F, 4, 3, 1);
		padBottom.setRotationPoint(0F, 15F, -6F);
		padBottom.setTextureSize(128,64);
		padBottom.mirror = true;
		//		setRotation(padBottom, 0F, 0F, 0F);

		latch1 = new ModelRenderer(this, 0, 47);
		latch1.addBox(-1F, -2F, -15F, 2, 4, 1);
		latch1.setRotationPoint(0F, 15F, 7F);
		latch1.setTextureSize(128, 64);
		latch1.mirror = true;
		//		setRotation(Latch1, 0F, 0F, 0F);

		hingeBottom1 = new ModelRenderer(this, 29, 47);
		hingeBottom1.addBox(-1F, 0F, -0.8F, 2, 2, 1);
		hingeBottom1.setRotationPoint(3F, 14F, 7F);
		hingeBottom1.setTextureSize(128, 64);
		hingeBottom1.mirror = true;
		//		setRotation(hingeBottom1, 0F, 0F, 0F);

		hingeBottom2 = new ModelRenderer(this, 29, 47);
		hingeBottom2.addBox(-1F, -1F, -0.8F, 2, 2, 1);
		hingeBottom2.setRotationPoint(-3F, 15F, 7F);
		hingeBottom2.setTextureSize(128, 64);
		hingeBottom2.mirror = true;
		//		setRotation(hingeBottom2, 0F, 0F, 0F);

		toothBottom1 = new ModelRenderer(this, 0, 54);
		toothBottom1.addBox(-1F, -3F, -13.9F, 2, 2, 1);
		toothBottom1.setRotationPoint(-3F, 15F, 7F);
		toothBottom1.setTextureSize(128, 64);
		toothBottom1.mirror = true;

		toothBottom2 = new ModelRenderer(this, 0, 54);
		toothBottom2.addBox(5F, -3F, -13.9F, 2, 2, 1);
		toothBottom2.setRotationPoint(-3F, 15F, 7F);
		toothBottom2.setTextureSize(128, 64);
		toothBottom2.mirror = true;

		toothTop1 = new ModelRenderer(this, 0, 54);
		toothTop1.addBox(-3F, 0F, -13.9F, 2, 2, 1);
		toothTop1.setRotationPoint(-3F, 15F, 7F);
		toothTop1.setTextureSize(128, 64);
		toothTop1.mirror = true;

		toothTop2 = new ModelRenderer(this, 0, 54);
		toothTop2.addBox(7F, 0F, -13.9F, 2, 2, 1);
		toothTop2.setRotationPoint(-3F, 15F, 7F);
		toothTop2.setTextureSize(128, 64);
		toothTop2.mirror = true;
		
	      toothBottom3 = new ModelRenderer(this, 7, 54);
	      toothBottom3.addBox(-3.9F, -3F, -13F, 1, 2, 2);
	      toothBottom3.setRotationPoint(-3F, 15F, 7F);
	      toothBottom3.setTextureSize(128, 64);
	      toothBottom3.mirror = true;

	      toothBottom4 = new ModelRenderer(this, 7, 54);
	      toothBottom4.addBox(8.9F, -3F, -13F, 1, 2, 2);
	      toothBottom4.setRotationPoint(-3F, 15F, 7F);
	      toothBottom4.setTextureSize(128, 64);
	      toothBottom4.mirror = true;

	      toothTop3 = new ModelRenderer(this, 7, 54);
	      toothTop3.addBox(-3.9F, 0F, -11F, 1, 2, 2);
	      toothTop3.setRotationPoint(-3F, 15F, 7F);
	      toothTop3.setTextureSize(128, 64);
	      toothTop3.mirror = true;

	      toothTop4 = new ModelRenderer(this, 7, 54);
	      toothTop4.addBox(8.9F, 0F, -11F, 1, 2, 2);
	      toothTop4.setRotationPoint(-3F, 15F, 7F);
	      toothTop4.setTextureSize(128, 64);
	      toothTop4.mirror = true;
	      
//	      rowTop = new ModelRenderer(this, 0, 60);
//	      rowTop.addBox(-3F, 0F, -13.9F, 12, 2, 0);
//	      rowTop.setRotationPoint(-3F, 15F, 7F);
//	      rowTop.setTextureSize(64, 32);
//	      rowTop.mirror = true;
//	      
//	      rowBottom = new ModelRenderer(this, 0, 60);
//	      rowBottom.addBox(-3F, -3F, -13.9F, 12, 2, 0);
//	      rowBottom.setRotationPoint(-3F, 15F, 7F);
//	      rowBottom.setTextureSize(64, 32);
//	      rowBottom.mirror = true;
	}

	/**
	 * 
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);

		setRotationAngles(f, f1, f2, f3, f4, f5, entity);

		// start render matrix
		GlStateManager.pushMatrix();

		// (rotate 180 degrees around the z-axis)
		GlStateManager.rotate(15F, 1.0F, 0F, 0F);

		// move mimic up slight so as not to be in the ground
		GlStateManager.translate(0, -0.2F, -0.2F);

		base.render(f5);
		lid.render(f5);
		padBottom.render(f5);
		padTop.render(f5);
		latch1.render(f5);
		hingeBottom1.render(f5);
		hingeBottom2.render(f5);
		toothBottom1.render(f5);
		toothBottom2.render(f5);
		toothTop1.render(f5);
		toothTop2.render(f5);
		toothBottom3.render(f5);
		toothBottom4.render(f5);
		toothTop3.render(f5);
		toothTop4.render(f5);
//		rowBottom.render(f5);
//		rowTop.render(f5);
		
		GlStateManager.popMatrix();
	}

	/**
	 * 
	 * @param model
	 * @param x
	 * @param y
	 * @param z
	 */
	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	/**
	 * 
	 */
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);

		// set the lid to the right angle (chomping motion)
		WoodMimicEntity we = (WoodMimicEntity) entity;

		lid.rotateAngleX = -degToRad(we.lidAngle * 90);
		padTop.rotateAngleX = lid.rotateAngleX;
		latch1.rotateAngleX = lid.rotateAngleX;
		toothTop1.rotateAngleX = lid.rotateAngleX;
		toothTop2.rotateAngleX = lid.rotateAngleX;
		toothTop3.rotateAngleX = lid.rotateAngleX;
		toothTop4.rotateAngleX = lid.rotateAngleX;
//		rowTop.rotateAngleX = lid.rotateAngleX;
	}

	/**
	 * 
	 * @param degrees
	 * @return
	 */
	protected float degToRad(float degrees) {
		return degrees * (float)Math.PI / 180 ;
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
}
