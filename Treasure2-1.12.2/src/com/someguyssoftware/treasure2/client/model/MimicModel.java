package com.someguyssoftware.treasure2.client.model;

import com.someguyssoftware.treasure2.entity.monster.WoodenMimicEntity;

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
	ModelRenderer Latch1;
	ModelRenderer hingeBottom1;
	ModelRenderer hingeBottom2;

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

		Latch1 = new ModelRenderer(this, 0, 47);
		Latch1.addBox(-1F, -2F, -15F, 2, 4, 1);
		Latch1.setRotationPoint(0F, 15F, 7F);
		Latch1.setTextureSize(128, 64);
		Latch1.mirror = true;
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
		 GlStateManager.translate(0F, -0.1F, 0F);

		base.render(f5);
		lid.render(f5);
		padBottom.render(f5);
		padTop.render(f5);
		Latch1.render(f5);
		hingeBottom1.render(f5);
		hingeBottom2.render(f5);

		GlStateManager.popMatrix();
	}

//	    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
//	        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
//		        
//	    }

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
		WoodenMimicEntity we = (WoodenMimicEntity) entity;

		lid.rotateAngleX = -degToRad(we.lidAngle * 90);
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
