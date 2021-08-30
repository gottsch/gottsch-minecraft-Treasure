/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
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
package com.someguyssoftware.treasure2.gui.render.tileentity;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.gui.model.SkullChestModel;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// NOTE @OnlyIn extremely important! add to all Renderers
@OnlyIn(Dist.CLIENT)
public class SkullChestTileEntityRenderer extends AbstractChestTileEntityRenderer {

	/**
	 * 
	 * @param tileEntityRendererDispatcher
	 * @return
	 */
	public static SkullChestTileEntityRenderer createGoldSkull(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
		return new SkullChestTileEntityRenderer(tileEntityRendererDispatcher, new ResourceLocation(Treasure.MODID + ":textures/entity/chest/gold-skull-chest.png"));
	}
	
	public static SkullChestTileEntityRenderer createCrystalSkull(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
		return new SkullChestTileEntityRenderer(tileEntityRendererDispatcher, new ResourceLocation(Treasure.MODID + ":textures/entity/chest/crystal-skull-chest.png"));
	}
	
	/**
	 * 
	 * @param tileEntityRendererDispatcher
	 */
	public SkullChestTileEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
		super(tileEntityRendererDispatcher);
		setTexture(new ResourceLocation(Treasure.MODID + ":textures/entity/chest/skull-chest.png"));
		setModel(new SkullChestModel());
	}
	
	/**
	 * 
	 * @param tileEntityRendererDispatcher
	 * @param texture
	 */
	public SkullChestTileEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher, ResourceLocation texture) {
		super(tileEntityRendererDispatcher);
		setTexture(texture);
		setModel(new SkullChestModel());
	}
	
	 /**
	  * Set the max swing angle of 60 degrees
	  */
	 @Override
	public float getAngleModifier() {
		return 3.0F;
	}
	 
	@Override
	public float getLockScaleModifier() {
		return 0.25F;
	}
}
