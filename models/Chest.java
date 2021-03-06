// Date: 9/17/2018 6:28:52 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX






package net.minecraft.src;

public class ModelChest extends ModelBase
{
  //fields
    ModelRenderer Base;
    ModelRenderer Lid;
    ModelRenderer padBottom;
    ModelRenderer Latch1;
    ModelRenderer padTop;
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
    ModelRenderer rowBottom;
  
  public ModelChest()
  {
    textureWidth = 128;
    textureHeight = 64;
    
      Base = new ModelRenderer(this, 0, 21);
      Base.addBox(-7F, 0F, -14F, 14, 10, 14);
      Base.setRotationPoint(0F, 14F, 7F);
      Base.setTextureSize(64, 32);
      Base.mirror = true;
      setRotation(Base, 0F, 0F, 0F);
      Lid = new ModelRenderer(this, 0, 0);
      Lid.addBox(-7F, -5F, -14F, 14, 5, 14);
      Lid.setRotationPoint(0F, 15F, 7F);
      Lid.setTextureSize(64, 32);
      Lid.mirror = true;
      setRotation(Lid, -0.7853982F, 0F, 0F);
      padBottom = new ModelRenderer(this, 7, 47);
      padBottom.addBox(-2F, 0F, -1.2F, 4, 3, 1);
      padBottom.setRotationPoint(0F, 15F, -6F);
      padBottom.setTextureSize(64, 32);
      padBottom.mirror = true;
      setRotation(padBottom, 0F, 0F, 0F);
      Latch1 = new ModelRenderer(this, 0, 47);
      Latch1.addBox(-1F, -2F, -15F, 2, 4, 1);
      Latch1.setRotationPoint(0F, 15F, 7F);
      Latch1.setTextureSize(64, 32);
      Latch1.mirror = true;
      setRotation(Latch1, 0F, 0F, 0F);
      padTop = new ModelRenderer(this, 18, 47);
      padTop.addBox(-2F, -3F, -14.2F, 4, 3, 1);
      padTop.setRotationPoint(0F, 15F, 7F);
      padTop.setTextureSize(64, 32);
      padTop.mirror = true;
      setRotation(padTop, 0F, 0F, 0F);
      hingeBottom1 = new ModelRenderer(this, 29, 47);
      hingeBottom1.addBox(-1F, 0F, -1F, 2, 2, 1);
      hingeBottom1.setRotationPoint(3F, 14F, 7F);
      hingeBottom1.setTextureSize(64, 32);
      hingeBottom1.mirror = true;
      setRotation(hingeBottom1, 0F, 0F, 0F);
      hingeBottom2 = new ModelRenderer(this, 29, 47);
      hingeBottom2.addBox(-1F, -1F, -1F, 2, 2, 1);
      hingeBottom2.setRotationPoint(-3F, 15F, 7F);
      hingeBottom2.setTextureSize(64, 32);
      hingeBottom2.mirror = true;
      setRotation(hingeBottom2, 0F, 0F, 0F);
      toothBottom1 = new ModelRenderer(this, 0, 54);
      toothBottom1.addBox(-1F, -3F, -13.9F, 2, 2, 1);
      toothBottom1.setRotationPoint(-3F, 15F, 7F);
      toothBottom1.setTextureSize(64, 32);
      toothBottom1.mirror = true;
      setRotation(toothBottom1, 0F, 0F, 0F);
      toothBottom2 = new ModelRenderer(this, 0, 54);
      toothBottom2.addBox(5F, -3F, -13.9F, 2, 2, 1);
      toothBottom2.setRotationPoint(-3F, 15F, 7F);
      toothBottom2.setTextureSize(64, 32);
      toothBottom2.mirror = true;
      setRotation(toothBottom2, 0F, 0F, 0F);
      toothTop1 = new ModelRenderer(this, 0, 54);
      toothTop1.addBox(-3F, 0F, -13.9F, 2, 2, 1);
      toothTop1.setRotationPoint(-3F, 15F, 7F);
      toothTop1.setTextureSize(64, 32);
      toothTop1.mirror = true;
      setRotation(toothTop1, 0F, 0F, 0F);
      toothTop2 = new ModelRenderer(this, 0, 54);
      toothTop2.addBox(7F, 0F, -13.9F, 2, 2, 1);
      toothTop2.setRotationPoint(-3F, 15F, 7F);
      toothTop2.setTextureSize(64, 32);
      toothTop2.mirror = true;
      setRotation(toothTop2, 0F, 0F, 0F);
      toothBottom3 = new ModelRenderer(this, 0, 54);
      toothBottom3.addBox(-3.9F, -3F, -13F, 1, 2, 2);
      toothBottom3.setRotationPoint(-3F, 15F, 7F);
      toothBottom3.setTextureSize(64, 32);
      toothBottom3.mirror = true;
      setRotation(toothBottom3, 0F, 0F, 0F);
      toothBottom4 = new ModelRenderer(this, 7, 54);
      toothBottom4.addBox(8.9F, -3F, -13F, 1, 2, 2);
      toothBottom4.setRotationPoint(-3F, 15F, 7F);
      toothBottom4.setTextureSize(64, 32);
      toothBottom4.mirror = true;
      setRotation(toothBottom4, 0F, 0F, 0F);
      toothTop3 = new ModelRenderer(this, 0, 54);
      toothTop3.addBox(-3.9F, 0F, -11F, 1, 2, 2);
      toothTop3.setRotationPoint(-3F, 15F, 7F);
      toothTop3.setTextureSize(64, 32);
      toothTop3.mirror = true;
      setRotation(toothTop3, 0F, 0F, 0F);
      toothTop4 = new ModelRenderer(this, 0, 54);
      toothTop4.addBox(8.9F, 0F, -11F, 1, 2, 2);
      toothTop4.setRotationPoint(-3F, 15F, 7F);
      toothTop4.setTextureSize(64, 32);
      toothTop4.mirror = true;
      setRotation(toothTop4, 0F, 0F, 0F);
      rowBottom = new ModelRenderer(this, 0, 60);
      rowBottom.addBox(-3F, -3F, -13.9F, 12, 2, 0);
      rowBottom.setRotationPoint(-3F, 15F, 7F);
      rowBottom.setTextureSize(64, 32);
      rowBottom.mirror = true;
      setRotation(rowBottom, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Base.render(f5);
    Lid.render(f5);
    padBottom.render(f5);
    Latch1.render(f5);
    padTop.render(f5);
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
    rowBottom.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
  }

}
