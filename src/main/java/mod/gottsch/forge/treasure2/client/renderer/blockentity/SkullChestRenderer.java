/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.client.renderer.blockentity;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.client.model.blockentity.ITreasureChestModel;
import mod.gottsch.forge.treasure2.client.model.blockentity.SkullChestModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// NOTE @OnlyIn extremely important! add to all Renderers
@OnlyIn(Dist.CLIENT)
public class SkullChestRenderer extends AbstractChestBlockEntityRenderer {
	public static final ResourceLocation SKULL_CHEST_RENDERER_ATLAS_TEXTURE = new ResourceLocation(Treasure.MODID, "entity/chest/skull_chest");
	public static final ResourceLocation GOLD_SKULL_CHEST_RENDERER_ATLAS_TEXTURE = new ResourceLocation(Treasure.MODID, "entity/chest/gold_skull_chest");
	public static final ResourceLocation CRYSTAL_SKULL_CHEST_RENDERER_ATLAS_TEXTURE = new ResourceLocation(Treasure.MODID, "entity/chest/crystal_skull_chest");
	
	/**
	 * 
	 * @param tileEntityRendererDispatcher
	 * @return
	 */
	public static SkullChestRenderer createGoldSkull(BlockEntityRendererProvider.Context context) {
		SkullChestRenderer renderer = new SkullChestRenderer(context, 
				new SkullChestModel(context.bakeLayer(SkullChestModel.LAYER_LOCATION)),
				new Material(TextureAtlas.LOCATION_BLOCKS, GOLD_SKULL_CHEST_RENDERER_ATLAS_TEXTURE));
		return renderer;
	}

	public static SkullChestRenderer createCrystalSkull(BlockEntityRendererProvider.Context context) {
		SkullChestRenderer renderer = new SkullChestRenderer(context, 
				new SkullChestModel(context.bakeLayer(SkullChestModel.LAYER_LOCATION)),
				new Material(TextureAtlas.LOCATION_BLOCKS, CRYSTAL_SKULL_CHEST_RENDERER_ATLAS_TEXTURE));
		return renderer;
	}
	
	/**
	 * 
	 * @param tileEntityRendererDispatcher
	 */
	public SkullChestRenderer(BlockEntityRendererProvider.Context context) {
		setModel(new SkullChestModel(context.bakeLayer(SkullChestModel.LAYER_LOCATION)));
		setMaterial(new Material(TextureAtlas.LOCATION_BLOCKS, SKULL_CHEST_RENDERER_ATLAS_TEXTURE));
	}
	
	/*
	 * 
	 */
	public SkullChestRenderer(BlockEntityRendererProvider.Context context, ITreasureChestModel model, Material material) {
		setModel(model);
		setMaterial(material);
	}
	 
	@Override
	public float getLockScaleModifier() {
		return 0.25F;
	}
}
