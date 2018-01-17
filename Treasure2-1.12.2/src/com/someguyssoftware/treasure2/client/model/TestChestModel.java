package com.someguyssoftware.treasure2.client.model;

import com.someguyssoftware.treasure2.tileentity.TreasureChestTileEntity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@Deprecated
public class TestChestModel extends ModelBase implements ITreasureChestModel {
	
  //fields
    ModelRenderer Base;
    ModelRenderer Lid;
    ModelRenderer Latch;
  
  public TestChestModel() {
    textureWidth = 128;
    textureHeight = 64;
    
      Base = new ModelRenderer(this, 0, 0);
      Base.addBox(-7F, 0F, -14F, 14, 10, 14);
      Base.setRotationPoint(0F, 14F, 7F);
      Base.setTextureSize(128, 64);
      Base.mirror = true;
      setRotation(Base, 0F, 0F, 0F);
      Lid = new ModelRenderer(this, 0, 26);
      Lid.addBox(-7F, -4F, -14F, 14, 4, 14);
      Lid.setRotationPoint(0F, 14F, 7F);
      Lid.setTextureSize(128, 64);
      Lid.mirror = true;
      setRotation(Lid, 0F, 0F, 0F);
      Latch = new ModelRenderer(this, 57, 0);
      Latch.addBox(-1F, 0F, -15F, 2, 3, 1);
      Latch.setRotationPoint(0F, 13F, 7F);
      Latch.setTextureSize(128, 64);
      Latch.mirror = true;
      setRotation(Latch, 0F, 0F, 0F);
  }

  /**
   * 
   * @param entity
   * @param f
   * @param f1
   * @param f2
   * @param f3
   * @param f4
   * @param f5
   */
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Base.render(f5);
    Lid.render(f5);
    Latch.render(f5);
  }
  
  public void renderAll(TreasureChestTileEntity te) {
	  Latch.rotateAngleX = Lid.rotateAngleX;
	  Base.render(0.0625F);
	  Lid.render(0.0625F);
	  Latch.render(0.0625F);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z) {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
  }

@Override
public ModelRenderer getLid() {
	// TODO Auto-generated method stub
	return null;
}

}
