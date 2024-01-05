package mod.gottsch.forge.treasure2.core.gui.render.entity;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.entity.monster.BoundSoulEntity;
import mod.gottsch.forge.treasure2.core.gui.model.entity.BoundSoulModel;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author Mark Gottschling on Feb 23, 2020
 *
 */
@OnlyIn(Dist.CLIENT)
public class BoundSoulRenderer extends BipedRenderer<BoundSoulEntity, BipedModel<BoundSoulEntity>> {
	private static final ResourceLocation BOUND_SOUL_TEXTURES = new ResourceLocation(Treasure.MODID + ":textures/entity/mob/bound-soul.png");

	/**
	 * NOTE uses the Zombie model
	 * @param renderManager
	 */
    public BoundSoulRenderer(EntityRendererManager renderManager, BipedModel<BoundSoulEntity> modelBiped, float shadowSize) {
    	 super(renderManager, new BoundSoulModel(), shadowSize);
    }

    /**
     * 
     * @param manager
     * @param modelBiped
     * @param armorModel1
     * @param armorModel2
     * @param shadowSize
     * @param textureName
     */
    public BoundSoulRenderer(EntityRendererManager manager, BoundSoulModel modelBiped,  BoundSoulModel armorModel1,  BoundSoulModel armorModel2, float shadowSize) {
		this(manager, modelBiped, shadowSize);
		this.addLayer(new BipedArmorLayer<>(this, armorModel1, armorModel2));
	}
    
    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
	public ResourceLocation getTextureLocation(BoundSoulEntity entity) {
        return BOUND_SOUL_TEXTURES;
    }

}