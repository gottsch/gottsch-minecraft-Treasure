/**
 * 
 */
package com.someguyssoftware.treasure2.client.render.entity;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.entity.monster.BoundSoulEntity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Mark Gottschling on Feb 23, 2020
 *
 */
@SideOnly(Side.CLIENT)
public class BoundSoulRenderer extends RenderBiped<BoundSoulEntity> {
	private static final ResourceLocation BOUND_SOUL_TEXTURES = new ResourceLocation(Treasure.MODID + ":textures/entity/mob/bound-soul.png");

	/**
	 * NOTE uses the Zombie model
	 * @param renderManager
	 */
    public BoundSoulRenderer(RenderManager renderManager) {
        super(renderManager, new ModelZombie(), 0.5F);
        LayerBipedArmor layerBipedArmor = new LayerBipedArmor(this) {
            protected void initArmor() {
                this.modelLeggings = new ModelZombie(0.5F, true);
                this.modelArmor = new ModelZombie(1.0F, true);
            }
        };
        this.addLayer(layerBipedArmor);
    }

    /**
     * Thanks to Xemnes for the transparency/alpha update to the bound soul. 
     */
    @Override
    public void doRender(BoundSoulEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();

        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();

        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.8F);

        super.doRender(entity, x, y, z, entityYaw, partialTicks);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1F);

        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
        GlStateManager.popMatrix();
    }
    
    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(BoundSoulEntity entity) {
        return BOUND_SOUL_TEXTURES;
    }

}
