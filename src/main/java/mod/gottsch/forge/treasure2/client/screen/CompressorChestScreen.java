/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.client.screen;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.inventory.CompressorChestContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CompressorChestScreen extends AbstractChestScreen<CompressorChestContainerMenu> {

	// this is the resource location for the background image for the GUI
	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(Treasure.MODID, "textures/gui/screen/compressor_chest.png");

	/**
	 * 
	 * @param screenContainer
	 * @param playerInventory
	 * @param title
	 */
	public CompressorChestScreen(CompressorChestContainerMenu screenContainer, Inventory playerInventory, Component title) {
		super(screenContainer, playerInventory, title);
		// Set the width and height of the gui.  Should match the size of the texture!
		imageWidth = 247;
		imageHeight = 184;
		// set the inventory label to the correct height
		this.inventoryLabelY = this.imageHeight - 93;
		setBgTexture(BG_TEXTURE);
	}
}
