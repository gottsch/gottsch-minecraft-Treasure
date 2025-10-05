/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.client.renderer.entity.layer;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.client.model.entity.CardboardBoxMimicModel;
import mod.gottsch.forge.treasure2.client.model.entity.MilkCrateMimicModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 *
 * @author Mark Gottschling on Apr 23, 2024
 *
 * @param <T>
 * @param <M>
 */
@OnlyIn(Dist.CLIENT)
public class MilkCrateMimicLayer<T extends Entity, M extends MilkCrateMimicModel<T>> extends EyesLayer<T, M> {
	private static final RenderType EYES = RenderType.eyes(new ResourceLocation(Treasure.MODID,"textures/entity/mob/milk_crate_mimic_eyes.png"));

	public MilkCrateMimicLayer(RenderLayerParent<T, M> layer) {
		super(layer);
	}

	public RenderType renderType() {
		return EYES;
	}	   

}
