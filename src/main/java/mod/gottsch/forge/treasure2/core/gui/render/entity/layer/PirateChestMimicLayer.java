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
package mod.gottsch.forge.treasure2.core.gui.render.entity.layer;


import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.gui.model.entity.PirateChestMimicModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 
 * @author Mark Gottschling on Jun 8, 2023
 *
 * @param <T>
 * @param <M>
 */
@OnlyIn(Dist.CLIENT)
public class PirateChestMimicLayer<T extends Entity, M extends PirateChestMimicModel<T>> extends AbstractEyesLayer<T, M> {
	private static final RenderType EYES = RenderType.eyes(new ResourceLocation(Treasure.MODID,"textures/entity/mob/pirate_chest_mimic_eyes.png"));

	public PirateChestMimicLayer(IEntityRenderer<T, M> layer) {
		super(layer);
	}

	public RenderType renderType() {
		return EYES;
	}	   

}
