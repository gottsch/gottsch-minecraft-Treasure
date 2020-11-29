package com.someguyssoftware.treasure2.client.model;

import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.ITreasureChestTileEntity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * 
 * @author Mark Gottschling on Mar 18, 2018
 *
 */
public class CompressorChestModel extends ModelBase implements ITreasureChestModel {

	//fields
    ModelRenderer base;
    ModelRenderer lid1;
    ModelRenderer lid2;
    ModelRenderer lid3;
    ModelRenderer lid4;
    ModelRenderer hinge1;
    ModelRenderer hinge2;
    ModelRenderer hinge3;
    ModelRenderer hinge4;
    ModelRenderer latch1;
    ModelRenderer latch2;
    ModelRenderer latch3;
    ModelRenderer latch4;
    
	public CompressorChestModel() {
	    textureWidth = 64;
	    textureHeight = 128;
	    
	      base = new ModelRenderer(this, 0, 0);
	      base.addBox(-7F, 0F, -14F, 14, 10, 14);
	      base.setRotationPoint(0F, 14F, 7F);
	      base.setTextureSize(64, 128);
	      base.mirror = true;
	      setRotation(base, 0F, 0F, 0F);
	      lid1 = new ModelRenderer(this, 0, 25);
	      lid1.addBox(-4F, -5F, 0F, 7, 5, 7);
	      lid1.setRotationPoint(4F, 15F, -7F);
	      lid1.setTextureSize(64, 32);
	      lid1.mirror = true;
	      setRotation(lid1, 0F, 0F, 0F);
	      lid2 = new ModelRenderer(this, 0, 38);
	      lid2.addBox(0F, -5F, -4F, 7, 5, 7);
	      lid2.setRotationPoint(-7F, 15F, -3F);
	      lid2.setTextureSize(64, 32);
	      lid2.mirror = true;
	      setRotation(lid2, 0F, 0F, 0F);
	      lid3 = new ModelRenderer(this, 0, 51);
	      lid3.addBox(-4F, -5F, -7F, 7, 5, 7);
	      lid3.setRotationPoint(-3F, 15F, 7F);
	      lid3.setTextureSize(64, 32);
	      lid3.mirror = true;
	      setRotation(lid3, 0F, 0F, 0F);
	      lid4 = new ModelRenderer(this, 0, 64);
	      lid4.addBox(-7F, -5F, -3F, 7, 5, 7);
	      lid4.setRotationPoint(7F, 15F, 3F);
	      lid4.setTextureSize(64, 32);
	      lid4.mirror = true;
	      setRotation(lid4, 0F, 0F, 0F);
	      hinge1 = new ModelRenderer(this, 0, 77);
	      hinge1.addBox(-2F, 0F, 0F, 5, 1, 1);
	      hinge1.setRotationPoint(3F, 14.5F, -7.5F);
	      hinge1.setTextureSize(64, 128);
	      hinge1.mirror = true;
	      setRotation(hinge1, 0F, 0F, 0F);
	      hinge2 = new ModelRenderer(this, 0, 80);
	      hinge2.addBox(0F, 0F, -3F, 1, 1, 5);
	      hinge2.setRotationPoint(-7.5F, 14.5F, -3F);
	      hinge2.setTextureSize(64, 128);
	      hinge2.mirror = true;
	      setRotation(hinge2, 0F, 0F, 0F);
	      hinge3 = new ModelRenderer(this, 0, 77);
	      hinge3.addBox(-2F, 0F, 0F, 5, 1, 1);
	      hinge3.setRotationPoint(-4F, 14.5F, 6.5F);
	      hinge3.setTextureSize(64, 128);
	      hinge3.mirror = true;
	      setRotation(hinge3, 0F, 0F, 0F);
	      hinge4 = new ModelRenderer(this, 0, 80);
	      hinge4.addBox(0F, 0F, -3F, 1, 1, 5);
	      hinge4.setRotationPoint(6.5F, 14.5F, 4F);
	      hinge4.setTextureSize(64, 128);
	      hinge4.mirror = true;
	      setRotation(hinge4, 0F, 0F, 0F);
	      latch2 = new ModelRenderer(this, 0, 87);
	      latch2.addBox(2F, -2F, -5F, 3, 4, 1);
	      latch2.setRotationPoint(-7F, 15F, -3F);
	      latch2.setTextureSize(64, 128);
	      latch2.mirror = true;
	      setRotation(latch2, 0F, 0F, 0F);
	      latch4 = new ModelRenderer(this, 0, 87);
	      latch4.addBox(-5F, -2F, 5F, 3, 4, 1);
	      latch4.setRotationPoint(7F, 15F, 2F);
	      latch4.setTextureSize(64, 128);
	      latch4.mirror = true;
	      setRotation(latch4, 0F, 0F, 0F);
	      latch3 = new ModelRenderer(this, 0, 93);
	      latch3.addBox(-5F, -2F, -5F, 1, 4, 3);
	      latch3.setRotationPoint(-3F, 15F, 7F);
	      latch3.setTextureSize(64, 32);
	      latch3.mirror = true;
	      setRotation(latch3, 0F, 0F, 0F);
	      latch1 = new ModelRenderer(this, 0, 93);
	      latch1.addBox(5F, -2F, 2F, 1, 4, 3);
	      latch1.setRotationPoint(2F, 15F, -7F);
	      latch1.setTextureSize(64, 128);
	      latch1.mirror = true;
	      setRotation(latch1, 0F, 0F, 0F);
	}

	/**
	 * 
	 */
	 public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	  {
	    super.render(entity, f, f1, f2, f3, f4, f5);
	    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	    base.render(f5);
	    lid1.render(f5);
	    lid2.render(f5);
	    lid3.render(f5);
	    lid4.render(f5);
	    hinge1.render(f5);
	    hinge2.render(f5);
	    hinge3.render(f5);
	    hinge4.render(f5);
	    latch2.render(f5);
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

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}

	/**
	 * 
	 */
	@Override
	public void renderAll(ITreasureChestTileEntity te) {
		float originalAngle = lid1.rotateAngleX;
		float f5 = 0.0625F;
		
//		lid3.rotateAngleX = originalAngle;
//		latch3.rotateAngleX = lid3.rotateAngleX;
//		
//		lid2.rotateAngleZ = originalAngle;
//		latch2.rotateAngleZ = lid2.rotateAngleZ;
		
		// reverse the angle direction
		lid1.rotateAngleX = -(lid1.rotateAngleX);
		latch1.rotateAngleX = lid1.rotateAngleX;
		lid2.rotateAngleZ = originalAngle;
		latch2.rotateAngleZ = lid2.rotateAngleZ;
		lid3.rotateAngleX = originalAngle;
		latch3.rotateAngleX = lid3.rotateAngleX;
		lid4.rotateAngleZ = lid1.rotateAngleX;
		latch4.rotateAngleZ = lid4.rotateAngleZ;
		
//		for (LockState state : te.getLockStates()) {
//			if (state.getLock() != null) {
//				switch(state.getSlot().getIndex()) {
//					case 1:
//						latch2.render(f5);
//						break;
//					case 2:
//						latch3.render(f5);
//						break;
//					case 3:
//						latch4.render(f5);
//						break;
//				}
//			}
//		}

//		lid4.rotateAngleZ = lid1.rotateAngleX;
//		latch4.rotateAngleZ = lid4.rotateAngleZ;
		
	    base.render(f5);
	    lid1.render(f5);
	    lid2.render(f5);
	    lid3.render(f5);
	    lid4.render(f5);
//	    hinge1.render(f5);
//	    hinge2.render(f5);
//	    hinge3.render(f5);
//	    hinge4.render(f5);
	    latch1.render(f5);
	    latch2.render(f5);
	    latch3.render(f5);
	    latch4.render(f5);
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
	 * @return the lid
	 */
	@Override
	public ModelRenderer getLid() {
		return lid1;
	}

	/**
	 * @param lid the lid to set
	 */
	public void setLid(ModelRenderer lid) {
		this.lid1 = lid;
	}

}
