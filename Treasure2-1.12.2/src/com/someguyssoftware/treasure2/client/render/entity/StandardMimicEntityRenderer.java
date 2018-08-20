package com.someguyssoftware.treasure2.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.client.model.StandardChestModel;
import com.someguyssoftware.treasure2.entity.monster.StandardMimicEntity;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * @author Mark Gottschling on Aug 18, 2018
 *
 */
@SideOnly(Side.CLIENT)
public class StandardMimicEntityRenderer extends RenderLiving<StandardMimicEntity> {

	private static final ResourceLocation MIMC_TEXTURES = new ResourceLocation(Treasure.MODID + ":textures/entity/mob/standard-mimic.png");
	
	/**
	 * 
	 * @param renderManager
	 */
    public StandardMimicEntityRenderer(RenderManager renderManager) {
    	super(renderManager, new StandardChestModel(), 0F); // no shadow
    }    
    
    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(StandardMimicEntity entity, float partialTick) {
    	// make the warrior slightly bigger than standard skeleton 
    	GL11.glScalef(1F, 1F, 1F);
    }
    
    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(StandardMimicEntity entity) {
        return MIMC_TEXTURES;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityLiving entity)
    {
        return this.getEntityTexture((StandardMimicEntity)entity);
    }
}
