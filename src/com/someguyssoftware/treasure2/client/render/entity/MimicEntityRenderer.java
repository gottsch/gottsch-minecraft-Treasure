package com.someguyssoftware.treasure2.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.someguyssoftware.treasure2.entity.monster.MimicEntity;

import net.minecraft.client.model.ModelBase;
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
public class MimicEntityRenderer extends RenderLiving<MimicEntity> {

	private ModelBase model;
	private ResourceLocation texture;
	
	/**
	 * 
	 * @param renderManager
	 */
//    public MimicEntityRenderer(RenderManager renderManager) {
//    	super(renderManager, new MimicModel(), 0.4F);
//    }   
    
    /**
     * 
     * @param renderManager
     * @param model
     * @param texture
     */
    public MimicEntityRenderer(RenderManager renderManager, ModelBase model, ResourceLocation texture) {
    	super(renderManager, model, 0.4F);
    	setModel(model);
    	setTexture(texture);    	
    }
    
    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(MimicEntity entity, float partialTick) {
    	// make the warrior slightly bigger than standard skeleton 
    	GL11.glScalef(1F, 1F, 1F);
    }
    
    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(MimicEntity entity) {
//        return MIMC_TEXTURES;
    	return getTexture();
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityLiving entity) {
        return this.getEntityTexture((MimicEntity)entity);
    }

	/**
	 * @return the model
	 */
	public ModelBase getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(ModelBase model) {
		this.model = model;
	}

	/**
	 * @return the texture
	 */
	public ResourceLocation getTexture() {
		return texture;
	}

	/**
	 * @param texture the texture to set
	 */
	public void setTexture(ResourceLocation texture) {
		this.texture = texture;
	}
}
