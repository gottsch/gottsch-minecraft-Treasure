/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
import mod.gottsch.forge.treasure2.client.model.entity.PirateChestMimicModel;
import mod.gottsch.forge.treasure2.client.renderer.entity.layer.PirateChestMimicLayer;
import mod.gottsch.forge.treasure2.core.entity.monster.PirateChestMimic;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Jun 8, 2023
 *
 */
public class PirateChestMimicRenderer extends MobRenderer<PirateChestMimic, PirateChestMimicModel<PirateChestMimic>> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(Treasure.MODID, "textures/entity/mob/pirate_chest_mimic.png");
	private final float scale;
	
	/**
	 * 
	 * @param context
	 */
	public PirateChestMimicRenderer(EntityRendererProvider.Context context) {
        super(context, new PirateChestMimicModel<>(context.bakeLayer(PirateChestMimicModel.LAYER_LOCATION)), 0.5F);
        this.addLayer(new PirateChestMimicLayer<>(this));
        this.scale = 1.0F;
	}

	@Override
	protected void scale(PirateChestMimic mimic, PoseStack pose, float scale) {
		pose.scale(this.scale, this.scale, this.scale);
	}
	
     @Override
    public ResourceLocation getTextureLocation(PirateChestMimic entity) {
        return TEXTURE;
    }
}
