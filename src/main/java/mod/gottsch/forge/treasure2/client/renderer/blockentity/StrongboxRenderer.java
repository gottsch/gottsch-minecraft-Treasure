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
package mod.gottsch.forge.treasure2.client.renderer.blockentity;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.client.model.blockentity.ITreasureChestModel;
import mod.gottsch.forge.treasure2.client.model.blockentity.StrongboxModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

/**
 * @author Mark Gottschling onJan 9, 2018
 *
 */
public class StrongboxRenderer extends AbstractChestBlockEntityRenderer {
	public static final ResourceLocation IRON_STRONGBOX_RENDERER_ATLAS_TEXTURE = new ResourceLocation(Treasure.MODID, "entity/chest/iron_strongbox");
	public static final ResourceLocation GOLD_STRONGBOX_RENDERER_ATLAS_TEXTURE = new ResourceLocation(Treasure.MODID, "entity/chest/gold_strongbox");
	
	/**
	 * 
	 * @param texture
	 */
	public StrongboxRenderer(BlockEntityRendererProvider.Context context) {
		setModel(new StrongboxModel(context.bakeLayer(StrongboxModel.LAYER_LOCATION)));
		setMaterial(new Material(TextureAtlas.LOCATION_BLOCKS, IRON_STRONGBOX_RENDERER_ATLAS_TEXTURE));
	}	

	public StrongboxRenderer(BlockEntityRendererProvider.Context context, ITreasureChestModel model, Material material) {
		setModel(model);
		setMaterial(material);
	}
	
	public static StrongboxRenderer createGoldSkull(BlockEntityRendererProvider.Context context) {
		StrongboxRenderer renderer = new StrongboxRenderer(context, 
				new StrongboxModel(context.bakeLayer(StrongboxModel.LAYER_LOCATION)),
				new Material(TextureAtlas.LOCATION_BLOCKS, GOLD_STRONGBOX_RENDERER_ATLAS_TEXTURE));
		return renderer;
	}
	
	@Override
	public float getLockScaleModifier() {
		return 0.25F;
	}
}
