/*
 * This file is part of  Treasure2.
 * Copyright (c) 2024 Mark Gottschling (gottsch)
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
import mod.gottsch.forge.treasure2.client.model.entity.CardboardBoxMimicModel;
import mod.gottsch.forge.treasure2.client.model.entity.WoodChestMimicModel;
import mod.gottsch.forge.treasure2.client.renderer.entity.layer.CardboardBoxMimicLayer;
import mod.gottsch.forge.treasure2.client.renderer.entity.layer.WoodChestMimicLayer;
import mod.gottsch.forge.treasure2.core.entity.monster.CardboardBoxMimic;
import mod.gottsch.forge.treasure2.core.entity.monster.WoodChestMimic;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 *
 * @author Mark Gottschling on Jan 1, 2024
 *
 */
public class CardboardBoxMimicRenderer extends MobRenderer<CardboardBoxMimic, CardboardBoxMimicModel<CardboardBoxMimic>> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(Treasure.MODID, "textures/entity/mob/cardboard_box_mimic.png");
	private final float scale;

	/**
	 *
	 * @param context
	 */
	public CardboardBoxMimicRenderer(EntityRendererProvider.Context context) {
        super(context, new CardboardBoxMimicModel<>(context.bakeLayer(CardboardBoxMimicModel.LAYER_LOCATION)), 0.5F);
        this.addLayer(new CardboardBoxMimicLayer<>(this));
        this.scale = 1.0F;
	}

	@Override
	protected void scale(CardboardBoxMimic mimic, PoseStack pose, float scale) {
		pose.scale(this.scale, this.scale, this.scale);
	}
	
     @Override
    public ResourceLocation getTextureLocation(CardboardBoxMimic entity) {
        return TEXTURE;
    }
}
