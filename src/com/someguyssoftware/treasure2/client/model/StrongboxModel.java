package com.someguyssoftware.treasure2.client.model;

import com.someguyssoftware.treasure2.tileentity.ITreasureChestTileEntity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class StrongboxModel extends ModelBase implements ITreasureChestModel {
	  //fields
    ModelRenderer box;
    ModelRenderer lid;
    ModelRenderer foot1;
    ModelRenderer foot2;
    ModelRenderer foot3;
    ModelRenderer foot4;
    ModelRenderer pad;
    ModelRenderer latch;
    ModelRenderer pad2;
    
    public StrongboxModel() {
    	   textureWidth = 64;
    	    textureHeight = 32;
    	    
    	      box = new ModelRenderer(this, 0, 12);
    	      box.addBox(-5F, 0F, -8F, 10, 5, 8);
    	      box.setRotationPoint(0F, 18.5F, 4F);
    	      box.setTextureSize(64, 32);
    	      box.mirror = true;
    	      setRotation(box, 0F, 0F, 0F);
    	      lid = new ModelRenderer(this, 0, 0);
    	      lid.addBox(-5F, -3F, -8F, 10, 3, 8);
    	      lid.setRotationPoint(0F, 19.5F, 4F);
    	      lid.setTextureSize(64, 32);
    	      lid.mirror = true;
    	      setRotation(lid, 0F, 0F, 0F);
    	      foot1 = new ModelRenderer(this, 0, 26);
    	      foot1.addBox(0F, 0F, 0F, 2, 1, 1);
    	      foot1.setRotationPoint(3F, 23F, -4F);
    	      foot1.setTextureSize(64, 32);
    	      foot1.mirror = true;
    	      setRotation(foot1, 0F, 0F, 0F);
    	      foot2 = new ModelRenderer(this, 7, 26);
    	      foot2.addBox(0F, 0F, 0F, 2, 1, 1);
    	      foot2.setRotationPoint(-5F, 23F, -4F);
    	      foot2.setTextureSize(64, 32);
    	      foot2.mirror = true;
    	      setRotation(foot2, 0F, 0F, 0F);
    	      foot3 = new ModelRenderer(this, 14, 26);
    	      foot3.addBox(0F, 0F, 0F, 2, 1, 1);
    	      foot3.setRotationPoint(-5F, 23F, 3F);
    	      foot3.setTextureSize(64, 32);
    	      foot3.mirror = true;
    	      setRotation(foot3, 0F, 0F, 0F);
    	      foot4 = new ModelRenderer(this, 21, 26);
    	      foot4.addBox(0F, 0F, 0F, 2, 1, 1);
    	      foot4.setRotationPoint(3F, 23F, 3F);
    	      foot4.setTextureSize(64, 32);
    	      foot4.mirror = true;
    	      setRotation(foot4, 0F, 0F, 0F);
    	      pad = new ModelRenderer(this, 37, 0);
    	      pad.addBox(-2F, -2F, -8.5F, 4, 2, 1);
    	      pad.setRotationPoint(0F, 19.5F, 4F);
    	      pad.setTextureSize(64, 32);
    	      pad.mirror = true;
    	      setRotation(pad, 0F, 0F, 0F);
    	      latch = new ModelRenderer(this, 37, 11);
    	      latch.addBox(-1F, -1.5F, -9F, 2, 2, 1);
    	      latch.setRotationPoint(0F, 19.5F, 4F);
    	      latch.setTextureSize(64, 32);
    	      latch.mirror = true;
    	      setRotation(latch, 0F, 0F, 0F);
    	      pad2 = new ModelRenderer(this, 37, 4);
    	      pad2.addBox(-2F, 0F, -3F, 4, 3, 1);
    	      pad2.setRotationPoint(0F, 18.5F, -1.5F);
    	      pad2.setTextureSize(64, 32);
    	      pad2.mirror = true;
    	      setRotation(pad2, 0F, 0F, 0F);
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
		latch.rotateAngleX = lid.rotateAngleX;
		pad.rotateAngleX = lid.rotateAngleX;
		
		box.render(0.0625F);
		lid.render(0.0625F);
		foot1.render(0.0625F);
		foot2.render(0.0625F);
		foot3.render(0.0625F);
		foot4.render(0.0625F);
		latch.render(0.0625F);
		pad.render(0.0625F);
		pad2.render(0.0625F);
	}
	
	@Override
	public ModelRenderer getLid() {
		return lid;
	}
}
