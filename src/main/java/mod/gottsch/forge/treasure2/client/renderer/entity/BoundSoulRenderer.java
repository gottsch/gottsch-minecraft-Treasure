/*
 * This file is part of  Treasure2.
 * Copyright (c) 2020 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.forge.treasure2.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.client.model.entity.BoundSoulModel;
import mod.gottsch.forge.treasure2.client.renderer.entity.layer.BoundSoulLayer;
import mod.gottsch.forge.treasure2.client.renderer.entity.layer.PirateChestMimicLayer;
import mod.gottsch.forge.treasure2.core.entity.monster.BoundSoul;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * @author Mark Gottschling on Feb 23, 2020
 *
 */
public class BoundSoulRenderer extends MobRenderer<BoundSoul, BoundSoulModel<BoundSoul>> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(Treasure.MODID, "textures/entity/mob/bound_soul.png");
	
	/**
	 * 
	 * @param context
	 */
	public BoundSoulRenderer(EntityRendererProvider.Context context) {
        super(context, new BoundSoulModel<>(context.bakeLayer(BoundSoulModel.LAYER_LOCATION)), 0F);
        this.addLayer(new BoundSoulLayer<>(this));
    }

	@Override
	public void render(BoundSoul shadow, float p_116532_, float p_116533_, PoseStack matrixStack, MultiBufferSource bufferSource, int p_116536_) {
		super.render(shadow, p_116532_, p_116533_, matrixStack, bufferSource, p_116536_);		
	}
	
     @Override
    public ResourceLocation getTextureLocation(BoundSoul entity) {
        return TEXTURE;
    }
}
