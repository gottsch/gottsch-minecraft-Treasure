/*
 * This file is part of  Treasure2.
 * Copyright (c) 2018 Mark Gottschling (gottsch)
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
import mod.gottsch.forge.treasure2.client.model.blockentity.StandardChestModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 
 * @author Mark Gottschling on 2018
 *
 */
// NOTE @OnlyIn extremely important! add to all Renderers
@OnlyIn(Dist.CLIENT)
// TODO StandardChestRenderer<T extends AbstractChestBlockEntity> extends AbstractChestBlockEntityRenderer<T>
public class WoodChestRenderer extends AbstractChestBlockEntityRenderer {//implements BlockEntityRenderer<WoodChestBlockEntity>/*extends AbstractChestTileEntityRenderer*/ {
	/*
	 * NOTE when defining a resource location for the Atlas, you don't need to specify the /textures/ parent folder nor, the .png extension
	 */
	// TODO move out to a common class, ex TreasureAtlasTextures
	public static final ResourceLocation WOOD_CHEST_RENDERER_ATLAS_TEXTURE = new ResourceLocation(Treasure.MODID, "entity/chest/wood_chest");


	/**
	 * 
	 * @param context
	 */
	public WoodChestRenderer(BlockEntityRendererProvider.Context context) {
		setModel(new StandardChestModel(context.bakeLayer(StandardChestModel.LAYER_LOCATION)));
		// TODO context.getBlockEntityRenderDispatcher().get block entity??
		// TODO or switch on T
		setMaterial(new Material(TextureAtlas.LOCATION_BLOCKS, WOOD_CHEST_RENDERER_ATLAS_TEXTURE));
	}
}
