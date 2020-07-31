// Date: 11/26/2018 9:30:45 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX






package net.minecraft.src;

public class ModelSkullChestModel extends ModelBase
{
  //fields
    ModelRenderer top;
    ModelRenderer lid;
    ModelRenderer jaw;
    ModelRenderer head;
  
  public ModelSkullChestModel()
  {
    textureWidth = 128;
    textureHeight = 128;
    
      top = new ModelRenderer(this, 0, 0);
      top.addBox(-6F, -1F, -13F, 12, 1, 12);
      top.setRotationPoint(0F, 9F, 7F);
      top.setTextureSize(128, 128);
      top.mirror = true;
      setRotation(top, 0F, 0F, 0F);
      lid = new ModelRenderer(this, 0, 15);
      lid.addBox(-7F, 0F, -14F, 14, 1, 14);
      lid.setRotationPoint(0F, 9F, 7F);
      lid.setTextureSize(128, 128);
      lid.mirror = true;
      setRotation(lid, 0F, 0F, 0F);
      jaw = new ModelRenderer(this, 0, 56);
      jaw.addBox(-5F, 0F, -7F, 10, 6, 9);
      jaw.setRotationPoint(0F, 18F, 0F);
      jaw.setTextureSize(128, 128);
      jaw.mirror = true;
      setRotation(jaw, 0F, 0F, 0F);
      head = new ModelRenderer(this, 0, 32);
      head.addBox(-7F, 0F, -14F, 14, 8, 14);
      head.setRotationPoint(0F, 10F, 7F);
      head.setTextureSize(128, 128);
      head.mirror = true;
      setRotation(head, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    top.render(f5);
    lid.render(f5);
    jaw.render(f5);
    head.render(f5);
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
