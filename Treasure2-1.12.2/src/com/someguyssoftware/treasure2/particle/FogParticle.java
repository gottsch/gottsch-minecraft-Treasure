package com.someguyssoftware.treasure2.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
//import ovh.corail.tombstone.ConfigTombstone;
//import ovh.corail.tombstone.helper.Helper;
//import ovh.corail.tombstone.particle.TombstoneParticleSprites;

@SideOnly(Side.CLIENT)
public class FogParticle extends Particle {
  protected float alpha = 0.08F;
  
  public FogParticle(World world, double x, double y, double z, double mX, double mY, double mZ) {
    super(world, x, y, z, mX, mY, mZ);
//    TombstoneParticleSprites.SpriteTypes.FAKE_FOG.setTexture(this);
//    this.motionX = mX;
//    this.motionY = mY;
//    this.motionZ = mZ;
//    this.field_82339_as = 0.0F;
//    func_70541_f(3.0F);
//    func_187114_a(80);
//    this.field_190017_n = false;
//    if (ConfigTombstone.client.enableHalloweenEffect && Helper.isDateAroundHalloween(world))
//      func_70538_b(1.0F, 1.0F, 1.0F); 
  }
  
  public void onUpdate() {
//    super.onUpdate();
//    int halfMaxAge = this.field_70547_e / 2;
//    if (this.field_70546_d >= halfMaxAge) {
//      float ratio = (this.field_70546_d - halfMaxAge) / halfMaxAge;
//      func_82338_g((1.0F - ratio) * this.alpha);
//    } else {
//      float ratio = this.field_70546_d / halfMaxAge;
//      func_82338_g(ratio * this.alpha);
//    } 
  }
  
  public boolean func_187111_c() {
    return true;
  }
  
  public int func_70537_b() {
    return 1;
  }
}

